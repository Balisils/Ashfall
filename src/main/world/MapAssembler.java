package main.world;

import main.core.GamePanel;
import java.io.*;
import java.util.*;

public class MapAssembler {
    GamePanel gp;
    TileManager tileManager;

    public MapAssembler(GamePanel gp, TileManager tileManager) {
        this.gp = gp;
        this.tileManager = tileManager;
    }

    public void assembleWorldFromChunks(String[][] chunkPaths, int chunkSize) {
        int rows = chunkPaths.length;
        int cols = chunkPaths[0].length;

        int totalRows = rows * chunkSize;
        int totalCols = cols * chunkSize;

        tileManager.mapTileNum = new int[totalRows][totalCols];
        tileManager.maxMapRows = totalRows;
        tileManager.maxMapCols = totalCols;

        for (int chunkRow = 0; chunkRow < rows; chunkRow++) {
            for (int chunkCol = 0; chunkCol < cols; chunkCol++) {
                String path = chunkPaths[chunkRow][chunkCol];
                if (path == null) continue;

                try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                    String line;
                    int rowInChunk = 0;
                    while ((line = br.readLine()) != null) {
                        String[] nums = line.trim().split("\\s+");
                        for (int colInChunk = 0; colInChunk < nums.length; colInChunk++) {
                            int worldRow = chunkRow * chunkSize + rowInChunk;
                            int worldCol = chunkCol * chunkSize + colInChunk;
                            tileManager.mapTileNum[worldRow][worldCol] = Integer.parseInt(nums[colInChunk]);
                        }
                        rowInChunk++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
