# Controle de Gastos - API REST

Aplicação básica para controle de gastos desenvolvida com Spring Boot.

## Tecnologias Utilizadas

- Java 21
- Spring Boot 4.0.2
- Spring Data JPA
- H2 Database (em memória)
- Lombok
- Maven

## Estrutura do Projeto

- **Entity**: `Gasto` - Entidade com campos `tipo` e `valor`
- **Repository**: `GastoRepository` - Interface JPA para acesso aos dados
- **Service**: `GastoService` - Lógica de negócio
- **Controller**: `GastoController` - Endpoints REST

## Endpoints da API

### Listar todos os gastos
```
GET /api/gastos
```

### Buscar gasto por ID
```
GET /api/gastos/{id}
```

### Buscar gastos por tipo
```
GET /api/gastos/tipo/{tipo}
```

### Criar novo gasto
```
POST /api/gastos
Content-Type: application/json

{
  "tipo": "Alimentação",
  "valor": 50.00
}
```

### Atualizar gasto
```
PUT /api/gastos/{id}
Content-Type: application/json

{
  "tipo": "Transporte",
  "valor": 100.00
}
```

### Deletar gasto
```
DELETE /api/gastos/{id}
```

### Calcular total de todos os gastos
```
GET /api/gastos/total
```

### Calcular total por tipo (categoria)
```
GET /api/gastos/total/tipo/{tipo}
Exemplo: GET /api/gastos/total/tipo/MERCADO
```

## Como Executar

1. Execute o projeto:
```bash
./mvnw spring-boot:run
```

2. A aplicação estará disponível em: `http://localhost:8080`

3. Console H2 disponível em: `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:gastosdb`
   - Username: `sa`
   - Password: (deixar em branco)

## Exemplos de Uso

### Listar todas as categorias
```bash
curl http://localhost:8080/api/categorias
```

### Criar um gasto
```bash
curl -X POST http://localhost:8080/api/gastos \
  -H "Content-Type: application/json" \
  -d '{"tipo":"ALIMENTACAO","valor":50.00}'
```

### Listar todos os gastos
```bash
curl http://localhost:8080/api/gastos
```

### Buscar gastos por categoria
```bash
curl http://localhost:8080/api/gastos/tipo/MERCADO
```

### Ver total de gastos
```bash
curl http://localhost:8080/api/gastos/total
```

### Ver total por categoria
```bash
curl http://localhost:8080/api/gastos/total/tipo/SAUDE
```

