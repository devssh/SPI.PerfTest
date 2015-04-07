Versions
------------

The current version of the project is based on the Gatling version : **2.0.0-M3a**.

Usage
-----

Checkout the project and run the simulations with the following command :

```
mvn gatling:execute
```

To run a specific simulation you have to use this command :

```
mvn gatling:execute -Dgatling.simulation=basic.BasicExampleSimulation

mvn gatling:execute -Dgatling.simulationClass=spicinemas.simulations.NowShowingSimulation
mvn gatling:execute -Dgatling.simulationClass=spicinemas.simulations.ShowTimeSimulation
```

Data Setup

```
psql -d spi_cinemas -h 192.168.57.104 -U postgres -c \ 
"COPY ( select slugged_movie_name as movie_name, id as session_id, cinema_name from sessions where start_time > '2015-04-11' ) TO STDOUT WITH CSV HEADER " \
 > sessions.csv
```

psql -d spi_cinemas -h 192.168.57.104 -U postgres \
-c "COPY ( select slugged_movie_name as movie_name, id as session_id, cinema_name from sessions where start_time > '2015-04-11' and cinema_name != 'thecinema@BROOKEFIELDS' ) TO STDOUT WITH CSV HEADER " \ 
>work/satyam/SPICinemasPerfTest/src/test/resources/data/sessions.csv

psql -d spi_cinemas -h 192.168.57.104 -U postgres \
-c "COPY ( select email from users where is_active = 't' limit 10000 ) TO STDOUT WITH CSV HEADER " \
 > work/satyam/SPICinemasPerfTest/src/test/resources/data/users.csv