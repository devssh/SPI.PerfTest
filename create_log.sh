#!/bin/bash 
mkdir -p "logs/${1}"
log="logs/${1}/"
fab -P --linewise   -i ~/.ssh/sathyam_id_rsa -H 192.168.57.110,192.168.57.111 -u root -- tail -f /var/log/nginx/access.log >> "${log}ngnix.log" &
fab -P --linewise   -i ~/.ssh/sathyam_id_rsa -H 192.168.57.154 -u root -- tail -f /spicinemas/logs/spicinemas-server-console.log >> "${log}app_server.log" &
fab -P --linewise   -i ~/.ssh/sathyam_id_rsa -H 192.168.57.68,192.168.57.69 -u root -- tail -f /spicinemas/logs/spicinemas-server-console.log >> "${log}static_server.log" &
fab -P --linewise   -i ~/.ssh/sathyam_id_rsa -H 192.168.57.155,192.168.57.156 -u root -- tail -f /logs/*.log  >> "${log}vista_dropwizard_server.log" &
fab -P --linewise   -i ~/.ssh/sathyam_id_rsa -H 192.168.57.106 -u root -- tail -f /root/installer-pg93-3.3.2/logs/pgpool.log  >> "${log}pg_pool.log" &
tail -f /Volumes/VISTA*/LOG/visSalesServer.log >> "${log}vista_sales_server.log" &
tail -f /Volumes/VISTA*/LOG/visDBEngine.log  >> "${log}vista_sales_server.log" &
