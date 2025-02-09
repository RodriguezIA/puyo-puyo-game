import game.Frame;

public class Main {
    public static void main(String[] args) {
        // Iniciamos la GUI de forma segura en el hilo de eventos de Swing
        javax.swing.SwingUtilities.invokeLater(() -> {
            new Frame(); // Solo creamos la instancia del Frame
        });
    }
}