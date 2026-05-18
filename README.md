# E-Commerce Platform

Fullstack e-commerce com **arquitetura hexagonal**, **Domain-Driven Design**, **Kafka** e integração entre domínios.

## Stack

- **Backend:** Java 21, Spring Boot 3.3, PostgreSQL 16, Kafka, Flyway, JWT
- **Frontend:** React 18, TypeScript, Vite, React Router
- **Infra:** Docker Compose (tudo orquestrado)

## Como rodar

**Requisito único:** Docker Desktop instalado e rodando.

```bash
docker compose up --build
```

Na primeira execução demora alguns minutos (Maven baixa dependências, npm instala). Depois fica rápido.

Quando aparecer `Started Application in X seconds` nos logs, está pronto:

- **Frontend:** http://localhost:5173
- **Backend (Swagger):** http://localhost:8080/swagger-ui.html
- **Kafka UI:** http://localhost:8081

### Usuário de teste

Já vem com seed no banco:
- **Email:** admin@ecomm.com
- **Senha:** admin123

E 5 produtos cadastrados (notebook, teclado, mouse, monitor, headphone).

## Para parar

```bash
docker compose down
```

Para apagar tudo (banco zerado):

```bash
docker compose down -v
```

## Arquitetura

3 bounded contexts seguindo arquitetura hexagonal:

```
src/main/java/com/ecomm/
├── auth/              # Domain: autenticação JWT
│   ├── domain/        # User, UserRepository (port)
│   ├── application/   # RegisterUserUseCase, LoginUseCase
│   ├── infrastructure/# UserRepositoryAdapter (JPA)
│   └── presentation/  # AuthController
│
├── catalog/           # Domain: produtos
│   ├── domain/        # Product (aggregate), ProductRepository
│   ├── application/   # use cases + DecrementStockOnOrderCreatedHandler (Kafka)
│   ├── infrastructure/# JPA adapter
│   └── presentation/  # ProductController
│
├── order/             # Domain: pedidos
│   ├── domain/        # Order (aggregate), OrderItem (VO), OrderEventPublisher (port)
│   ├── application/   # CreateOrderUseCase, etc
│   ├── infrastructure/# JPA + KafkaOrderEventPublisher
│   └── presentation/  # OrderController
│
└── shared/            # Cross-cutting (security, kafka config)
```

### Fluxo Kafka real

Quando um pedido é criado, o `Order` domain publica `order.created` no Kafka. O `Catalog` domain tem um listener (`DecrementStockOnOrderCreatedHandler`) que **consome esse evento e decrementa o estoque**. Isso desacopla os domínios — eles não se chamam diretamente.

Você consegue ver isso ao vivo:

1. Crie um pedido pelo frontend
2. Abra Kafka UI em http://localhost:8081
3. Veja o evento no tópico `order.created`
4. Recarregue a página de produtos — o estoque diminuiu

## Endpoints principais

| Método | Path | Auth | Descrição |
|--------|------|------|-----------|
| POST | `/api/auth/register` | - | Cadastrar usuário |
| POST | `/api/auth/login` | - | Login (retorna JWT) |
| GET | `/api/products` | - | Listar produtos |
| GET | `/api/products/{id}` | - | Detalhes do produto |
| POST | `/api/products` | ADMIN | Criar produto |
| POST | `/api/orders` | user | Criar pedido |
| GET | `/api/orders` | user | Meus pedidos |
| GET | `/api/orders/{id}` | user | Detalhes do pedido |

Swagger completo: http://localhost:8080/swagger-ui.html

## Estrutura do projeto

```
.
├── docker-compose.yml      # Orquestra tudo: postgres, kafka, backend, frontend
├── backend/
│   ├── Dockerfile          # Build multi-stage (Maven dentro do container)
│   ├── pom.xml
│   └── src/
└── frontend/
    ├── Dockerfile          # Build com Node + serve com Nginx
    ├── package.json
    └── src/
```

## Próximos passos para evoluir

Esse projeto cobre o essencial (3 domínios, Kafka entre eles, JWT, frontend conectado). Para evoluir como portfólio:

1. **Adicionar Payment domain** com Saga (Order → Payment → confirmar/cancelar)
2. **Event Sourcing no Order** (tabela `order_events` armazenando cada mudança)
3. **Testes** com Testcontainers (já configurado no `pom.xml`)
4. **Métricas** com Micrometer + Prometheus
5. **Deploy** em Kubernetes (estrutura já preparada)

## Licença

MIT
