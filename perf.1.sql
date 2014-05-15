create view session_agg_experiences (id, experiences)
as
SELECT s.id,
      string_agg(distinct(se.experience), ',') AS experiences
      FROM sessions s   
      LEFT OUTER JOIN session_experiences se ON se.session_id = s.id  
      where se.active = true
      group by s.id;


SELECT s.id, s.start_time, m.language, s.show_number, s.movie_name,  
s.screen_name, s.cinema_name, s.slugged_movie_name, s.session_status, m.certification,  
se.experiences AS experiences,  
c.display_name  AS cinema_display_name  
FROM sessions s   
INNER JOIN cinemas c ON s.cinema_name = c.name  
LEFT OUTER JOIN movies m ON s.movie_name = m.name and m.name = s.movie_name  
LEFT OUTER JOIN session_agg_experiences se ON se.id = s.id  
where s.id = 'LUXE1285'      

create index idx_cinemas_name on cinemas (name);
create index idx_cinemas_display_name on cinemas (display_name);

alter table orders add column created_date date;
update orders set created_date = date(created_at);
create index orders_created_date on orders (created_date);
create index orders_customer_email on orders (customer_email);


#drop index idx_cinemas_name;
#drop index idx_cinemas_display_name;

#drop index orders_customer_email;
#drop index orders_created_date;
#alter table orders drop column created_date;