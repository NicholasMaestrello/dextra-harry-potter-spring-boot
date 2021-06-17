
# Projeto de APIs do harry potter

A intenção é fazer um CRUD em uma base de dados local de pessoas do harry potter atraves de uma API REST.  
Para cada pessoa for cadastrada na base dados é validado se ela esta se utilizando de uma "house" existente, e para isso validamos com o uso da seguinte api publica  [potterapi](http://us-central1-rh-challenges.cloudfunctions.net/potterApi/houses).

A plataforma utilizada para o projeto foi o  [Java 11](https://jdk.java.net/) na versão 14.17 LTS.  
Como gerenciador de dependencias foi utilizado o [Maven](https://maven.apache.org/download.cgi) na versão 3.6.0  
O banco de dados utilizado é um mongodb, e pode ser instalado atraves do site: [mongodb](https://www.mongodb.com/try/download/community).

## Preparando o ambiente
Para preparar o abiente, baixe o fonte e então execute dentro da pasta do projeto o seguinte comando `mvn clean package`.    
Com isso o código fonte vai ser compilado e a aplicação estara pronta para ser executada.  
Caso voce queira mudar URL do banco de dados modifique o arquivo de propriedades `src/main/resources/application.properties` e troque o valor da propriedade `spring.data.mongodb.host` para alterar o valor do host e a propriedade `spring.data.mongodb.database` para alterar o valor da porta do banco de dados.  
Caso voce queira alterar a porta padrão do servidor altere no arquivo de propriedades `src/main/resources/application.properties` o  campo `server.port` para o valor que voce desejar.  
A utilização das apis do potterapi requer a utilização de um apikey. Para alteralo modifique o arquivo de propriedades `src/main/resources/application.properties` e altere o valor da propriedade `hp.apikey` para o valor da chave que apikey que você desejar.

## Executando o projeto
Para executar o projeto, basta rodar o comando`mvn spring-boot:run` com isso a aplicação vai subir um servidor express e disponibilizar a API na porta configurada (default 8080).

## Testes
Para rodar os testes da aplicação, executar o seguinte comando `mvn test`

## Swagger
Para ter acesso local ao conjunto de APIs é possivel abrir pelo swagger atraves do seguinte link: `http://localhost:{port}/swagger-ui.html`.  

## Docker
É possivel executar a aplicação via docker. Para isso primeiro compile a imagem com o seguinte comando: `docker build -t api-harry-potter .`