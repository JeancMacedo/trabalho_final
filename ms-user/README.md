# ms-user

Aplicação Spring Boot para User Service com JWT e MySQL.

Passos para rodar:

1. Criar banco no MySQL:

```sql
CREATE DATABASE ms_user;
```

2. Ajustar `src/main/resources/application.properties` com usuário e senha do MySQL.
3. Rodar:

```bash
mvn -f ms-user clean package
java -jar ms-user/target/ms-user-0.0.1-SNAPSHOT.jar
```

Endpoints:
- `POST /users` criar usuário: {"username":"u","password":"p","role":"ROLE_CUSTOMER"}
- `POST /users/login` autenticar: {"username":"u","password":"p"}
- `GET /users/test/customer` endpoint protegido (necessário Bearer token)
