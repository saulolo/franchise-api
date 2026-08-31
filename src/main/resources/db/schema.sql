/*
* Saul Echeverri
* 2026-08-29
* Se crea la tabla franchises para el almacenamiento y gestión de las franquicias
*/
CREATE TABLE IF NOT EXISTS franchises (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

/*
* Saul Echeverri
* 2026-08-29
* Se crea la tabla branches relacionada con franchises para la gestión de las sucursales
*/
CREATE TABLE IF NOT EXISTS branches (
    id BIGSERIAL PRIMARY KEY,
    franchise_id BIGINT NOT NULL,
    name VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_franchise FOREIGN KEY (franchise_id) REFERENCES franchises(id) ON DELETE CASCADE
);

/*
* Saul Echeverri
* 2026-08-29
* Se crea la tabla products relacionada con branches para administrar el inventario y stock de los productoss
*/
CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    branch_id BIGINT NOT NULL,
    name VARCHAR(30) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_branch FOREIGN KEY (branch_id) REFERENCES branches(id) ON DELETE CASCADE
);
