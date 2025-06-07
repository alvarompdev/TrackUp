-- 0) Eliminación de tablas existentes en orden de dependencias
DROP TABLE IF EXISTS daily_record;
DROP TABLE IF EXISTS goal;
DROP TABLE IF EXISTS habit;
DROP TABLE IF EXISTS habit_type;
DROP TABLE IF EXISTS users;

-- 1) Creación de tablas
-- Las claves primarias se definen como INTEGER PRIMARY KEY.
-- Si quisieras que MySQL gestionara la autoincrementación de IDs, usarías AUTO_INCREMENT.
-- Ejemplo: id INT PRIMARY KEY AUTO_INCREMENT,
CREATE TABLE users (
  id INTEGER PRIMARY KEY,
  username VARCHAR(255) NOT NULL, -- TEXT en H2, VARCHAR(255) es común en MySQL
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL
);

CREATE TABLE habit_type (
  id INTEGER PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

CREATE TABLE habit (
  id INTEGER PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description TEXT, -- TEXT es compatible en MySQL
  frequency VARCHAR(50) CHECK(frequency IN ('Diaria', 'Semanal', 'Mensual')), -- VARCHAR con CHECK
  start_date DATE,
  end_date DATE,
  user_id INTEGER,
  habit_type_id INTEGER,
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (habit_type_id) REFERENCES habit_type(id)
);

CREATE TABLE daily_record (
  id INTEGER PRIMARY KEY,
  date DATE NOT NULL,
  completed BOOLEAN NOT NULL, -- BOOLEAN en MySQL es un alias para TINYINT(1)
  habit_id INTEGER,
  FOREIGN KEY (habit_id) REFERENCES habit(id)
);

CREATE TABLE goal (
  id INTEGER PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  user_id INTEGER,
  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 2) Inserción de usuarios
INSERT INTO users (id, username, email, password) VALUES
  (1, 'alvarompdev',    'alvaromp.dev@gmail.com',     'alvarompdev'),
  (2, 'maria',   'maria@example.com',    'maria'),
  (3, 'luis',    'luis@example.com',     'luis'),
  (4, 'ana',     'ana@example.com',      'ana'),
  (5, 'carlos',  'carlos@example.com',   'carlos');

-- 3) Tipos de hábito
INSERT INTO habit_type (id, name) VALUES
  (1, 'Salud'),
  (2, 'Desarrollo Personal'),
  (3, 'Productividad'),
  (4, 'Bienestar'),
  (5, 'Social');

-- 4) Hábitos
-- Hábitos existentes para el Usuario 1
INSERT INTO habit (id, name, description, frequency, start_date, end_date, user_id, habit_type_id) VALUES
  ( 1, 'Ejercicio Diario',     'Rutina de ejercicio físico',           'Diaria',   '2025-05-01', '2025-12-31', 1, 1),
  ( 2, 'Leer Libros',          'Leer al menos 30 minutos al día',      'Diaria',   '2025-05-01', '2025-12-31', 1, 2),
  ( 3, 'Meditación',           'Meditar 15 minutos cada mañana',       'Diaria',   '2025-06-01', '2025-12-31', 1, 4),

-- Nuevos hábitos para el Usuario 1 (más completos)
  (16, 'Aprender un nuevo idioma', 'Estudiar vocabulario y gramática 3 veces a la semana', 'Semanal', '2025-04-01', '2025-12-31', 1, 2),
  (17, 'Planificar el día siguiente', 'Revisar y organizar las tareas del día siguiente', 'Diaria', '2025-04-15', '2025-12-31', 1, 3),
  (18, 'Desconexión digital', 'Evitar pantallas 1 hora antes de dormir', 'Diaria', '2025-05-01', '2025-12-31', 1, 4),
  (19, 'Networking profesional', 'Contactar con 2 profesionales al mes', 'Mensual', '2025-03-01', '2025-12-31', 1, 5),
  (20, 'Cocinar saludable', 'Preparar al menos 3 comidas caseras a la semana', 'Semanal', '2025-04-01', '2025-12-31', 1, 1),
  (21, 'Escribir código', 'Dedicar 1 hora diaria a proyectos personales de programación', 'Diaria', '2025-03-01', '2025-12-31', 1, 3),
  (22, 'Cuidado de plantas', 'Regar y cuidar las plantas de interior', 'Semanal', '2025-05-01', '2025-12-31', 1, 4),

-- Hábitos para otros usuarios (sin cambios)
  ( 4, 'Caminar 10000 pasos',  'Completar 10000 pasos diarios',       'Diaria',   '2025-05-15', '2025-12-31', 2, 1),
  ( 5, 'Estudio Java',         'Avanzar en el curso de Java',          'Semanal',  '2025-04-01', '2025-10-01', 2, 3),
  ( 6, 'Aprender guitarra',    'Practicar guitarra 20 minutos',       'Diaria',   '2025-05-20', '2025-12-31', 2, 2),
  ( 7, 'Beber Agua',           'Tomar 2 litros de agua',               'Diaria',   '2025-05-01', '2025-12-31', 3, 1),
  ( 8, 'Escribir Diario',      'Anotar pensamientos cada noche',       'Diaria',   '2025-05-10', '2025-12-31', 3, 4),
  ( 9, 'Cursos Online',        'Completar módulos semanales',          'Semanal',  '2025-04-15', '2025-11-30', 3, 3),
  (10, 'Yoga',                 'Sesión de yoga matutina',              'Diaria',   '2025-06-01', '2025-12-31', 4, 1),
  (11, 'Podcast Educativo',    'Escuchar podcast 30 minutos',          'Diaria',   '2025-05-05', '2025-12-31', 4, 2),
  (12, 'Revisión de Tareas',   'Planificar tareas semanales',          'Semanal',  '2025-05-01', '2025-12-31', 4, 3),
  (13, 'Voluntariado',         'Participar en acciones sociales',      'Mensual',  '2025-05-01', '2025-12-31', 5, 5),
  (14, 'Aprender Idioma',      'Estudiar inglés 3 veces por semana',   'Semanal',  '2025-04-01', '2025-12-31', 5, 2),
  (15, 'Fotografía',           'Practicar fotografía fines de semana', 'Semanal',  '2025-06-01', '2025-12-31', 5, 4);


-- 5) Registros diarios para el Usuario 1 (Extendido y completo)
-- Generamos una secuencia de IDs para daily_record a partir de 1
INSERT INTO daily_record (id, date, completed, habit_id) VALUES
-- Hábitos del usuario 1 (ID 1, 2, 3, 16-22)
-- Ejercicio Diario (ID 1) - Diaria
  (1,  '2025-05-01', TRUE, 1), (2,  '2025-05-02', TRUE, 1), (3,  '2025-05-03', FALSE, 1),
  (4,  '2025-05-04', TRUE, 1), (5,  '2025-05-05', TRUE, 1), (6,  '2025-05-06', FALSE, 1),
  (7,  '2025-05-07', TRUE, 1), (8,  '2025-05-08', TRUE, 1), (9,  '2025-05-09', TRUE, 1),
  (10, '2025-05-10', FALSE, 1), (11, '2025-05-11', TRUE, 1), (12, '2025-05-12', TRUE, 1),
  (13, '2025-05-13', TRUE, 1), (14, '2025-05-14', FALSE, 1), (15, '2025-05-15', TRUE, 1),
  (16, '2025-05-16', TRUE, 1), (17, '2025-05-17', TRUE, 1), (18, '2025-05-18', FALSE, 1),
  (19, '2025-05-19', TRUE, 1), (20, '2025-05-20', TRUE, 1), (21, '2025-05-21', TRUE, 1),
  (22, '2025-05-22', FALSE, 1), (23, '2025-05-23', TRUE, 1), (24, '2025-05-24', TRUE, 1),
  (25, '2025-05-25', TRUE, 1), (26, '2025-05-26', FALSE, 1), (27, '2025-05-27', TRUE, 1),
  (28, '2025-05-28', TRUE, 1), (29, '2025-05-29', TRUE, 1), (30, '2025-05-30', FALSE, 1),
  (31, '2025-05-31', TRUE, 1), (32, '2025-06-01', TRUE, 1), (33, '2025-06-02', TRUE, 1),
  (34, '2025-06-03', FALSE, 1), (35, '2025-06-04', TRUE, 1), (36, '2025-06-05', TRUE, 1),
  (37, '2025-06-06', TRUE, 1), (38, '2025-06-07', FALSE, 1),

-- Leer Libros (ID 2) - Diaria
  (39, '2025-05-01', TRUE, 2), (40, '2025-05-02', TRUE, 2), (41, '2025-05-03', TRUE, 2),
  (42, '2025-05-04', FALSE, 2), (43, '2025-05-05', TRUE, 2), (44, '2025-05-06', TRUE, 2),
  (45, '2025-05-07', TRUE, 2), (46, '2025-05-08', TRUE, 2), (47, '2025-05-09', FALSE, 2),
  (48, '2025-05-10', TRUE, 2), (49, '2025-05-11', TRUE, 2), (50, '2025-05-12', TRUE, 2),
  (51, '2025-05-13', TRUE, 2), (52, '2025-05-14', TRUE, 2), (53, '2025-05-15', FALSE, 2),
  (54, '2025-05-16', TRUE, 2), (55, '2025-05-17', TRUE, 2), (56, '2025-05-18', TRUE, 2),
  (57, '2025-05-19', TRUE, 2), (58, '2025-05-20', FALSE, 2), (59, '2025-05-21', TRUE, 2),
  (60, '2025-05-22', TRUE, 2), (61, '2025-05-23', TRUE, 2), (62, '2025-05-24', TRUE, 2),
  (63, '2025-05-25', FALSE, 2), (64, '2025-05-26', TRUE, 2), (65, '2025-05-27', TRUE, 2),
  (66, '2025-05-28', TRUE, 2), (67, '2025-05-29', TRUE, 2), (68, '2025-05-30', TRUE, 2),
  (69, '2025-05-31', FALSE, 2), (70, '2025-06-01', TRUE, 2), (71, '2025-06-02', TRUE, 2),
  (72, '2025-06-03', TRUE, 2), (73, '2025-06-04', TRUE, 2), (74, '2025-06-05', FALSE, 2),
  (75, '2025-06-06', TRUE, 2), (76, '2025-06-07', TRUE, 2),

-- Meditación (ID 3) - Diaria
  (77, '2025-06-01', TRUE, 3), (78, '2025-06-02', TRUE, 3), (79, '2025-06-03', TRUE, 3),
  (80, '2025-06-04', FALSE, 3), (81, '2025-06-05', TRUE, 3), (82, '2025-06-06', TRUE, 3),
  (83, '2025-06-07', TRUE, 3),

-- Aprender un nuevo idioma (ID 16) - Semanal (ej. Lunes, Miércoles, Viernes)
  (84, '2025-04-01', TRUE, 16), (85, '2025-04-03', TRUE, 16), (86, '2025-04-05', FALSE, 16),
  (87, '2025-04-07', TRUE, 16), (88, '2025-04-09', TRUE, 16), (89, '2025-04-11', TRUE, 16),
  (90, '2025-04-14', TRUE, 16), (91, '2025-04-16', FALSE, 16), (92, '2025-04-18', TRUE, 16),
  (93, '2025-04-21', TRUE, 16), (94, '2025-04-23', TRUE, 16), (95, '2025-04-25', TRUE, 16),
  (96, '2025-04-28', FALSE, 16), (97, '2025-04-30', TRUE, 16), (98, '2025-05-02', TRUE, 16),
  (99, '2025-05-05', TRUE, 16), (100, '2025-05-07', TRUE, 16), (101, '2025-05-09', FALSE, 16),
  (102, '2025-05-12', TRUE, 16), (103, '2025-05-14', TRUE, 16), (104, '2025-05-16', TRUE, 16),
  (105, '2025-05-19', TRUE, 16), (106, '2025-05-21', FALSE, 16), (107, '2025-05-23', TRUE, 16),
  (108, '2025-05-26', TRUE, 16), (109, '2025-05-28', TRUE, 16), (110, '2025-05-30', FALSE, 16),
  (111, '2025-06-02', TRUE, 16), (112, '2025-06-04', TRUE, 16), (113, '2025-06-06', TRUE, 16),

-- Planificar el día siguiente (ID 17) - Diaria
  (114, '2025-04-15', TRUE, 17), (115, '2025-04-16', TRUE, 17), (116, '2025-04-17', TRUE, 17),
  (117, '2025-04-18', FALSE, 17), (118, '2025-04-19', TRUE, 17), (119, '2025-04-20', TRUE, 17),
  (120, '2025-04-21', TRUE, 17), (121, '2025-04-22', TRUE, 17), (122, '2025-04-23', FALSE, 17),
  (123, '2025-04-24', TRUE, 17), (124, '2025-04-25', TRUE, 17), (125, '2025-04-26', TRUE, 17),
  (126, '2025-04-27', TRUE, 17), (127, '2025-04-28', FALSE, 17), (128, '2025-04-29', TRUE, 17),
  (129, '2025-04-30', TRUE, 17), (130, '2025-05-01', TRUE, 17), (131, '2025-05-02', TRUE, 17),
  (132, '2025-05-03', FALSE, 17), (133, '2025-05-04', TRUE, 17), (134, '2025-05-05', TRUE, 17),
  (135, '2025-05-06', TRUE, 17), (136, '2025-05-07', TRUE, 17), (137, '2025-05-08', FALSE, 17),
  (138, '2025-05-09', TRUE, 17), (139, '2025-05-10', TRUE, 17), (140, '2025-05-11', TRUE, 17),
  (141, '2025-05-12', TRUE, 17), (142, '2025-05-13', FALSE, 17), (143, '2025-05-14', TRUE, 17),
  (144, '2025-05-15', TRUE, 17), (145, '2025-05-16', TRUE, 17), (146, '2025-05-17', TRUE, 17),
  (147, '2025-05-18', FALSE, 17), (148, '2025-05-19', TRUE, 17), (149, '2025-05-20', TRUE, 17),
  (150, '2025-05-21', TRUE, 17), (151, '2025-05-22', TRUE, 17), (152, '2025-05-23', FALSE, 17),
  (153, '2025-05-24', TRUE, 17), (154, '2025-05-25', TRUE, 17), (155, '2025-05-26', TRUE, 17),
  (156, '2025-05-27', TRUE, 17), (157, '2025-05-28', FALSE, 17), (158, '2025-05-29', TRUE, 17),
  (159, '2025-05-30', TRUE, 17), (160, '2025-05-31', TRUE, 17), (161, '2025-06-01', TRUE, 17),
  (162, '2025-06-02', FALSE, 17), (163, '2025-06-03', TRUE, 17), (164, '2025-06-04', TRUE, 17),
  (165, '2025-06-05', TRUE, 17), (166, '2025-06-06', TRUE, 17), (167, '2025-06-07', FALSE, 17),

-- Desconexión digital (ID 18) - Diaria
  (168, '2025-05-01', TRUE, 18), (169, '2025-05-02', TRUE, 18), (170, '2025-05-03', FALSE, 18),
  (171, '2025-05-04', TRUE, 18), (172, '2025-05-05', TRUE, 18), (173, '2025-05-06', TRUE, 18),
  (174, '2025-05-07', TRUE, 18), (175, '2025-05-08', FALSE, 18), (176, '2025-05-09', TRUE, 18),
  (177, '2025-05-10', TRUE, 18), (178, '2025-05-11', TRUE, 18), (179, '2025-05-12', TRUE, 18),
  (180, '2025-05-13', FALSE, 18), (181, '2025-05-14', TRUE, 18), (182, '2025-05-15', TRUE, 18),
  (183, '2025-05-16', TRUE, 18), (184, '2025-05-17', TRUE, 18), (185, '2025-05-18', FALSE, 18),
  (186, '2025-05-19', TRUE, 18), (187, '2025-05-20', TRUE, 18), (188, '2025-05-21', TRUE, 18),
  (189, '2025-05-22', TRUE, 18), (190, '2025-05-23', FALSE, 18), (191, '2025-05-24', TRUE, 18),
  (192, '2025-05-25', TRUE, 18), (193, '2025-05-26', TRUE, 18), (194, '2025-05-27', TRUE, 18),
  (195, '2025-05-28', FALSE, 18), (196, '2025-05-29', TRUE, 18), (197, '2025-05-30', TRUE, 18),
  (198, '2025-05-31', TRUE, 18), (199, '2025-06-01', TRUE, 18), (200, '2025-06-02', FALSE, 18),
  (201, '2025-06-03', TRUE, 18), (202, '2025-06-04', TRUE, 18), (203, '2025-06-05', TRUE, 18),
  (204, '2025-06-06', TRUE, 18), (205, '2025-06-07', FALSE, 18),

-- Networking profesional (ID 19) - Mensual (ej. el día 15 de cada mes)
  (206, '2025-03-15', TRUE, 19),
  (207, '2025-04-15', TRUE, 19),
  (208, '2025-05-15', FALSE, 19),
  (209, '2025-06-07', TRUE, 19), -- Ejemplo de cumplimiento en otra fecha para este mes

-- Cocinar saludable (ID 20) - Semanal (ej. cada Domingo, Lunes y Jueves)
  (210, '2025-04-01', TRUE, 20), (211, '2025-04-04', TRUE, 20), (212, '2025-04-07', TRUE, 20),
  (213, '2025-04-08', FALSE, 20), (214, '2025-04-11', TRUE, 20), (215, '2025-04-14', TRUE, 20),
  (216, '2025-04-15', TRUE, 20), (217, '2025-04-18', TRUE, 20), (218, '2025-04-21', FALSE, 20),
  (219, '2025-04-22', TRUE, 20), (220, '2025-04-25', TRUE, 20), (221, '2025-04-28', TRUE, 20),
  (222, '2025-04-29', TRUE, 20), (223, '2025-05-02', FALSE, 20), (224, '2025-05-05', TRUE, 20),
  (225, '2025-05-06', TRUE, 20), (226, '2025-05-09', TRUE, 20), (227, '2025-05-12', FALSE, 20),
  (228, '2025-05-13', TRUE, 20), (229, '2025-05-16', TRUE, 20), (230, '2025-05-19', TRUE, 20),
  (231, '2025-05-20', FALSE, 20), (232, '2025-05-23', TRUE, 20), (233, '2025-05-26', TRUE, 20),
  (234, '2025-05-27', TRUE, 20), (235, '2025-05-30', FALSE, 20), (236, '2025-06-02', TRUE, 20),
  (237, '2025-06-03', TRUE, 20), (238, '2025-06-06', TRUE, 20),

-- Escribir código (ID 21) - Diaria
  (239, '2025-03-01', TRUE, 21), (240, '2025-03-02', TRUE, 21), (241, '2025-03-03', TRUE, 21),
  (242, '2025-03-04', FALSE, 21), (243, '2025-03-05', TRUE, 21), (244, '2025-03-06', TRUE, 21),
  (245, '2025-03-07', TRUE, 21), (246, '2025-03-08', TRUE, 21), (247, '2025-03-09', FALSE, 21),
  (248, '2025-03-10', TRUE, 21), (249, '2025-03-11', TRUE, 21), (250, '2025-03-12', TRUE, 21),
  (251, '2025-03-13', TRUE, 21), (252, '2025-03-14', FALSE, 21), (253, '2025-03-15', TRUE, 21),
  (254, '2025-03-16', TRUE, 21), (255, '2025-03-17', TRUE, 21), (256, '2025-03-18', TRUE, 21),
  (257, '2025-03-19', FALSE, 21), (258, '2025-03-20', TRUE, 21), (259, '2025-03-21', TRUE, 21),
  (260, '2025-03-22', TRUE, 21), (261, '2025-03-23', TRUE, 21), (262, '2025-03-24', FALSE, 21),
  (263, '2025-03-25', TRUE, 21), (264, '2025-03-26', TRUE, 21), (265, '2025-03-27', TRUE, 21),
  (266, '2025-03-28', TRUE, 21), (267, '2025-03-29', FALSE, 21), (268, '2025-03-30', TRUE, 21),
  (269, '2025-03-31', TRUE, 21),
  (270, '2025-04-01', TRUE, 21), (271, '2025-04-02', TRUE, 21), (272, '2025-04-03', FALSE, 21),
  (273, '2025-04-04', TRUE, 21), (274, '2025-04-05', TRUE, 21), (275, '2025-04-06', TRUE, 21),
  (276, '2025-04-07', TRUE, 21), (277, '2025-04-08', FALSE, 21), (278, '2025-04-09', TRUE, 21),
  (279, '2025-04-10', TRUE, 21), (280, '2025-04-11', TRUE, 21), (281, '2025-04-12', TRUE, 21),
  (282, '2025-04-13', FALSE, 21), (283, '2025-04-14', TRUE, 21), (284, '2025-04-15', TRUE, 21),
  (285, '2025-04-16', TRUE, 21), (286, '2025-04-17', TRUE, 21), (287, '2025-04-18', FALSE, 21),
  (288, '2025-04-19', TRUE, 21), (289, '2025-04-20', TRUE, 21), (290, '2025-04-21', TRUE, 21),
  (291, '2025-04-22', TRUE, 21), (292, '2025-04-23', FALSE, 21), (293, '2025-04-24', TRUE, 21),
  (294, '2025-04-25', TRUE, 21), (295, '2025-04-26', TRUE, 21), (296, '2025-04-27', TRUE, 21),
  (297, '2025-04-28', FALSE, 21), (298, '2025-04-29', TRUE, 21), (299, '2025-04-30', TRUE, 21),
  (300, '2025-05-01', TRUE, 21), (301, '2025-05-02', TRUE, 21), (302, '2025-05-03', FALSE, 21),
  (303, '2025-05-04', TRUE, 21), (304, '2025-05-05', TRUE, 21), (305, '2025-05-06', TRUE, 21),
  (306, '2025-05-07', TRUE, 21), (307, '2025-05-08', FALSE, 21), (308, '2025-05-09', TRUE, 21),
  (309, '2025-05-10', TRUE, 21), (310, '2025-05-11', TRUE, 21), (311, '2025-05-12', TRUE, 21),
  (312, '2025-05-13', FALSE, 21), (313, '2025-05-14', TRUE, 21), (314, '2025-05-15', TRUE, 21),
  (315, '2025-05-16', TRUE, 21), (316, '2025-05-17', TRUE, 21), (317, '2025-05-18', FALSE, 21),
  (318, '2025-05-19', TRUE, 21), (319, '2025-05-20', TRUE, 21), (320, '2025-05-21', TRUE, 21),
  (321, '2025-05-22', TRUE, 21), (322, '2025-05-23', FALSE, 21), (323, '2025-05-24', TRUE, 21),
  (324, '2025-05-25', TRUE, 21), (325, '2025-05-26', TRUE, 21), (326, '2025-05-27', TRUE, 21),
  (327, '2025-05-28', FALSE, 21), (328, '2025-05-29', TRUE, 21), (329, '2025-05-30', TRUE, 21),
  (330, '2025-05-31', TRUE, 21), (331, '2025-06-01', TRUE, 21), (332, '2025-06-02', FALSE, 21),
  (333, '2025-06-03', TRUE, 21), (334, '2025-06-04', TRUE, 21), (335, '2025-06-05', TRUE, 21),
  (336, '2025-06-06', TRUE, 21), (337, '2025-06-07', FALSE, 21),

-- Cuidado de plantas (ID 22) - Semanal (ej. cada miércoles)
  (338, '2025-05-07', TRUE, 22), (339, '2025-05-14', TRUE, 22), (340, '2025-05-21', FALSE, 22),
  (341, '2025-05-28', TRUE, 22), (342, '2025-06-04', TRUE, 22),

-- Registros para otros usuarios (manteniendo lo que tenías, puedes expandirlos si quieres)
  (343, '2025-05-01', TRUE, 4), -- Usuario 2
  (344, '2025-05-02', TRUE, 4),
  (345, '2025-04-05', TRUE, 5),
  (346, '2025-04-12', FALSE, 5),
  (347, '2025-05-01', TRUE, 7), -- Usuario 3
  (348, '2025-05-02', TRUE, 7),
  (349, '2025-05-05', TRUE, 11), -- Usuario 4
  (350, '2025-05-12', FALSE, 12),
  (351, '2025-05-15', TRUE, 13), -- Usuario 5
  (352, '2025-04-07', TRUE, 14);


-- 6) Metas
-- Metas existentes para el Usuario 1
INSERT INTO goal (id, name, description, user_id) VALUES
  (1,  'Correr 5K',         'Completar una carrera de 5 km',        1),
  (2,  'Leer 5 libros',     'Terminar 5 libros este año',           1),

-- Nuevas metas para el Usuario 1 (más completas)
  (11, 'Dominar un idioma', 'Alcanzar fluidez en inglés',           1),
  (12, 'Productividad Élite', 'Automatizar tareas rutinarias y mejorar eficiencia', 1),
  (13, 'Bienestar Pleno',   'Reducir el estrés y mejorar la calidad del sueño', 1),
  (14, 'Red de contactos', 'Crear una sólida red de contactos profesionales', 1),
  (15, 'Desarrollo Fullstack', 'Construir una aplicación web completa y desplegarla', 1),
  (16, 'Volumen de lectura', 'Leer 12 libros en el año', 1),

-- Metas para otros usuarios (sin cambios)
  (3,  'Caminar 50K',       'Alcanzar 50 km a la semana caminando',  2),
  (4,  'Certificación Java','Obtener la certificación Oracle Java',  2),
  (5,  'Hidratarme',        'Mantener 2L de agua diarios',          3),
  (6,  'Escribir 30 días',  'Escribir en el diario 30 días seguidos', 3),
  (7,  'Asistir Yoga',      'Ir a clase de yoga 8 veces',           4),
  (8,  'Podcast 100 hrs',   'Escuchar 100 horas de podcast',        4),
  (9,  'Voluntariado+',     'Participar mínimo 6 meses en ONG',     5),
  (10, 'Inglés B2',         'Alcanzar nivel B2 en TOEIC',           5);