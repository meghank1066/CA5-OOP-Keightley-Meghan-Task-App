CREATE DATABASE IF NOT EXISTS task_db;

USE task_db;

DROP TABLE IF EXISTS tasks;

CREATE TABLE tasks (
  task_id INT NOT NULL AUTO_INCREMENT,
  title VARCHAR(50),
  status ENUM('OPEN', 'DONE', 'PROGRESS'),
 priority ENUM('CRITICAL', 'HIGH', 'MEDIUM', 'LOW', 'MIN'),
  description TEXT,
   due_date DATE,
 PRIMARY KEY (task_id)
);

INSERT INTO tasks (title, status, priority, description, due_date) VALUES
     ("CA2 for Server Side", 'PROGRESS', 'HIGH', "Create a Blog Website with PHP & MySQL in Laravel", '2024-03-15'),
     ("CA2 for Full Stack Web Development", 'DONE', 'CRITICAL', "Build an E-commerce Website by using the MERN Stack", '2024-03-03'),
     ("Study for C++ CA", 'DONE', 'MEDIUM', "For Handwritten CA: Iterators, Vectors, Files, Pointers", '2024-03-05'),
     ("Workout at the gym", 'OPEN', 'MEDIUM', "Hit the gym for a workout session to stay fit and healthy", '2024-03-09'),
     ("Organise Files", 'OPEN', 'LOW', "Organise files on the hard drive", '2024-03-26'),
     ("Create user personas for Ux design", 'PROGRESS', 'MEDIUM', "Develop user personas to guide Ui design decisions and improvements", '2024-03-11'),
     ("Go to Amber", 'OPEN', 'MIN', "Check if they have anything new and maybe stock up supplies", '2024-03-28'),
     ("Organise notes for Networking", 'OPEN', 'LOW', "Reorganise information in a better format", '2024-03-19'),
     ("Prepare presentation slides", 'OPEN', 'MEDIUM', "Create slides for the upcoming presentation on Work Placement", '2024-03-20'),
     ("Organise files in Downloads", 'OPEN', 'LOW', "Get rid of anything not needed and organise", '2024-03-10')
;

SELECT * FROM tasks;