CREATE SCHEMA IF NOT EXISTS public;

CREATE TABLE IF NOT EXISTS public.session
(
    user_id    BIGINT,
    token      VARCHAR(256) NOT NULL,
    session_id VARCHAR(256) NOT NULL,
    PRIMARY KEY (user_id)
)
;

CREATE TABLE IF NOT EXISTS public.stories
(
    stories_id BIGINT,
    session_id VARCHAR(256),
    stories    VARCHAR NOT NULL,
    PRIMARY KEY (stories_id)
)
;