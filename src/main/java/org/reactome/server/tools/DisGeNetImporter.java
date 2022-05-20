package org.reactome.server.tools;

import com.martiansoftware.jsap.*;

import java.io.*;

import static org.reactome.server.tools.Importer.*;


public class DisGeNetImporter {


    public static void main(String[] args) throws Exception {
        /*prepare the args*/
        SimpleJSAP jsap = new SimpleJSAP(
                DisGeNetParser.class.getName(),
                "Read, transfer and save disease-gene association table data from file to database.",
                new Parameter[]{
                        new FlaggedOption("name", JSAP.STRING_PARSER, DB_NAME, JSAP.NOT_REQUIRED, 'n', "name", "[Optional] Specify the database"),
                        new FlaggedOption("user", JSAP.STRING_PARSER, null, JSAP.REQUIRED, 'u', "user", "[Need] The database user name"),
                        new FlaggedOption("password", JSAP.STRING_PARSER, null, JSAP.REQUIRED, 'p', "password", "[Need] The database password"),
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
            // TODO: 2020/6/8 refactor the code in this section
            BufferedReader bufferedReader = downloadFile(jsapResult.getURL("url"));
            saveDiseaseItems(new DisGeNetParser(bufferedReader));
        } catch (Exception e) {
            logger.warn(e.getMessage());
            e.printStackTrace();
        } finally {
            if (session.isOpen()) session.close();
            System.exit(0);
        }
    }

}