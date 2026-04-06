# language: pt
Funcionalidade: Processamento de pedidos

  Cenário: Deve processar pedido recebido da fila
    Dado que existe um pedido criado
    Quando a mensagem é enviada para a fila
    Então o pedido deve ser salvo no banco

  Cenário: Deve retornar valor total do pedido com sucesso
    Dado que existe um pedido com id 1001
    Quando consultar o total do pedido 1001
    Então o status deve ser 200
    E o valor total deve ser "120.00"

  Cenário: Deve retornar 404 quando pedido não existe
    Quando consultar o total do pedido 999
    Então o status deve ser 404

  Cenário: Deve retornar quantidade de pedidos por cliente
    Dado que existe pedido para cliente 1
    Quando consultar quantidade de pedidos do cliente 1
    Então o status deve ser 200
    E a quantidade de pedidos deve ser 1

  Cenário: Deve retornar 404 quando cliente não tem pedidos
    Quando consultar quantidade de pedidos do cliente 999
    Então o status deve ser 404

  Cenário: Deve listar pedidos do cliente
    Dado que existe pedido para cliente 1
    Quando consultar pedidos do cliente 1 pagina 0 tamanho 10
    Então o status deve ser 200
    E deve retornar lista com 1 pedidos

  Cenário: Deve retornar lista vazia
    Quando consultar pedidos do cliente 999 pagina 0 tamanho 10
    Então o status deve ser 200
    E deve retornar lista vazia

  Cenário: Deve retornar resumo de pedidos por cliente
    Dado que existe pedido para cliente 1
    Quando eu chamar o endpoint de resumo de pedidos
    Então o status deve ser 200
    E deve retornar a lista de resumo de pedidos

# validar DLQ
  Cenário: Deve gerar um erro enviar para fila DLQ
    Dado que existe um pedido sem itens
    Quando a mensagem é enviada para a fila
    Então o pedido não deve ser salvo no banco
    E o pedido deve ser enviado para a fila DLQ