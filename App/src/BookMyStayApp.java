import java.util.*;

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation class
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType;
    }
}

// Validator Class
class InvalidBookingValidator {

    private Set<String> validRoomTypes;

    public InvalidBookingValidator() {
        validRoomTypes = new HashSet<>(Arrays.asList("Standard", "Deluxe", "Suite"));
    }

    // Validate booking input
    public void validate(String guestName, String roomType, Map<String, Integer> inventory)
            throws InvalidBookingException {

        // Fail-fast checks
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        if (!validRoomTypes.contains(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException("Room type not available in inventory.");
        }

        if (inventory.get(roomType) <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }
    }
}

// Booking Service
class BookingService {

    private Map<String, Integer> inventory;
    private int counter = 100;

    public BookingService() {
        inventory = new HashMap<>();
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 0); // intentionally zero to test validation
    }

    public Reservation createBooking(String guestName, String roomType,
                                     InvalidBookingValidator validator)
            throws InvalidBookingException {

        // Validate before processing (fail-fast)
        validator.validate(guestName, roomType, inventory);

        // Safe state update
        inventory.put(roomType, inventory.get(roomType) - 1);

        // Generate reservation
        String reservationId = "R" + (++counter);
        return new Reservation(reservationId, guestName, roomType);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " Rooms: " + entry.getValue());
        }
    }
}

// Main Class
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        BookingService bookingService = new BookingService();
        InvalidBookingValidator validator = new InvalidBookingValidator();

        bookingService.displayInventory();

        // Test cases (valid + invalid scenarios)
        String[][] testInputs = {
                {"Alice", "Deluxe"},     // valid
                {"", "Standard"},        // invalid name
                {"Bob", "Luxury"},       // invalid room type
                {"Charlie", "Suite"}     // no availability
        };

        for (String[] input : testInputs) {
            try {
                System.out.println("\nProcessing booking for: " + input[0] + ", Room: " + input[1]);

                Reservation reservation = bookingService.createBooking(
                        input[0], input[1], validator
                );

                System.out.println("Booking Successful: " + reservation);

            } catch (InvalidBookingException e) {
                // Graceful failure handling
                System.out.println("Booking Failed: " + e.getMessage());
            }
        }

        bookingService.displayInventory();
    }
}