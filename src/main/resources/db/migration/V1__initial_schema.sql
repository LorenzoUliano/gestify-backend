CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100),
    created_at TIMESTAMP,
    role VARCHAR(255) NOT NULL
);

CREATE TABLE subjects (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    user_id UUID REFERENCES users(id)
);

CREATE TABLE study_session (
    id UUID PRIMARY KEY,
    start_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    subject_id UUID REFERENCES subjects(id),
    user_id UUID REFERENCES users(id)
);

CREATE TABLE achievement (
    id UUID PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    unlocked_at TIMESTAMP WITHOUT TIME ZONE,
    user_id UUID REFERENCES users(id)
);