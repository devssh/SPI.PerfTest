#!/bin/sh
psql -d spi_cinemas -h 192.168.57.104 -U postgres -c "COPY (select email from users where is_active='t' and password='c++0IkxtJVE='  limit 10000 ) TO STDOUT WITH CSV HEADER " > ./src/test/resources/data/users.csv
