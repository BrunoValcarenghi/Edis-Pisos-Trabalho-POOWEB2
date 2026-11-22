CREATE TABLE IF NOT EXISTS usuario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    email VARCHAR(180) NOT NULL,
    senha VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS produto (
    id SERIAL PRIMARY KEY,
    marca VARCHAR(100) NOT NULL,
    modelo VARCHAR(120) NOT NULL
);

CREATE TABLE IF NOT EXISTS favorito (
    usuario_id INTEGER NOT NULL,
    produto_id INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS avaliacao (
    id SERIAL PRIMARY KEY,
    texto VARCHAR(2000) NOT NULL,
    usuario_id INTEGER NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_usuario_email ON usuario (LOWER(email));
CREATE UNIQUE INDEX IF NOT EXISTS ux_favorito_usuario_produto ON favorito (usuario_id, produto_id);

DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_favorito_usuario') THEN
        ALTER TABLE favorito ADD CONSTRAINT fk_favorito_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE;
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_favorito_produto') THEN
        ALTER TABLE favorito ADD CONSTRAINT fk_favorito_produto FOREIGN KEY (produto_id) REFERENCES produto(id) ON DELETE CASCADE;
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_avaliacao_usuario') THEN
        ALTER TABLE avaliacao ADD CONSTRAINT fk_avaliacao_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE;
    END IF;
END $$;
