-- Очищаем таблицы
DELETE FROM order_items;
DELETE FROM orders;
DELETE FROM dishes;
DELETE FROM users;

-- Сброс идентификаторов
DBCC CHECKIDENT ('users', RESEED, 0);
DBCC CHECKIDENT ('dishes', RESEED, 0);
DBCC CHECKIDENT ('orders', RESEED, 0);
DBCC CHECKIDENT ('order_items', RESEED, 0);

-- Добавляем пользователей
INSERT INTO users (username, password, full_name, phone, role, created_at) VALUES
    ('admin', '$2a$10$rRy8GSJ5QyqKqZVZQqQqQeQqQqQqQqQqQqQqQqQqQqQqQqQqQq', 'Администратор', '+7 (999) 111-11-11', 'ADMIN', GETDATE());

INSERT INTO users (username, password, full_name, phone, role, created_at) VALUES
    ('cook', '$2a$10$rRy8GSJ5QyqKqZVZQqQqQeQqQqQqQqQqQqQqQqQqQqQqQqQqQq', 'Повар Иван', '+7 (999) 222-22-22', 'COOK', GETDATE());

INSERT INTO users (username, password, full_name, phone, role, created_at) VALUES
    ('courier', '$2a$10$rRy8GSJ5QyqKqZVZQqQqQeQqQqQqQqQqQqQqQqQqQqQqQqQqQq', 'Курьер Петр', '+7 (999) 333-33-33', 'COURIER', GETDATE());

-- Добавляем блюда с НОВЫМИ ССЫЛКАМИ (Pexels - быстрее)
INSERT INTO dishes (name, description, price, category, available, image_url) VALUES
    ('Маргарита', 'Томатный соус, моцарелла, базилик, оливковое масло', 450.00, 'Пицца', 1, 'https://images.pexels.com/photos/2147491/pexels-photo-2147491.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop');

INSERT INTO dishes (name, description, price, category, available, image_url) VALUES
    ('Пепперони', 'Томатный соус, моцарелла, пепперони, орегано', 550.00, 'Пицца', 1, 'https://images.pexels.com/photos/803290/pexels-photo-803290.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop');

INSERT INTO dishes (name, description, price, category, available, image_url) VALUES
    ('Борщ', 'Украинский борщ с говядиной, свеклой, сметаной', 320.00, 'Супы', 1, 'https://images.pexels.com/photos/539451/pexels-photo-539451.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop');

INSERT INTO dishes (name, description, price, category, available, image_url) VALUES
    ('Тыквенный суп', 'Крем-суп из тыквы с имбирем и сливками', 280.00, 'Супы', 1, 'https://images.pexels.com/photos/566566/pexels-photo-566566.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop');

INSERT INTO dishes (name, description, price, category, available, image_url) VALUES
    ('Цезарь с курицей', 'Салат с курицей, салатом романо, пармезаном и соусом цезарь', 380.00, 'Салаты', 1, 'https://images.pexels.com/photos/1213710/pexels-photo-1213710.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop');

INSERT INTO dishes (name, description, price, category, available, image_url) VALUES
    ('Греческий салат', 'Свежие овощи, фета, оливки, оливковое масло', 320.00, 'Салаты', 1, 'https://images.pexels.com/photos/1213711/pexels-photo-1213711.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop');

INSERT INTO dishes (name, description, price, category, available, image_url) VALUES
    ('Паста Карбонара', 'Спагетти с беконом в сливочном соусе', 420.00, 'Горячее', 1, 'https://images.pexels.com/photos/1279330/pexels-photo-1279330.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop');

INSERT INTO dishes (name, description, price, category, available, image_url) VALUES
    ('Котлета по-киевски', 'Куриная котлета с маслом, картофельное пюре', 390.00, 'Горячее', 1, 'https://images.pexels.com/photos/6210876/pexels-photo-6210876.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop');

INSERT INTO dishes (name, description, price, category, available, image_url) VALUES
    ('Рис с овощами', 'Рис с болгарским перцем, морковью и горошком', 250.00, 'Горячее', 1, 'https://images.pexels.com/photos/723198/pexels-photo-723198.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop');

INSERT INTO dishes (name, description, price, category, available, image_url) VALUES
    ('Чай черный', 'Черный чай с лимоном', 100.00, 'Напитки', 1, 'https://images.pexels.com/photos/302899/pexels-photo-302899.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop');

INSERT INTO dishes (name, description, price, category, available, image_url) VALUES
    ('Кофе американо', 'Американо', 150.00, 'Напитки', 1, 'https://images.pexels.com/photos/312418/pexels-photo-312418.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop');

INSERT INTO dishes (name, description, price, category, available, image_url) VALUES
    ('Морс клюквенный', 'Домашний клюквенный морс', 120.00, 'Напитки', 1, 'https://images.pexels.com/photos/96974/pexels-photo-96974.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop');

INSERT INTO dishes (name, description, price, category, available, image_url) VALUES
    ('Чизкейк', 'Нью-йоркский чизкейк с ягодным соусом', 280.00, 'Десерты', 1, 'https://images.pexels.com/photos/4109998/pexels-photo-4109998.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop');

INSERT INTO dishes (name, description, price, category, available, image_url) VALUES
    ('Тирамису', 'Классический итальянский десерт', 320.00, 'Десерты', 1, 'https://images.pexels.com/photos/239581/pexels-photo-239581.jpeg?auto=compress&cs=tinysrgb&w=400&h=300&fit=crop');

-- Добавляем тестовый заказ
INSERT INTO orders (client_name, client_phone, address, status, total_amount, created_at, updated_at) VALUES
    ('Иван Петров', '+7 (999) 123-45-67', 'ул. Пушкина, д. 10, кв. 5', 'NEW', 1250.00, GETDATE(), GETDATE());

-- Добавляем позиции заказа
INSERT INTO order_items (order_id, dish_id, quantity, price)
SELECT id, 1, 1, 450.00 FROM orders WHERE client_phone = '+7 (999) 123-45-67' AND client_name = 'Иван Петров';

INSERT INTO order_items (order_id, dish_id, quantity, price)
SELECT id, 10, 2, 100.00 FROM orders WHERE client_phone = '+7 (999) 123-45-67' AND client_name = 'Иван Петров';

INSERT INTO order_items (order_id, dish_id, quantity, price)
SELECT id, 13, 1, 280.00 FROM orders WHERE client_phone = '+7 (999) 123-45-67' AND client_name = 'Иван Петров';