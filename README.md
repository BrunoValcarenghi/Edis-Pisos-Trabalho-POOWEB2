# Edis Pisos - POOWEB II

Projeto do catálogo Edis Pisos evoluído com os conteúdos do semestre: API REST, Spring Data JPA, Flyway, Swagger/OpenAPI, validação, respostas HTTP e Spring Security com JWT.

## Executar localmente

Requisitos: Java 17+, PostgreSQL e Maven (o repositório inclui Maven Wrapper).

1. Crie o banco PostgreSQL `pisos` (ou informe outro nome em `DB_URL`).
2. Configure `DB_USER` e `DB_PASSWORD` se as credenciais locais forem diferentes das configurações de desenvolvimento.
3. Inicie o projeto


O Flyway aplica as migrações de `src/main/resources/db/migration`; o Hibernate valida o esquema e não cria tabelas automaticamente. A migration V1 usa `CREATE TABLE IF NOT EXISTS` e não apaga dados existentes. Se a base legada tiver tabelas incompatíveis ou registros órfãos, faça um backup e ajuste a migration para o esquema local antes de executar.

Abra a home pública em `http://localhost:8080/` e a documentação interativa em `http://localhost:8080/swagger-ui.html`.

## API principal

| Método | Rota | Acesso | Resultado |
| --- | --- | --- | --- |
| `GET` | `/api/produtos` | Público | Lista o catálogo |
| `POST` | `/api/auth/cadastro` | Público | Cria conta e devolve JWT |
| `POST` | `/api/auth/login` | Público | Autentica e devolve JWT |
| `GET` | `/api/avaliacoes` | Público | Lista avaliações |
| `POST` | `/api/avaliacoes` | JWT | Publica avaliação como o usuário do token |
| `PUT` | `/api/avaliacoes/{id}` | JWT, autor | Edita a própria avaliação |
| `DELETE` | `/api/avaliacoes/{id}` | JWT, autor | Exclui a própria avaliação (`204`) |
| `GET` | `/api/favoritos` | JWT | Lista favoritos da conta |
| `PUT` | `/api/favoritos/{produtoId}` | JWT | Favorita um piso |
| `DELETE` | `/api/favoritos/{produtoId}` | JWT | Remove dos favoritos |

Depois de autenticar no Swagger, use o botão **Authorize** e informe `Bearer <token>`.

## Decisões de segurança

- A home, o catálogo, a listagem de avaliações, o cadastro/login e o Swagger são públicos.
- Favoritos e operações de escrita em avaliações exigem JWT. O ID do autor vem do token e nunca do corpo da requisição.
- Senhas novas são armazenadas com BCrypt. Tokens duram 120 minutos por padrão.
- Defina `JWT_SECRET` com uma chave Base64 de pelo menos 256 bits fora do ambiente local de desenvolvimento.
- As respostas de validação usam `400`, recursos ausentes `404`, conflitos `409`, falta de autenticação `401` e falta de autorização `403`.

## Relação Many-to-Many

`Usuario.produtosFavoritos` e `Produto.usuariosQueFavoritaram` representam muitos usuários favoritos para muitos produtos. A tabela de junção `favorito` guarda os dois IDs e tem índice único para impedir favoritos duplicados.

## Migrations

As migrations Flyway são versionadas em `src/main/resources/db/migration`. Não edite uma migration que já foi aplicada em uma base compartilhada; crie uma nova versão (`V2__...sql`) para alterações futuras.
