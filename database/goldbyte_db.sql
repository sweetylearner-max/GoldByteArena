CREATE DATABASE IF NOT EXISTS goldbyte_db CHARACTER SET utf8mb4;
USE goldbyte_db;

DROP TABLE IF EXISTS mission_submissions;
DROP TABLE IF EXISTS missions;
DROP TABLE IF EXISTS admins;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id            INT AUTO_INCREMENT PRIMARY KEY,
  username      VARCHAR(40)  NOT NULL UNIQUE,
  email         VARCHAR(100) NOT NULL UNIQUE,
  password_hash CHAR(64)     NOT NULL,
  xp            INT NOT NULL DEFAULT 0,
  level         INT NOT NULL DEFAULT 1,
  streak        INT NOT NULL DEFAULT 0,
  last_active   DATE NULL,
  created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE admins (
  id            INT AUTO_INCREMENT PRIMARY KEY,
  username      VARCHAR(40) NOT NULL UNIQUE,
  password_hash CHAR(64)    NOT NULL
);

CREATE TABLE missions (
  id             INT AUTO_INCREMENT PRIMARY KEY,
  title          VARCHAR(150) NOT NULL,
  description    TEXT NOT NULL,
  category       ENUM('Java','SQL','DSA') NOT NULL,
  difficulty     ENUM('Easy','Medium','Hard') NOT NULL,
  option_a       VARCHAR(255) NOT NULL,
  option_b       VARCHAR(255) NOT NULL,
  option_c       VARCHAR(255) NOT NULL,
  option_d       VARCHAR(255) NOT NULL,
  correct_option CHAR(1) NOT NULL,
  xp_reward      INT NOT NULL,
  created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT chk_correct CHECK (correct_option IN ('A','B','C','D')),
  CONSTRAINT chk_xp CHECK (xp_reward > 0)
);

CREATE TABLE mission_submissions (
  id              INT AUTO_INCREMENT PRIMARY KEY,
  user_id         INT NOT NULL,
  mission_id      INT NOT NULL,
  selected_option CHAR(1) NOT NULL,
  is_correct      BOOLEAN NOT NULL,
  xp_awarded      INT NOT NULL DEFAULT 0,
  submitted_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id)    REFERENCES users(id)    ON DELETE CASCADE,
  FOREIGN KEY (mission_id) REFERENCES missions(id) ON DELETE CASCADE,
  UNIQUE KEY one_attempt (user_id, mission_id)
);

-- Passwords are SHA-256 hashes. admin / admin123, others / pass123
INSERT INTO admins (username, password_hash) VALUES
 ('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9');

INSERT INTO users (username, email, password_hash, xp, level, streak, last_active) VALUES
 ('AuricKnight', 'auric@arena.dev',  '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 450, 5, 4, CURDATE()),
 ('ViolaByte',   'viola@arena.dev',  '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 300, 4, 2, CURDATE()),
 ('NovaCoder',   'nova@arena.dev',   '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 200, 3, 1, CURDATE());

INSERT INTO missions (title, description, category, difficulty, option_a, option_b, option_c, option_d, correct_option, xp_reward) VALUES
('The Final Keyword', 'What does declaring a Java class as "final" do?', 'Java', 'Easy', 'Makes all its fields constant', 'Prevents it from being subclassed', 'Prevents it from being instantiated', 'Makes all its methods static', 'B', 20),
('Equals vs ==', 'Two String objects are created with "new String(\"gold\")". What does == compare?', 'Java', 'Medium', 'The characters inside the strings', 'The string lengths', 'The memory references', 'The hash codes only', 'C', 30),
('Checked Exceptions', 'Which of these is a checked exception in Java?', 'Java', 'Medium', 'NullPointerException', 'ArrayIndexOutOfBoundsException', 'IOException', 'ArithmeticException', 'C', 30),
('HashMap Internals', 'In Java 8+, a HashMap bucket converts its linked list to a tree when collisions grow. Which tree is used?', 'Java', 'Hard', 'AVL tree', 'Red-black tree', 'B-tree', 'Trie', 'B', 40),
('Filter the Rows', 'Which clause filters rows BEFORE grouping happens?', 'SQL', 'Easy', 'HAVING', 'WHERE', 'ORDER BY', 'LIMIT', 'B', 20),
('Join Mastery', 'Which JOIN returns all rows from the left table, even without a match on the right?', 'SQL', 'Medium', 'INNER JOIN', 'RIGHT JOIN', 'LEFT JOIN', 'CROSS JOIN', 'C', 30),
('Unique Identity', 'Which constraint guarantees uniqueness AND disallows NULL?', 'SQL', 'Easy', 'UNIQUE', 'PRIMARY KEY', 'FOREIGN KEY', 'CHECK', 'B', 20),
('ACID Guardian', 'In ACID, which property ensures a committed transaction survives a crash?', 'SQL', 'Hard', 'Atomicity', 'Consistency', 'Isolation', 'Durability', 'D', 40),
('Stack Discipline', 'Which data structure follows Last-In-First-Out order?', 'DSA', 'Easy', 'Queue', 'Stack', 'Heap', 'Graph', 'B', 20),
('Binary Search Cost', 'What is the time complexity of binary search on a sorted array of n items?', 'DSA', 'Easy', 'O(n)', 'O(n log n)', 'O(log n)', 'O(1)', 'C', 20),
('Sorting Showdown', 'Which sorting algorithm has O(n log n) worst-case time and is NOT in-place?', 'DSA', 'Medium', 'Quick sort', 'Merge sort', 'Bubble sort', 'Insertion sort', 'B', 30),
('Graph Traversal', 'Which traversal finds the shortest path in an unweighted graph?', 'DSA', 'Hard', 'Depth-first search', 'Breadth-first search', 'Inorder traversal', 'Topological sort', 'B', 40);

-- Seed submissions so leaderboard counts look alive
INSERT INTO mission_submissions (user_id, mission_id, selected_option, is_correct, xp_awarded) VALUES
 (1,1,'B',1,20),(1,2,'C',1,45),(1,5,'B',1,20),(1,9,'B',1,20),(1,12,'B',1,80),
 (2,1,'B',1,20),(2,5,'B',1,20),(2,9,'B',1,20),
 (3,1,'B',1,20),(3,5,'B',1,20);
