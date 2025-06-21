package main.world;

import main.core.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;

    public boolean isCollision(Rectangle hitbox) {
        // Will return true if collision detected
        for (int row = 0; row < gp.screenRow; row++) {
            for (int col = 0; col < gp.screenCol; col++) {
                int tileNum = mapTileNum[col][row];

                // Check if this tile is solid/collidable
                if (tile[tileNum].collision) {
                    // Tile position and size
                    int tileX = col * gp.tileSize;
                    int tileY = row * gp.tileSize;

                    Rectangle tileRect = new Rectangle(tileX, tileY, gp.tileSize, gp.tileSize);

                    // Check if hitbox intersects this tile
                    if (hitbox.intersects(tileRect)) {
                        return true; // Collision detected
                    }
                }
            }
        }
        return false; // No collision found
    }


    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[10];
        mapTileNum = new int[gp.screenCol][gp.screenRow];

        loadTileImages();
        loadMap("res/maps/ASHFALLTESTMAP1.txt"); // fixed path format too
    }

    public void loadTileImages() {

        try {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(new File("res/tilesets/grass.png"));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(new File("res/tilesets/dirt.png"));

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(new File("res/tilesets/sand.png"));

            tile[3] = new Tile();
            tile[3].image = ImageIO.read(new File("res/tilesets/water.png"));
            tile[3].collision = true;

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(new File("res/tilesets/stone.png"));
            tile[4].collision = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath) {
        try {
            InputStream is = new FileInputStream("res/maps/ASHFALLTESTMAP1.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int row = 0;
            while (row < gp.screenRow) {
                String line = br.readLine();
                String[] nums = line.split(" ");
                for (int col = 0; col < gp.screenCol; col++) {
                    int num = Integer.parseInt(nums[col]);
                    mapTileNum[col][row] = num;
                }
                row++;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        for (int row = 0; row < gp.screenRow; row++) {
            for (int col = 0; col < gp.screenCol; col++) {
                int tileNum = mapTileNum[col][row];
                int x = col * gp.tileSize;
                int y = row * gp.tileSize;
                g2.drawImage(tile[tileNum].image, x, y, gp.tileSize, gp.tileSize, null);
            }
        }
    }

    public boolean isCollision(int x, int y) {
        int col = x / gp.tileSize;
        int row = y / gp.tileSize;

        if (col < 0 || row < 0 || col >= gp.screenCol || row >= gp.screenRow) return true;

        int tileNum = mapTileNum[col][row];
        return tile[tileNum].collision;
    }

}
