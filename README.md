# Proyecto: Juego de Cartas "Corazones"

## 📘 Asignatura
**Programación Orientada a Objetos (POO)**

## 👨‍🎓 Alumno
**Nombre:** Eliseo Sanchez  
**Legajo:** 164514

---

## 🃏 Descripción del Juego

El juego de cartas **"Corazones"** es un clásico en el que los jugadores compiten para **evitar sumar puntos de penalización**.  

### 🎯 Objetivo
El objetivo principal es **acumular la menor cantidad de puntos posible** al final de la partida.

### ⚙️ Reglas Básicas
- Cada jugador intenta **evitar ganar bazas** que contengan cartas de penalización.  
- **Cartas que otorgan puntos:**
  - Cada **carta de corazones** vale **1 punto**.
  - La **Reina de Picas (Q♠)** vale **13 puntos**.
- Si un jugador logra **capturar todas las cartas de penalización** (todas las de corazones y la Reina de Picas), se dice que **"tiró a la luna"**, obteniendo **0 puntos**, mientras que **los demás jugadores reciben 26 puntos**.
- La partida termina cuando **un jugador alcanza o supera los 100 puntos**.
- El **ganador** es el jugador con la **menor puntuación total** en ese momento.

---

## 🧱 Implementación en POO
El proyecto aplica los **principios de la Programación Orientada a Objetos**, tales como:
- **Encapsulamiento:** manejo de atributos y métodos dentro de clases.
- **Herencia:** definición de clases derivadas para distintos tipos de cartas o jugadores.
- **Polimorfismo:** implementación de comportamientos específicos según el tipo de objeto.
- **Abstracción:** modelado del juego, las cartas y los jugadores de forma modular y reutilizable.

### 🏗️ Patrones de Diseño

#### Modelo–Vista–Controlador (MVC)
El proyecto está estructurado siguiendo el patrón **MVC**, que separa claramente las responsabilidades:
- **Modelo:** gestiona la lógica del juego, las reglas, los jugadores y las cartas.  
- **Vista:** se encarga de mostrar el estado del juego al usuario (ya sea en consola o interfaz gráfica).  
- **Controlador:** actúa como intermediario entre la vista y el modelo, manejando las acciones del usuario y actualizando el estado del juego.

---

© 2025 - Eliseo Sanchez
