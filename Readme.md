# Puyo Puyo - Juego en Java

## Descripción
Este proyecto es una implementación del clásico juego Puyo Puyo en Java, utilizando Swing para la interfaz gráfica. El juego consiste en manipular pares de esferas de colores ("puyos") que caen desde la parte superior de la pantalla, con el objetivo de formar grupos de cuatro o más puyos del mismo color para hacerlos desaparecer.

## Características
- Interfaz gráfica usando Java Swing
- Sistema de rotación de puyos
- Detección de coincidencias de 4 o más puyos del mismo color
- Sistema de gravedad que hace caer los puyos cuando hay espacios vacíos
- Controles intuitivos mediante teclado
- Soporte para imágenes personalizadas de puyos


## Compatibilidad y Compilación

### Versión de Desarrollo
- **Entorno de Desarrollo**: OpenJDK 23
- **Herramienta de Compilación**: javac


### Compilación para Diferentes Versiones de Java


```bash
# Compilación básica
javac -source 8 -target 8 src/game/*.java

# Compilación con versiones específicas
javac --release 8 -d bin src/game/*.java
```

#### Matriz de Compatibilidad

| Versión de Java | Comando de Compilación |
|----------------|------------------------|
| Java 8         | `javac --release 8`    | 
| Java 11        | `javac --release 11`   | 
| Java 17        | `javac --release 17`   |
| Java 21        | `javac --release 21`   | 
| Java 23        | `javac`    |

## Estructura del Proyecto
El proyecto está organizado en los siguientes archivos principales:

- `Main.java`: Punto de entrada de la aplicación
- `Frame.java`: Gestiona la ventana principal del juego
- `GamePanel.java`: Maneja la lógica del juego y el renderizado
- `Board.java`: Implementa la lógica del tablero
- `Puyo.java`: Define el comportamiento de los pares de puyos
- `Constants.java`: Contiene las constantes globales del juego

## Controles Detallados

### Movimiento Horizontal
- **Flecha Izquierda**: Mueve el par de puyos un espacio hacia la izquierda
    - Solo se puede mover si no hay obstáculos
    - Respeta los límites del tablero

- **Flecha Derecha**: Mueve el par de puyos un espacio hacia la derecha
    - Solo se puede mover si no hay obstáculos
    - Respeta los límites del tablero

### Caída y Velocidad
- **Flecha Abajo**: Acelera la caída del par de puyos
    - Aumenta la velocidad de descenso
    - Útil para posicionar rápidamente los puyos

### Rotación
- **Tecla Z**: Rota el par de puyos en sentido horario
    - La rotación respeta los límites del tablero
    - Si la rotación no es posible en la posición actual, el juego intentará ajustar la posición
    - Permite rotar incluso cuando un puyo ya ha colisionado

## Instalación y Ejecución
1. Compila el proyecto:
   ```bash
   javac -d bin src/game/*.java
   ```
2. Ejecuta el juego:
   ```bash
   java -cp bin game.Main
   ```

## Mecánicas del Juego
- Los puyos caen en pares desde la parte superior del tablero
- Puedes rotar y mover los pares mientras caen
- Forma grupos de 4 o más puyos del mismo color para eliminarlos
- Los puyos superiores caerán cuando hay espacios vacíos debajo
- El juego termina cuando los puyos alcanzan la parte superior del tablero

## Limitaciones Actuales

### Reinicio de Partida
- **Problema**: No existe un botón o método para reiniciar el juego después de Game Over
- **Comportamiento Actual**: Una vez que el juego termina, el usuario debe cerrar y volver a abrir la aplicación
- **Impacto**: Reduce la experiencia de juego y la usabilidad

### Sistema de Puntuación
- **Problema**: Ausencia de un sistema de puntuación
- **Carencias Actuales**:
    - No se registran puntos por eliminación de grupos
    - Sin contador de puyos eliminados
    - Falta de progresión o desafío para el jugador

## Bugs Conocidos y Comportamientos Inesperados

### Problemas de Caída y Colisión
1. **Separación Vertical de Puyos**
    - **Descripción**: En algunas situaciones, los puyos de un mismo par pueden separarse verticalmente
    - **Efecto**: Un puyo puede quedar en una posición diferente a su par original
    - **Posible Causa**: Inconsistencias en el algoritmo de caída y detección de colisiones

2. **Control de Par Después de Colisión Parcial**
    - **Descripción**: Si un puyo de un par ya ha colisionado, aún se puede controlar el par completo
    - **Efecto**: Permite movimientos y rotaciones que podrían no se deberían permitir
    - **Ejemplo**: Rotar un par donde un puyo ya ha tocado otro objeto o el fondo

3. **Eliminación Inesperada de Puyos**
    - **Descripción**: Puyos de un color diferente pueden ser eliminados junto con un grupo grande
    - **Efecto**: Pérdida no intencional de puyos que no forman parte del grupo de 4 o más
    - **Posible Causa**: Problemas en el algoritmo de búsqueda de grupos



## Algoritmos
- **Búsqueda de Grupos**: Algoritmo para establecer las coincidencias de los grupos de colores
- - <img src="./Docs/assets/diagrama%20busqueda.png" alt="Descripción" width="100%"/>
- **Búsqueda en Anchura (BFS)**: Algoritmo utilizado para encontrar grupos de puyos del mismo color
- - <img src="./Docs/assets/diagrama%20BFS.png" alt="Descripción" width="100%"/>
    

