#!/bin/sh
query="select movie_name as full_movie_name,slugged_movie_name as movie_name, session_id,cinema_name,date(start_time) as date,category from session_category_prices
 join sessions on session_id = sessions.id where
 sessions.id in (select id from sessions where status = 'ACTIVE' and date(start_time) in (CURRENT_DATE+1) order by start_time  limit 20)"
psql -d spi_cinemas -h 192.168.57.99 -U postgres -c "COPY ( $query ) TO STDOUT WITH CSV HEADER " > ./src/test/resources/sessions.csv
scp -i ~/.ssh/amazaon_load_test_instance.pem ./src/test/resources/sessions.csv centos@54.145.239.15:~/SPICinemasPerfTest/src/test/resources/sessions.csv
