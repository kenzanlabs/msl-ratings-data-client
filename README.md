# MSL ratings data client

## Overview
Data Client Layer
Simplification to a traditional edge/middle architecture, this project uses a edge/data client architecture instead.
The data clients are jars, each containing all the methods and DTOs for accessing all the tables within a Cassandra cluster.
To enhance scalability and configuration flexibility, the Cassandra tables are split into three independent clusters: account, catalog, and rating.
Each of these clusters has a data client jar dedicated to accessing it: account-data-client, catalog-data-client, and rating-data-client, respectively.

So a microservice that needs to access Cassandra data will include one or more of the data client jars.

| Table           | Method  |
|:-------------:| -----:|
| **average_ratings** |addOrUpdateAverageRating(AverageRatingDto) |
| | Observable<AverageRatingDto> getAverageRating(UUID contentId, String contentType) |
| | deleteAverageRating(UUID contentId, String contentType) |
| **user_ratings** | addOrUpdateUserRatings(UserRatingsDto) |
| | Observable<UserRatingsDto> getUserRatings(UUID userUuid, String contentType, UUID contentUuid) |
| | Observable<ResultSet> getUserRatings(UUID userUuid, String contentType, Optional<Integer> limit) |
| | Observable<ResultSet> getUserRatings(UUID userUuid, Optional<Integer> limit) |
| | Observable<Result<UserRatingsDto> map(Observable<ResultSet>) |
| | deleteUserRatings(UUID userUuid, String contentType, UUID contentUuid) | 

## Packaging & Installation

```bash 
mvn clean package && mvn -P install compile
```

To format code
```
mvn clean formatter:format
```

##Reports
###Surefire reports:
```
mvn site
```
report gets generated under /target/site/index.html
 
###Cobertura
```
mvn cobertura:cobertura
```
report gets generated under /target/site/cobertura/index.html