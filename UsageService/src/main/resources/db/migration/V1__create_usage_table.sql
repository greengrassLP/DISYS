CREATE TABLE IF NOT EXISTS usage (
hour                TIMESTAMP    PRIMARY KEY,
community_produced  DOUBLE PRECISION NOT NULL,
community_used      DOUBLE PRECISION NOT NULL,
grid_used           DOUBLE PRECISION NOT NULL
);
