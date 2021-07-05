# Nested-set-tree

A simple implementation of a nested set tree in spring boot and postgres without using recursion, just because I couldn't find any online.

Sql functions are based on the implementation in [werc/TreeTraversal](https://github.com/werc/TreeTraversal).

## Getting Started

`gradle clean build bootRun` from the projects root folder should build and run the service given the prerequisites are met.

## Technologies

The tech stack core of the project:

* [JDK 11](https://docs.oracle.com/en/java/javase/11/?xd_co_f=eb8de50e-09cd-4af3-975f-e509c3e78beb)
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Postgresql](https://www.postgresql.org/)

### Prerequisites

In order to run the service locally you need to have an instance of Postgresql 9.6 cluster running (either installed locally or running in a Docker container) with a database named `nestedTree`.

### Installing

The standard installations of the tools mentioned above will suffice.
On the first run the service will initialize the local database with the necessary table structure. 
Running this docker command will get you an instance that needs no further configuration:
```docker
docker run --name=nestedTree -e POSTGRES_PASSWORD=CHANGEME -e POSTGRES_USER=CHANGEME -e POSTGRES_DB=nestedTree -p 5432:5432 -d postgres:9.6
```
