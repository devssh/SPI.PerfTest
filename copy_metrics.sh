#!/bin/bash
mkdir -p "logs/${1}"
log="logs/${1}/"
curl http://192.168.57.154:8081/metrics?pretty=true -o "${log}154_81.json"
curl http://192.168.57.154:8083/metrics?pretty=true -o "${log}154_83.json" 
curl http://192.168.57.69:8081/metrics?pretty=true -o "${log}69_81.json" 
curl http://192.168.57.69:8083/metrics?pretty=true -o "${log}69_83.json" 
curl http://192.168.57.68:8081/metrics?pretty=true -o "${log}68_81.json" 
curl http://192.168.57.68:8083/metrics?pretty=true -o "${log}68_83.json" 
curl http://192.168.57.70:8081/metrics?pretty=true -o "${log}70_81.json" 
curl http://192.168.57.70:8083/metrics?pretty=true -o "${log}70_83.json" 

open -a firefox "${log}/*.json"
wc -l "{log}/*.log"
