# Edis Pisos - POOWEB II

Projeto do catálogo Edis Pisos evoluído com os conteúdos do semestre: API REST, Spring Data JPA, Flyway, Swagger/OpenAPI, validação, respostas HTTP e Spring Security com JWT.

## Executar localmente

Requisitos: Java 17+, PostgreSQL e Maven (o repositório inclui Maven Wrapper).

1. Crie o banco PostgreSQL `pisos` (ou informe outro nome em `DB_URL`).
2. Configure `DB_USER` e `DB_PASSWORD` se as credenciais locais forem diferentes das configurações de desenvolvimento.
3. Na pasta do projeto, inicie com `./mvnw spring-boot:run` (Linux/macOS) ou `mvnw.cmd spring-boot:run` (Windows).

O Flyway aplica as migrações de `src/main/resources/db/migration`; o Hibernate valida o esquema (`ddl-auto=validate`) e não cria tabelas automaticamente. A V1 usa `CREATE TABLE IF NOT EXISTS` e não apaga dados existentes. Se a base legada tiver tabelas incompatíveis ou registros órfãos, faça backup e ajuste o esquema local antes de executar.

Abra a home pública em `http://localhost:8080/` e a documentação interativa em `http://localhost:8080/swagger-ui.html`.

## Testar com Postman

Importe `postman/Edis-Pisos.postman_collection.json` no Postman e execute a coleção na ordem apresentada. O cadastro gera um email exclusivo e salva o JWT; a criação da avaliação salva o ID usado nos passos de edição e exclusão. A variável `produtoId` começa em `1`, correspondente ao primeiro produto inserido pela migration V2. A coleção espera a API rodando em `http://localhost:8080`; altere `baseUrl` nas variáveis da coleção se usar outra porta.

## Executar com Docker

Com Docker Desktop aberto, na pasta do projeto execute:

```bash
docker compose up --build
```

O Compose inicia PostgreSQL 17 e a API. O banco fica no volume `dados-db`, então os dados permanecem entre reinicializações dos containers. A API fica disponível em `http://localhost:8080/` e o Swagger em `http://localhost:8080/swagger-ui.html`.

Para parar os containers, use `Ctrl+C` e depois `docker compose down`. Para apagar também os dados persistidos do banco, use `docker compose down -v`.

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

## Organização do código

- `model`: entidades JPA que representam `Usuario`, `Produto` e `Avaliacao`.
- `repository`: interfaces `JpaRepository` e consultas derivadas dos nomes dos métodos.
- `service`: regras de negócio e operações transacionais.
- `controller`: rotas HTTP, validação de entrada e códigos de resposta.
- `dto`: objetos de entrada e saída da API; as entidades não são devolvidas diretamente nos endpoints.
- `infra`: tratamento global de erros, OpenAPI e configuração de segurança/JWT.

Os services recebem os repositories por construtor. A validação dos DTOs usa Jakarta Validation, como `@NotBlank`, `@Email` e `@Size`; o controller ativa essas regras com `@Valid`.

## CRUD e códigos HTTP

O catálogo de produtos é somente leitura pela API. Avaliações têm criação, listagem, edição e exclusão. A edição e a exclusão procuram a avaliação pelo ID e pelo ID do autor autenticado; isso impede alterar ou apagar a avaliação de outra conta.

Respostas usadas neste projeto:

- `200 OK`: consulta, login ou edição bem-sucedida.
- `201 Created`: cadastro de conta ou criação de avaliação.
- `204 No Content`: inclusão/remoção de favorito ou exclusão de avaliação.
- `400 Bad Request`: dados de entrada inválidos.
- `401 Unauthorized`: token ausente ou inválido, ou credenciais incorretas.
- `404 Not Found`: recurso ausente ou avaliação que não pertence ao usuário autenticado.
- `409 Conflict`: tentativa de cadastrar um email já utilizado.

## Validação e anotações nas entidades

As entidades usam `@Entity`, `@Table`, `@Id` e `@GeneratedValue` para persistência JPA. Neste projeto, getters, setters e construtores estão escritos explicitamente; Lombok não é uma dependência do `pom.xml`.

`@NotBlank` valida texto não vazio; `@NotNull` valida que um valor não seja nulo; `@Min`, `@Max` e `@DecimalMin` validam limites numéricos; `@Size` valida o tamanho de texto ou coleção. `@NonNull` do Lombok não substitui `jakarta.validation.constraints.NotNull` para validar requisições.

## Decisões de segurança

- A home, o catálogo, a listagem de avaliações, o cadastro/login e o Swagger são públicos.
- Favoritos e operações de escrita em avaliações exigem JWT. O ID do autor vem do token e nunca do corpo da requisição.
- Senhas novas são armazenadas com BCrypt. Tokens duram 120 minutos por padrão.
- Defina `JWT_SECRET` com uma chave Base64 de pelo menos 256 bits fora do ambiente local de desenvolvimento.
- As respostas de validação usam `400`, recursos ausentes `404`, conflitos `409`, falta de autenticação `401` e falta de autorização `403`.

## Relação Many-to-Many

`Usuario.produtosFavoritos` e `Produto.usuariosQueFavoritaram` representam muitos usuários favoritos para muitos produtos. A tabela de junção `favorito` guarda os dois IDs e tem índice único para impedir favoritos duplicados.

`Usuario.produtosFavoritos` é o lado que define a tabela com `@JoinTable`; `Produto.usuariosQueFavoritaram` é o lado inverso, indicado por `mappedBy`. As coleções são `LAZY` e o service carrega os dados necessários dentro da transação.

## Migrations

As migrations Flyway são versionadas em `src/main/resources/db/migration`, com o padrão `V<versão>__<descricao>.sql` (V maiúsculo e dois underlines), por exemplo `V3__ajustar_tabela_produto.sql`. V1 cria as tabelas e relacionamentos; V2 insere os produtos iniciais. O Flyway guarda nome, versão e checksum das migrations executadas. Não altere uma migration já aplicada em uma base compartilhada; crie uma nova versão para cada mudança futura.

Mantenha `spring.jpa.hibernate.ddl-auto=validate`: a estrutura do banco é controlada pelo Flyway, enquanto o Hibernate verifica se ela corresponde às entidades.

## Configuração local

As propriedades aceitam variáveis de ambiente para facilitar a configuração sem editar o arquivo:

| Variável | Padrão local | Uso |
| --- | --- | --- |
| `SERVER_PORT` | `8080` | Porta da aplicação |
| `DB_URL` | `jdbc:postgresql://localhost:5432/pisos` | URL JDBC do PostgreSQL |
| `DB_USER` | `postgres` | Usuário do banco |
| `DB_PASSWORD` | `1234` | Senha do banco |
| `JWT_SECRET` | chave de desenvolvimento | Chave Base64 do JWT; use uma chave própria com pelo menos 256 bits fora do ambiente local |
| `JWT_DURATION_MINUTES` | `120` | Validade do JWT em minutos |

