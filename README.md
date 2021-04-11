
[<img src=https://user-images.githubusercontent.com/6883670/31999264-976dfb86-b98a-11e7-9432-0316345a72ea.png height=75 />](https://reactome.org)

# Reactome DisGeNET overlays 

Description
------
The Reactome DisGeNET overlays present a intuitive way for user to browser a target disease with its associated genes, and allow to one-click analyze a set of genes data with Reactome analysis service.

Prerequisites
------
* MySQL 5.7
* Maven 3.5
* Java  1.8
* A good network environment since the tool need to download data from DisGeNET

Installation
------
* create a data called `overlays` in your MySQL database: `CREATE DATABASE IF NOT EXISTS overlays DEFAULT CHARACTER SET utf8;`.
* create a new config node `<profile></profile>` section in your Maven `home/.m2/settings.xml` file like:
```xml
    <profile>
        <id>mvn_profile_local</id>
        <properties>
            <!-- MySQL Configuration For Local Test Only-->
            <mysql.host>localhost</mysql.host>
            <mysql.port>3306</mysql.port>
            <mysql.disease.database>overlays</mysql.disease.database>
            <mysql.user>your_username</mysql.user>
            <mysql.password>your_password</mysql.password>
            <template.server>https://dev.reactome.org/</template.server>
        </properties>
    </profile>
```
* clone project from github `git clone https://github.com/reactome/disease-digester.git`
* `cd disease-digester/`
* `mvn install`
* `java -jar target/digester-importer-jar-with-dependencies.jar --user=your_mysql_username --password=your_mysql_password`, this command&tool will prepare all project data by downloading&process file from DisGeNET into two database tables *gene* and *disease* in database *overlays*. 
* `mvn tomcat7:run-war-only -P mvn_profile_local`, `mvn_profile_local` is the profile name defined in your `.m2/settings.xml`(it's a good practice that config your passwords into settings file rather than hardcode into the codes).
* Then you will see server runs in [http://localhost:8080/overlays/disgenet](http://localhost:8080/overlays/disgenet).
* Or you can copy the file `disease-digester/target/overlays.war` into your Tomcat `webapps/` directory then restart the Tomcat get the same server running in port.
