#!/bin/sh
query="select movie_name as full_movie_name,slugged_movie_name as movie_name, session_id,cinema_name,date(start_time) as date,category from session_category_prices join sessions on session_id = sessions.id where start_time>CURRENT_DATE+1 and cinema_name in ('thecinema@BROOKEFIELDS')"
limit=300
psql -d spi_cinemas -h 192.168.57.99 -U postgres -c "COPY ( $query limit $limit ) TO STDOUT WITH CSV HEADER " > ./src/test/resources/sessions.csv
