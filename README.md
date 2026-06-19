# trabalho_final

## Arquitetura

- `ms-user`: serviço Spring Boot com JWT, MySQL, cadastro de perfil e endpoints protegidos.
- `ms-email`: consumidor RabbitMQ que persiste e tenta enviar o e-mail real por SMTP.
- `frontend`: aplicação Node.js com fluxo de código, cadastro de nome/cargo e dashboard.

## Pré-requisitos

- Java 17
- Maven
- Node.js 22 ou compatível
- MySQL local
- RabbitMQ/CloudAMQP configurado no `application.properties`

## Como executar

```powershell
cd C:\Users\jeanc\Desktop\Trabalho_Final\trabalho_final\ms-user
..\.tools\apache-maven-3.9.9\bin\mvn.cmd clean package -DskipTests
java -jar target\ms-user-0.0.1-SNAPSHOT.jar
```

```powershell
cd C:\Users\jeanc\Desktop\Trabalho_Final\trabalho_final\ms-email
..\.tools\apache-maven-3.9.9\bin\mvn.cmd clean package -DskipTests
java -jar target\ms-email-0.0.1-SNAPSHOT.jar
```

```powershell
cd C:\Users\jeanc\Desktop\Trabalho_Final\trabalho_final\frontend
npm install
npm start
```

## Fluxo da entrega

1. Acesse [http://localhost:3000](http://localhost:3000).
2. Solicite o código e valide em `GET /verify`.
3. Faça o cadastro em `GET /register`.
4. Teste `GET /api/protected` e `GET /users/me` no dashboard.

## Entregáveis

- `entregavel/dashboard-token.png`
- `entregavel/README.md`
- `iniciar.ps1`