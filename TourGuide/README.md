# Tour Guide Application

## Structure
The application is separated in five modules, among which 
there are four Spring Applications

- **common** contains all models and dtos
- **spring applications**
    - **gps** gives API to GpsUtil jar
    - **rewards** gives API to RewardCentral jar
    - **trip-pricer** gives API to TripPriver jar
    - **tour-guide** is the main application
    
## How to run integration tests
If you want to run itest task from **tour-guide** module, 
first run instances of **gps**, **rewards**, and **trip-pricer** 
modules in separated terminals.
