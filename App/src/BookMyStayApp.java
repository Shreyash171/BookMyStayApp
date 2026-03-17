import java.util.*;

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }

    @Override
    public String toString() {
        return "Reservation{" + "guestName='" + guestName + '\'' + ", roomType='" + roomType + '\'' + '}';
    }
}

class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    public void submitRequest(Reservation reservation) {
        queue.offer(reservation);
    }

    public Reservation pollRequest() {
        return queue.poll();
    }

    public boolean hasPendingRequests() {
        return !queue.isEmpty();
    }

    public int pendingRequestsCount() {
        return queue.size();
    }

    public void printQueue() {
        System.out.println("Current Booking Queue:");
        for (Reservation r : queue) {
            System.out.println(r);
        }
    }
}

public class UseCase5BookingRequestQueue {
    public static void main(String[] args) {
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        bookingQueue.submitRequest(new Reservation("Alice", "Standard"));
        bookingQueue.submitRequest(new Reservation("Bob", "Deluxe"));
        bookingQueue.submitRequest(new Reservation("Charlie", "Suite"));

        bookingQueue.printQueue();

        System.out.println("\nProcessing requests in FIFO order:");
        while (bookingQueue.hasPendingRequests()) {
            Reservation r = bookingQueue.pollRequest();
            System.out.println("Processing: " + r);
        }

        System.out.println("\nPending requests after processing: " + bookingQueue.pendingRequestsCount());
    }
}