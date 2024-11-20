--Initial Migration scripts (Queries to create tables)

create table if not exists users (
	id serial NOT NULL PRIMARY KEY,
	name varchar(200) not null,
	email varchar(50),
	password varchar(512),
	is_active boolean default true,
	created_at timestamp,
	updated_at timestamp,
	created_by int8,
	updated_by int8
);

create table if not exists roles (
	id serial NOT NULL PRIMARY KEY,
	user_role varchar(30) NOT NULL UNIQUE,
	role_message_code varchar(250) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_roles (
	id serial not null primary key,
    user_id bigint,
    role_id bigint,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

INSERT INTO public.roles
(id, user_role, role_message_code)
VALUES(1, 'ROLE_ADMIN', 'role.admin'),
(2, 'ROLE_CLIENT', 'role.client')
ON CONFLICT (id) DO NOTHING;