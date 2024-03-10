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
class dfs {
    static int ROW = 30;
    static int COL = 30;
    static int dRow[] = { 0, 1, 0, -1 };
    static int dCol[] = { 1, 0, -1, 0 };


    static Boolean isValid(int row, int col) {
        // If cell is out of bounds
        if (row < 0 || col < 0 || row >= ROW || col >= COL)
            return false;

        // Otherwise, it can be visited
        return true;
    }

    // Function to perform DFS Traversal on the matrix grid[]
    static List<Location> findShortestPath(char[][] grid, Location start, Location target) {
        Boolean[][] visited = new Boolean[ROW][COL];
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                visited[i][j] = false;
            }
        }
        Stack<Location> stack = new Stack<>();
        Map<Location, Location> parentMap = new HashMap<>();

        stack.push(start);

        while (!stack.empty()) {
            Location curr = stack.pop();
            int row = curr.getX();
            int col = curr.getY();

            if (row == target.getX() && col == target.getY()) {
                return constructPath(parentMap, target);
            }

            if (!isValid(row, col) || visited[row][col] || grid[row][col]=='O') {
                continue;
            }

            visited[row][col] = true;
            System.out.print(row+","+col + "-");

            // Push all the adjacent cells
            for (int i = 0; i < 4; i++) {
                int adjx = row + dRow[i];
                int adjy = col + dCol[i];
                stack.push(new Location(adjx, adjy));
                parentMap.put(new Location(adjx, adjy), curr);
            }
        }

        return null; // No path found
    }

    // Helper method to construct the path
    static List<Location> constructPath(Map<Location, Location> parentMap, Location target) {
        List<Location> path = new ArrayList<>();
        Location current = target;
        while (parentMap.containsKey(current)) {
            path.add(current);
            current = parentMap.get(current);
        }
        Collections.reverse(path);
        return path;
    }

}

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
            char[][] map = readGridFromFile("C:\\Users\\Emre\\Desktop\\matrix.txt");
            dfs s = new dfs();
            dfs.findShortestPath(map,new Location(0,0),searchForGridValue(map,'G'));

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
    private static Location searchForGridValue(char[][] map, char value) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == value) {
                    return new Location(i,j);
                }

            }
        }
        return null;
    }
}


