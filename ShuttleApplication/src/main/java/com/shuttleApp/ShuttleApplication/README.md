# Shuttle Application 

The Campus Shuttle App is a solution designed to streamline and enhance campus transportation services. 

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)



## Features

**Campus Shuttle App Features**

- **Using Geocoding for Accurate Location Data**
    - *Geocoding Services*: Passengers can input their addresses, and the app utilizes geocoding services to accurately convert these addresses into latitude and longitude coordinates. This ensures precise shuttle pick-up and drop-off locations.

- **Using Mapbox API for Accurate ETA Calculation**
    - *Mapbox Integration*: The app seamlessly integrates Mapbox APIs to provide passengers with highly accurate estimated time of arrival (ETA) information between locations. Passengers can rely on this data to plan their schedules effectively.

- **Dynamic Database Calls for Passenger Drop-Off Updates**
    - *Real-Time Updates*: The app employs dynamic database calls to monitor the status of passenger drop-offs. When a passenger reaches their destination, the database is updated in real time, ensuring accurate tracking of passengers on the shuttle.

- **Dynamic Database Calls for Passenger Additions at Campus**
    - *Efficient Campus Transportation*: When the shuttle returns to the campus, the app checks its location and initiates dynamic database calls. If there are available seats, additional students are added to the shuttle's passenger list, optimizing campus transportation services.


## Installation

### Prerequisites

Before you begin, ensure you have met the following requirements:

- **Java**: Your system should have Java installed. You can download it [here](https://www.oracle.com/java/technologies/javase-downloads.html).

- **Maven**: We use Maven as a build tool. You can download and install Maven [here](https://maven.apache.org/download.cgi).

- **PostgreSQL Database**: Our project relies on a PostgreSQL database. Please make sure you have it installed. You can download PostgreSQL [here](https://www.postgresql.org/download/).

### Database Setup

1. Create a new PostgreSQL database using your preferred PostgreSQL management tool or the command line.

2. Run the provided SQL script (`database.sql`) to create the necessary tables and set up the database schema.

   ```bash
   psql -U <your_username> -d <your_database_name> -a -f database.sql


## Usage

You can interact with the application through the following endpoints in your web browser:

### Shuttle Location Update
- This endpoint simulates updating the shuttle's location based on longitude and latitude.
    - Access in Browser: [Shuttle Location Update](http://localhost:8080/shuttleLocation?longitude=-76.13133627075227&latitude=43.03789557301325)

### Request Pickup
- This endpoint allows you to request a pickup by providing a valid SUID (Syracuse University ID).
    - Access in Browser: [Request Pickup](http://localhost:8080/requestPickup?suid=123456)

### Add Passenger
- Use this endpoint to add a passenger. Provide a valid SUID and address, which will be geocoded to obtain latitude and longitude.
    - Access in Browser: [Add Passenger](http://localhost:8080/addPassenger?suid=123451&address=%22415%20Clarendon%20St,%20syracuse,New%20York,%2013210)

Remember to adjust the URLs and port number (e.g., `8080`) as needed based on your application's configuration.

