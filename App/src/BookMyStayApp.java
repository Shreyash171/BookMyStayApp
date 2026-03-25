import java.util.*;

// Reservation class
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private boolean isCancelled;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isCancelled = false;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancel() {
        this.isCancelled = true;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType +
                ", Room ID: " + roomId +
                ", Status: " + (isCancelled ? "Cancelled" : "Confirmed");
    }
}

// Booking Service (handles booking + inventory)
class BookingService {

    private Map<String, Integer> inventory;
    private Map<String, Stack<String>> availableRooms;
    private Map<String, Reservation> reservations;
    private int counter = 100;

    public BookingService() {
        inventory = new HashMap<>();
        availableRooms = new HashMap<>();
        reservations = new HashMap<>();

        // Initialize inventory and room IDs
        initializeRooms("Standard", 2);
        initializeRooms("Deluxe", 2);
    }

    private void initializeRooms(String type, int count) {
        inventory.put(type, count);
        Stack<String> rooms = new Stack<>();

        for (int i = 1; i <= count; i++) {
            rooms.push(type.charAt(0) + String.valueOf(i)); // e.g., S1, S2
        }

        availableRooms.put(type, rooms);
    }

    // Create booking
    public Reservation bookRoom(String guestName, String roomType) {
        if (!inventory.containsKey(roomType) || inventory.get(roomType) <= 0) {
            System.out.println("Booking failed: No rooms available for " + roomType);
            return null;
        }

        // Allocate room (LIFO)
        String roomId = availableRooms.get(roomType).pop();
        inventory.put(roomType, inventory.get(roomType) - 1);

        String reservationId = "R" + (++counter);
        Reservation reservation = new Reservation(reservationId, guestName, roomType, roomId);

        reservations.put(reservationId, reservation);

        System.out.println("Booking Successful: " + reservation);
        return reservation;
    }

    // Cancel booking (rollback logic)
    public void cancelBooking(String reservationId) {

        System.out.println("\nAttempting cancellation for: " + reservationId);

        // Validate existence
        if (!reservations.containsKey(reservationId)) {
            System.out.println("Cancellation Failed: Reservation does not exist.");
            return;
        }

        Reservation reservation = reservations.get(reservationId);

        // Prevent duplicate cancellation
        if (reservation.isCancelled()) {
            System.out.println("Cancellation Failed: Already cancelled.");
            return;
        }

        String roomType = reservation.getRoomType();
        String roomId = reservation.getRoomId();

        // Rollback using stack (LIFO)
        availableRooms.get(roomType).push(roomId);

        // Restore inventory
        inventory.put(roomType, inventory.get(roomType) + 1);

        // Mark as cancelled
        reservation.cancel();

        System.out.println("Cancellation Successful: " + reservationId);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " Rooms Available: " + inventory.get(type));
        }
    }

    public void displayReservations() {
        System.out.println("\nAll Reservations:");
        for (Reservation r : reservations.values()) {
            System.out.println(r);
        }
    }
}

// Main class
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        BookingService service = new BookingService();

        service.displayInventory();

        // Create bookings
        Reservation r1 = service.bookRoom("Alice", "Standard");
        Reservation r2 = service.bookRoom("Bob", "Standard");

        service.displayInventory();

        // Cancel a booking
        if (r1 != null) {
            service.cancelBooking(r1.getReservationId());
        }

        service.displayInventory();

        // Try invalid cancellation
        service.cancelBooking("R999"); // non-existent
        service.cancelBooking(r1.getReservationId()); // duplicate cancellation

        service.displayReservations();
    }
}