# Microservicio Productos

Servicio de productos del proyecto CapySoft.

## Docker

Construir la imágen:

```shell
docker build -t products-service .
```

Ejecutar contenedor:

```shell
docker run --name products-service --network capysoft_network -v ./uploads:/app/uploads -d -p 8001:8001 products-service
```

## Uso

Esta aplicación requiere que se este levantado el servicio de eureka-server y la base de datos mysql-server.

http://localhost:8001/producto