# Proyecto Quarkus - Sistema de Gestión de Videojuegos 🎮

Este proyecto es un backend desarrollado con **[Quarkus](https://quarkus.io/)** que implementa un sistema de gestión de videojuegos, órdenes y usuarios.  
Está diseñado para ejecutarse de manera local y servir como base para un entorno productivo en la nube.

---

## 🚀 Requisitos previos

Antes de levantar el proyecto, asegúrate de tener instaladas las siguientes herramientas en tu PC:

- **Java 17** o superior  
  Verifica la versión instalada:
  ```bash
  java -version

# 🐳 Instrucciones para levantar y bajar Docker Compose

  Este documento explica cómo iniciar y detener servicios definidos en un archivo `docker-compose.yml`.

---

## Ejecución del proyecto

### ▶️ Levantar los contenedores

  Para iniciar los servicios definidos en tu `docker-compose.yml`, ejecuta:
  
  ```bash
  docker compose up -d
  ```

---

### ▶️ Bajar y eliminar volúmenes asociados

Para eliminar volumnes creados por los servicios definidos en tu `docker-compose.yml`, ejecuta:

  ```bash
  docker compose down -v
  ```

### ▶️ Ejecutar en modo de desarrollo

  ```bash
  ./mvnw quarkus:dev
  ```
  ```bash
  mvn quarkus:dev
  ```
---

## 📦 Aplicación estará disponible

`http://localhost:8080`