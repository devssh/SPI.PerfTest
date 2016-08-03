##Steps before running performance test 

* reindex and vaccum databases
* run the test over wired connection. turn off wifi
* make host entry for test app domain to point nginx, so we'll not hit the firewall
* setup ulimit (see below)
* after deploying app server run a warm up test

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
Update /etc/sysctl.conf
```
net.ipv4.tcp_max_syn_backlog = 40000
net.core.somaxconn = 40000
net.core.wmem_default = 8388608
net.core.rmem_default = 8388608
net.ipv4.tcp_sack = 1
net.ipv4.tcp_window_scaling = 1
net.ipv4.tcp_fin_timeout = 15
net.ipv4.tcp_keepalive_intvl = 30
net.ipv4.tcp_tw_reuse = 1
net.ipv4.tcp_moderate_rcvbuf = 1
net.core.rmem_max = 134217728
net.core.wmem_max = 134217728
net.ipv4.tcp_mem  = 134217728 134217728 134217728
net.ipv4.tcp_rmem = 4096 277750 134217728
net.ipv4.tcp_wmem = 4096 277750 134217728
net.core.netdev_max_backlog = 300000

net.inet.ip.portrange.first=32768
net.inet.ip.portrange.hifirst=32768
net.inet.tcp.msl=1000

# for postgresql
 kern.sysv.shmall=65536
 kern.sysv.shmmax=16777216

# defaults
net.inet.ip.portrange.lowfirst=1023
net.inet.ip.portrange.lowlast=600
net.inet.ip.portrange.last=65535
net.inet.ip.portrange.hilast=65535
```
