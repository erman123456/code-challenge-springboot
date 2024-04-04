
# Build a REST API with Springboot MVN & Docker

## What are we using?
* Springboot - Web server
* H2 - Database
* Postman - Client Http
* Java - Code Language

## What will I need?
* [Postman](https://www.postman.com/) - Make API requests
* https://hub.docker.com/r/1000kit/h2 - Database Docker
* [Code editor](https://jetbrains.com/idea/) - Editor my code

## Features
* Authorization [register & login]
* Posts CRUD
* Users Read
* Request & response validation (Dto)

## step building docker
* Run cmd `docker-compose up -d` to pull & setup docker image h2-database
* Run cmd `docker build -t your-springboot-app .` to build docker image
* Run cmd `docker run -d -p 8080:8080 your-springboot-app` to run docker container app

# how do I try project run well?
* Run cmd `mvn test` to run test JUnit
* Run cmd `./mvnw spring-boot:run` to run project with debug
* Run cmd `docker run -d -p 8080:8080 your-springboot-app` to run project with docker container
* Base url `http://localhost:8080/` to test using postman or insomnia
* We have provided collection & environment postman in postman folder 

Happy coding :)
Best regards [Erman Sibarani]
