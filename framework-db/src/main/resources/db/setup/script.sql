+--Change the DB name according to the env (in local it can be framework-local-db )
create database "framework-{env}-db";

create user framework_{env}_admin with password '{DB_password}';

GRANT ALL privileges on database "framework-{env}-db" to oms_{env}_admin;

--This should be run on created DB
ALTER SCHEMA public OWNER to postgres;

GRANT ALL PRIVILEGES ON SCHEMA public TO framework_{env}_admin;