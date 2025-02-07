package game;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    public static final int SPHERE_SIZE = 32;
    public static final int COLUMS = 6;
    public static final int ROWS = 12;
    public static final int WIDTH = COLUMS * SPHERE_SIZE;
    public static final int HEIGHT = ROWS * SPHERE_SIZE;

    public GameFrame(){
        setTitle("Puyo-Puyo Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);


        setVisible(true);
    }
}
