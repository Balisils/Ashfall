package main.entities;

import main.core.GamePanel;
import main.core.KeyHandler;
import main.world.TileManager;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Player extends Entity{
    GamePanel gp;
    KeyHandler keyH;
    TileManager tileM;

    public Rectangle solidArea;
    public int solidAreaDefaultX, solidAreaDefaultY;
    int cameraX, cameraY;


    BufferedImage[] upSprites, downSprites, leftSprites, rightSprites;
    String direction = "down";
    public void update() {
        boolean moving = false;
        int newX = x;
        int newY = y;

        if (keyH.upPressed) {
            newY -= speed;
            direction = "up";
            moving = true;
        } else if (keyH.downPressed) {
            newY += speed;
            direction = "down";
            moving = true;
        } else if (keyH.leftPressed) {
            newX -= speed;
            direction = "left";
            moving = true;
        } else if (keyH.rightPressed) {
            newX += speed;
            direction = "right";
            moving = true;
        }

        if (moving) {
            Rectangle nextPos = new Rectangle(
                    newX + solidArea.x,
                    newY + solidArea.y,
                    solidArea.width,
                    solidArea.height
            );

            if (!gp.tileManager.isCollision(nextPos)) {
                x = newX;
                y = newY;
                spriteCounter++;
                if (spriteCounter > 12) {
                    spriteNum = (spriteNum == 0) ? 2 : 0;
                    spriteCounter = 0;
                }
            } else {
                spriteNum = 1;  // standing sprite when blocked
            }
        } else {
            spriteNum = 1;  // standing sprite when no movement
        }
    }

    int spriteCounter = 0;

    int spriteNum = 0;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.tileM = gp.tileManager;
        this.keyH = keyH;

        solidArea = new Rectangle(8, 14, 32, 32); // a 32x32 box inside 48x48 sprite, offset by (8,16)
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        x = 400;
        y = 400;
        speed = 8;

        loadPlayerSprites();
    }
    public void draw(Graphics2D g2, int cameraX, int cameraY) {
        BufferedImage image = null;

        switch (direction) {
            case "up" -> image = upSprites[spriteNum];
            case "down" -> image = downSprites[spriteNum];
            case "left" -> image = leftSprites[spriteNum];
            case "right" -> image = rightSprites[spriteNum];
        }

        // Draw relative to the camera
        int drawX = x - cameraX;
        int drawY = y - cameraY;
        g2.drawImage(image, drawX, drawY, gp.tileSize, gp.tileSize, null);

        int screenX = x - cameraX;
        int screenY = y - cameraY;
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);

    }


    private void loadPlayerSprites() {
        try{
            BufferedImage spriteSheet = ImageIO.read(new File("D:/RPG/res/sprites/PlayerWalk.png"));
            InputStream is = getClass().getResourceAsStream("/sprites/PlayerWalk.png");

            if (is == null) {
                System.err.println("ERROR: Could not load /sprites/PlayerWalk.png");
            } else {
                System.out.println("Sprite loaded successfully.");
            }

            BufferedImage spritesheet = ImageIO.read(Objects.requireNonNull(is));
            int tileSize = gp.originalTileSize;

            upSprites = new BufferedImage[3];
            downSprites = new BufferedImage[3];
            leftSprites = new BufferedImage[3];
            rightSprites = new BufferedImage[3];

            for (int i = 0; i < 3; i++) {
                int spriteSize = 16; // original size of sprite
                downSprites[i] = spriteSheet.getSubimage(i * spriteSize, 0, spriteSize, spriteSize);       // row 0
                leftSprites[i] = spriteSheet.getSubimage(i * spriteSize, spriteSize * 3, spriteSize, spriteSize); // row 1
                rightSprites[i] = spriteSheet.getSubimage(i * spriteSize, spriteSize * 2, spriteSize, spriteSize); // row 2
                upSprites[i] = spriteSheet.getSubimage(i * spriteSize, spriteSize, spriteSize, spriteSize);   // row 3
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}