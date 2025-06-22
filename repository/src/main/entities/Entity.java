package main.entities;

import main.core.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    public int x, y;
    public int speed;
    public BufferedImage image;
    public Rectangle hitbox;


    public void update() {}

    public void draw(Graphics2D g2) {}
}
