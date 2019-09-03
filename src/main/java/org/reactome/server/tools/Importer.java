package org.reactome.server.tools;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.Parameter;
import com.martiansoftware.jsap.SimpleJSAP;
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
    private static final String FILE_NAME_GZIP = "curated_gene_disease_associations.tsv.gz";
    private static final String DOWNLOAD_LINK = "http://www.disgenet.org/static/disgenet_ap1/files/downloads/curated_gene_disease_associations.tsv.gz";
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
                        new FlaggedOption("url", JSAP.URL_PARSER, DOWNLOAD_LINK, JSAP.REQUIRED, 'd', "url", "The disease overlay table data in tsv/csv format with columns: 'diseaseId', 'diseaseName' and 'geneSymbol' from DisGeNet"),
                }
        );

        try {
            BufferedReader bufferedReader = downloadFile(jsap.parse(args).getURL("url"));
            saveDiseaseItems(new DiseaseParser(bufferedReader).getDiseaseItems());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    private static BufferedReader downloadFile(URL url) throws IOException {
        String directory = "./";
        File file = new File(directory, FILE_NAME_GZIP);
        if (file.exists()) {
            if (file.delete()) {
                logger.info("Found old data file exists, delete it and create new one.");
                file = new File(directory, FILE_NAME_GZIP);
            }
        }
        logger.info("Downloading Disease-gene Association File from: " + url);
        FileUtils.copyURLToFile(url, file);
        String fileLocation = file.getAbsolutePath();
        logger.info("Saved file in location: " + fileLocation);
        return new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(fileLocation)), StandardCharsets.UTF_8));
    }
}