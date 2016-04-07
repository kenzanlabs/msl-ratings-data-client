# MSL Ratings Data Client

This repository is a sub-repository of the [Kenzan Million Song Library](https://github.com/kenzanmedia/million-song-library) (MSL) project, a microservices-based Web application built using [AngularJS](https://angularjs.org/), a [Cassandra](http://cassandra.apache.org/) NoSQL database, and [Netflix OSS](http://netflix.github.io/) tools.

> **NOTE:** For an overview of the Million Song Library microservices architecture, as well as step-by-step instructions for running the MSL demonstration, see the [Million Song Library Project Documentation](https://github.com/kenzanmedia/million-song-library/tree/develop/docs).

## Overview

Instead of a traditional edge/middle architecture, the Million Song Library project uses a simplified edge/data client architecture.

The data clients are JARs, each one containing the methods and data transfer objects (DTOs) needed to access all of the tables in a Cassandra cluster.

To enhance scalability and configuration flexibility, the Cassandra tables are split into three independent clusters: account, catalog, and rating. Each of these clusters has a data client JAR dedicated to accessing it: account-data-client, catalog-data-client, and rating-data-client, respectively. This means that a microservice that needs to access Cassandra data will include one or more of the data client JARs.

> **NOTE:** If you receive an error when running any of the commands below, try using `sudo` (Mac and Linux) or run PowerShell as an administrator (Windows).

## Packaging and Installation

Use the following command to package and compile the application code:

```
mvn clean package && mvn -P install compile
```

## Code Formatting

If you make changes to the application code, use the following command to format the code according to [project styles and standards](https://github.com/kenzanmedia/styleguide):

```
mvn clean formatter:format
```

## Testing and Reports

Use the following command to run all unit tests and generate a report on test coverage (report is located in `/target/site/cobertura/index.html`):

```
mvn cobertura:cobertura
```

Use the following command to run all unit tests without generating a report:

```
mvn test
```
