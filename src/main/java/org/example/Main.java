package org.example;
//yazlabinho
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.List;


// Location class to represent coordinates
class Location {
    private int x;
    private int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Override toString method to print coordinates
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
//search c
class DepthFirstSearch {

    // Function to perform DFS
    public static void DFS(char[][] grid, int row, int col, boolean[][] visited, ArrayList<Location> path, ArrayList<int[]> targets) {
        int numRows = grid.length;
        int numCols = grid[0].length;

        // Check if the current position is within the bounds of the grid and not visited
        if (row < 0 || col < 0 || row >= numRows || col >= numCols || visited[row][col] || grid[row][col] == 0) {
            return;
        }

        // Mark the current position as visited
        visited[row][col] = true;

        // Add the current position to the path
        path.add(new Location(row,col));

        // Check if the current position is one of the target positions
        for (int[] target : targets) {
            if (row == target[0] && col == target[1]) {
                System.out.println("Target found at position (" + row + ", " + col + ")");
                for (Location p: path) {
                    //visited[p.getX()][p.getY()] = false;
                    System.out.println("Path:"+p.getX()+","+p.getY());
                }
                path.clear();
                // Assuming you want to continue searching for other targets, you may remove the target from the list.
                break; // Break the loop after finding the target
            }
        }

        // Perform DFS recursively in all four directions
        DFS(grid, row + 1, col, visited, path, targets); // Down
        DFS(grid, row - 1, col, visited, path, targets); // Up
        DFS(grid, row, col + 1, visited, path, targets); // Right
        DFS(grid, row, col - 1, visited, path, targets); // Left

        // If no target found in this path, remove the last position from the path
        if(path.size()!=0)
        path.remove(path.size() - 1);
    }
}

    // Helper method to construct the path


// Character class
class Character {
    private String id;
    private Location location;
    private ArrayList<Treasure> discovered;

    public Character(String id, Location location, ArrayList<Treasure> discovered) {
        this.id = id;
        this.location = location;
        this.discovered = discovered;
    }

    public String getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}


// Obstacle class (abstract)
abstract class Obstacle {
    protected Location location;

    public Obstacle(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    // Method to check if character can pass through
    public abstract boolean canPassThrough();
}

//hareket etmeyecek engeller
class StaticObstacle extends Obstacle {
    public StaticObstacle(Location location) {
        super(location);
    }

    @Override
    public boolean canPassThrough() {
        return false;
    }
}

// dinamik engeller
class DynamicObstacle extends Obstacle {
    private int movementRange;

    public DynamicObstacle(Location location, int movementRange) {
        super(location);
        this.movementRange = movementRange;
    }

    @Override
    public boolean canPassThrough() {
        return false;
    }

    // Method to move dynamic obstacle up
    public void moveUp() {
        location = new Location(location.getX(), location.getY() - 1);
    }

    // Method to move dynamic obstacle down
    public void moveDown() {
        location = new Location(location.getX(), location.getY() + 1);
    }

    // Method to move dynamic obstacle left
    public void moveLeft() {
        location = new Location(location.getX() - 1, location.getY());
    }

    // Method to move dynamic obstacle right
    public void moveRight() {
        location = new Location(location.getX() + 1, location.getY());
    }

}

// Treasure class representing different types of treasures
class Treasure {
    private String type;
    private Location location;

    public Treasure(String type, Location location) {
        this.type = type;
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public Location getLocation() {
        return location;
    }

    // Override toString method to print treasure details
    @Override
    public String toString() {
        return type + " found at " + location.toString();
    }
}

// Main class
public class Main {
    public static void main(String[] args) {
        try {
            // Read the text file representing the grid
            char[][] map = readGridFromFile("C:\\Users\\Emre\\Desktop\\matrix2.txt");
            boolean[][] visited = new boolean[map.length][map[0].length];
            ArrayList<int[]> targets = new ArrayList<>();
            ArrayList<Location> path = new ArrayList<>();
            targets.add(new int[]{5, 12}); // First target
            targets.add(new int[]{11, 11}); // Second target

            // Perform DFS for each target
            for (int[] target : targets) {
                DepthFirstSearch.DFS(map, 0, 0, visited, path,targets);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }
    private static char[][] readGridFromFile(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        char[][] grid = new char[30][30];

        for (int i = 0; i < 30; i++) {
            String line = scanner.nextLine();
            for (int j = 0; j < 30; j++) {
                grid[i][j] = line.charAt(j);
            }
        }

        scanner.close();

        return grid;
    }

    // Method to search for a grid value in the map
    private static ArrayList<int[]> searchForGridValue(char[][] map, char value) {
        ArrayList<int[]> loclist = new ArrayList<>();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == value) {
                    System.out.println("HERE:"+i+","+j);
                    loclist.add(new int[]{i,j});
                }

            }
        }
        return loclist;
    }
}


