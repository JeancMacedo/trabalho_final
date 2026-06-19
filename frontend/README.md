# Frontend da Etapa 3

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
- `GET /dashboard` mostra o JWT salvo na `sessionStorage`.

## Observações

- Configure `USER_SERVICE_URL` se o backend estiver em outra porta.
- Para envio real de e-mail, ajuste `GMAIL_EMAIL` e `GMAIL_APP_PASSWORD` no `ms-email`.
