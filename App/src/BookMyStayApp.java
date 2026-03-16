import java.util.HashMap;
import java.util.Map;

/**
 * Book My Stay App
 * Use Case 3: Centralized Room Inventory Management
 *
 * Demonstrates how a HashMap can be used to manage
 * room availability in a centralized inventory system.
 *
 * @author Student
 * @version 3.1
 */

// Inventory management class
class RoomInventory {

    private HashMap<String, Integer> inventory;

    // Constructor initializes inventory
    public RoomInventory() {
        inventory = new HashMap<>();

        // Register room types with availability
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Method to get availability of a room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Method to update room availability
    public void updateAvailability(String roomType, int count) {
        inventory.put(roomType, count);
    }

    // Display current inventory
    public void displayInventory() {
        System.out.println("Current Room Inventory:");

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue() + " rooms available");
        }
    }
}

// Main application class
public class UseCase3InventorySetup {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("      Book My Stay App v3.1      ");
        System.out.println(" Centralized Room Inventory      ");
        System.out.println("=================================\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Display initial inventory
        inventory.displayInventory();

        // Example update
        System.out.println("\nUpdating availability for Single Room...\n");
        inventory.updateAvailability("Single Room", 4);

        // Display updated inventory
        inventory.displayInventory();
    }
}