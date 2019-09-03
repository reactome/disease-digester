
[<img src=https://user-images.githubusercontent.com/6883670/31999264-976dfb86-b98a-11e7-9432-0316345a72ea.png height=75 />](https://reactome.org)

# Reactome Disease Digester
To represent the disease-gene binary relationship with a REACTOME overlay           
---

digester-importer [usage:]

- use maven plugin to package/install codes into executable jar file, remember to change the **database configuration** in 
location: `org.reactome.server.tools.Importer.java` before you run command

- `java -jar ${classpath}/digester-importer-jar-with-dependencies.jar -d`

disease-digester api [usage:]

- run: `mvn tomcat7:run-war`

- [/disease-digester/findAll?pageNumber=2&pageSize=40&sortBy=diseaseName&orderBy=asc]()

- [/disease-digester/findAll?pageNumber=2&pageSize=40&sortBy=geneNumber&orderBy=asc]()

- [/disease-digester/findByDiseaseClass?diseaseClass=immune&pageNumber=1&pageSize=40&sortBy=diseaseName&orderBy=asc]()

- [/disease-digester/findByDiseaseClass?diseaseClass=immune&pageNumber=1&pageSize=40&sortBy=geneNumber&orderBy=asc]()

- [/disease-digester/findByDiseaseClass?diseaseClass=immune&pageNumber=1&pageSize=40&sortBy=diseaseName&orderBy=desc]()

- [/disease-digester/findByDiseaseClass?diseaseClass=immune&pageNumber=1&pageSize=40&sortBy=geneNumber&orderBy=desc]()

- [/disease-digester/findByDiseaseName?diseaseName=immune&pageNumber=1&pageSize=40&sortBy=diseaseName&orderBy=asc]()

- [/disease-digester/findByDiseaseName?diseaseName=immune&pageNumber=1&pageSize=40&sortBy=geneNumber&orderBy=asc]()

- [/disease-digester/findByDiseaseName?diseaseName=immune&pageNumber=1&pageSize=40&sortBy=diseaseName&orderBy=desc]()

- [/disease-digester/findByDiseaseName?diseaseName=immune&pageNumber=1&pageSize=40&sortBy=geneNumber&orderBy=desc]()

and response body should something be like:
```json
{
  "pageNumber": 2,
  "pageSize": 40,
  "sortBy": "diseaseName",
  "orderBy": "asc",
  "totalCount": 20284,
  "diseaseItems": [
    {
      "diseaseId": "C0751594",
      "diseaseName": "Zellweger_Like_Syndrome",
      "diseaseClass": "Congenital, Hereditary, and Neonatal Diseases and Abnormalities",
      "geneItems": [
        {
          "geneId": "55670",
          "geneSymbol": "PEX26",
          "accessionNumber": "Q7Z412"
        },
        {
          "geneId": "5194",
          "geneSymbol": "PEX13",
          "accessionNumber": "Q92968"
        }]
    },
    {
      "diseaseId": "C1839615",
      "diseaseName": "X_linked_myopathy_with_excessive_autophagy",
      "diseaseClass": "Nervous System Diseases",
      "geneItems": [
        {
          "geneId": "203547",
          "geneSymbol": "VMA21",
          "accessionNumber": "Q3ZAQ7"
        }
      ]
    }
  ]
}
```



## Task list, ordered in descending importance: 

### 1. disease overlay table showed in webpage: 

create a table show in reactome webpage (Reactome >> Tools >> Disease Overlay) with format like:

|Disease name|Disease class|Number of genes|Gene list|Disease id|Check in Pathway Browser|       
|:---|:----|:---|:---|:---|:---|:---|       
|Testotoxicosis|Cancer|1|LHCGR|C1504412|[Analysis](https://reactome.org/PathwayBrowser#/DTAB=AN&ANALYSIS=)|     


features about this table:

- table data from DisGeNet is a binary data about 'Entity - Molecule', This is reformatted to aggregate all molecules 
related to one entity: Clicking the last column named 'Analysis' in each row link will execute pathway analysis for all 
proteins associated with Entity. and redirect to the analysis result in Pathway Browser page

- allow to order by Disease name and Number of genes, ascending and
descending, (sort whole table, so that the subsequent retrieved results still keep the same order) 

- pagination **Done √**

- ★ Make the column "Disease name" searchable with an auto-completing field
shown above the column (searchable against whole data from database with pagination) 

- within each box, order the gene names alphabetically **Done √**

- in the future, there will be an additional column "Disease class", with
a mapping file from "Disease ID" to "Disease Class". The new "Disease
class" column should also be searchable and auto-completable.  This is
not for now, just as a heads-up. Please note that there is an n:m mapping. If you have more than one disease class, 
then duplicate the row, one row for each disease class. 


### 2. interactor overlay:
**this task should be another part which different from task one(not include in task one)**

 - Reformat the table above to be suitable as input table for user-provided **interactor overlay**.
 
 - Desirable additional features of interactor overlay
     - Provide target URL for linking to external data resource for Entity
        - Possible implementation through CompactIdentifiers
     - Colour overlay boxes differently for different types of Entity
     - Provide different visual cues in pathway viewer for different types of Entity, for example “blue bubble in top 
     right corner of protein”.
     - Allow more than one interactor overlay type at the same time. Perhaps limit to four, one for each corner
