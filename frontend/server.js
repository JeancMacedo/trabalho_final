const express = require('express');
const path = require('path');

const app = express();
const port = process.env.PORT || 3000;
const userServiceBaseUrl = process.env.USER_SERVICE_URL || 'http://localhost:8081';

app.use(express.urlencoded({ extended: true }));
app.use(express.json());
app.use(express.static(path.join(__dirname, 'public')));

app.get('/', (req, res) => {
  res.sendFile(path.join(__dirname, 'public', 'index.html'));
});

app.get('/verify', (req, res) => {
  res.sendFile(path.join(__dirname, 'public', 'verify.html'));
});

app.get('/register', (req, res) => {
  res.sendFile(path.join(__dirname, 'public', 'register.html'));
});

app.get('/dashboard', (req, res) => {
  res.sendFile(path.join(__dirname, 'public', 'dashboard.html'));
});

function forwardAuth(req) {
  const authorization = req.get('authorization');
  return authorization ? { Authorization: authorization } : {};
}

async function proxyText(req, res, targetPath, method = 'GET', body = undefined) {
  try {
    const response = await fetch(`${userServiceBaseUrl}${targetPath}`, {
      method,
      headers: {
        'Content-Type': 'application/json',
        ...forwardAuth(req)
      },
      body: body === undefined ? undefined : JSON.stringify(body)
    });

    const text = await response.text();
    if (!response.ok) {
      return res.status(response.status).send(text || 'Falha ao processar requisição');
    }

    return res.type(response.headers.get('content-type') || 'text/plain').send(text);
  } catch (error) {
    return res.status(500).send('Erro ao conectar no User Service');
  }
}

async function proxyJson(req, res, targetPath, method = 'GET', body = undefined) {
  try {
    const response = await fetch(`${userServiceBaseUrl}${targetPath}`, {
      method,
      headers: {
        'Content-Type': 'application/json',
        ...forwardAuth(req)
      },
      body: body === undefined ? undefined : JSON.stringify(body)
    });

    const payloadText = await response.text();
    let payload;
    try {
      payload = payloadText ? JSON.parse(payloadText) : {};
    } catch {
      payload = { message: payloadText };
    }

    if (!response.ok) {
      return res.status(response.status).json({
        message: payload.message || payloadText || 'Falha ao processar requisição'
      });
    }

    return res.json(payload);
  } catch (error) {
    return res.status(500).json({ message: 'Erro ao conectar no User Service' });
  }
}

app.post('/send-code', async (req, res) => {
  const email = String(req.body.email || '').trim().toLowerCase();

  if (!email) {
    return res.status(400).send('Email e obrigatorio');
  }

  try {
    const response = await fetch(`${userServiceBaseUrl}/auth/request-code`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ email })
    });

    if (!response.ok) {
      const message = await response.text();
      return res.status(response.status).send(message || 'Falha ao solicitar codigo');
    }

    return res.redirect(`/verify?email=${encodeURIComponent(email)}`);
  } catch (error) {
    return res.status(500).send('Erro ao conectar no User Service');
  }
});

app.post('/verify-code', async (req, res) => {
  const email = String(req.body.email || '').trim().toLowerCase();
  const code = String(req.body.code || '').trim();

  if (!email || !code) {
    return res.status(400).json({ message: 'Email e codigo sao obrigatorios' });
  }

  try {
    const response = await fetch(`${userServiceBaseUrl}/auth/verify-code`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ email, code })
    });

    const payloadText = await response.text();
    let payload;
    try {
      payload = payloadText ? JSON.parse(payloadText) : {};
    } catch {
      payload = { message: payloadText };
    }

    if (!response.ok) {
      return res.status(response.status).json({
        message: payload.message || payloadText || 'Codigo invalido ou expirado'
      });
    }

    return res.json(payload);
  } catch (error) {
    return res.status(500).json({ message: 'Erro ao conectar no User Service' });
  }
});

app.post('/register', async (req, res) => {
  const name = String(req.body.name || '').trim();
  const role = String(req.body.role || 'ROLE_CUSTOMER').trim();

  if (!name) {
    return res.status(400).json({ message: 'Nome é obrigatório' });
  }

  return proxyJson(req, res, '/users/update-profile', 'POST', { name, role });
});

app.get('/api/protected', async (req, res) => {
  return proxyText(req, res, '/users/test/customer', 'GET');
});

app.get('/users/me', async (req, res) => {
  return proxyJson(req, res, '/users/me', 'GET');
});

app.listen(port, () => {
  console.log(`Frontend rodando em http://localhost:${port}`);
});
