import java.util.*;

// Reservation class (core booking entity)
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private double price;

    public Reservation(String reservationId, String guestName, String roomType, double price) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.price = price;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType +
                ", Price: ₹" + price;
    }
}

// Booking History (stores confirmed reservations)
class BookingHistory {

    // List preserves insertion order (chronological tracking)
    private List<Reservation> confirmedBookings;

    public BookingHistory() {
        confirmedBookings = new ArrayList<>();
    }

    // Add confirmed reservation
    public void addReservation(Reservation reservation) {
        confirmedBookings.add(reservation);
    }

    // Retrieve all reservations
    public List<Reservation> getAllReservations() {
        return confirmedBookings;
    }
}

// Booking Report Service (separate from storage)
class BookingReportService {

    // Display all bookings
    public void displayAllBookings(List<Reservation> reservations) {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    // Generate summary report
    public void generateSummaryReport(List<Reservation> reservations) {
        int totalBookings = reservations.size();
        double totalRevenue = 0.0;

        for (Reservation r : reservations) {
            totalRevenue += r.getPrice();
        }

        System.out.println("\n--- Booking Summary Report ---");
        System.out.println("Total Bookings: " + totalBookings);
        System.out.println("Total Revenue: ₹" + totalRevenue);
    }
}

// Main class
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        // Initialize booking history
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings
        Reservation r1 = new Reservation("R101", "Alice", "Deluxe", 3000);
        Reservation r2 = new Reservation("R102", "Bob", "Suite", 5000);
        Reservation r3 = new Reservation("R103", "Charlie", "Standard", 2000);

        // Add to booking history (in order)
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Admin accesses reports
        BookingReportService reportService = new BookingReportService();

        // Display all bookings
        reportService.displayAllBookings(history.getAllReservations());

        // Generate summary
        reportService.generateSummaryReport(history.getAllReservations());
    }
}