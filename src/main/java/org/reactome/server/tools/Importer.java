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

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;


public class Importer {
    private static final Logger logger = LoggerFactory.getLogger(Importer.class);
    private static final String FILE_NAME = "curated_gene_disease_associations.tsv";
    private static final String FILE_NAME_GZIP = "curated_gene_disease_associations.tsv.gz";
    private static final String URL_FTP = "http://www.disgenet.org/static/disgenet_ap1/files/downloads/curated_gene_disease_associations.tsv.gz";
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


    public static void main(String[] args) throws JSAPException {

        SimpleJSAP jsap = new SimpleJSAP(
                DiseaseParser.class.getName(),
                "Read, transfer and save disease-gene association table data from file to database.",
                new Parameter[]{
                        new FlaggedOption("fileName", JSAP.STRING_PARSER, JSAP.NO_DEFAULT, JSAP.NOT_GREEDY, 'f', "fileName", "The disease overlay table data in tsv/csv format with columns: 'diseaseId', 'diseaseName' and 'geneSymbol' from DisGeNet"),
                        new FlaggedOption("urlFtp", JSAP.STRING_PARSER, JSAP.NO_DEFAULT, JSAP.NOT_GREEDY, 'd', "urlFtp", "The disease overlay table data in tsv/csv format with columns: 'diseaseId', 'diseaseName' and 'geneSymbol' from DisGeNet"),
                }
        );

        try {
            // TODO: 19-8-29 download file automatically from DisGeNet
            saveDiseaseItems(new DiseaseParser(getFileFromArg(jsap.parse(args))).getDiseaseItems());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static BufferedReader getFileFromArg(JSAPResult arg) throws IOException {
        // TODO: 19-8-30 add more parameter from outside
        BufferedReader file;
        if (arg.getString("urlFtp") != null) {
            file = downloadFile(arg.getString("urlFtp"));
        } else if (arg.getString("urlFtp") == null) {
            logger.info("No [urlFtp] value been given, use internal default one: " + URL_FTP);
            file = downloadFile(URL_FTP);
        } else if (arg.getString("fileName") != null) {
            logger.info("No [urlFtp] value been given, use internal default one: " + URL_FTP);
            String fileName = arg.getString("fileName");
            assert fileName.endsWith("sv") : "Unknown file type: " + fileName.subSequence(fileName.lastIndexOf('.'), fileName.length() - 1);
            file = new BufferedReader(new FileReader(fileName));
        } else {
            file = new BufferedReader(new InputStreamReader(DiseaseItem.class.getResourceAsStream(FILE_NAME)));
        }
        return file;
    }

    private static void saveDiseaseItems(List<DiseaseItem> diseaseItems) {
        long start = System.currentTimeMillis();
        Transaction transaction = session.beginTransaction();
        diseaseItems.forEach(session::save);
        transaction.commit();
        session.close();
        System.out.println("Load: " + diseaseItems.size() + " items in: " + (System.currentTimeMillis() - start) / 1000.0 + "s into database.");
        System.exit(0);
    }


    private static BufferedReader downloadFile(String urlFtp) throws IOException {
        logger.info("Downloading Disease-gene Association File...");
        URL url = new URL(urlFtp);
        String directory = "./";
        File file = new File(directory, FILE_NAME_GZIP);
        if (file.exists()) {
            if (file.delete()) {
                logger.info("Find old file exists, delete it and create new one.");
                file = new File(directory, FILE_NAME_GZIP);
            }
        }
        FileUtils.copyURLToFile(url, file);
        // TODO: 19-8-31 add unzip information to notice the process
        return new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(file.getAbsolutePath())), StandardCharsets.UTF_8));
    }
}