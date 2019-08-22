[<img src=https://user-images.githubusercontent.com/6883670/31999264-976dfb86-b98a-11e7-9432-0316345a72ea.png height=75 />](https://reactome.org)

# Reactome Disease Digester
To represent the disease-gene binary relationship with a REACTOME overlay           
---
disease-import [usage:] 
- download raw .gz data from [DisGeNET](http://www.disgenet.org/static/disgenet_ap1/files/downloads/curated_gene_disease_associations.tsv.gz) , and unzip it get the `curated_gene_disease_associations.tsv` file as input
- ```java -jar org.reactome.server.tools.DiseaseParser -f curated_gene_disease_associations.tsv```

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