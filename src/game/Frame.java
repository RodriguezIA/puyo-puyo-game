package game;

import javax.swing.*;

public class Frame extends JFrame {
    public Frame() {
        super("Puyo Puyo"); // Establecemos el título directamente

        // Configuración de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Agregamos el panel del juego
        GamePanel gamePanel = new GamePanel();
        add(gamePanel);
        pack(); // Ajusta el tamaño de la ventana al contenido

        // Centramos la ventana en la pantalla
        setLocationRelativeTo(null);
        setVisible(true);

        // Aseguramos que el panel tenga el foco para capturar eventos
        gamePanel.requestFocusInWindow();
    }
}