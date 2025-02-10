Basado en el código proporcionado, crearé un README.md completo en español para tu proyecto de Puyo Puyo.





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

## Requisitos del Sistema
- Java Development Kit (JDK) 8 o superior
- Sistema operativo compatible con Java (Windows, Linux, macOS)

## Estructura del Proyecto
El proyecto está organizado en los siguientes archivos principales:

- `Main.java`: Punto de entrada de la aplicación
- `Frame.java`: Gestiona la ventana principal del juego
- `GamePanel.java`: Maneja la lógica del juego y el renderizado
- `Board.java`: Implementa la lógica del tablero
- `Puyo.java`: Define el comportamiento de los pares de puyos
- `Constants.java`: Contiene las constantes globales del juego

## Controles
- **Flecha Izquierda**: Mover el par de puyos a la izquierda
- **Flecha Derecha**: Mover el par de puyos a la derecha
- **Flecha Abajo**: Acelerar la caída
- **Tecla Z**: Rotar el par de puyos

## Instalación y Ejecución

1. Clona el repositorio:
```bash
git clone [URL_DEL_REPOSITORIO]
```

2. Navega al directorio del proyecto:
```bash
cd puyo-puyo
```

3. Compila el proyecto:
```bash
javac -d bin src/game/*.java
```

4. Ejecuta el juego:
```bash
java -cp bin game.Main
```

## Mecánicas del Juego
- Los puyos caen en pares desde la parte superior del tablero
- Puedes rotar y mover los pares mientras caen
- Forma grupos de 4 o más puyos del mismo color para eliminarlos
- Los puyos superiores caerán cuando hay espacios vacíos debajo
- El juego termina cuando los puyos alcanzan la parte superior del tablero

## Personalización
El juego permite personalizar varios aspectos:
- Tamaño del tablero (modificando `Constants.java`)
- Colores de los puyos
- Imágenes de los puyos (ubicadas en `src/assets/`)
- Velocidad de caída (ajustando el temporizador en `GamePanel.java`)

## Contribución
Si deseas contribuir al proyecto:
1. Haz un fork del repositorio
2. Crea una rama para tu función: `git checkout -b nueva-funcion`
3. Realiza tus cambios y haz commit: `git commit -m 'Agrega nueva función'`
4. Empuja los cambios a tu fork: `git push origin nueva-funcion`
5. Crea un Pull Request

## Licencia
[Especifica la licencia de tu proyecto]

## Contacto
[Tu información de contacto]


He creado un README.md completo para tu proyecto. Incluye todas las secciones importantes como descripción, características, requisitos, controles, instalación y más.

¿Te gustaría que ajuste o agregue alguna sección específica? Por ejemplo, podríamos:
- Agregar más detalles sobre alguna característica específica
- Incluir capturas de pantalla del juego
- Expandir la sección de personalización
- Agregar información sobre pruebas o debugging