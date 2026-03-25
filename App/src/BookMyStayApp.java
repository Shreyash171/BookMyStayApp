import java.io.*;
import java.util.*;

// Reservation class (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType;
    }
}

// Wrapper class for persistence (entire system snapshot)
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    List<Reservation> bookingHistory;

    public SystemState(Map<String, Integer> inventory, List<Reservation> bookingHistory) {
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.ser";

    // Save state to file
    public void save(SystemState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(state);
            System.out.println("\nSystem state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    // Load state from file
    public SystemState load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            System.out.println("System state loaded successfully.");
            return (SystemState) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No previous data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading state. Starting with safe defaults.");
        }
        return null;
    }
}

// Booking Service
class BookingService {

    private Map<String, Integer> inventory;
    private List<Reservation> bookingHistory;
    private int counter = 100;

    public BookingService() {
        inventory = new HashMap<>();
        bookingHistory = new ArrayList<>();

        // Default inventory
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
    }

    // Restore state
    public void restore(SystemState state) {
        if (state != null) {
            this.inventory = state.inventory;
            this.bookingHistory = state.bookingHistory;
            System.out.println("System state restored.");
        }
    }

    // Create booking
    public void book(String guestName, String roomType) {
        if (!inventory.containsKey(roomType) || inventory.get(roomType) <= 0) {
            System.out.println("Booking failed for " + guestName + ": No rooms available.");
            return;
        }

        inventory.put(roomType, inventory.get(roomType) - 1);
        String reservationId = "R" + (++counter);

        Reservation r = new Reservation(reservationId, guestName, roomType);
        bookingHistory.add(r);

        System.out.println("Booking successful: " + r);
    }

    // Get current state snapshot
    public SystemState getState() {
        return new SystemState(inventory, bookingHistory);
    }

    public void displayState() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        System.out.println("\nBooking History:");
        for (Reservation r : bookingHistory) {
            System.out.println(r);
        }
    }
}

// Main Class
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        PersistenceService persistenceService = new PersistenceService();
        BookingService bookingService = new BookingService();

        // Load previous state (if exists)
        SystemState loadedState = persistenceService.load();
        bookingService.restore(loadedState);

        // Simulate operations
        bookingService.book("Alice", "Standard");
        bookingService.book("Bob", "Deluxe");
        bookingService.book("Charlie", "Standard");

        bookingService.displayState();

        // Save state before shutdown
        persistenceService.save(bookingService.getState());

        System.out.println("\n--- Restart the program to see recovery in action ---");
    }
}