# 🏥 Proyecto Clínica - Microservicios

Este repositorio contiene los microservicios que forman parte del **Proyecto Clínica**, desarrollado con arquitectura basada en microservicios para gestionar distintos módulos de la aplicación.

---
## Link de Proyecto en Notion
[Proyecto Clinica](https://difficult-tadpole-88e.notion.site/Unit-2-Microservicios-para-Clinica-34f679506dff802dbf7fe69933282628?pvs=73)

## 📂 Estructura del repositorio
```
/microservicios-proyecto-clinica
  /autenticacion
  /reserva
  /ficha_clinica
  /pagos
  /examenes
  /recetas
  /notificaciones
  /soporte
```
## 🚀 Guía de Inicio Rápido (Para los PC de la U)

Sigue estos pasos exactos cada vez que te sientes en un computador de la universidad para levantar el entorno. Como las terminales de los PC están bloqueadas, usaremos la terminal interna de Docker Desktop.
### Paso 1: Clonar el proyecto

    Clona este repositorio en el equipo usando la interfaz visual de VS Code.

    Abre la carpeta del proyecto (microservicios-proyecto-clinica) en VS Code.

    Haz clic derecho sobre la carpeta raíz en el explorador de VS Code y selecciona "Copy Path" (Copiar ruta). La necesitaremos en el paso 3.

### Paso 2: Abrir Docker Desktop

    Abre la aplicación de Docker Desktop desde el menú de inicio.

    Espera a que el motor encienda por completo (icono verde abajo a la izquierda).

### Paso 3: Levantar las Bases de Datos (Desde la Terminal de Docker)

Dado que la terminal de VS Code no reconoce el comando docker por restricciones de la U, usaremos la terminal propia de Docker Desktop:

    En Docker Desktop, ve abajo a la derecha y haz clic en el botón Terminal (o el icono de la consola >_).

    Para moverte rápido a la carpeta del proyecto sin usar mil veces ls, escribe cd seguido de un espacio, pega la ruta que copiaste en el Paso 1 y presiona Enter:
    Bash
    cd "ruta_que_copiaste_de_vsc"
    
    Por lo general la ruta en Docker en los pcs de la universidad es:
    cd C:\Users\SSDD\microservicios-proyecto-clinica\

    Una vez situados en la carpeta del proyecto, ejecuta el comando para encender los contenedores en segundo plano:
    Bash

    docker compose up -d

### Paso 4: Verificar las Bases de Datos (Uso de Laragon)

Dado que los PC de la universidad no cuentan con HeidiSQL instalado de forma independiente, utilizaremos el que viene integrado en Laragon:

    Abre Laragon.

    ⚠️ IMPORTANTE: NO hagas clic en "Iniciar todo" (Start All). Los servicios de Laragon deben quedarse apagados para que no choquen con Docker.

    Haz clic directamente en el botón Base de Datos (Database).

    Dale a Conectar con los datos por defecto (Usuario: root, sin contraseña, puerto 3306).

    Verifica en el panel izquierdo que aparezcan creadas automáticamente las bases de datos del proyecto (ej: clinica_pagos_db, clinica_autenticacion_db, etc.).

Paso 5: Correr los Microservicios

Vuelve a VS Code, ve al Spring Boot Dashboard (o usa la terminal integrada de VS Code que sí te deja compilar Java) y dale Play a cada microservicio para que se conecten a la base de datos de Docker y empiecen a funcionar.
