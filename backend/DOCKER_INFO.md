# Docker info

This guide is created for learning purposes, to keep track of and remember what we're doing.

## Running the backend in docker
Before building the project, ensure that you have the .env file with postgreSQL name, username and password. 

To create a container of the whole the backend running together, run:
```bash
docker compose up
```

## Understanding Docker compose

### Bridge app network

The **bridge app-network** allows DNS resolution by service name. Containers can reach each other via db, eureka-server, etc. This also works on the default network, but it's more predictable with an explicit one

```yml
networks:
  app-network:
    driver: bridge
```

OBS: Every service that needs to talk to another must be on the same network.

### Health check

depends_on only waits for the container to start, not for the app inside to be ready. Eureka takes several seconds to boot — your backends will try to register before it's up and fail. The health check prevents this.

```yml
healthcheck:
  test: ["CMD-SHELL", "pg_isready -U lifestage -d lifestage"]
  interval: 5s
  retries: 5
```

Other services then specify waiting for the health check:

```yml
depends_on:
  db:
    condition: service_healthy
  eureka-server:
    condition: service_healthy
```



## Teck support

Using ``docker compose down --remove-orphans`` ensures eureka and app-network gets shut down, since they sometimes won't otherwise