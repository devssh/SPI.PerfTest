#!/bin/sh
query="select email,mobile_number from users where is_active='t' and password='yd+8vQnf2ajO3RZxAecJXw=='"
limit=10000
psql -d spi_cinemas -h 192.168.57.99 -U postgres -c "COPY ($query limit $limit ) TO STDOUT WITH CSV HEADER " > ./src/test/resources/users.csv
scp -i ~/.ssh/amazaon_load_test_instance.pem ./src/test/resources/users.csv centos@54.173.87.119:~/SPICinemasPerfTest/src/test/resources/users.csv
