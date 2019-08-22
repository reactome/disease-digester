package org.reactome.server.tools;

import com.martiansoftware.jsap.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.reactome.server.domain.DiseaseItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class DiseaseParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiseaseParser.class);
    private static final String DB_NAME = "digester";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";
    private static final String DB_CREATE = "create";
    private static Map<String, String> settings = new HashMap<>();
    private static EntityManager entityManager;

    static {
        settings.put("connection.driver_class", "com.mysql.cj.jdbc.Driver");
        settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
        settings.put(Environment.URL, "jdbc:mysql://localhost:3306/" + DB_NAME + "?&characterEncoding=utf-8&useUnicode=true&serverTimezone=America/Toronto");
        settings.put(Environment.USER, DB_USER);
        settings.put(Environment.PASS, DB_PASS);
        settings.put(Environment.HBM2DDL_AUTO, DB_CREATE);
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(DiseaseItem.class);
        SessionFactory sessionFactory = configuration.buildSessionFactory(new StandardServiceRegistryBuilder().applySettings(settings).build());
        entityManager = sessionFactory.createEntityManager();
    }


    public static void main(String[] args) throws JSAPException {

        SimpleJSAP jsap = new SimpleJSAP(
                DiseaseParser.class.getName(),
                "Read and save disease table data from file to database.",
                new Parameter[]{
                        new FlaggedOption("fileName", JSAP.STRING_PARSER, JSAP.NO_DEFAULT, JSAP.REQUIRED, 'f', "fileName", "The disease overlay table data in tsv/csv format with columns: 'diseaseId', 'diseaseName' and 'geneSymbol' from DisGeNet").setList(true).setListSeparator(','),
                }
        );

        try {
            saveDiseaseItems(getDiseaseItemsFromFile(jsap.parse(args)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveDiseaseItems(List<DiseaseItem> diseaseItems) {
        entityManager.getTransaction().begin();
        diseaseItems.forEach(entityManager::persist);
        entityManager.getTransaction().commit();
        entityManager.close();
        LOGGER.info("Load " + diseaseItems.size() + " items into database.");
        System.exit(0);
    }

    public static List<DiseaseItem> getDiseaseItemsFromFile(JSAPResult config) throws Exception {
        return getDiseaseItemsFromFile(config.getString("fileName"));
    }

    public static List<DiseaseItem> getDiseaseItemsFromFile(String fileName) throws Exception {
        StringTokenizer st;

        BufferedReader TSVFile = new BufferedReader(new FileReader(fileName));
        String delimiter = fileName.endsWith(".tsv") ? "\t" : ",";

//      <-- read table header start -->
        String headerLine = TSVFile.readLine(); // Read header line.
        List<String> columns = Arrays.asList(Objects.requireNonNull(headerLine.split(delimiter)));
        int[] columnIndex = new int[]{
                columns.indexOf("diseaseId"),
                columns.indexOf("diseaseName"),
                columns.indexOf("geneSymbol"),
        };
//      <-- read table header start -->

        List<List<String>> dataRaw = new ArrayList<>();

//      <-- read content start -->
        String line = TSVFile.readLine(); // Read first data row.
        while (line != null) {
            st = new StringTokenizer(line, delimiter);
            List<String> dataArray = new ArrayList<>();
            while (st.hasMoreElements()) {
                dataArray.add(st.nextElement().toString());
            }
            dataRaw.add(Arrays.asList(
                    dataArray.get(columnIndex[0]),
                    dataArray.get(columnIndex[1]),
                    dataArray.get(columnIndex[2])));
            line = TSVFile.readLine(); // Read next line of data.
        }
        TSVFile.close();
//      <-- read content stop -->

//      <-- digest data start: group items by diseaseName, and aggregate genes -->
        Map<String, String> disease = new HashMap<>();
        Map<String, Set<String>> overlay = new HashMap<>();
        String diseaseId;
        String diseaseName;
        String geneSymbol;
        for (List<String> row : dataRaw) {
            diseaseId = row.get(0);
            diseaseName = row.get(1);
            geneSymbol = row.get(2);
            if (!overlay.containsKey(diseaseId)) {
                disease.put(diseaseId, diseaseName.replaceAll("[^\\w]+", "_"));
                overlay.put(diseaseId, new HashSet<>(Collections.singletonList(geneSymbol)));
            }
            overlay.get(diseaseId).add(geneSymbol);
        }
//      <-- digest data stop -->

        List<DiseaseItem> diseaseItems = new ArrayList<>();
        for (Map.Entry<String, String> entry : disease.entrySet()) {
            /*
              entry.key is disease id
              entry.value is disease name
              overlay.key is disease id
              overlay.value is gene set
             */
            DiseaseItem diseaseItem = new DiseaseItem();
            diseaseItem.setDiseaseId(entry.getKey());
            diseaseItem.setDiseaseName(entry.getValue());
            Set<String> geneSet = overlay.get(entry.getKey());
            diseaseItem.setGeneList((new ArrayList<>(geneSet)));
            diseaseItems.add(diseaseItem);
        }
        return diseaseItems;
    }
}