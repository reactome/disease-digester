[<img src=https://user-images.githubusercontent.com/6883670/31999264-976dfb86-b98a-11e7-9432-0316345a72ea.png height=75 />](https://reactome.org)

# Reactome Disease Digester 


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