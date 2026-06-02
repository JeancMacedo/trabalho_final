# ms-email

Esqueleto do Email Service (sem consumer ainda).

Passos para rodar:

1. Criar banco no MySQL:

```sql
CREATE DATABASE ms_email;
```

2. Ajustar `src/main/resources/application.properties` com usuário e senha do MySQL.
3. Rodar:

```bash
mvn -f ms-email clean package
java -jar ms-email/target/ms-email-0.0.1-SNAPSHOT.jar
```
