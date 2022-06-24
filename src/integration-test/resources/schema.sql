DROP TABLE IF EXISTS item_stock;
DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS item_view;

CREATE TABLE item (
    id INT AUTO_INCREMENT PRIMARY KEY,
--     name VARCHAR(50) NOT NULL UNIQUE, problem in inserting data
    name VARCHAR(50) NOT NULL,
    description VARCHAR(200),
    price NUMERIC(20, 2) NOT NULL CHECK (price > 0)
);

CREATE TABLE item_view (
    id INT AUTO_INCREMENT PRIMARY KEY,
    item_Id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE item_stock (
    id INT AUTO_INCREMENT PRIMARY KEY,
--     item_id INT NOT NULL UNIQUE, problem in inserting data
    item_id INT NOT NULL,
    quantity_for_sell INT NOT NULL,
    FOREIGN KEY (item_id) REFERENCES item (id)
);
