# SpacestoreRestApp


### Technology Stack
Component                    |    Technology                                                      
---                          |    ---                                                             
Frontend                     |    React.js                                                        
Backend (REST)               |    [SpringBoot](https://projects.spring.io/spring-boot) (Java)     
RDMS       | MySQL
Server Build                 |    Maven(Java)                                                     


### Build Backend (SpringBoot Java)
```bash
# Maven Build : Navigate to the root folder where pom.xml is present and run:
mvn clean install
```

### Accessing Application
Component         | URL                                      | Credentials
---               |------------------------------------------| ---
Backend         | http://localhost:8080/SpacestoreRestAdmin | ``
H2 Database       |         |  Driver:`org.h2.Driver` <br/> JDBC URL:`jdbc:h2:mem:demo` <br/> User Name:`sa`
Swagger (API Ref) | http://localhost:9119/swagger-ui.html    | 
