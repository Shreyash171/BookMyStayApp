/**
 * Book My Stay App
 * Use Case 2: Basic Room Types & Static Availability
 *
 * Demonstrates abstraction, inheritance, and polymorphism
 * using different types of hotel rooms.
 *
 * @author Student
 * @version 2.1
 */

// Abstract Room class
abstract class Room {

    protected String roomType;
    protected int beds;
    protected double size;
    protected double price;

    public Room(String roomType, int beds, double size, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public void displayRoomDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Room Size: " + size + " sq.ft");
        System.out.println("Price per Night: $" + price);
    }
}

// Single Room class
class SingleRoom extends Room {

    public SingleRoom() {
        super("Single Room", 1, 200, 100);
    }
}

// Double Room class
class DoubleRoom extends Room {

    public DoubleRoom() {
        super("Double Room", 2, 350, 180);
    }
}

// Suite Room class
class SuiteRoom extends Room {

    public SuiteRoom() {
        super("Suite Room", 3, 600, 350);
    }
}

// Main Application Class
public class UseCase2RoomInitialization {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("      Book My Stay App v2.1      ");
        System.out.println(" Room Types & Static Availability");
        System.out.println("=================================\n");

        // Creating room objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static availability variables
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        // Display Single Room
        single.displayRoomDetails();
        System.out.println("Available Rooms: " + singleAvailable);
        System.out.println("---------------------------------\n");

        // Display Double Room
        doubleRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + doubleAvailable);
        System.out.println("---------------------------------\n");

        // Display Suite Room
        suite.displayRoomDetails();
        System.out.println("Available Rooms: " + suiteAvailable);
        System.out.println("---------------------------------\n");

        System.out.println("Thank you for exploring Book My Stay!");
    }
}