package org.reactome.server.tools;

import com.martiansoftware.jsap.*;
import org.apache.commons.io.FileUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.reactome.server.domain.model.Disease;
import org.reactome.server.domain.model.GDA;
import org.reactome.server.domain.model.Gene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;


public class Importer {
    static final Logger logger = LoggerFactory.getLogger(Importer.class);
    static final String RAW_ZIPPED_FILE_NAME = "curated_gene_disease_associations.tsv.gz";
    static final String DOWNLOAD_LINK = "https://www.disgenet.org/static/disgenet_ap1/files/downloads/curated_gene_disease_associations.tsv.gz";
    static final String DB_NAME = "overlays";
    static final String DB_CREATE = "create";
    static final Map<String, String> settings = new HashMap<>();
    static Session session;


    public static void main(String[] args) throws Exception {
        /*prepare the args*/
        SimpleJSAP jsap = new SimpleJSAP(
                DisGeNetParser.class.getName(),
                "Read, transfer and save disease-gene association table data from file to database.",
                new Parameter[]{
                        new FlaggedOption("name", JSAP.STRING_PARSER, DB_NAME, JSAP.NOT_REQUIRED, 'n', "name", "[Optional] Specify the database"),
                        new FlaggedOption("user", JSAP.STRING_PARSER, null, JSAP.REQUIRED, 'u', "user", "[Need] The database user name"),
                        new FlaggedOption("password", JSAP.STRING_PARSER, null, JSAP.REQUIRED, 'p', "password", "[Need] The database password"),
                        new FlaggedOption("file", JSAP.STRING_PARSER, DOWNLOAD_LINK, JSAP.REQUIRED, 'f', "file", "[Need] The genetics target list data in json rows"),
                        new FlaggedOption("mode", JSAP.STRING_PARSER, DB_CREATE, JSAP.NOT_REQUIRED, 'm', "mode", "[Optional] The session mode [create | create-drop | update]"),
                        new FlaggedOption("url", JSAP.URL_PARSER, DOWNLOAD_LINK, JSAP.NOT_REQUIRED, 'd', "url", "[Optional] The disease overlay table data in tsv/csv format with columns: 'diseaseId', 'diseaseName' and 'geneSymbol' from DisGeNet"),
                }
        );
        JSAPResult jsapResult = jsap.parse(args);
        if (!jsapResult.success()) {
            System.exit(0);
        }

        /*prepare the sql session*/
        prepareSession(jsapResult);

        /*try to download the compressed association file from the given url*/
        try {
            BufferedReader bufferedReader = downloadFile(jsapResult.getURL("url"));
            saveDiseaseItems(new DisGeNetParser(bufferedReader));
            saveDiseaseItems(new OpenTargetParser(new BufferedReader(new FileReader(jsapResult.getString("file")))));
        } catch (Exception e) {
            logger.warn(e.getMessage());
            e.printStackTrace();
        } finally {
            if (session.isOpen()) session.close();
            System.exit(0);
        }
    }

    static BufferedReader downloadFile(URL url) throws IOException {
        String directory = Paths.get("").toAbsolutePath().toString().concat(File.separator);
        File rawZippedFile = new File(directory, RAW_ZIPPED_FILE_NAME);
        if (rawZippedFile.exists()) {
            if (rawZippedFile.delete()) {
                logger.info("Found old data file exists, delete it and create new one.");
                rawZippedFile = new File(directory, RAW_ZIPPED_FILE_NAME);
            }
        }
        logger.info("Downloading Disease-gene Association raw zipped file from: " + url);
        doHttpsDownload(url, rawZippedFile);
        logger.info("Saved file in location: " + rawZippedFile.getAbsolutePath());
        return new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(rawZippedFile)), StandardCharsets.UTF_8));
    }

    static void doHttpsDownload(URL url, File dest) {
        // code from https://zwbetz.com/download-a-file-over-https-using-apache-commons-fileutils-copyurltofile/
        try {
            // Create a new trust manager that trusts all certificates
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }
                    }
            };

            // Activate the new trust manager
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Open connection to the URL
            URLConnection connection = url.openConnection();

            FileUtils.copyURLToFile(connection.getURL(), dest, 30 * 1000, 30 * 1000);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }
    }

    static void prepareSession(JSAPResult jsapResult) {
        settings.put("connection.driver_class", "com.mysql.cj.jdbc.Driver");
        settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        settings.put(Environment.URL, "jdbc:mysql://localhost:3306/" + jsapResult.getString("name") + "?&characterEncoding=utf-8&useUnicode=true&serverTimezone=America/Toronto");
        settings.put(Environment.USER, jsapResult.getString("user"));
        settings.put(Environment.PASS, jsapResult.getString("password"));
        settings.put(Environment.HBM2DDL_AUTO, jsapResult.getString("mode"));
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(GDA.class);
        configuration.addAnnotatedClass(Disease.class);
        configuration.addAnnotatedClass(Gene.class);
        SessionFactory sessionFactory = configuration.buildSessionFactory(new StandardServiceRegistryBuilder().applySettings(settings).build());
        session = sessionFactory.openSession();
    }

    static void saveDiseaseItems(Parser parser) {
        long start = System.currentTimeMillis();
        Transaction transaction = session.beginTransaction();
        parser.getGenes().forEach(session::save);
        parser.getDiseases().forEach(session::save);
        parser.getGdaList().forEach(session::save);
        transaction.commit();
        logger.info("Load: " + parser.getGdaList().size() + " items in: " + (System.currentTimeMillis() - start) / 1000.0 + "s into database.");
    }
}