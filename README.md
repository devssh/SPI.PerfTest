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
mvn gatling:execute -Dgatling.simulationClass=spicinemas.simulations.EndToEndSimulation
```

Data Setup

```
./load_sessions.sh 
./load_users.sh 
```
