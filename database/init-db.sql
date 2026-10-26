CREATE DATABASE IF NOT EXISTS bd_fragrances
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE bd_fragrances;

CREATE TABLE IF NOT EXISTS usuario (
  id INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL,
  password VARCHAR(255) NOT NULL,
  telefono VARCHAR(9) NOT NULL,
  dni VARCHAR(8) NOT NULL,
  direccion VARCHAR(200) NOT NULL,
  rol VARCHAR(20) NOT NULL DEFAULT 'CLIENTE',
  PRIMARY KEY (id),
  UNIQUE KEY uk_usuario_email (email),
  UNIQUE KEY uk_usuario_dni (dni)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

ALTER TABLE usuario
  ADD COLUMN IF NOT EXISTS rol VARCHAR(20) NOT NULL DEFAULT 'CLIENTE';

INSERT INTO usuario (nombre, email, password, telefono, dni, direccion, rol)
SELECT 'Grace Galán', 'mindsgraceluu@gmail.com', 'Ariana18.', '952729036', '73203436', 'Calle los pinos 441, Puente Piedra', 'ADMIN'
WHERE NOT EXISTS (
  SELECT 1 FROM usuario WHERE email = 'mindsgraceluu@gmail.com' OR dni = '73203436'
);

CREATE TABLE IF NOT EXISTS producto (
  id INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(150) NOT NULL,
  marca VARCHAR(100) NOT NULL,
  precio DOUBLE NOT NULL,
  descripcion VARCHAR(300),
  categoria VARCHAR(50),
  image_url VARCHAR(255),
  descuento_label VARCHAR(50),
  stock INT NOT NULL DEFAULT 0,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS contacto (
  id INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL,
  asunto VARCHAR(150) NOT NULL,
  mensaje VARCHAR(1000) NOT NULL,
  fecha DATETIME(6),
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS pedido (
  id INT NOT NULL AUTO_INCREMENT,
  usuario_id INT NOT NULL,
  producto_id INT NOT NULL,
  cantidad INT NOT NULL,
  total DOUBLE NOT NULL,
  fecha DATETIME(6),
  estado VARCHAR(50),
  metodo_pago VARCHAR(50),
  metodo_entrega VARCHAR(50),
  direccion_entrega VARCHAR(200),
  telefono_contacto VARCHAR(20),
  notas VARCHAR(500),
  PRIMARY KEY (id),
  KEY idx_pedido_usuario (usuario_id),
  KEY idx_pedido_producto (producto_id),
  CONSTRAINT fk_pedido_usuario
    FOREIGN KEY (usuario_id) REFERENCES usuario (id),
  CONSTRAINT fk_pedido_producto
    FOREIGN KEY (producto_id) REFERENCES producto (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

ALTER TABLE pedido
  ADD COLUMN IF NOT EXISTS metodo_pago VARCHAR(50),
  ADD COLUMN IF NOT EXISTS metodo_entrega VARCHAR(50),
  ADD COLUMN IF NOT EXISTS direccion_entrega VARCHAR(200),
  ADD COLUMN IF NOT EXISTS telefono_contacto VARCHAR(20),
  ADD COLUMN IF NOT EXISTS notas VARCHAR(500);
