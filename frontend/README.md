# Frontend da Etapa 4

## Rodar

```powershell
npm install
npm start
```

## Fluxo

- `GET /` abre o formulário de solicitação de código.
- `POST /send-code` chama `http://localhost:8081/auth/request-code`.
- `GET /verify` abre a tela de validação.
- `POST /verify-code` chama `http://localhost:8081/auth/verify-code`.
- `GET /register` abre o cadastro de nome e cargo.
- `POST /register` chama `http://localhost:8081/users/update-profile`.
- `GET /api/protected` faz proxy para `http://localhost:8081/users/test/customer`.
- `GET /users/me` faz proxy para `http://localhost:8081/users/me`.
- `GET /dashboard` mostra o token e o perfil salvo na `sessionStorage`.

## Observações

- Configure `USER_SERVICE_URL` se o backend estiver em outra porta.
- O JWT precisa estar salvo na `sessionStorage` antes de abrir `register` ou `dashboard`.
