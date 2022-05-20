package org.reactome.server.tools;

import com.martiansoftware.jsap.*;
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

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import static org.reactome.server.tools.Importer.*;


public class OpenTargetImporter {

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
            saveDiseaseItems(new OpenTargetParser(new BufferedReader(new FileReader(jsapResult.getString("file")))));
        } catch (Exception e) {
            logger.warn(e.getMessage());
            e.printStackTrace();
        } finally {
            if (session.isOpen()) session.close();
            System.exit(0);
        }
    }


}