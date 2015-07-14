Run all simulations
-------------------

```bash
$ sbt test
```

Run a single simulation
-----------------------

```bash
$ sbt "testOnly spi.simulations.EndToEnd"
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
