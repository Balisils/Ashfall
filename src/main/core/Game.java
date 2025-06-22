package main.core;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import java.awt.*;

public class Game extends JFrame {
    public static void main(String[] args) {
        JFrame frame = new JFrame(); // Creates a JFrame
        frame.setTitle("ASHFALL"); // sets the name of window
        frame.setResizable(false); // cannot resize window
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE); // closes when x is clicked

        ImageIcon logo = new ImageIcon("/RPG/res/sprites/earthlogo.png"); // creates an ImageIcon
        frame.setIconImage(logo.getImage()); // change icon of window

        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.pack();
        gamePanel.startGameThread(); // starts Game Loop

        frame.setLocationRelativeTo(null); // puts window in middle of screen
        frame.setVisible(true); // allows you to see the screen
    }
}