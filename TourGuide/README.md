# Tour Guide Application
[![shields](https://img.shields.io/badge/version-0.0.1-blue)](https://shields.io/)
[![shields](https://img.shields.io/badge/made_in-java-orange)](https://shields.io/)
[![shields](https://img.shields.io/badge/for-openClassrooms_project-blueviolet)](https://shields.io/)
[![shields](https://img.shields.io/badge/using-spring-deepgreen)](https://shields.io/)

## Description
Tour Guide is an application that makes tourism even more fun! 
Make your children (or yourself) enjoy walking for hours from one attraction to another, gathering
rewards and saving money.

## Structure
The application is separated in five containers, among which 
there are four Spring Applications

- **common** contains all models and dtos
- **spring applications**
    - **gps** gives API to GpsUtil jar
    - **rewards** gives API to RewardCentral jar
    - **trip-pricer** gives API to TripPricer jar
    - **tour-guide** is the main application
    
##Technologies
- Java
- Spring Boot
- Docker
    
## How to run integration tests
Integration tests are not included in build lifecycle. After build, run following command in
 project root directory : 
 
 - _docker-compose up --build_. 
 
 Once all modules are ready, run 
 _itest_ task.
 
 ## Authors
####Trip Master