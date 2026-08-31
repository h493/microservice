INSERT INTO product (id, title, price, stock) VALUES (1, 'iPhone 15', 79999.00, 50);
INSERT INTO product (id, title, price, stock) VALUES (2, 'Samsung Galaxy S24', 69999.00, 40);
INSERT INTO product (id, title, price, stock) VALUES (3, 'Sony WH-1000XM5 Headphones', 29999.00, 100);
INSERT INTO product (id, title, price, stock) VALUES (4, 'Dell XPS 13 Laptop', 119999.00, 20);
INSERT INTO product (id, title, price, stock) VALUES (5, 'Logitech MX Master 3S Mouse', 8999.00, 150);
INSERT INTO product (id, title, price, stock) VALUES (6, 'Apple Watch Series 9', 41999.00, 60);
INSERT INTO product (id, title, price, stock) VALUES (7, 'Kindle Paperwhite', 13999.00, 80);
INSERT INTO product (id, title, price, stock) VALUES (8, 'Nintendo Switch OLED', 34999.00, 35);

ALTER SEQUENCE IF EXISTS product_seq RESTART WITH 1000;
