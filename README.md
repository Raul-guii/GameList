# GameList

Um sistema para gerenciamento de listas de jogos, inspirado em sites de ROMs, mas sem downloads de jogos, apenas informações e organização de títulos.  
O projeto segue o padrão MVC, com Spring Boot(Java) no backend, Angular no frontend e MySQL como banco de dados, além de integração com
a API externa IGDB para busca de informações sobre os jogos. Toda a aplicação é orquestrada com Docker Compose.

# Tecnologias utilizadas
## Backend:
- Java 21
- Spring Boot
- Spring Security + JWT
- MySQL
- API Externa: [IGDB](https://api-docs.igdb.com/)

## Frontend:
- Angular
- TypeScript / HTML / CSS

## Infraestrutura
- Docker e Docker Compose
- Postman (para testes de requisição)

# Estrutura do Projeto (MVC + Angular)
├── src/
│ ├── main/java/com/gamelist/
│ │ ├── controller/ # Camada de controle (endpoints REST)
│ │ ├── service/ # Regras de negócio
│ │ ├── model/ # Entidades JPA
│ │ ├── repository/ # Acesso ao banco
│ │ └── config/ # Segurança, CORS, etc
│ ├── main/resources/
│ │ └── application.properties
│ └── test/
│
├── frontend/
│ ├── src/app/
│ │ ├── components/ # Componentes visuais (Login, Home, Listas, etc)
│ │ ├── services/ # Comunicação com a API via HttpClient
│ │ ├── models/ # Interfaces e classes de dados
│ │ ├── guards/ # Proteção de rotas (AuthGuard)
│ │ └── interceptors/ # Intercepta requisições (ex: JWT)
│ ├── src/environments/ # Variáveis de ambiente (API URLs, modo dev/prod)
│ └── package.json
│
├── docker-compose.yml
├── pom.xml
└── README.md

# Opções para rodar o projeto
## Opção 1(Certifique-se de ter Docker e Docker Compose instalados)
- Use esse comando na raiz do projeto com o Docker aberto:
- docker compose up --build

## Opção 2
- Usar o Angular somente no ambiente local(o backend e o banco continuarão rodando no Docker)
- use esse comando na pasta frontend/gamelist-frontend:
- npm install
- pm start/ng serve

# API Externa (IGDB)
- A integração com a IGDB API permite buscar informações detalhadas dos jogos.
- Autenticação feita via OAuth2 com Client-ID e Access Token da Twitch.
- O backend faz as requisições e serve os dados tratados para o frontend.

# Autor:
Raul Guilherme
- Email(raulawp460@gmail.com)
- LinkedIn(https://www.linkedin.com/in/raul-guilherme-549030367/)

