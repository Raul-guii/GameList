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
```
├── src/
│   ├── main/java/com/gamelist/
│   │   ├── controller/         # Camada de controle (endpoints REST)
│   │   ├── service/            # Regras de negócio
│   │   ├── model/              # Entidades JPA
│   │   ├── repository/         # Acesso ao banco
│   │   └── config/             # Segurança, CORS, etc
│   ├── main/resources/
│   │   └── application.properties
│   └── test/
│
├──frontend/
├── src/
│   ├── app/
│   │   ├── pages/          # Páginas principais ligadas às rotas
│   │   ├── components/     # Componentes reutilizáveis da interface (navbar, sidebar, footer, etc)
│   │   ├── layouts/        # Estruturas de layout da aplicação (organização visual e router-outlet)
│   │   ├── services/       # Comunicação com a API e lógica compartilhada
│   │   ├── models/         # Interfaces e modelos de dados
│   │   ├── guards/         # Proteção de rotas (AuthGuard)
│   │   └── auths/          # Autenticação, interceptors e controle de sessão
│   ├── environments/      # Variáveis de ambiente (dev/prod)
│   └── assets/             # Arquivos estáticos (imagens, ícones)
│
├── angular.json
├── package.json
└── tsconfig.json

```

# Como rodar o projeto?
## Certifique-se de ter Docker e Docker Compose instalados
- Na primeira vez use esse comando na raiz do projeto com o Docker aberto:
```docker compose up --build```
- Se for rodar da segunda vez ou mais (somente se vc n fez mudanças no código) use este código:
```docker compose up -d```

# API Externa (IGDB)
- A integração com a IGDB API permite buscar informações detalhadas dos jogos.
- Autenticação feita via OAuth2 com Client-ID e Access Token da Twitch.
- O backend faz as requisições e serve os dados tratados para o frontend.

# Autor:
### Raul Guilherme
- Email(raulawp460@gmail.com)
- LinkedIn(https://www.linkedin.com/in/raul-guilherme-549030367/)

