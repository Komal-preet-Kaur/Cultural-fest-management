INSERT INTO participants (id, name, email, password, role) VALUES
  (1, 'Admin User', 'admin@festival.com', '$2a$10$u4Yk4Q8JmZxLk7z3m1z5oOeYwE3Yv8J7m3lYk1tX3e4M6v1s7O3g6', 'ADMIN'),
  (2, 'John Doe', 'john@example.com', '$2a$10$u4Yk4Q8JmZxLk7z3m1z5oOeYwE3Yv8J7m3lYk1tX3e4M6v1s7O3g6', 'USER'),
  (3, 'Jane Smith', 'jane@example.com', '$2a$10$u4Yk4Q8JmZxLk7z3m1z5oOeYwE3Yv8J7m3lYk1tX3e4M6v1s7O3g6', 'USER');

INSERT INTO events (id, name, category, scheduled_at, location, capacity) VALUES
  (1, 'Battle of Bands', 'Music', '2024-12-25T18:00:00', 'Main Auditorium', 300),
  (2, 'Cultural Dance Show', 'Dance', '2024-12-26T19:30:00', 'Dance Hall', 200),
  (3, 'Art Exhibition', 'Art', '2024-12-27T10:00:00', 'Gallery', 150),
  (4, 'Poetry Recitation', 'Literature', '2024-12-28T16:00:00', 'Library', 100),
  (5, 'Food Festival', 'Food', '2024-12-29T12:00:00', 'Outdoor Plaza', 500);


