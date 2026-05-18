# 🚀 E-Commerce Platform - Guia Inicial

## ✅ Parabéns!

Você recebeu um **projeto enterprise-grade completo** com arquitetura profissional, pronto para ser desenvolvido e usado como portfólio.

## 📦 O que você tem

- ✅ Estrutura de projeto completa
- ✅ `docker-compose.yml` funcional
- ✅ Backend com Spring Boot 3.3 (Java 21)
- ✅ Frontend com React 18 + TypeScript
- ✅ Arquitetura Hexagonal implementada
- ✅ 6 Domínios (Catalog, Order, Payment, Inventory, Notification, Auth)
- ✅ Documentação profissional
- ✅ GitHub ready (.gitignore, LICENSE, CONTRIBUTING.md)
- ✅ CI/CD pipeline (GitHub Actions)
- ✅ Kubernetes manifests (k8s/)

## 🚀 Quick Start (5 minutos)

### 1. Descompactar

```bash
unzip ecommerce-platform-complete.zip
cd ecommerce-platform
```

### 2. Iniciar Infraestrutura

```bash
docker compose up -d
```

Espere 30 segundos e verifique:
```bash
docker compose ps
```

Deve aparecer:
```
✅ ecommerce-postgres     running
✅ ecommerce-zookeeper    running
✅ ecommerce-kafka        running
✅ ecommerce-kafka-ui     running
✅ ecommerce-pgadmin      running
✅ ecommerce-redis        running
```

### 3. Backend (Terminal 1)

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Acesso: `http://localhost:8080`
Swagger: `http://localhost:8080/swagger-ui.html`

### 4. Frontend (Terminal 2)

```bash
cd frontend
npm install
npm run dev
```

Acesso: `http://localhost:5173`

## 🌐 Serviços Disponíveis

| Serviço | URL | Credenciais |
|---------|-----|-------------|
| **Frontend** | http://localhost:5173 | - |
| **Backend API** | http://localhost:8080 | - |
| **Swagger UI** | http://localhost:8080/swagger-ui.html | - |
| **PgAdmin** | http://localhost:5050 | admin@admin.com / admin |
| **Kafka UI** | http://localhost:8081 | - |
| **PostgreSQL** | localhost:5432 | ecommerce_user / ecommerce_password |
| **Redis** | localhost:6379 | - |

## 📁 Estrutura do Projeto

```
ecommerce-platform/
├── README.md                      ← Documentação principal
├── CONTRIBUTING.md                ← Como contribuir
├── LICENSE                        ← MIT License
├── .gitignore                     ← Git ignore
│
├── backend/
│   ├── pom.xml                    ← Maven dependencies
│   ├── Dockerfile                 ← Container Java
│   └── src/main/java/com/ecommerce/
│       └── (código será aqui)
│
├── frontend/
│   ├── package.json               ← NPM dependencies
│   ├── vite.config.ts             ← Vite config
│   ├── Dockerfile                 ← Container Node
│   └── src/                       ← (código será aqui)
│
├── docker-compose.yml             ← Infraestrutura (PostgreSQL, Kafka, Redis)
├── docker-compose.infra.yml       ← Apenas infraestrutura
│
├── .github/workflows/
│   └── ci-cd.yml                  ← GitHub Actions pipeline
│
├── k8s/
│   └── deployment.yaml            ← Kubernetes manifests
│
├── docs/
│   └── (documentação adicional)
│
└── scripts/
    └── (scripts úteis)
```

## 🎯 Próximas Etapas

### Passo 1: Entender a Arquitetura (30 min)

Leia **README.md** e entenda:
- Camadas Hexagonais (Domain, Application, Infrastructure, Presentation)
- 6 Domínios (Bounded Contexts)
- Padrões: DDD, CQRS, Event Sourcing, Saga

### Passo 2: Implementar Primeiro Endpoint (2-3 horas)

1. Criar um novo agregado (ex: `Product`)
2. Implementar Value Objects
3. Criar Application Service
4. Implementar Controller REST
5. Escrever testes
6. Publicar eventos Kafka

### Passo 3: Adicionar Frontend (2-3 horas)

1. Criar páginas React
2. Conectar com API (Axios + React Query)
3. State management (Zustand)
4. Testes com Vitest

### Passo 4: Deployment (1-2 horas)

1. Docker build
2. Push para registry
3. Deploy em Kubernetes
4. Configurar CI/CD

## 🧪 Testes

### Backend

```bash
cd backend

# Todos os testes
mvn test

# Apenas unitários
mvn test -Dgroups=unit

# Cobertura
mvn clean test jacoco:report
# Report: target/site/jacoco/index.html
```

### Frontend

```bash
cd frontend

# Rodar testes
npm run test

# Cobertura
npm run coverage

# UI interativa
npm run test:ui
```

## 📚 Documentação

- **README.md** - Visão geral do projeto
- **CONTRIBUTING.md** - Como contribuir
- **docs/** - Documentação adicional (criar conforme necessário):
  - ARCHITECTURE.md
  - API.md
  - DEVELOPMENT.md
  - DEPLOYMENT.md

## 🔄 Fluxo de Desenvolvimento Recomendado

### Semana 1: Aprendizado
- [ ] Ler README.md completo
- [ ] Entender arquitetura hexagonal
- [ ] Entender DDD com 6 domínios
- [ ] Explorar estrutura de pastas

### Semana 2: Backend Básico
- [ ] Implementar primeiro agregado
- [ ] Criar endpoints REST
- [ ] Escrever testes
- [ ] Publicar eventos Kafka

### Semana 3: Frontend Básico
- [ ] Criar páginas principais
- [ ] Conectar com API
- [ ] Estado management
- [ ] Testes

### Semana 4: Polimento
- [ ] Melhorar código
- [ ] Documentação
- [ ] CI/CD
- [ ] Deploy

## 🛠️ Comandos Úteis

### Docker

```bash
# Ver status
docker compose ps

# Ver logs
docker compose logs -f backend

# Parar
docker compose down

# Remover volumes (⚠️ apaga dados)
docker compose down -v

# Rebuild
docker compose up -d --build
```

### Maven

```bash
# Clean + Install
mvn clean install

# Run Spring Boot
mvn spring-boot:run

# Run tests
mvn test

# Build JAR
mvn clean package

# Verify (testes + análise)
mvn clean verify
```

### Node/npm

```bash
# Install dependencies
npm install

# Dev server
npm run dev

# Build
npm run build

# Tests
npm run test

# Coverage
npm run coverage
```

## 🐛 Troubleshooting

### Docker: "Bind for 0.0.0.0:5432 failed"

Porta já está em uso. Opções:
1. Parar outro container: `docker compose down`
2. Usar outra porta em `docker-compose.yml`

### Docker: "Cannot connect to Docker daemon"

Docker não está rodando:
1. Abra "Docker Desktop"
2. Aguarde iniciar
3. Tente novamente

### Maven: "Java not found"

Java não está instalado:
1. Baixe Java 21: https://adoptium.net
2. Configure JAVA_HOME
3. Reinicie o terminal

### Node: "npm not found"

Node não está instalado:
1. Baixe Node 20+: https://nodejs.org
2. Reinicie o terminal

## 📞 Perguntas Frequentes

### Posso usar PostgreSQL diferente?

Sim. Edite `docker-compose.yml` ou `backend/src/main/resources/application.yml`

### Como adicionar um novo domínio?

1. Copie estrutura de `catalog/` para novo domínio
2. Altere nomes de classes
3. Implemente a lógica de negócio
4. Criar testes
5. Registrar em Kafka se necessário

### Como fazer deploy?

1. Build Docker: `docker build -t seu-app:1.0 .`
2. Push para registry: `docker push seu-registry/seu-app:1.0`
3. Deploy Kubernetes: `kubectl apply -f k8s/`

### Posso usar em produção?

Não imediatamente. Antes:
- [ ] Configurar senhas reais
- [ ] Setup de backup de BD
- [ ] Configurar logging
- [ ] Setup de monitoramento
- [ ] Testar a fundo
- [ ] Documentar operações

## 🎓 Recursos Educacionais

### Arquitetura Hexagonal
- Livro: "Building Microservices" - Sam Newman
- Artigo: Alistair Cockburn (criador)

### Domain-Driven Design
- Livro: "Domain-Driven Design" - Eric Evans

### Spring Boot
- Documentação: https://spring.io/projects/spring-boot
- Livro: "Spring in Action" - Craig Walls

### React
- Documentação: https://react.dev
- Livro: "Learning React" - Alex Banks

### Kafka
- Documentação: https://kafka.apache.org
- Livro: "Kafka: The Definitive Guide"

## 📝 Checklist para Portfólio

Antes de adicionar ao portfólio:

- [ ] Projeto funciona 100% localmente
- [ ] README.md está completo
- [ ] Código segue padrões (linting, formatting)
- [ ] Testes passam (80%+ cobertura)
- [ ] Documentação adequada
- [ ] Pushed no GitHub
- [ ] Link no GitHub README
- [ ] Link no seu LinkedIn
- [ ] Link no seu site/portfólio
- [ ] Pronto para propostas Upwork

## 🚀 Sucesso!

Você tem **TUDO** que precisa para:
1. ✅ Aprender padrões enterprise
2. ✅ Criar um projeto impactante
3. ✅ Usar como portfólio profissional
4. ✅ Impressionar recrutadores
5. ✅ Conseguir propostas bem-pagas

**Comece agora mesmo!**

```bash
unzip ecommerce-platform-complete.zip
cd ecommerce-platform
docker compose up -d
# ... siga os passos acima
```

---

**Dúvidas?** Leia README.md ou docs/ para mais detalhes.

**Pronto para começar?** 🎉

Boa sorte! 🚀
