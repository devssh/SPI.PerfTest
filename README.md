##Steps before running performance test 

* reindex and vaccum databases
* run the test over wired connection. turn off wifi
* make host entry for test app domain to point nginx, so we'll not hit the firewall
* setup ulimit (see below)
*

Run a single simulation
-----------------------

```bash
$ sbt "gatling:testOnly spi.simulations.EndToEnd"
```

Open the last report
--------------------

```bash
$ sbt lastReport
```

CentOs Setup

```bash
ulimit -n 65536
sudo sysctl -w net.ipv4.ip_local_port_range="1025 65535"
echo 300000 | sudo tee /proc/sys/fs/nr_open
echo 300000 | sudo tee /proc/sys/fs/file-max
```          

Mac Setup 
```bash
ulimit -n 65536
```

