create table if not exists image(
	id serial not null primary key,
	image_url VARCHAR(255),
	image_b64 VARCHAR
);



CREATE TABLE IF NOT EXISTS location (
    id SERIAL PRIMARY KEY,
    lat DOUBLE PRECISION,
    long DOUBLE PRECISION,
    description VARCHAR(255)
);


CREATE TABLE IF NOT EXISTS reports (
    id SERIAL NOT NULL PRIMARY KEY,
    user_id BIGINT,
    breed VARCHAR(50),
    description VARCHAR(255),
    dog_size VARCHAR(10),
    aggression_level VARCHAR(30),
    attended_state VARCHAR(30),
    image BIGINT,
    location BIGINT,
    dog_mental_state VARCHAR(50),
    dog_attended_state VARCHAR(50),
    created_at timestamp,
    created_by BIGINT,
    updated_at timestamp,
    updated_by BIGINT,
    CONSTRAINT fk_reports_users FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_reports_image FOREIGN KEY (image) REFERENCES image(id),
    CONSTRAINT fk_reports_location FOREIGN KEY (location) REFERENCES location(id)
);




