## Iniciando...

- `git clone https://github.com/.../exemplo`
- `cd exemplo`

Agora você poderá executar os vários comandos abaixo.

## Pré-requisitos

- `mvn --version`<br>
  você deverá ver a indicação da versão do Maven instalada e
  a versão do JDK, dentre outras. Observe que o JDK é obrigatório, assim como
  a definição das variáveis de ambiente **JAVA_HOME** e **M2_HOME**.

## Limpar, compilar, executar testes de unidade e cobertura

- `mvn clean`<br>
  remove diretório _target_

- `mvn compile`<br>
  compila o projeto, deposita resultados no diretório _target_

- `mvn test`<br>
  executa todos os testes do projeto. Para executar apenas parte dos testes, por exemplo,
  aqueles contidos em uma dada classe execute `mvn -Dtest=NomeDaClasseTest test`. Observe
  que o sufixo do nome da classe de teste é `Test` (padrão recomendado). Para executar um
  único teste `mvn -Dtest=NomeDaClasseTest#nomeDoMetodo test`.


## Executando a aplicação e a RESTFul API

- `mvn exec:java -Dexec.mainClass="nome.completo.Classe" -Dexec.args="arg1 arg2"`<br>
  executa o método _main_ da classe indicada na configuração do _plugin_ pertinente
  no arquivo pom.xml. Depende de `mvn compile`.


- `java -jar target/api.jar` ou ainda `mvn spring-boot:run`<br>
  coloca em execução a API gerada por `mvn package -P api` na porta padrão (8080). Para fazer uso de porta