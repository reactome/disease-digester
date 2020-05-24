package org.reactome.server.tools;

import com.martiansoftware.jsap.*;
import org.apache.commons.io.FileUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.reactome.server.domain.DiseaseItem;
import org.reactome.server.domain.GeneItem;
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
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;


public class Importer {
    private static final Logger logger = LoggerFactory.getLogger(Importer.class);
    private static final String RAW_ZIPPED_FILE_NAME = "curated_gene_disease_associations.tsv.gz";
    private static final String EXPORT_BINARY_FILE_NAME = "DiseaseName_UniProtAccNum.tsv";
    private static final String DOWNLOAD_LINK = "https://www.disgenet.org/static/disgenet_ap1/files/downloads/curated_gene_disease_associations.tsv.gz";
    private static final String DB_NAME = "digester";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";
    private static final String DB_CREATE = "create";
    //        private static final String DB_CREATE = "update";
    private static Map<String, String> settings = new HashMap<>();
    private static Session session;

    static {
        settings.put("connection.driver_class", "com.mysql.cj.jdbc.Driver");
        settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
//        settings.put(Environment.URL, "jdbc:mysql://localhost:3306/" + DB_NAME + "?&characterEncoding=utf-8&useUnicode=true&serverTimezone=America/Toronto&useSSL=false");
        settings.put(Environment.URL, "jdbc:mysql://localhost:3306/" + DB_NAME + "?&characterEncoding=utf-8&useUnicode=true&serverTimezone=America/Toronto");
        settings.put(Environment.USER, DB_USER);
        settings.put(Environment.PASS, DB_PASS);
        settings.put(Environment.HBM2DDL_AUTO, DB_CREATE);
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(DiseaseItem.class);
        configuration.addAnnotatedClass(GeneItem.class);
        SessionFactory sessionFactory = configuration.buildSessionFactory(new StandardServiceRegistryBuilder().applySettings(settings).build());
        session = sessionFactory.openSession();
    }


    public static void main(String[] args) throws Exception {

        SimpleJSAP jsap = new SimpleJSAP(
                DiseaseParser.class.getName(),
                "Read, transfer and save disease-gene association table data from file to database.",
                new Parameter[]{
                        new FlaggedOption("url", JSAP.URL_PARSER, DOWNLOAD_LINK, JSAP.REQUIRED, 'd', "url", "[Optional] The disease overlay table data in tsv/csv format with columns: 'diseaseId', 'diseaseName' and 'geneSymbol' from DisGeNet"),
                }
        );
        JSAPResult jsapResult = jsap.parse(args);
        try {
            BufferedReader bufferedReader = downloadFile(jsapResult.getURL("url"));
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream("src/main/java/org/reactome/server/tools/curated_gene_disease_associations.tsv.gz")), StandardCharsets.UTF_8));
            List<DiseaseItem> diseaseItems = new DiseaseParser(bufferedReader).getDiseaseItems();
            saveDiseaseItems(diseaseItems);
//            saveDiseaseName2UniProtAccNumBinaryDataAsTSV(DiseaseItemC);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    private static void saveDiseaseName2UniProtAccNumBinaryDataAsTSV(List<DiseaseItem> diseaseItems) {
        long start = System.currentTimeMillis();
        File file = new File(Paths.get("").toAbsolutePath().toString().concat(File.separator).concat(EXPORT_BINARY_FILE_NAME));
        BufferedOutputStream outputStream;
//        List<DiseaseItem> sorted = diseaseItems.stream().sorted().distinct().collect(Collectors.toList());
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            outputStream.write("#Disease\tUniProt\n".getBytes());
            for (DiseaseItem diseaseItem : diseaseItems) {
                for (GeneItem geneItem : diseaseItem.getGeneItems()) {
                    if (geneItem.getAccessionNumber() != null) {
                        outputStream.write(diseaseItem.getDiseaseName().concat("\t").concat(geneItem.getAccessionNumber().concat("\n")).getBytes());
                    }
                }
            }
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Load: " + diseaseItems.size() + " entry in: " + (System.currentTimeMillis() - start) / 1000.0 + "s into " + file.getName());
    }

    private static void saveDiseaseItems(List<DiseaseItem> diseaseItemList) {
        long start = System.currentTimeMillis();
        Transaction transaction = session.beginTransaction();
        diseaseItemList.forEach(session::save);
        transaction.commit();
        session.close();
        logger.info("Load: " + diseaseItemList.size() + " items in: " + (System.currentTimeMillis() - start) / 1000.0 + "s into database.");
    }

    private static BufferedReader downloadFile(URL url) throws IOException {
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

    private static void doHttpsDownload(URL url, File dest) {
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
//            dest.delete();
        }
    }
}