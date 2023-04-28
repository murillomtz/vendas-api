### Guia para ultilização


Listagem de todos os Pedidos:

    localhost:8080/pedidos
    
Listagem de todos os Pedidos onde houver determinado produto:    

    localhost:8080/pedidos/produtos/1
    
Buscar pedido pelo ID:

    localhost:8080/pedidos/1
    
Adicionar Produtos ao pedido:
    
    localhost:8080/pedidos/10/produtos
    
    Body:
    [
      {
        "id": 1
      },
      {
        "id": 2
      }
    ]
    
Deletar Pedido pelo ID:

    localhost:8080/pedidos/1
    
Criar um novo Pedido:

    localhost:8080/pedidos
    
    Body:
       {
         "idCliente": 1,
         "idAtendente": 4,
         "dataPedido": "2023-04-23T10:00:00Z",
         "valorTotal": 50.00,
         "produtos": [
             {
                "id": 3
             },
             {
                "id": 1
             }
          ]
       }
    
 Editar um pedido:
 
      localhost:8080/pedidos/10
      
      Body:
      
      {
         "idCliente": 2,
         "dataPedido": "2023-04-23T10:00:00Z",
         "valorTotal": 50.00,
         "idAtendente": 1,
         "produtos": [
             {
                "id": 3
             }
          ]
      }
      
Ordenação pelo "ValorTotal" e Paginação:

     localhost:8080/pedidos?ordenacao=valorTotal&pagina=1