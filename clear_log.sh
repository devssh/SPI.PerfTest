fab -P --linewise   -i ~/.ssh/sathyam_id_rsa -H 192.168.57.111,192.168.57.110 -u root -- 'echo ""  > /var/log/nginx/access.log ; echo ""  > /var/log/nginx/error.log'
fab -P --linewise   -i ~/.ssh/sathyam_id_rsa -H 192.168.57.154,192.168.57.70,192.168.57.69,192.168.57.68 -u root -- 'echo ""  > /spicinemas/logs/spicinemas-server-console_8080.log;echo ""  > /spicinemas/logs/spicinemas-server-console_8082.log; echo ""  > /spicinemas/logs/spi-cinemas-service.log; echo > /spicinemas/logs/spicinemas-server.err'
fab -P --linewise   -i ~/.ssh/sathyam_id_rsa -H 192.168.57.155 -u root -- 'echo ""  > /logs/sathyam.log' 
fab -P --linewise   -i ~/.ssh/sathyam_id_rsa -H 192.168.57.155 -u root -- 'echo ""  > /logs/escape.log' 
fab -P --linewise   -i ~/.ssh/sathyam_id_rsa -H 192.168.57.155 -u root -- 'echo ""  > /logs/luxe.log'
