CREATE DATABASE universidad_db;

USE universidad_db;

CREATE TABLE estudiantes (
    id INT IDENTITY(1,1) PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    carrera VARCHAR(100) NOT NULL,
    semestre INT NOT NULL
);

INSERT INTO estudiantes
    (codigo, nombre, apellido, carrera, semestre)
VALUES
    ('E001', 'Juan', 'Perez', 'Ingeniería de Sistemas', 5),
    ('E002', 'Ana', 'Lopez', 'Ingeniería de Software', 6),
    ('E003', 'Pedro', 'Torres', 'Ingeniería de Sistemas', 4),
    ('E004', 'Maria', 'Quispe', 'Ingeniería de Redes', 7);

SELECT * FROM estudiantes;

