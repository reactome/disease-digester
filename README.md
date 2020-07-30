
[<img src=https://user-images.githubusercontent.com/6883670/31999264-976dfb86-b98a-11e7-9432-0316345a72ea.png height=75 />](https://reactome.org)

# Reactome Disease Digester
To represent the disease-gene binary relationship with a REACTOME overlay           
---

digester-importer [usage:]

- use `mvn clean package` to package codes into executable jar file

- use `java -jar digester-importer-jar-with-dependencies.jar --help` to show the help message:
    ```
    Usage:
      org.reactome.server.tools.DiseaseParser [--help] [(-n|--name) <name>]
      (-u|--user) <user> (-p|--password) <password> [(-m|--mode) <mode>] [(-d|--url)
      <url>]
    
    Read, transfer and save disease-gene association table data from file to
    database.
    
    
      [--help]
            Prints this help message.
    
      [(-n|--name) <name>]
            [Optional] Specify the database (default: overlays)
    
      (-u|--user) <user>
            [Need] The database user name
    
      (-p|--password) <password>
            [Need] The database password
    
      [(-m|--mode) <mode>]
            [Optional] The session mode [create | create-drop | update] (default:
            create)
    
      [(-d|--url) <url>]
            [Optional] The disease overlay table data in tsv/csv format with
            columns: 'diseaseId', 'diseaseName' and 'geneSymbol' from DisGeNet
            (default:
            https://www.disgenet.org/static/disgenet_ap1/files/downloads/curated_gene_disease_associations.tsv.gz)
    ```
- use `java -jar digester-importer-jar-with-dependencies.jar -n DATABASE_NAME -u USER_NAME -p PASS_WORD` to download the latest update data and load into database

disease-digester api [usage:]

- run: `mvn tomcat7:run-war`

- [/disease-digester/findAll?page=2&size=40&sort=disease&order=asc]()

- [/disease-digester/findAll?page=2&size=40&sort=gene&order=asc]()

- [/disease-digester/findByDiseaseClass?class=immune&page=1&size=40&sort=disease&order=asc]()

- [/disease-digester/findByDiseaseClass?class=immune&page=1&size=40&sort=gene&order=asc]()

- [/disease-digester/findByDiseaseClass?class=immune&page=1&size=40&sort=disease&order=desc]()

- [/disease-digester/findByDiseaseClass?class=immune&page=1&size=40&sort=gene&order=desc]()

- [/disease-digester/findByDiseaseName?name=immune&page=1&size=40&sort=disease&order=asc]()

- [/disease-digester/findByDiseaseName?name=immune&page=1&size=40&sort=gene&order=asc]()

- [/disease-digester/findByDiseaseName?name=immune&page=1&size=40&sort=disease&order=desc]()

- [/disease-digester/findByDiseaseName?name=immune&page=1&size=40&sort=gene&order=desc]()

## Task list, ordered in descending importance: 

### 1. disease overlay table showed in webpage: 

create a table show in reactome webpage (Reactome >> Tools >> Disease Overlay) with format like:

|Disease name|Disease class|Number of genes|Gene list|Disease id|Check in Pathway Browser|       
|:---|:----|:---|:---|:---|:---|:---|       
|Testotoxicosis|Cancer|1|LHCGR|C1504412|[Analysis](https://reactome.org/PathwayBrowser#/DTAB=AN&ANALYSIS=)|     


features about this table:

- table data from DisGeNet is a binary data about 'Entity - Molecule', This is reformatted to aggregate all molecules 
related to one entity: Clicking the last column named 'Analysis' in each row link will execute pathway analysis for all 
genes associated with Entity. and redirect to the analysis result in Pathway Browser page

- allow to order by Disease name and Number of genes, ascending and
descending, (sort whole table, so that the subsequent retrieved results still keep the same order) **Done √**

- pagination **Done √**

- ★ Make the column "Disease name" searchable with an auto-completing field
shown above the column (searchable against whole data from database with pagination) **Done √**

- within each box, order the gene names alphabetically **Done √**

- in the future, there will be an additional column "Disease class", with
a mapping file from "Disease ID" to "Disease Class". The new "Disease
class" column should also be searchable and auto-completable.  This is
not for now, just as a heads-up. Please note that there is an n:m mapping. If you have more than one disease class, 
then duplicate the row, one row for each disease class. **Done √**


### 2. interactor overlay:
**this task should be another code repo which different from task one(and not include in task one)**

 - Reformat the table above to be suitable as input table for user-provided **interactor overlay**.
 
 - Desirable additional features of interactor overlay
     - Provide target URL for linking to external data resource for Entity
        - Possible implementation through CompactIdentifiers
     - Colour overlay boxes differently for different types of Entity
     - Provide different visual cues in pathway viewer for different types of Entity, for example “blue bubble in top 
     right corner of protein”.
     - Allow more than one interactor overlay type at the same time. Perhaps limit to four, one for each corner