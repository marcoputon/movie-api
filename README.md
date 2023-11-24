
# movie-api

API RESTful para leitura da lista de indicados e vencedores da categoria Pior Filme do Golden Raspberry Awards.

A aplicação foi desenvolvida utilizando _Java 11_, com _Spring Boot_. Para o banco de dados, é utilizado um _H2_ em memória e os dados não são salvos ao reiniciar.

Ao iniciar a aplicação, a tabela _AWARD_ é preenchida automaticamente com os dados do arquivo **/src/main/resources/movielist.cvs**, seguindo o cabeçalho abaixo. Apenas o campo _winner_ não é obrigatório, ou seja, a aplicação não irá inserir os dados corretamente caso tente inserir valores nulos nos demais campos.
```
year;title;studios;producers;winner
```
Assim que a aplicacão inicia, o método ```populateTables()``` da classe ```AwardService``` é chamado para popular a tabela de produtores, separando cada produtor em uma linha distinta. Em seguida, é populada outra tabela apenas com os intervalos dos produtores premiados.

Ao chamar a rota ```GET /api/award-intervals```, a aplicação irá buscar todos produtores que tiveram o menor e maior intervalo de tempo entre premios.

## Documentação da API

#### Retorna duas listas de produtores, contendo os menores e maiores (min e max, respectivamente) intervalos de premiações

```http
  GET /api/award-intervals
```
#### Resultado

```json
{
    "min": [
        {
            "producer": "Producer 1",
            "interval": 1,
            "previousWin": 2008,
            "followingWin": 2009
        },
        {
            "producer": "Producer 2",
            "interval": 1,
            "previousWin": 2018,
            "followingWin": 2019
        }
    ],
    "max": [
        {
            "producer": "Producer 1",
            "interval": 99,
            "previousWin": 1900,
            "followingWin": 1999
        },
        {
            "producer": "Producer 2",
            "interval": 99,
            "previousWin": 2000,
            "followingWin": 2099
        }
    ]
}
```

## Rodando localmente

Clone o projeto

```bash
  git clone git@github.com:marcoputon/movie-api.git
```

Entre no diretório do projeto

```bash
  cd movie-api
```

Rode os testes

```bash
  mvn test
```

Inicie o servidor

```bash
  mvn spring-boot:run
```



