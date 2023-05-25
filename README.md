# lexicon-movie-service
This is a servie to list all movies from Cinama World, Film World cinemas and highlight cheapest price of ticket.
The movies data are crawled from external API which is <b>unreliable</b>. <b>Therefore, result list will be empty or only movies of one cinema are retunred in sometimes.</b>
<br>
This service is applied <b>reactive</b> technologies.
# Prerequisite
- Install Java 16
- Install & configure maven 3.9.2
- Install Docker
- Install & setup Lombok
# Project import
1/ Clone repository<br>
2/ Import project into IDE.<br>
3/ Enable context annotation for Lombok (if required)<br>
# Execute applciation
## From JAR file
1/ Run command: to package codes into executable JAR file
```script
mvn clean package
```
2/ Run command: to run Spring boot application
```script
java -jar -Dspring.profiles.active=docker movie-service-0.0.1-SNAPSHOT.jar
```
3/ Run command: to call the API
```script
curl --location 'localhost:8080/movies/compare'
```
## From Docker
1/ Run command: to build docker file
```script
docker build -t lexicon-movie-service .
```
2/ Run command: to run Spring boot application
```script
docker run -dp 8080:8080 lexicon-movie-service
```
3/ Run command: to call the API
```script
curl --location 'localhost:8080/movies/compare'
```
# API
- GET `/movies/compare`: Get a list of all movies from Cinama World, Film World cinemas. Highlight cheapest price of ticket<br>
**Response** : The lowest price will be marked ```"cheapest": true```
```JSON
[
    {
        "title": "Rogue One: A Star Wars Story",
        "poster": "https://m.media-amazon.com/images/M/MV5BMjEwMzMxODIzOV5BMl5BanBnXkFtZTgwNzg3OTAzMDI@._V1_SX300.jpg",
        "movies": [
            {
                "title": "Rogue One: A Star Wars Story",
                "type": "movie",
                "poster": "https://m.media-amazon.com/images/M/MV5BMjEwMzMxODIzOV5BMl5BanBnXkFtZTgwNzg3OTAzMDI@._V1_SX300.jpg",
                "actors": "Felicity Jones, Diego Luna, Alan Tudyk, Donnie Yen",
                "price": 25.0,
                "provider": "Cinema World",
                "id": "cw3748528",
                "cheapest": true
            },
            {
                "title": "Rogue One: A Star Wars Story",
                "type": "movie",
                "poster": "https://m.media-amazon.com/images/M/MV5BMjEwMzMxODIzOV5BMl5BanBnXkFtZTgwNzg3OTAzMDI@._V1_SX300.jpg",
                "actors": "Felicity Jones, Diego Luna, Alan Tudyk, Donnie Yen",
                "price": 28.0,
                "provider": "Film World",
                "id": "fw3748528",
                "cheapest": false
            }
        ]
    },
    ...
]
```
# Web UI
- After start the application
- Open browser: http://localhost:8080/movies

# Implementation
- Movies crawling configuration is in ```application.yaml```.
- If you want to crawl movies from another source, you can implement new crawler by ```implements MovieCrawler``` interface, and provide a converter to convert the response.

