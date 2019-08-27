[<img src=https://user-images.githubusercontent.com/6883670/31999264-976dfb86-b98a-11e7-9432-0316345a72ea.png height=75 />](https://reactome.org)

# Reactome Disease Digester
To represent the disease-gene binary relationship with a REACTOME overlay           
---

disease-import [usage:]
- use maven plugin to package/install codes into executable jar file, remember to change the database configuration in location: `org.reactome.server.tools.Importer.java` before you run command
- download raw data [curated_gene_disease_associations.tsv.gz](http://www.disgenet.org/static/disgenet_ap1/files/downloads/curated_gene_disease_associations.tsv.gz) from [DisGeNET](http://www.disgenet.org) , and unzip it release the `curated_gene_disease_associations.tsv` file as input          
- java -jar ${classpath}/digester-importer-jar-with-dependencies.jar -f ${location}/curated_gene_disease_associations.tsv


dataSource.driverClassName=com.mysql.cj.jdbc.Driver
dataSource.url=jdbc:mysql://${mysql.host}:${mysql.port}/${mysql.report.database}?characterEncoding=utf-8&useUnicode=true&serverTimezone=America/Toronto
dataSource.username=${mysql.user}
dataSource.password=${mysql.password}

hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
# validate | update | create | create-drop
hibernate.hbm2ddl.auto=validate
hibernate.show_sql=false
hibernate.format_sql=false
hibernate.use_sql_comments=false

###Task list, ordered in descending importance:

- introduce a new column "number of genes"  **Done √**

- allow to order by Disease name and Number of genes, ascending and
descending.  whole table

- pagination **Done √**

- Make the column "Disease name" searchable with an auto-completing field
shown above the column (!) whole with pagination

    - within each box, order the gene names alphabetically **Done √**

- the "check in-->" can be removed **Done √**

- disease ID is not very important, move column to be LAST column. **Done √**


- in the future, there will be an additional column "Disease class", with
a mapping file from "Disease ID" to "Disease Class". The new "Disease
class" column should also be searchable and auto-completable.  This is
not for now, just as a heads-up. Please note that there is an n:m mapping. If you have more than one disease class, then duplicate the row, one row for each disease class. 

disease class 
cancer
imm
lung cancer
sort with pagination searchable

should i use some jsp page to show the results or just privoide the restful apis?