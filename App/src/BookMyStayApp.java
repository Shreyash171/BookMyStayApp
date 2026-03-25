import java.util.*;

// Booking Request
class BookingRequest {
    private String guestName;
    private String roomType;

    public BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Thread-safe Booking Processor
class ConcurrentBookingProcessor {

    private Map<String, Integer> inventory;
    private Queue<BookingRequest> bookingQueue;
    private int counter = 100;

    public ConcurrentBookingProcessor() {
        inventory = new HashMap<>();
        bookingQueue = new LinkedList<>();

        // Initialize inventory
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
    }

    // Add booking request to shared queue
    public synchronized void addRequest(BookingRequest request) {
        bookingQueue.offer(request);
        System.out.println(Thread.currentThread().getName() +
                " added request for " + request.getGuestName());
    }

    // Process booking (critical section)
    public void processBookings() {
        while (true) {
            BookingRequest request;

            // Synchronized block for safe queue access
            synchronized (this) {
                if (bookingQueue.isEmpty()) {
                    break;
                }
                request = bookingQueue.poll();
            }

            // Critical section: inventory update must be synchronized
            synchronized (this) {
                String roomType = request.getRoomType();

                if (inventory.containsKey(roomType) && inventory.get(roomType) > 0) {
                    inventory.put(roomType, inventory.get(roomType) - 1);
                    String reservationId = "R" + (++counter);

                    System.out.println(Thread.currentThread().getName() +
                            " SUCCESS -> " + request.getGuestName() +
                            " booked " + roomType +
                            " | Reservation ID: " + reservationId);
                } else {
                    System.out.println(Thread.currentThread().getName() +
                            " FAILED -> No rooms available for " +
                            request.getGuestName());
                }
            }

            // Simulate processing delay
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void displayInventory() {
        System.out.println("\nFinal Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " Rooms Left: " + entry.getValue());
        }
    }
}

// Worker Thread
class BookingWorker extends Thread {

    private ConcurrentBookingProcessor processor;

    public BookingWorker(ConcurrentBookingProcessor processor, String name) {
        super(name);
        this.processor = processor;
    }

    @Override
    public void run() {
        processor.processBookings();
    }
}

// Main Class
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        ConcurrentBookingProcessor processor = new ConcurrentBookingProcessor();

        // Simulate multiple guest requests
        processor.addRequest(new BookingRequest("Alice", "Standard"));
        processor.addRequest(new BookingRequest("Bob", "Standard"));
        processor.addRequest(new BookingRequest("Charlie", "Standard"));
        processor.addRequest(new BookingRequest("David", "Deluxe"));
        processor.addRequest(new BookingRequest("Eve", "Deluxe"));

        // Create multiple threads (simulating concurrent users)
        Thread t1 = new BookingWorker(processor, "Thread-1");
        Thread t2 = new BookingWorker(processor, "Thread-2");
        Thread t3 = new BookingWorker(processor, "Thread-3");

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for all threads to finish
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Final inventory state
        processor.displayInventory();
    }
}