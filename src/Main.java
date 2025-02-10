import game.Frame;

public class Main {
    public static void main(String[] args) {
        // Esto asegura que todas las operaciones de la interfaz se ejecuten en el Event Dispatch Thread.
        javax.swing.SwingUtilities.invokeLater(() -> {
            new Frame();
        });
    }
}