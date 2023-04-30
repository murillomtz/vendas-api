# VENDAS API


### Documentação de Referência
Para referência adicional, considere as seguintes seções:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.6/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.6/maven-plugin/reference/html/#build-image)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.0.6/reference/htmlsingle/#using.devtools)


## Guia De Inicialização
[Ver o guia de Inicialização do Projeto](./doc/manualIniciarProjeto.md)


## Objetivo

> Projeto Vendas API
>
> - O projeto tem o objetivo de gerenciar um sistema de vendas, o mesmo pode CRIAR ,EDITAR, EXCLUIR e BUSCAR não somente Pedidos mais todos os elementos envolvidos.
>
> - Podemos criar , deletar e fazer buscas personalisadas para as entidades Pedido, Produto, Cliente e Usuário (Atendente, Gerente)
>


## Tecnologia

- A aplicação foi desenvolvida com Java 11. 
- Foi utilizado o Framework Spring Boot e Hibernate.
- Armazenamento - Utilizando MySql.

## Informações Importantes:
### **Banco de Dados MySQL

>
 Certifique-se de ter instalado o MySQL, e após  a inicialização do sistema ele criará de forma automatizada o banco de dados para esse projeto, devido ao comando:
>
 "/mtzvendas?createDatabaseIfNotExist=true"
>
  Introduzido no arquivo "application.properties".
>

### **Lombok

>
 Certifique-se de ter instalado o Lombok.
>
 O lombok é responsável pela criação de codigos como os Getters e Setters.	
>
 Para instalação siga essa documentação para instalação do Lombok na IDE Eclipse e Spring Tool Suite.
>
 * [Official Project Lombok documentation](https://projectlombok.org/setup/eclipse)
>

## Esquema de banco de dados

[Ver esquema do B.D](./doc/img/VENDAS-API-DB.png)

## Exemplo das funcionalidades da entidade pedidos:
[Ver exemplos de algumas funcionalidades](./doc/pedidoUrls.md)


## Melhorias futuras:

- MELHORAR PADRONIZAÇÃO DE EXCEÇÕES
- APLICAR MELHORIAS PARA ATINGIR 3 NIVEL DO MODELO DE MATURIDADE RICHARDSON 
- CRIAÇÃO DO FRONT-END.

