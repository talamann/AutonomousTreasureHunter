package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Main extends JPanel implements KeyListener {

    private static final int CELL_SIZE = 30;
    private static char[][] grid;
    private static boolean[][] visited;
    private static int numRows;
    private static int numCols;
    private static final int TREASURE_LIMIT = 3; // Change this value to set the limit of treasures to find
    private static int treasureFoundCount = 0;
    private static Stack<int[]> path = new Stack<>();
    private static int currentStep = 0;

    public static void main(String[] args) {
        try {
            readGridFromFile("C:\\Users\\Emre\\Desktop\\matrix2.txt"); // Provide the path to your grid text file
            System.out.println("Grid from file:");

            // Perform DFS from each cell of the grid
            iterativeDFS(0, 0);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
        JFrame frame = new JFrame("DFS Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Main mainPanel = new Main();
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        frame.getContentPane().add(scrollPane);
        frame.pack();

        frame.setLocationRelativeTo(null); // Center the frame
        frame.setVisible(true);
        frame.addKeyListener(mainPanel);
    }

    private static void readGridFromFile(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        numRows = 0;
        numCols = 0;

        // Determine the number of rows and columns
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            numRows++;
            numCols = Math.max(numCols, line.length());
        }

        // Reset scanner to read from the beginning of the file
        scanner.close();
        scanner = new Scanner(new File(filename));

        // Initialize grid and visited arrays
        grid = new char[numRows][numCols];
        visited = new boolean[numRows][numCols];

        // Populate the grid with characters from the file
        for (int i = 0; i < numRows; i++) {
            String line = scanner.nextLine();
            for (int j = 0; j < line.length(); j++) {
                grid[i][j] = line.charAt(j);
            }
        }

        scanner.close();
    }

    private static void iterativeDFS(int startRow, int startCol) {
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{startRow, startCol});

        while (!stack.isEmpty() && treasureFoundCount < TREASURE_LIMIT) {
            int[] current = stack.pop();
            int row = current[0];
            int col = current[1];

            // Check if cell is out of bounds or already visited
            if (row < 0 || col < 0 || row >= numRows || col >= numCols || visited[row][col] || grid[row][col] == 'X') {
                continue;
            }

            // Mark the current cell as visited
            visited[row][col] = true;
            path.push(new int[]{row, col});
            System.out.println("Visiting cell (" + row + ", " + col + "): " + grid[row][col]);

            // If the current cell contains 'G', reset visited cells and continue
            if (grid[row][col] == 'G') {
                System.out.println("Treasure found at: (" + row + ", " + col + ")");
                treasureFoundCount++;
                resetVisitedCells();
                grid[row][col] = 'F';
                visited[row][col] = true;
                continue;
            }

            // Push neighbors onto the stack
            stack.push(new int[]{row + 1, col}); // Down
            stack.push(new int[]{row - 1, col}); // Up
            stack.push(new int[]{row, col + 1}); // Right
            stack.push(new int[]{row, col - 1}); // Left

            // Repaint the panel
            SwingUtilities.invokeLater(() -> {
                Main.getInstance().repaint();
                try {
                    Thread.sleep(200); // Add delay for visualization
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private static void resetVisitedCells() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                visited[i][j] = false;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                int x = j * CELL_SIZE;
                int y = i * CELL_SIZE;

                // Draw grid cell
                g.setColor(Color.BLACK);
                g.drawRect(x, y, CELL_SIZE, CELL_SIZE);

                // Draw cell content
                switch (grid[i][j]) {
                    case 'X':
                        g.setColor(Color.BLACK);
                        g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                        break;
                    case '.':
                        g.setColor(Color.WHITE);
                        g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                        break;
                    case 'F':
                        g.setColor(Color.YELLOW);
                        g.fillOval(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);
                        break;
                    default:
                        break;
                }

                // Highlight visited cells as red lines
                if (grid[i][j] == '.') {
                    g.setColor(Color.RED);
                    g.drawLine(x, y, x + CELL_SIZE, y + CELL_SIZE);
                    g.drawLine(x, y + CELL_SIZE, x + CELL_SIZE, y);
                }

                // Highlight current step with blue color
                if (currentStep < path.size()) {
                    int[] step = path.get(currentStep);
                    int row = step[0];
                    int col = step[1];
                    if (i == row && j == col) {
                        g.setColor(Color.BLUE);
                        g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                    }
                }
            }
        }
    }

    public static Main getInstance() {
        return new Main();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (currentStep < path.size() - 1) {
                currentStep++;
                int[] step = path.get(currentStep);
                int row = step[0];
                int col = step[1];
                System.out.println("Step " + currentStep + ": Move to cell (" + row + ", " + col + ")");
                Main.getInstance().repaint();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (currentStep > 0) {
                currentStep--;
                int[] step = path.get(currentStep);
                int row = step[0];
                int col = step[1];
                System.out.println("Step " + currentStep + ": Move to cell (" + row + ", " + col + ")");
                Main.getInstance().repaint();
            }
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {
    }


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
    }
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
        private int lastMoveDirection = -1; // Initialize with an invalid direction

        public DynamicObstacle(Location location, int movementRange) {
            super(location);
            this.movementRange = movementRange;
        }

        @Override
        public boolean canPassThrough() {
            return false;
        }

        // Method to move dynamic obstacle
        public void move() {
            int moveDirection;

            // Choose a random direction different from the last move direction
            do {
                moveDirection = (int) (Math.random() * 4);
            } while (moveDirection == lastMoveDirection);

            // Update the cell value in the grid before moving
            updateGridCell(location, '.');

            switch (moveDirection) {
                case 0: // Move up
                    if (location.getX() - 1 >= 0) {
                        location = new Location(location.getX() - 1, location.getY());
                        System.out.println("Dynamic obstacle moved up.");
                    } else {
                        location = new Location(location.getX() + 1, location.getY()); // Move in opposite direction
                        System.out.println("Dynamic obstacle exceeded movement range and moved down.");
                    }
                    break;
                case 1: // Move down
                    if (location.getX() + 1 < Main.numRows) {
                        location = new Location(location.getX() + 1, location.getY());
                        System.out.println("Dynamic obstacle moved down.");
                    } else {
                        location = new Location(location.getX() - 1, location.getY()); // Move in opposite direction
                        System.out.println("Dynamic obstacle exceeded movement range and moved up.");
                    }
                    break;
                case 2: // Move left
                    if (location.getY() - 1 >= 0) {
                        location = new Location(location.getX(), location.getY() - 1);
                        System.out.println("Dynamic obstacle moved left.");
                    } else {
                        location = new Location(location.getX(), location.getY() + 1); // Move in opposite direction
                        System.out.println("Dynamic obstacle exceeded movement range and moved right.");
                    }
                    break;
                case 3: // Move right
                    if (location.getY() + 1 < Main.numCols) {
                        location = new Location(location.getX(), location.getY() + 1);
                        System.out.println("Dynamic obstacle moved right.");
                    } else {
                        location = new Location(location.getX(), location.getY() - 1); // Move in opposite direction
                        System.out.println("Dynamic obstacle exceeded movement range and moved left.");
                    }
                    break;
                default:
                    break;
            }

            // Update the cell value in the grid after moving
            updateGridCell(location, 'X');

            lastMoveDirection = moveDirection; // Update last move direction
        }

        // Method to update the cell value in the grid
        private void updateGridCell(Location location, char value) {
            Main.grid[location.getX()][location.getY()] = value;
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
}



