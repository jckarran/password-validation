#Backend Challenge

## Tecnologias

Lista de tecnologias do projeto:

* Kotlin linguagem escolhida
* Jacoco para analise de código
* Rest assured para teste de componente
* Koin para injeção de dependencia
* Javalin web frameworks
* Mokk mocking library for Kotlin
* docker container

## Requisitos

A aplicação valida se uma senha é válida de acordo com as regas:

* Nove ou mais caracteres
* Ao menos 1 dígito
* Ao menos 1 letra minúscula
* Ao menos 1 letra maiúscula
* Ao menos 1 caractere especial

## Build and Run

A aplicação executa na porta 7000 e o prometheus na porta 9090

Para construir o container docker
 
```bash
./gradlew clean jar dockerBuildImage
```

Para rodar o container

```bash
docker run --rm -it -p 7000:7000 -p 9090:9090 --name backend-challenge backend-challenge:0.3
```

## API

### Valida senha

#### Request

'POST' /users/validate-password

##### Body

```json
{
    "password":"T3stes^baCkend"
}
```

#### Response

- status se senha tem formato válido

##### Body

```json
{
   "status": true
}
```

##### Response codes:

| code   | description           |
|-------------------|------------|
| 200               | success    |
| 400               | invalid request|

#### Exemplo de requisição

```bash
curl -v -X POST 'http://localhost:7000/users/validate-password' \
--header 'transaction-id: 1' \
--header 'Content-Type: application/json' \
--data-raw '{
	"password":"T3stes^baCkend"
}'

> POST /users/validate-password HTTP/1.1
> Host: localhost:7000
> User-Agent: curl/7.69.1
> Accept: */*
> transaction-id: 1
> Content-Type: application/json
> Content-Length: 31
>
* upload completely sent off: 31 out of 31 bytes
* Mark bundle as not supporting multiuse
< HTTP/1.1 200 OK
< Date: Mon, 04 May 2020 23:14:33 GMT
< Server: Javalin
< Content-Type: application/json
< Content-Length: 15
<
{"status":true}
```
## Testes

### Postman

Esta disponibilizada uma coleção do POSTMAN para facilitar os testes da aplicação: \

```
Backend-challenge.postman_collection.json
```

### Teste unitário e componente

```bash
./gradlew test jacocoTestReport jacocoTestCoverageVerification
```

O relatório do jacoco vai ser gerado no diretório:
```
/build/reports/coverage
```