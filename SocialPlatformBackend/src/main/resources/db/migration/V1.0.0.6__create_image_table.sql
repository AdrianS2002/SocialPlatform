CREATE TABLE image (
     id          SERIAL PRIMARY KEY,
     filename    VARCHAR(255) NOT NULL,
     path        TEXT NOT NULL,
     uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     album_id    INTEGER NOT NULL,
     FOREIGN KEY (album_id) REFERENCES album(id) ON DELETE CASCADE
);