package main.world;

import main.core.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;
    int maxMapCols;
    int maxMapRows;

    public boolean isCollision(Rectangle hitbox) {
        // Loop over all rows and columns correctly
        for (int row = 0; row < maxMapRows; row++) {
            for (int col = 0; col < maxMapCols; col++) {
                int tileNum = mapTileNum[row][col];

                if (tile[tileNum].collision) {
                    int tileX = col * gp.tileSize;
                    int tileY = row * gp.tileSize;

                    Rectangle tileRect = new Rectangle(tileX, tileY, gp.tileSize, gp.tileSize);

                    if (hitbox.intersects(tileRect)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[21];

        // first read the map file once here to get rows and cols or hardcode these
//        maxMapCols = 50;  // for example, your map width in tiles
//        maxMapRows = 50;  // map height in tiles

//        mapTileNum = new int[maxMapRows][maxMapCols];  // rows first, then columns

        loadTileImages();
//        loadMap('');
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

            tile[5] = new Tile();
            tile[5].image = ImageIO.read(new File("res/tilesets/ash.png"));

            tile[6] = new Tile();
            tile[6].image = ImageIO.read(new File("res/tilesets/snow.png"));

            tile[7] = new Tile();
            tile[7].image = ImageIO.read(new File("res/tilesets/bluecarpet.png"));

            tile[8] = new Tile();
            tile[8].image = ImageIO.read(new File("res/tilesets/pinkcarpet.png"));

            tile[9] = new Tile();
            tile[9].image = ImageIO.read(new File("res/tilesets/orangecarpet.png"));

            tile[10] = new Tile();
            tile[10].image = ImageIO.read(new File("res/tilesets/brickwall1.png"));
            tile[10].collision = true;

            tile[11] = new Tile();
            tile[11].image = ImageIO.read(new File("res/tilesets/brickwall2.png"));
            tile[11].collision = true;

            tile[12] = new Tile();
            tile[12].image = ImageIO.read(new File("res/tilesets/stonewall1.png"));
            tile[12].collision = true;

            tile[13] = new Tile();
            tile[13].image = ImageIO.read(new File("res/tilesets/stonewall2.png"));
            tile[13].collision = true;

            tile[14] = new Tile();
            tile[14].image = ImageIO.read(new File("res/tilesets/logwall1.png"));
            tile[14].collision = true;

            tile[15] = new Tile();
            tile[15].image = ImageIO.read(new File("res/tilesets/logwall2.png"));
            tile[15].collision = true;

            tile[16] = new Tile();
            tile[16].image = ImageIO.read(new File("res/tilesets/lava.png"));
            tile[16].collision = true;

            tile[17] = new Tile();
            tile[17].image = ImageIO.read(new File("res/tilesets/corrodedcopper.png"));
            tile[17].collision = true;

            tile[18] = new Tile();
            tile[18].image = ImageIO.read(new File("res/tilesets/semicorrodedcopper.png"));
            tile[18].collision = true;

            tile[19] = new Tile();
            tile[19].image = ImageIO.read(new File("res/tilesets/copper.png"));
            tile[19].collision = true;

            tile[20] = new Tile();
            tile[20].image = ImageIO.read(new File("res/tilesets/mountains.png"));
            tile[20].collision = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {
            // First pass: count rows and columns
            int rows = 0;
            int cols = 0;
            String line;

            while ((line = br.readLine()) != null) {
                String[] nums = line.trim().split("\\s+");
                cols = Math.max(cols, nums.length);  // track max columns (in case rows vary)
                rows++;
            }

            // Save map size
            maxMapCols = cols;
            maxMapRows = rows;

            // Allocate array now that we know size
            mapTileNum = new int[maxMapRows][maxMapCols];

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Second pass: read file again to fill mapTileNum
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {
            String line;
            int row = 0;

            while ((line = br.readLine()) != null && row < maxMapRows) {
                String[] nums = line.trim().split("\\s+");
                for (int col = 0; col < nums.length; col++) {
                    mapTileNum[row][col] = Integer.parseInt(nums[col]);
                }
                row++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2, int cameraX, int cameraY) {
        for (int row = 0; row < maxMapRows; row++) {
            for (int col = 0; col < maxMapCols; col++) {
                int tileNum = mapTileNum[row][col];
                int x = col * gp.tileSize - cameraX;
                int y = row * gp.tileSize - cameraY;
                g2.drawImage(tile[tileNum].image, x, y, gp.tileSize, gp.tileSize, null);
            }
        }
    }

    public boolean isCollision(int x, int y) {
        int col = x / gp.tileSize;
        int row = y / gp.tileSize;

        if (col < 0 || row < 0 || col >= maxMapCols || row >= maxMapRows) return true;

        int tileNum = mapTileNum[row][col];
        return tile[tileNum].collision;
    }

}
