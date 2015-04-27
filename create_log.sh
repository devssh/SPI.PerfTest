#!/bin/bash 
mkdir -p "logs/${1}"
log="logs/${1}/"
fab -P --linewise   -i ~/.ssh/sathyam_id_rsa -H 192.168.57.110 -u root -- tail -f /var/log/nginx/access.log >> "${log}ngnix0.log" &
fab -P --linewise   -i ~/.ssh/sathyam_id_rsa -H 192.168.57.110 -u root -- tail -f /var/log/nginx/error.log >> "${log}ngnix0_err.log" &
fab -P --linewise   -i ~/.ssh/sathyam_id_rsa -H 192.168.57.111 -u root -- tail -f /var/log/nginx/access.log >> "${log}ngnix1.log" &
fab -P --linewise   -i ~/.ssh/sathyam_id_rsa -H 192.168.57.111 -u root -- tail -f /var/log/nginx/error.log >> "${log}ngnix1_err.log" &
#fab -P --linewise   -i ~/.ssh/sathyam_id_rsa -H 192.168.57.70 -u root -- tail -f /spicinemas/logs/spicinemas-server-console.log >> "${log}app_server0.log" &
#fab -P --linewise   -i ~/.ssh/sathyam_id_rsa -H 192.168.57.154 -u root -- tail -f /spicinemas/logs/spicinemas-server-console.log >> "${log}app_server1.log" &
#fab -P --linewise   -i ~/.ssh/sathyam_id_rsa -H 192.168.57.69 -u root -- tail -f /spicinemas/logs/spicinemas-server-console.log >> "${log}static_server0.log" &
#fab -P --linewise   -i ~/.ssh/sathyam_id_rsa -H 192.168.57.68 -u root -- tail -f /spicinemas/logs/spicinemas-server-console.log >> "${log}static_server1.log" &
#fab -P --linewise   -i ~/.ssh/sathyam_id_rsa -H 192.168.57.155 -u root -- tail -f /logs/*.log  >> "${log}vista_dropwizard_server0.log" &
#fab -P --linewise   -i ~/.ssh/sathyam_id_rsa -H 192.168.57.156 -u root -- tail -f /logs/*.log  >> "${log}vista_dropwizard_server1.log" &
#fab -P --linewise   -i ~/.ssh/sathyam_id_rsa -H 192.168.57.106 -u root -- tail -f /root/installer-pg93-3.3.2/logs/pgpool.log  >> "${log}pg_pool.log" &
#tail -f /Volumes/VISTA*/LOG/visSalesServer.log >> "${log}vista_sales_server.log" &
#tail -f /Volumes/VISTA*/LOG/visDBEngine.log  >> "${log}vista_db.log" &
