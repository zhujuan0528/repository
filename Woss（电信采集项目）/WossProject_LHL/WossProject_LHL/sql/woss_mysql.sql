CREATE TABLE bidr(
   id   int(7)   primary key,
   AAA_login_name   VARCHAR(100),
   login_ip VARCHAR(100),
   NAS_ip VARCHAR(100),
   login_date date,
   loginout_date date,
   time_duration int(20)
);