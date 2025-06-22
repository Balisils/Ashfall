package main.core;

import main.world.TileManager;
import main.entities.Player;
import main.world.MapAssembler;

import javax.swing.JPanel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {
    public final int originalTileSize = 16;
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;

    public final int screenRow = 16;
    public final int screenCol = 16;

    public int worldWidth;  // total width in pixels
    public int worldHeight; // total height in pixels

    public int maxMapCol;
    public int maxMapRow;

    public final int screenWidth = tileSize * screenRow; // 720 pixels
    public final int screenHeight = tileSize * screenCol; // 720 pixels
    public TileManager tileManager = new TileManager(this);


    int FPS = 60;
    Thread gameThread;
    KeyHandler keyH = new KeyHandler();
    Player player = new Player(this, keyH);


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(new Color(255, 255, 255));
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        // 📦 Chunk setup
// 1. Scan the chunks folder for .txt files
        File chunkFolder = new File("res/maps/chunks");
        File[] chunkFiles = chunkFolder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (chunkFiles == null || chunkFiles.length == 0) {
            System.out.println("No chunk files found in the folder!");
            // Handle this case appropriately (throw exception or default map)
        }

// 2. Convert to a list of paths (for easier random selection)
        ArrayList<String> chunkFilePaths = new ArrayList<>();
        for (File f : chunkFiles) {
            chunkFilePaths.add(f.getPath().replace("\\", "/"));  // Use forward slashes for consistency
        }

// 3. Create 5x5 chunkPaths array and fill it randomly
        String[][] chunkPaths = new String[5][5];
        Random rand = new Random();

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                int randomIndex = rand.nextInt(chunkFilePaths.size());
                chunkPaths[row][col] = chunkFilePaths.get(randomIndex);
            }
        }

// 4. Use MapAssembler to assemble world from chunkPaths
        MapAssembler assembler = new MapAssembler(this, tileManager);
        assembler.assembleWorldFromChunks(chunkPaths, 32);

        // testing chunk layout
        System.out.println("Chunk layout:");
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                System.out.print(chunkPaths[row][col] + " ");
            }
            System.out.println();
        }

        maxMapCol = tileManager.mapTileNum.length;          // number of columns
        maxMapRow = tileManager.mapTileNum[0].length;       // number of rows
        worldWidth = tileSize * maxMapCol;
        worldHeight = tileSize * maxMapRow;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();

    }

    @Override
    public void run() {
        double drawInterval = 1000000000.0 / FPS; // 60 FPS
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }

    }

    private void update() {
        player.update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        draw(g2);  // Use the camera-aware draw method
        g2.dispose();
    }

    int cameraX, cameraY;

    public void draw(Graphics2D g2) {
        // Calculate camera offset based on player position
        cameraX = player.x - screenWidth / 2;
        cameraY = player.y - screenHeight / 2;

        // Clamp camera so it doesn't show beyond the map edges
        if (cameraX < 0) cameraX = 0;
        if (cameraY < 0) cameraY = 0;
        if (cameraX > worldWidth - screenWidth) cameraX = worldWidth - screenWidth;
        if (cameraY > worldHeight - screenHeight) cameraY = worldHeight - screenHeight;

        // Draw everything offset by camera
        tileManager.draw(g2, cameraX, cameraY);

        player.draw(g2, cameraX, cameraY);
    }
}