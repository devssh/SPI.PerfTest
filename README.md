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
