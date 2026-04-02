# Exemplo de desafio Engenheiro de software

## Escopo
Processar pedidos e gerar relatório.

## Atividades
1. Elabore e entregue um plano de trabalho.
    - Crie suas atividades em tasks
    - Estime horas
2. Crie uma aplicação, na tecnologia de sua preferência (JAVA, DOTNET, NODEJS)
3. Modele e implemente uma base de dados (PostgreSQL, MySQL, MongoDB).
4. Crie um micro serviço que consuma dados de uma fila RabbitMQ e grave os dados para conseguir listar as informações:
    - Valor total do pedido
    - Quantidade de Pedidos por Cliente
    - Lista de pedidos realizados por cliente

Exemplo da mensagem que deve ser consumida:

```
   {
       "codigoPedido": 1001,
       "codigoCliente":1,
       "itens": [
           {
               "produto": "lápis",
               "quantidade": 100,
               "preco": 1.10
           },
           {
               "produto": "caderno",
               "quantidade": 10,
               "preco": 1.00
           }
       ]
   }
```


5. Crie uma API REST, em que permita o consultar as seguintes informações:
    - Valor total do pedido
    - Quantidade de Pedidos por Cliente
    - Lista de pedidos realizados por cliente

---
# Resolução
Projeto: java-processar-pedidos

## Plano de trabalho
Elabore e entregue um plano de trabalho.
  - [x] Criar aplicação Java (JDK 21, Spring 3.5.13 e Maven) [Spring Initializr](https://start.spring.io/)
  > Dependências: Spring Web, Spring Data MongoDB e Spring for RabbitMQ.
  - [x] Criar repositório publico no GitHub [java-processar-pedidos](https://github.com/stefano2007/java-processar-pedidos)
  - [x] Configurar o RabbitMQ e MongoDB no Docker.
  - [x] Configurar a Comunicação do Spring Boot com o RabbitMQ
  - [x] Configurar a Comunicação do Spring Boot com o MongoDB
  - [x] Criar a funcionalidade de cadastro de Pedido no MongoDB
  - [x] Criar endpoint de listagem de pedidos do cliente
  - [x] Criar serviço de listagem de pedidos do cliente
  - [x] Criar serviço que calcula o valor total de todos os pedidos do cliente
  - [x] Testar aplicação completa
  - [x] Criar testes unitários e integrados
  - [x] Criar testes integrados (Cucumber + Rest assured + Testcontainers)
  - [ ] **Plus** Lista todos clientes agrupados por quantidade e valor total dos pedidos (usar agregação do MongoDB)

## Tecnologias utilizadas
- Java 21
- Spring Boot 3.5.13
- Spring Data MongoDB
- Spring for RabbitMQ
- MongoDB
- RabbitMQ
- Docker
- Maven

# Endpoints

- Valor total do pedido
```
curl --request GET \
--url http://localhost:9090/api/pedidos/1001/total
```
Status: 200 OK
```
{
	"pedidoId": 1001,
	"valorTotal": 120.00
}
```
Status: 404 Not Found
```
```
- Quantidade de Pedidos por Cliente
Status: 200 OK
```
curl --request GET \
  --url http://localhost:9090/api/clientes/1/pedidos/quantidade
```
```
{
	"clienteId": 1,
	"quantidadePedidos": 1
}
```
Status: 404 Not Found
```
```
- Lista de pedidos realizados por cliente
```
curl --request GET \
--url 'http://localhost:9090/api/clientes/1/pedidos?page=0&pageSize=10'
```
Status: 200 OK
```
{
  "content": [
    {
        "pedidoId": 1001,
        "valorTotal": 120.00,
        "itens": [
            {
                "produto": "lápis",
                "quantidade": 100,
                "preco": 1.10
            },
            {
                "produto": "caderno",
                "quantidade": 10,
                "preco": 1.00
            }
        ]
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1
}
```
Status: 200 OK (sem pedidos)
```
{
  "content": [],
  "page": 0,
  "size": 10,
  "totalElements": 0,
  "totalPages": 0
}
```
## Iniciar Projeto
1. Clone o repositório: `git clone
2. Navegue até o diretório do projeto: `cd java-processar-pedidos`
3. Configure o MongoDB e RabbitMQ no Docker: 
```bash
cd local
docker compose up -d
```
4. RabbitMQ: `http://localhost:15672/` (usuário: guest, senha: guest)
 - No RabbitMQ deve ser configurado com uma fila chamada `pedido-criado`.

5. MongoDB: `mongodb://localhost:27017/` (banco de dados: pedidosdb)
 - No MongoDB deve ser configurado com uma coleção chamada `tb_pedidos`.