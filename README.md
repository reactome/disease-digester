
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

and response body should something be like:
```json
{
  "content": [
    {
      "diseaseId": "C0342302",
      "diseaseName": "Brittle_diabetes",
      "diseaseClass": "Immune System Diseases",
      "geneItems": [
        {
          "geneId": "1356",
          "geneSymbol": "CP",
          "accessionNumber": "P00450"
        },
        {
          "geneId": "23274",
          "geneSymbol": "CLEC16A",
          "accessionNumber": "Q2KHT3"
        }
      ]
    },
    {
      "diseaseId": "C0014518",
      "diseaseName": "Toxic_Epidermal_Necrolysis",
      "diseaseClass": "Immune System Diseases",
      "geneItems": [
        {
          "geneId": "857",
          "geneSymbol": "CAV1",
          "accessionNumber": "Q03135"
        }
      ]
    }
  ],
  "pageable": {
    "sort": {
      "sorted": false,
      "unsorted": true
    },
    "pageNumber": 1,
    "pageSize": 20,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 19,
  "number": 1,
  "size": 20,
  "sort": {
    "sorted": false,
    "unsorted": true
  },
  "numberOfElements": 20
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