	SELECT s.id, s.start_time, m.language, s.show_number, s.movie_name,  
            s.screen_name, s.cinema_name, s.slugged_movie_name, s.session_status, m.certification,  
            string_agg(distinct(se.experience), ',') AS experiences,  
            c.display_name  AS cinema_display_name  
            FROM sessions s   
            LEFT OUTER JOIN movies m ON s.movie_name = m.name and m.name = s.movie_name  
            LEFT OUTER JOIN session_experiences se ON se.session_id = s.id  
            INNER JOIN cinemas c ON s.cinema_name = c.name  
            where s.id = 'LUXE1285'
            AND  se.active = 'true' GROUP BY s.id, s.start_time, m.language, s.show_number, s.movie_name, 
             s.screen_name, s.cinema_name, s.slugged_movie_name, c.display_name, m.certification

-----------------------             

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

      --
      

      select
      string_agg(distinct(se.experience), ',') AS experiences
            
            from session_experiences se 
            where se.active = true
            and se.session_id = 'LUXE1285'
            group by se.session_id;            


      SELECT s.id, s.start_time, m.language, s.show_number, s.movie_name,  
      s.screen_name, s.cinema_name, s.slugged_movie_name, s.session_status, m.certification,  
      se.experiences AS experiences,  
      c.display_name  AS cinema_display_name  
      FROM sessions s   
      INNER JOIN cinemas c ON s.cinema_name = c.name  
      LEFT OUTER JOIN movies m ON s.movie_name = m.name and m.name = s.movie_name  
      LEFT OUTER JOIN session_agg_experiences se ON se.id = s.id  
      where s.id = 'LUXE1285'      


      SELECT s.id, s.start_time, m.language, s.show_number, s.movie_name,  
      s.screen_name, s.cinema_name, s.slugged_movie_name, s.session_status, m.certification,  
      (      select
      string_agg(distinct(se.experience), ',') AS experiences
            
            from session_experiences se 
            where se.active = true
            and se.session_id = 'LUXE1285'
            group by se.session_id

      ),
      c.display_name  AS cinema_display_name  
      FROM sessions s   
      INNER JOIN cinemas c ON s.cinema_name = c.name  
      LEFT OUTER JOIN movies m ON s.movie_name = m.name and m.name = s.movie_name        
      where s.id = 'LUXE1285'      


      create index idx_cinemas_name on cinemas (name);


                  SELECT s.id, s.start_time, m.language, s.show_number, s.movie_name,  
      s.screen_name, s.cinema_name, s.slugged_movie_name, s.session_status, m.certification,  
      se.experiences AS experiences,  
      c.display_name  AS cinema_display_name  
      FROM sessions s   
      INNER JOIN cinemas c ON s.cinema_name = c.name  
      LEFT OUTER JOIN movies m ON s.movie_name = m.name and m.name = s.movie_name  
      LEFT OUTER JOIN session_agg_experiences se ON se.id = s.id  
      where s.id = 'LUXE1285'      



-----------------------

      SELECT s.id
      FROM sessions s   
      INNER JOIN cinemas c ON s.cinema_name = c.name  
      where s.id = 'LUXE1285'      


      create view s_delMe
      as

      SELECT s.id, s.start_time, m.language, s.show_number, s.movie_name,  
      s.screen_name, s.cinema_name, s.slugged_movie_name, s.session_status, m.certification,  
      se.experiences AS experiences,  
      c.display_name  AS cinema_display_name  
      FROM sessions s   
      JOIN movies m ON s.movie_name = m.name and m.name = s.movie_name  
      LEFT OUTER JOIN session_agg_experiences se ON se.id = s.id  
      INNER JOIN cinemas c ON s.cinema_name = c.name  
      where s.id = 'LUXE1285'