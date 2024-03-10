package org.example;
//yazlabinho
import java.util.*;

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

// Character class
class Character {
    private String id;
    private Location location;
    private Queue<Treasure> discovered = new LinkedList<>();

    public Character(String id, Location location, Queue<Treasure> discovered) {
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
    // hareket metodları
    public void moveUp() {
        location = new Location(location.getX(), location.getY() - 1);
    }

    public void moveDown() {
        location = new Location(location.getX(), location.getY() + 1);
    }

    public void moveLeft() {
        location = new Location(location.getX() - 1, location.getY());
    }

    public void moveRight() {
        location = new Location(location.getX() + 1, location.getY());
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

// DynamicObstacle class representing dynamic obstacles (birds, bees, etc.)
class DynamicObstacle extends Obstacle {
    private int movementRange; // Movement range of dynamic obstacle

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
        // Code implementation goes here
    }
}
