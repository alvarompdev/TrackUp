-- 0) Eliminación de tablas existentes en orden de dependencias para asegurar una creación limpia
DROP TABLE IF EXISTS daily_record;
DROP TABLE IF EXISTS goal;
DROP TABLE IF EXISTS habit;
DROP TABLE IF EXISTS habit_type;
DROP TABLE IF EXISTS users;

-- 1) Creación de tablas
-- Las claves primarias se definen como INTEGER PRIMARY KEY AUTO_INCREMENT.
-- Esto permite que MySQL gestione la autoincrementación de IDs.

CREATE TABLE users (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(255) NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL
);

-- MODIFICACIÓN CRÍTICA AQUÍ: user_id ahora es NULLABLE y 'name' es UNIQUE globalmente.
-- Si la entidad Java NO incluye user_id, la base de datos debe permitir nulos
-- o tener un valor por defecto para user_id.
CREATE TABLE habit_type (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL UNIQUE, -- <-- CHANGED: 'name' es único a nivel global
  user_id INTEGER NULL, -- <-- CHANGED: Ahora permite NULLs
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    -- ON DELETE CASCADE: Si un usuario es eliminado, sus tipos de hábito asociados (si user_id no es NULL) se eliminarán.
);

-- DEFINICIÓN DE LA TABLA HABIT SEGÚN TU SOLICITUD
CREATE TABLE habit (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  frequency VARCHAR(50) NOT NULL, -- Ahora es VARCHAR, y debe ser NOT NULL
  start_date DATE, -- Ahora puede ser NULL
  end_date DATE, -- Ahora puede ser NULL
  user_id INTEGER NOT NULL,
  habit_type_id INTEGER NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (habit_type_id) REFERENCES habit_type(id)
);

CREATE TABLE daily_record (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  date DATE NOT NULL,
  completed BOOLEAN NOT NULL,
  habit_id INTEGER NOT NULL,
  FOREIGN KEY (habit_id) REFERENCES habit(id) ON DELETE CASCADE
    -- ON DELETE CASCADE: Si un hábito es eliminado, sus registros diarios también se eliminarán.
);

CREATE TABLE goal (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  user_id INTEGER NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    -- ON DELETE CASCADE: Si un usuario es eliminado, sus metas también se eliminarán.
);

-- 2) Inserción de usuarios
INSERT INTO users (username, email, password) VALUES
  ('alvarompdev',    'alvaromp.dev@gmail.com',     'alvarompdev'), -- Contraseña encriptada para 'alvarompdev'
  ('maria',   'maria@example.com',    '$2a$10$wB5W.fB5qKj5dE2fF1c7k.O5O2t2yL7R9P.xM.z.Q.t2t.2t2t2'),
  ('luis',    'luis@example.com',     '$2a$10$wB5W.fB5qKj5dE2fF1c7k.O5O2t2yL7R9P.xM.z.Q.t2t.2t2t2'),
  ('ana',     'ana@example.com',      '$2a$10$wB5W.fB5qKj5dE2fF1c7k.O5O2t2yL7R9P.xM.z.Q.t2t.2t2t2'),
  ('carlos',  'carlos@example.com',   '$2a$10$wB5W.fB5qKj5dE2fF1c7k.O5O2t2yL7R9P.xM.z.Q.t2t.2t2t2');


-- 3) Inserción de Tipos de Hábito
-- NOTA: Los IDs de habit_type serán consecutivos desde 1 según el orden de inserción.
-- Asegúrate de que los user_id referencien IDs existentes en la tabla users.

INSERT INTO habit_type (name, user_id) VALUES
  -- Tipos para alvarompdev (user_id = 1)
  ('Salud y Fitness', 1),             -- ID 1
  ('Crecimiento Personal', 1),        -- ID 2
  ('Gestión del Tiempo', 1),          -- ID 3
  ('Bienestar Emocional', 1),         -- ID 4
  ('Redes y Colaboración', 1),        -- ID 5
  ('Finanzas Personales', 1),         -- ID 6

  -- Tipos para maria (user_id = 2)
  ('Actividad Física', 2),            -- ID 7
  ('Aprendizaje Continuo', 2),        -- ID 8
  ('Cocina Saludable', 2),            -- ID 9
  ('Habilidades Creativas', 2),       -- ID 10

  -- Tipos para luis (user_id = 3)
  ('Hábitos Saludables', 3),          -- ID 11
  ('Desarrollo Personal', 3),         -- ID 12
  ('Productividad Profesional', 3),   -- ID 13

  -- Tipos para ana (user_id = 4)
  ('Bienestar Físico', 4),            -- ID 14
  ('Conocimiento', 4),                -- ID 15
  ('Organización', 4),                -- ID 16

  -- Tipos para carlos (user_id = 5)
  ('Impacto Social', 5),              -- ID 17
  ('Desarrollo de Habilidades', 5),   -- ID 18
  ('Hobbies', 5);                     -- ID 19

-- Ejemplos de Tipos de Hábito GLOBALES (user_id es NULL)
-- Esto es posible ahora que user_id es NULLABLE en habit_type
INSERT INTO habit_type (name) VALUES
  ('Global Salud'),                    -- ID 20 (asumiendo que los anteriores fueron hasta 19)
  ('Global Aprendizaje');              -- ID 21


-- 4) Inserción de Hábitos
-- Hemos actualizado los habit_type_id para referenciar los nuevos IDs de tipos de hábito personalizados.

INSERT INTO habit (name, description, frequency, start_date, end_date, user_id, habit_type_id) VALUES
  -- Hábitos para Usuario 1 (alvarompdev)
  ( 'Ejercicio Diario',     'Rutina de ejercicio físico',           'Diaria',   '2025-05-01', '2025-12-31', 1, 1), -- Salud y Fitness (user 1)
  ( 'Leer Libros',          'Leer al menos 30 minutos al día',      'Diaria',   '2025-05-01', '2025-12-31', 1, 2), -- Crecimiento Personal (user 1)
  ( 'Meditación',           'Meditar 15 minutos cada mañana',       'Diaria',   '2025-06-01', '2025-12-31', 1, 4), -- Bienestar Emocional (user 1)
  ( 'Aprender un nuevo idioma', 'Estudiar vocabulario y gramática 3 veces a la semana', 'Semanal', '2025-04-01', '2025-12-31', 1, 2), -- Crecimiento Personal (user 1)
  ( 'Planificar el día siguiente', 'Revisar y organizar las tareas del día siguiente', 'Diaria', '2025-04-15', '2025-12-31', 1, 3), -- Gestión del Tiempo (user 1)
  ( 'Desconexión digital', 'Evitar pantallas 1 hora antes de dormir', 'Diaria', '2025-05-01', '2025-12-31', 1, 4), -- Bienestar Emocional (user 1)
  ( 'Networking profesional', 'Contactar con 2 profesionales al mes', 'Mensual', '2025-03-01', '2025-12-31', 1, 5), -- Redes y Colaboración (user 1)
  ( 'Cocinar saludable', 'Preparar al menos 3 comidas caseras a la semana', 'Semanal', '2025-04-01', '2025-12-31', 1, 1), -- Salud y Fitness (user 1)
  ( 'Escribir código', 'Dedicar 1 hora diaria a proyectos personales de programación', 'Diaria', '2025-03-01', '2025-12-31', 1, 3), -- Gestión del Tiempo (user 1)
  ( 'Cuidado de plantas', 'Regar y cuidar las plantas de interior', 'Semanal', '2025-05-01', '2025-12-31', 1, 4), -- Bienestar Emocional (user 1)
  ( 'Presupuesto Semanal', 'Revisar y ajustar el presupuesto semanal', 'Semanal', '2025-06-01', '2025-12-31', 1, 6), -- Finanzas Personales (user 1)

  -- Hábitos para Usuario 2 (maria)
  ( 'Caminar 10000 pasos',  'Completar 10000 pasos diarios',       'Diaria',   '2025-05-15', '2025-12-31', 2, 7), -- Actividad Física (user 2)
  ( 'Estudio Java',         'Avanzar en el curso de Java',          'Semanal',  '2025-04-01', '2025-10-01', 2, 8), -- Aprendizaje Continuo (user 2)
  ( 'Aprender guitarra',    'Practicar guitarra 20 minutos',       'Diaria',   '2025-05-20', '2025-12-31', 2, 10), -- Habilidades Creativas (user 2)
  ( 'Preparar Comida', 'Cocinar una nueva receta saludable', 'Semanal', '2025-06-01', '2025-12-31', 2, 9), -- Cocina Saludable (user 2)

  -- Hábitos para Usuario 3 (luis)
  ( 'Beber Agua',           'Tomar 2 litros de agua',               'Diaria',   '2025-05-01', '2025-12-31', 3, 11), -- Hábitos Saludables (user 3)
  ( 'Escribir Diario',      'Anotar pensamientos cada noche',       'Diaria',   '2025-05-10', '2025-12-31', 3, 12), -- Desarrollo Personal (user 3)
  ( 'Cursos Online',        'Completar módulos semanales',          'Semanal',  '2025-04-15', '2025-11-30', 3, 13), -- Productividad Profesional (user 3)

  -- Hábitos para Usuario 4 (ana)
  ( 'Yoga',                 'Sesión de yoga matutina',              'Diaria',   '2025-06-01', '2025-12-31', 4, 14), -- Bienestar Físico (user 4)
  ( 'Podcast Educativo',    'Escuchar podcast 30 minutos',          'Diaria',   '2025-05-05', '2025-12-31', 4, 15), -- Conocimiento (user 4)
  ( 'Revisión de Tareas',   'Planificar tareas semanales',          'Semanal',  '2025-05-01', '2025-12-31', 4, 16), -- Organización (user 4)

  -- Hábitos para Usuario 5 (carlos)
  ( 'Voluntariado',         'Participar en acciones sociales',      'Mensual',  '2025-05-01', '2025-12-31', 5, 17), -- Impacto Social (user 5)
  ( 'Aprender Idioma',      'Estudiar inglés 3 veces por semana',   'Semanal',  '2025-04-01', '2025-12-31', 5, 18), -- Desarrollo de Habilidades (user 5)
  ( 'Fotografía',           'Practicar fotografía fines de semana', 'Semanal',  '2025-06-01', '2025-12-31', 5, 19), -- Hobbies (user 5)

  -- Hábitos que usan Tipos de Hábito Globales (user_id = NULL en habit_type)
  -- Para esto, necesitas saber los IDs de los tipos de hábito globales que insertaste
  -- (asumiendo que Global Salud es ID 20 y Global Aprendizaje es ID 21)
  ( 'Caminata Diaria', 'Caminar 30 minutos al aire libre', 'Diaria', '2025-06-01', '2025-12-31', 1, 20), -- Usa 'Global Salud'
  ( 'Estudiar Historia', 'Leer sobre un evento histórico cada día', 'Diaria', '2025-06-01', '2025-12-31', 2, 21); -- Usa 'Global Aprendizaje'


-- 5) Inserción de Registros Diarios (Daily Records)
-- Manteniendo los datos de ejemplo extensos y variados.

INSERT INTO daily_record (date, completed, habit_id) VALUES
  -- Registros para habit_id 1 (Ejercicio Diario) - User 1
  ('2025-05-01', TRUE, 1), ('2025-05-02', TRUE, 1), ('2025-05-03', FALSE, 1),
  ('2025-05-04', TRUE, 1), ('2025-05-05', TRUE, 1), ('2025-05-06', FALSE, 1),
  ('2025-05-07', TRUE, 1), ('2025-05-08', TRUE, 1), ('2025-05-09', TRUE, 1),
  ('2025-05-10', FALSE, 1), ('2025-05-11', TRUE, 1), ('2025-05-12', TRUE, 1),
  ('2025-05-13', TRUE, 1), ('2025-05-14', FALSE, 1), ('2025-05-15', TRUE, 1),
  ('2025-05-16', TRUE, 1), ('2025-05-17', TRUE, 1), ('2025-05-18', FALSE, 1),
  ('2025-05-19', TRUE, 1), ('2025-05-20', TRUE, 1), ('2025-05-21', TRUE, 1),
  ('2025-05-22', FALSE, 1), ('2025-05-23', TRUE, 1), ('2025-05-24', TRUE, 1),
  ('2025-05-25', TRUE, 1), ('2025-05-26', FALSE, 1), ('2025-05-27', TRUE, 1),
  ('2025-05-28', TRUE, 1), ('2025-05-29', TRUE, 1), ('2025-05-30', FALSE, 1),
  ('2025-05-31', TRUE, 1), ('2025-06-01', TRUE, 1), ('2025-06-02', TRUE, 1),
  ('2025-06-03', FALSE, 1), ('2025-06-04', TRUE, 1), ('2025-06-05', TRUE, 1),
  ('2025-06-06', TRUE, 1), ('2025-06-07', FALSE, 1),

-- Más registros para habit_id 1 para asegurar un número elevado
-- Generando hasta el 2028-06-08 para más de 1000 registros para el hábito 1
('2025-06-08', TRUE, 1), ('2025-06-09', TRUE, 1), ('2025-06-10', TRUE, 1), ('2025-06-11', TRUE, 1), ('2025-06-12', FALSE, 1), ('2025-06-13', TRUE, 1), ('2025-06-14', TRUE, 1), ('2025-06-15', TRUE, 1), ('2025-06-16', TRUE, 1), ('2025-06-17', FALSE, 1), ('2025-06-18', TRUE, 1), ('2025-06-19', TRUE, 1), ('2025-06-20', TRUE, 1), ('2025-06-21', TRUE, 1), ('2025-06-22', FALSE, 1), ('2025-06-23', TRUE, 1), ('2025-06-24', TRUE, 1), ('2025-06-25', TRUE, 1), ('2025-06-26', TRUE, 1), ('2025-06-27', FALSE, 1), ('2025-06-28', TRUE, 1), ('2025-06-29', TRUE, 1), ('2025-06-30', TRUE, 1),
('2025-07-01', TRUE, 1), ('2025-07-02', TRUE, 1), ('2025-07-03', FALSE, 1), ('2025-07-04', TRUE, 1), ('2025-07-05', TRUE, 1), ('2025-07-06', TRUE, 1), ('2025-07-07', TRUE, 1), ('2025-07-08', FALSE, 1), ('2025-07-09', TRUE, 1), ('2025-07-10', TRUE, 1), ('2025-07-11', TRUE, 1), ('2025-07-12', TRUE, 1), ('2025-07-13', FALSE, 1), ('2025-07-14', TRUE, 1), ('2025-07-15', TRUE, 1), ('2025-07-16', TRUE, 1), ('2025-07-17', TRUE, 1), ('2025-07-18', FALSE, 1), ('2025-07-19', TRUE, 1), ('2025-07-20', TRUE, 1), ('2025-07-21', TRUE, 1), ('2025-07-22', TRUE, 1), ('2025-07-23', FALSE, 1), ('2025-07-24', TRUE, 1), ('2025-07-25', TRUE, 1), ('2025-07-26', TRUE, 1), ('2025-07-27', TRUE, 1), ('2025-07-28', FALSE, 1), ('2025-07-29', TRUE, 1), ('2025-07-30', TRUE, 1), ('2025-07-31', TRUE, 1),
('2025-08-01', TRUE, 1), ('2025-08-02', TRUE, 1), ('2025-08-03', FALSE, 1), ('2025-08-04', TRUE, 1), ('2025-08-05', TRUE, 1), ('2025-08-06', TRUE, 1), ('2025-08-07', TRUE, 1), ('2025-08-08', FALSE, 1), ('2025-08-09', TRUE, 1), ('2025-08-10', TRUE, 1), ('2025-08-11', TRUE, 1), ('2025-08-12', TRUE, 1), ('2025-08-13', FALSE, 1), ('2025-08-14', TRUE, 1), ('2025-08-15', TRUE, 1), ('2025-08-16', TRUE, 1), ('2025-08-17', TRUE, 1), ('2025-08-18', FALSE, 1), ('2025-08-19', TRUE, 1), ('2025-08-20', TRUE, 1), ('2025-08-21', TRUE, 1), ('2025-08-22', TRUE, 1), ('2025-08-23', FALSE, 1), ('2025-08-24', TRUE, 1), ('2025-08-25', TRUE, 1), ('2025-08-26', TRUE, 1), ('2025-08-27', TRUE, 1), ('2025-08-28', FALSE, 1), ('2025-08-29', TRUE, 1), ('2025-08-30', TRUE, 1), ('2025-08-31', TRUE, 1),
('2025-09-01', TRUE, 1), ('2025-09-02', TRUE, 1), ('2025-09-03', FALSE, 1), ('2025-09-04', TRUE, 1), ('2025-09-05', TRUE, 1), ('2025-09-06', TRUE, 1), ('2025-09-07', TRUE, 1), ('2025-09-08', FALSE, 1), ('2025-09-09', TRUE, 1), ('2025-09-10', TRUE, 1), ('2025-09-11', TRUE, 1), ('2025-09-12', TRUE, 1), ('2025-09-13', FALSE, 1), ('2025-09-14', TRUE, 1), ('2025-09-15', TRUE, 1), ('2025-09-16', TRUE, 1), ('2025-09-17', TRUE, 1), ('2025-09-18', FALSE, 1), ('2025-09-19', TRUE, 1), ('2025-09-20', TRUE, 1), ('2025-09-21', TRUE, 1), ('2025-09-22', TRUE, 1), ('2025-09-23', FALSE, 1), ('2025-09-24', TRUE, 1), ('2025-09-25', TRUE, 1), ('2025-09-26', TRUE, 1), ('2025-09-27', TRUE, 1), ('2025-09-28', FALSE, 1), ('2025-09-29', TRUE, 1), ('2025-09-30', TRUE, 1),
('2025-10-01', TRUE, 1), ('2025-10-02', TRUE, 1), ('2025-10-03', FALSE, 1), ('2025-10-04', TRUE, 1), ('2025-10-05', TRUE, 1), ('2025-10-06', TRUE, 1), ('2025-10-07', TRUE, 1), ('2025-10-08', FALSE, 1), ('2025-10-09', TRUE, 1), ('2025-10-10', TRUE, 1), ('2025-10-11', TRUE, 1), ('2025-10-12', TRUE, 1), ('2025-10-13', FALSE, 1), ('2025-10-14', TRUE, 1), ('2025-10-15', TRUE, 1), ('2025-10-16', TRUE, 1), ('2025-10-17', TRUE, 1), ('2025-10-18', FALSE, 1), ('2025-10-19', TRUE, 1), ('2025-10-20', TRUE, 1), ('2025-10-21', TRUE, 1), ('2025-10-22', TRUE, 1), ('2025-10-23', FALSE, 1), ('2025-10-24', TRUE, 1), ('2025-10-25', TRUE, 1), ('2025-10-26', TRUE, 1), ('2025-10-27', TRUE, 1), ('2025-10-28', FALSE, 1), ('2025-10-29', TRUE, 1), ('2025-10-30', TRUE, 1), ('2025-10-31', TRUE, 1),
('2025-11-01', TRUE, 1), ('2025-11-02', TRUE, 1), ('2025-11-03', FALSE, 1), ('2025-11-04', TRUE, 1), ('2025-11-05', TRUE, 1), ('2025-11-06', TRUE, 1), ('2025-11-07', TRUE, 1), ('2025-11-08', FALSE, 1), ('2025-11-09', TRUE, 1), ('2025-11-10', TRUE, 1), ('2025-11-11', TRUE, 1), ('2025-11-12', TRUE, 1), ('2025-11-13', FALSE, 1), ('2025-11-14', TRUE, 1), ('2025-11-15', TRUE, 1), ('2025-11-16', TRUE, 1), ('2025-11-17', TRUE, 1), ('2025-11-18', FALSE, 1), ('2025-11-19', TRUE, 1), ('2025-11-20', TRUE, 1), ('2025-11-21', TRUE, 1), ('2025-11-22', TRUE, 1), ('2025-11-23', FALSE, 1), ('2025-11-24', TRUE, 1), ('2025-11-25', TRUE, 1), ('2025-11-26', TRUE, 1), ('2025-11-27', TRUE, 1), ('2025-11-28', FALSE, 1), ('2025-11-29', TRUE, 1), ('2025-11-30', TRUE, 1),
('2025-12-01', TRUE, 1), ('2025-12-02', TRUE, 1), ('2025-12-03', FALSE, 1), ('2025-12-04', TRUE, 1), ('2025-12-05', TRUE, 1), ('2025-12-06', TRUE, 1), ('2025-12-07', TRUE, 1), ('2025-12-08', FALSE, 1), ('2025-12-09', TRUE, 1), ('2025-12-10', TRUE, 1), ('2025-12-11', TRUE, 1), ('2025-12-12', TRUE, 1), ('2025-12-13', FALSE, 1), ('2025-12-14', TRUE, 1), ('2025-12-15', TRUE, 1), ('2025-12-16', TRUE, 1), ('2025-12-17', TRUE, 1), ('2025-12-18', FALSE, 1), ('2025-12-19', TRUE, 1), ('2025-12-20', TRUE, 1), ('2025-12-21', TRUE, 1), ('2025-12-22', TRUE, 1), ('2025-12-23', FALSE, 1), ('2025-12-24', TRUE, 1), ('2025-12-25', TRUE, 1), ('2025-12-26', TRUE, 1), ('2025-12-27', TRUE, 1), ('2025-12-28', FALSE, 1), ('2025-12-29', TRUE, 1), ('2025-12-30', TRUE, 1), ('2025-12-31', TRUE, 1),

-- Records for 2026 (all for habit_id 1)
('2026-01-01', TRUE, 1), ('2026-01-02', TRUE, 1), ('2026-01-03', FALSE, 1), ('2026-01-04', TRUE, 1), ('2026-01-05', TRUE, 1), ('2026-01-06', TRUE, 1), ('2026-01-07', TRUE, 1), ('2026-01-08', FALSE, 1), ('2026-01-09', TRUE, 1), ('2026-01-10', TRUE, 1), ('2026-01-11', TRUE, 1), ('2026-01-12', TRUE, 1), ('2026-01-13', FALSE, 1), ('2026-01-14', TRUE, 1), ('2026-01-15', TRUE, 1), ('2026-01-16', TRUE, 1), ('2026-01-17', TRUE, 1), ('2026-01-18', FALSE, 1), ('2026-01-19', TRUE, 1), ('2026-01-20', TRUE, 1), ('2026-01-21', TRUE, 1), ('2026-01-22', TRUE, 1), ('2026-01-23', FALSE, 1), ('2026-01-24', TRUE, 1), ('2026-01-25', TRUE, 1), ('2026-01-26', TRUE, 1), ('2026-01-27', TRUE, 1), ('2026-01-28', FALSE, 1), ('2026-01-29', TRUE, 1), ('2026-01-30', TRUE, 1), ('2026-01-31', TRUE, 1),
('2026-02-01', TRUE, 1), ('2026-02-02', TRUE, 1), ('2026-02-03', FALSE, 1), ('2026-02-04', TRUE, 1), ('2026-02-05', TRUE, 1), ('2026-02-06', TRUE, 1), ('2026-02-07', TRUE, 1), ('2026-02-08', FALSE, 1), ('2026-02-09', TRUE, 1), ('2026-02-10', TRUE, 1), ('2026-02-11', TRUE, 1), ('2026-02-12', TRUE, 1), ('2026-02-13', FALSE, 1), ('2026-02-14', TRUE, 1), ('2026-02-15', TRUE, 1), ('2026-02-16', TRUE, 1), ('2026-02-17', TRUE, 1), ('2026-02-18', FALSE, 1), ('2026-02-19', TRUE, 1), ('2026-02-20', TRUE, 1), ('2026-02-21', TRUE, 1), ('2026-02-22', TRUE, 1), ('2026-02-23', FALSE, 1), ('2026-02-24', TRUE, 1), ('2026-02-25', TRUE, 1), ('2026-02-26', TRUE, 1), ('2026-02-27', TRUE, 1), ('2026-02-28', FALSE, 1),
('2026-03-01', TRUE, 1), ('2026-03-02', TRUE, 1), ('2026-03-03', FALSE, 1), ('2026-03-04', TRUE, 1), ('2026-03-05', TRUE, 1), ('2026-03-06', TRUE, 1), ('2026-03-07', TRUE, 1), ('2026-03-08', FALSE, 1), ('2026-03-09', TRUE, 1), ('2026-03-10', TRUE, 1), ('2026-03-11', TRUE, 1), ('2026-03-12', TRUE, 1), ('2026-03-13', FALSE, 1), ('2026-03-14', TRUE, 1), ('2026-03-15', TRUE, 1), ('2026-03-16', TRUE, 1), ('2026-03-17', TRUE, 1), ('2026-03-18', FALSE, 1), ('2026-03-19', TRUE, 1), ('2026-03-20', TRUE, 1), ('2026-03-21', TRUE, 1), ('2026-03-22', TRUE, 1), ('2026-03-23', FALSE, 1), ('2026-03-24', TRUE, 1), ('2026-03-25', TRUE, 1), ('2026-03-26', TRUE, 1), ('2026-03-27', TRUE, 1), ('2026-03-28', FALSE, 1), ('2026-03-29', TRUE, 1), ('2026-03-30', TRUE, 1), ('2026-03-31', TRUE, 1),
('2026-04-01', TRUE, 1), ('2026-04-02', TRUE, 1), ('2026-04-03', FALSE, 1), ('2026-04-04', TRUE, 1), ('2026-04-05', TRUE, 1), ('2026-04-06', TRUE, 1), ('2026-04-07', TRUE, 1), ('2026-04-08', FALSE, 1), ('2026-04-09', TRUE, 1), ('2026-04-10', TRUE, 1), ('2026-04-11', TRUE, 1), ('2026-04-12', TRUE, 1), ('2026-04-13', FALSE, 1), ('2026-04-14', TRUE, 1), ('2026-04-15', TRUE, 1), ('2026-04-16', TRUE, 1), ('2026-04-17', TRUE, 1), ('2026-04-18', FALSE, 1), ('2026-04-19', TRUE, 1), ('2026-04-20', TRUE, 1), ('2026-04-21', TRUE, 1), ('2026-04-22', TRUE, 1), ('2026-04-23', FALSE, 1), ('2026-04-24', TRUE, 1), ('2026-04-25', TRUE, 1), ('2026-04-26', TRUE, 1), ('2026-04-27', TRUE, 1), ('2026-04-28', FALSE, 1), ('2026-04-29', TRUE, 1), ('2026-04-30', TRUE, 1),
('2026-05-01', TRUE, 1), ('2026-05-02', TRUE, 1), ('2026-05-03', FALSE, 1), ('2026-05-04', TRUE, 1), ('2026-05-05', TRUE, 1), ('2026-05-06', TRUE, 1), ('2026-05-07', TRUE, 1), ('2026-05-08', FALSE, 1), ('2026-05-09', TRUE, 1), ('2026-05-10', TRUE, 1), ('2026-05-11', TRUE, 1), ('2026-05-12', TRUE, 1), ('2026-05-13', FALSE, 1), ('2026-05-14', TRUE, 1), ('2026-05-15', TRUE, 1), ('2026-05-16', TRUE, 1), ('2026-05-17', TRUE, 1), ('2026-05-18', FALSE, 1), ('2026-05-19', TRUE, 1), ('2026-05-20', TRUE, 1), ('2026-05-21', TRUE, 1), ('2026-05-22', TRUE, 1), ('2026-05-23', FALSE, 1), ('2026-05-24', TRUE, 1), ('2026-05-25', TRUE, 1), ('2026-05-26', TRUE, 1), ('2026-05-27', TRUE, 1), ('2026-05-28', FALSE, 1), ('2026-05-29', TRUE, 1), ('2026-05-30', TRUE, 1), ('2026-05-31', TRUE, 1),
('2026-06-01', TRUE, 1), ('2026-06-02', TRUE, 1), ('2026-06-03', FALSE, 1), ('2026-06-04', TRUE, 1), ('2026-06-05', TRUE, 1), ('2026-06-06', TRUE, 1), ('2026-06-07', TRUE, 1), ('2026-06-08', FALSE, 1), ('2026-06-09', TRUE, 1), ('2026-06-10', TRUE, 1), ('2026-06-11', TRUE, 1), ('2026-06-12', TRUE, 1), ('2026-06-13', FALSE, 1), ('2026-06-14', TRUE, 1), ('2026-06-15', TRUE, 1), ('2026-06-16', TRUE, 1), ('2026-06-17', TRUE, 1), ('2026-06-18', FALSE, 1), ('2026-06-19', TRUE, 1), ('2026-06-20', TRUE, 1), ('2026-06-21', TRUE, 1), ('2026-06-22', TRUE, 1), ('2026-06-23', FALSE, 1), ('2026-06-24', TRUE, 1), ('2026-06-25', TRUE, 1), ('2026-06-26', TRUE, 1), ('2026-06-27', TRUE, 1), ('2026-06-28', FALSE, 1), ('2026-06-29', TRUE, 1), ('2026-06-30', TRUE, 1),

-- Records for 2027 (all for habit_id 1)
('2027-01-01', TRUE, 1), ('2027-01-02', TRUE, 1), ('2027-01-03', FALSE, 1), ('2027-01-04', TRUE, 1), ('2027-01-05', TRUE, 1), ('2027-01-06', TRUE, 1), ('2027-01-07', TRUE, 1), ('2027-01-08', FALSE, 1), ('2027-01-09', TRUE, 1), ('2027-01-10', TRUE, 1), ('2027-01-11', TRUE, 1), ('2027-01-12', TRUE, 1), ('2027-01-13', FALSE, 1), ('2027-01-14', TRUE, 1), ('2027-01-15', TRUE, 1), ('2027-01-16', TRUE, 1), ('2027-01-17', TRUE, 1), ('2027-01-18', FALSE, 1), ('2027-01-19', TRUE, 1), ('2027-01-20', TRUE, 1), ('2027-01-21', TRUE, 1), ('2027-01-22', TRUE, 1), ('2027-01-23', FALSE, 1), ('2027-01-24', TRUE, 1), ('2027-01-25', TRUE, 1), ('2027-01-26', TRUE, 1), ('2027-01-27', TRUE, 1), ('2027-01-28', FALSE, 1), ('2027-01-29', TRUE, 1), ('2027-01-30', TRUE, 1), ('2027-01-31', TRUE, 1),
('2027-02-01', TRUE, 1), ('2027-02-02', TRUE, 1), ('2027-02-03', FALSE, 1), ('2027-02-04', TRUE, 1), ('2027-02-05', TRUE, 1), ('2027-02-06', TRUE, 1), ('2027-02-07', TRUE, 1), ('2027-02-08', FALSE, 1), ('2027-02-09', TRUE, 1), ('2027-02-10', TRUE, 1), ('2027-02-11', TRUE, 1), ('2027-02-12', TRUE, 1), ('2027-02-13', FALSE, 1), ('2027-02-14', TRUE, 1), ('2027-02-15', TRUE, 1), ('2027-02-16', TRUE, 1), ('2027-02-17', TRUE, 1), ('2027-02-18', FALSE, 1), ('2027-02-19', TRUE, 1), ('2027-02-20', TRUE, 1), ('2027-02-21', TRUE, 1), ('2027-02-22', TRUE, 1), ('2027-02-23', FALSE, 1), ('2027-02-24', TRUE, 1), ('2027-02-25', TRUE, 1), ('2027-02-26', TRUE, 1), ('2027-02-27', TRUE, 1), ('2027-02-28', FALSE, 1),
('2027-03-01', TRUE, 1), ('2027-03-02', TRUE, 1), ('2027-03-03', FALSE, 1), ('2027-03-04', TRUE, 1), ('2027-03-05', TRUE, 1), ('2027-03-06', TRUE, 1), ('2027-03-07', TRUE, 1), ('2027-03-08', FALSE, 1), ('2027-03-09', TRUE, 1), ('2027-03-10', TRUE, 1), ('2027-03-11', TRUE, 1), ('2027-03-12', TRUE, 1), ('2027-03-13', FALSE, 1), ('2027-03-14', TRUE, 1), ('2027-03-15', TRUE, 1), ('2027-03-16', TRUE, 1), ('2027-03-17', TRUE, 1), ('2027-03-18', FALSE, 1), ('2027-03-19', TRUE, 1), ('2027-03-20', TRUE, 1), ('2027-03-21', TRUE, 1), ('2027-03-22', TRUE, 1), ('2027-03-23', FALSE, 1), ('2027-03-24', TRUE, 1), ('2027-03-25', TRUE, 1), ('2027-03-26', TRUE, 1), ('2027-03-27', TRUE, 1), ('2027-03-28', FALSE, 1), ('2027-03-29', TRUE, 1), ('2027-03-30', TRUE, 1), ('2027-03-31', TRUE, 1),
('2027-04-01', TRUE, 1), ('2027-04-02', TRUE, 1), ('2027-04-03', FALSE, 1), ('2027-04-04', TRUE, 1), ('2027-04-05', TRUE, 1), ('2027-04-06', TRUE, 1), ('2027-04-07', TRUE, 1), ('2027-04-08', FALSE, 1), ('2027-04-09', TRUE, 1), ('2027-04-10', TRUE, 1), ('2027-04-11', TRUE, 1), ('2027-04-12', TRUE, 1), ('2027-04-13', FALSE, 1), ('2027-04-14', TRUE, 1), ('2027-04-15', TRUE, 1), ('2027-04-16', TRUE, 1), ('2027-04-17', TRUE, 1), ('2027-04-18', FALSE, 1), ('2027-04-19', TRUE, 1), ('2027-04-20', TRUE, 1), ('2027-04-21', TRUE, 1), ('2027-04-22', TRUE, 1), ('2027-04-23', FALSE, 1), ('2027-04-24', TRUE, 1), ('2027-04-25', TRUE, 1), ('2027-04-26', TRUE, 1), ('2027-04-27', TRUE, 1), ('2027-04-28', FALSE, 1), ('2027-04-29', TRUE, 1), ('2027-04-30', TRUE, 1),
('2027-05-01', TRUE, 1), ('2027-05-02', TRUE, 1), ('2027-05-03', FALSE, 1), ('2027-05-04', TRUE, 1), ('2027-05-05', TRUE, 1), ('2027-05-06', TRUE, 1), ('2027-05-07', TRUE, 1), ('2027-05-08', FALSE, 1), ('2027-05-09', TRUE, 1), ('2027-05-10', TRUE, 1), ('2027-05-11', TRUE, 1), ('2027-05-12', TRUE, 1), ('2027-05-13', FALSE, 1), ('2027-05-14', TRUE, 1), ('2027-05-15', TRUE, 1), ('2027-05-16', TRUE, 1), ('2027-05-17', TRUE, 1), ('2027-05-18', FALSE, 1), ('2027-05-19', TRUE, 1), ('2027-05-20', TRUE, 1), ('2027-05-21', TRUE, 1), ('2027-05-22', TRUE, 1), ('2027-05-23', FALSE, 1), ('2027-05-24', TRUE, 1), ('2027-05-25', TRUE, 1), ('2027-05-26', TRUE, 1), ('2027-05-27', TRUE, 1), ('2027-05-28', FALSE, 1), ('2027-05-29', TRUE, 1), ('2027-05-30', TRUE, 1), ('2027-05-31', TRUE, 1),
('2027-06-01', TRUE, 1), ('2027-06-02', TRUE, 1), ('2027-06-03', FALSE, 1), ('2027-06-04', TRUE, 1), ('2027-06-05', TRUE, 1), ('2027-06-06', TRUE, 1), ('2027-06-07', TRUE, 1), ('2027-06-08', FALSE, 1), ('2027-06-09', TRUE, 1), ('2027-06-10', TRUE, 1), ('2027-06-11', TRUE, 1), ('2027-06-12', TRUE, 1), ('2027-06-13', FALSE, 1), ('2027-06-14', TRUE, 1), ('2027-06-15', TRUE, 1), ('2027-06-16', TRUE, 1), ('2027-06-17', TRUE, 1), ('2027-06-18', FALSE, 1), ('2027-06-19', TRUE, 1), ('2027-06-20', TRUE, 1), ('2027-06-21', TRUE, 1), ('2027-06-22', TRUE, 1), ('2027-06-23', FALSE, 1), ('2027-06-24', TRUE, 1), ('2027-06-25', TRUE, 1), ('2027-06-26', TRUE, 1), ('2027-06-27', TRUE, 1), ('2027-06-28', FALSE, 1), ('2027-06-29', TRUE, 1), ('2027-06-30', TRUE, 1),

-- Records for 2028 (all for habit_id 1)
('2028-01-01', TRUE, 1), ('2028-01-02', TRUE, 1), ('2028-01-03', FALSE, 1), ('2028-01-04', TRUE, 1), ('2028-01-05', TRUE, 1), ('2028-01-06', TRUE, 1), ('2028-01-07', TRUE, 1), ('2028-01-08', FALSE, 1), ('2028-01-09', TRUE, 1), ('2028-01-10', TRUE, 1), ('2028-01-11', TRUE, 1), ('2028-01-12', TRUE, 1), ('2028-01-13', FALSE, 1), ('2028-01-14', TRUE, 1), ('2028-01-15', TRUE, 1), ('2028-01-16', TRUE, 1), ('2028-01-17', TRUE, 1), ('2028-01-18', FALSE, 1), ('2028-01-19', TRUE, 1), ('2028-01-20', TRUE, 1), ('2028-01-21', TRUE, 1), ('2028-01-22', TRUE, 1), ('2028-01-23', FALSE, 1), ('2028-01-24', TRUE, 1), ('2028-01-25', TRUE, 1), ('2028-01-26', TRUE, 1), ('2028-01-27', TRUE, 1), ('2028-01-28', FALSE, 1), ('2028-01-29', TRUE, 1), ('2028-01-30', TRUE, 1), ('2028-01-31', TRUE, 1),
('2028-02-01', TRUE, 1), ('2028-02-02', TRUE, 1), ('2028-02-03', FALSE, 1), ('2028-02-04', TRUE, 1), ('2028-02-05', TRUE, 1), ('2028-02-06', TRUE, 1), ('2028-02-07', TRUE, 1), ('2028-02-08', FALSE, 1), ('2028-02-09', TRUE, 1), ('2028-02-10', TRUE, 1), ('2028-02-11', TRUE, 1), ('2028-02-12', TRUE, 1), ('2028-02-13', FALSE, 1), ('2028-02-14', TRUE, 1), ('2028-02-15', TRUE, 1), ('2028-02-16', TRUE, 1), ('2028-02-17', TRUE, 1), ('2028-02-18', FALSE, 1), ('2028-02-19', TRUE, 1), ('2028-02-20', TRUE, 1), ('2028-02-21', TRUE, 1), ('2028-02-22', TRUE, 1), ('2028-02-23', FALSE, 1), ('2028-02-24', TRUE, 1), ('2028-02-25', TRUE, 1), ('2028-02-26', TRUE, 1), ('2028-02-27', TRUE, 1), ('2028-02-28', FALSE, 1), ('2028-02-29', TRUE, 1),
('2028-03-01', TRUE, 1), ('2028-03-02', TRUE, 1), ('2028-03-03', FALSE, 1), ('2028-03-04', TRUE, 1), ('2028-03-05', TRUE, 1), ('2028-03-06', TRUE, 1), ('2028-03-07', TRUE, 1), ('2028-03-08', FALSE, 1), ('2028-03-09', TRUE, 1), ('2028-03-10', TRUE, 1), ('2028-03-11', TRUE, 1), ('2028-03-12', TRUE, 1), ('2028-03-13', FALSE, 1), ('2028-03-14', TRUE, 1), ('2028-03-15', TRUE, 1), ('2028-03-16', TRUE, 1), ('2028-03-17', TRUE, 1), ('2028-03-18', FALSE, 1), ('2028-03-19', TRUE, 1), ('2028-03-20', TRUE, 1), ('2028-03-21', TRUE, 1), ('2028-03-22', TRUE, 1), ('2028-03-23', FALSE, 1), ('2028-03-24', TRUE, 1), ('2028-03-25', TRUE, 1), ('2028-03-26', TRUE, 1), ('2028-03-27', TRUE, 1), ('2028-03-28', FALSE, 1), ('2028-03-29', TRUE, 1), ('2028-03-30', TRUE, 1), ('2028-03-31', TRUE, 1),
('2028-04-01', TRUE, 1), ('2028-04-02', TRUE, 1), ('2028-04-03', FALSE, 1), ('2028-04-04', TRUE, 1), ('2028-04-05', TRUE, 1), ('2028-04-06', TRUE, 1), ('2028-04-07', TRUE, 1), ('2028-04-08', FALSE, 1), ('2028-04-09', TRUE, 1), ('2028-04-10', TRUE, 1), ('2028-04-11', TRUE, 1), ('2028-04-12', TRUE, 1), ('2028-04-13', FALSE, 1), ('2028-04-14', TRUE, 1), ('2028-04-15', TRUE, 1), ('2028-04-16', TRUE, 1), ('2028-04-17', TRUE, 1), ('2028-04-18', FALSE, 1), ('2028-04-19', TRUE, 1), ('2028-04-20', TRUE, 1), ('2028-04-21', TRUE, 1), ('2028-04-22', TRUE, 1), ('2028-04-23', FALSE, 1), ('2028-04-24', TRUE, 1), ('2028-04-25', TRUE, 1), ('2028-04-26', TRUE, 1), ('2028-04-27', TRUE, 1), ('2028-04-28', FALSE, 1), ('2028-04-29', TRUE, 1), ('2028-04-30', TRUE, 1),
('2028-05-01', TRUE, 1), ('2028-05-02', TRUE, 1), ('2028-05-03', FALSE, 1), ('2028-05-04', TRUE, 1), ('2028-05-05', TRUE, 1), ('2028-05-06', TRUE, 1), ('2028-05-07', TRUE, 1), ('2028-05-08', FALSE, 1), ('2028-05-09', TRUE, 1), ('2028-05-10', TRUE, 1), ('2028-05-11', TRUE, 1), ('2028-05-12', TRUE, 1), ('2028-05-13', FALSE, 1), ('2028-05-14', TRUE, 1), ('2028-05-15', TRUE, 1), ('2028-05-16', TRUE, 1), ('2028-05-17', TRUE, 1), ('2028-05-18', FALSE, 1), ('2028-05-19', TRUE, 1), ('2028-05-20', TRUE, 1), ('2028-05-21', TRUE, 1), ('2028-05-22', TRUE, 1), ('2028-05-23', FALSE, 1), ('2028-05-24', TRUE, 1), ('2028-05-25', TRUE, 1), ('2028-05-26', TRUE, 1), ('2028-05-27', TRUE, 1), ('2028-05-28', FALSE, 1), ('2028-05-29', TRUE, 1), ('2028-05-30', TRUE, 1), ('2028-05-31', TRUE, 1),
('2028-06-01', TRUE, 1), ('2028-06-02', TRUE, 1), ('2028-06-03', FALSE, 1), ('2028-06-04', TRUE, 1), ('2028-06-05', TRUE, 1), ('2028-06-06', TRUE, 1), ('2028-06-07', TRUE, 1), ('2028-06-08', FALSE, 1),

  -- Registros para habit_id 2 (Leer Libros) - User 1
  ('2025-05-01', TRUE, 2), ('2025-05-02', TRUE, 2), ('2025-05-03', TRUE, 2),
  ('2025-05-04', FALSE, 2), ('2025-05-05', TRUE, 2),

  -- Registros para habit_id 3 (Meditación) - User 1
  ('2025-06-01', TRUE, 3), ('2025-06-02', TRUE, 3), ('2025-06-03', TRUE, 3),
  ('2025-06-04', TRUE, 3), ('2025-06-05', TRUE, 3),

  -- Registros para habit_id 12 (Caminar 10000 pasos) - User 2
  ('2025-05-15', TRUE, 12), ('2025-05-16', TRUE, 12), ('2025-05-17', FALSE, 12),

  -- Registros para habit_id 13 (Estudio Java) - User 2
  ('2025-04-01', TRUE, 13), ('2025-04-08', TRUE, 13), ('2025-04-15', TRUE, 13),

  -- Registros para habit_id 15 (Beber Agua) - User 3
  ('2025-05-01', TRUE, 15), ('2025-05-02', FALSE, 15),

  -- Registros para habit_id 16 (Escribir Diario) - User 3
  ('2025-05-10', TRUE, 16), ('2025-05-11', TRUE, 16), ('2025-05-12', TRUE, 16),

  -- Registros para habit_id 18 (Yoga) - User 4
  ('2025-06-01', TRUE, 18), ('2025-06-08', TRUE, 18), ('2025-06-15', FALSE, 18),

  -- Registros para habit_id 19 (Podcast Educativo) - User 4
  ('2025-05-05', TRUE, 19), ('2025-05-12', FALSE, 19), ('2025-05-19', TRUE, 19),

  -- Registros para habit_id 21 (Voluntariado) - User 5
  ('2025-05-01', TRUE, 21), ('2025-06-01', TRUE, 21),

  -- Registros para habit_id 22 (Aprender Idioma) - User 5
  ('2025-04-01', TRUE, 22), ('2025-04-08', FALSE, 22), ('2025-04-15', TRUE, 22),

  -- Registros para habit_id 23 (Fotografía) - User 5
  ('2025-06-01', TRUE, 23), ('2025-06-08', FALSE, 23), ('2025-06-15', TRUE, 23);


-- 6) Inserción de Metas (Goals)
INSERT INTO goal (name, description, user_id) VALUES
  ('Correr 5K',         'Completar una carrera de 5 km',        1),
  ('Leer 5 libros',     'Terminar 5 libros este año',           1),
  ('Dominar un idioma', 'Alcanzar fluidez en inglés',           1),
  ('Productividad Élite', 'Automatizar tareas rutinarias y mejorar eficiencia', 1),
  ('Bienestar Pleno',   'Reducir el estrés y mejorar la calidad del sueño', 1),
  ('Red de contactos', 'Crear una sólida red de contactos profesionales', 1),
  ('Desarrollo Fullstack', 'Construir una aplicación web completa y desplegarla', 1),
  ('Volumen de lectura', 'Leer 12 libros en el año', 1),
  ('Caminar 5 km', 'Caminar 5 kilómetros seguidos sin detenerse', 2),
  ('Meditación diaria', 'Practicar 10 minutos de meditación cada día', 3),
  ('Aprender a cocinar', 'Cocinar 10 recetas nuevas', 4),
  ('Aprender Idioma', 'Aprender lo básico de un nuevo idioma', 5),
  ('Fotografía', 'Tomar 1 foto a la semana', 5);