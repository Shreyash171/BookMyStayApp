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
}

class Inventory {
    private Map<String, Integer> roomAvailability;

    public Inventory() {
        roomAvailability = new HashMap<>();
    }

    public void addRoomType(String roomType, int quantity) {
        roomAvailability.put(roomType, quantity);
    }

    public boolean isAvailable(String roomType) {
        return roomAvailability.getOrDefault(roomType, 0) > 0;
    }

    public void decrementRoom(String roomType) {
        roomAvailability.put(roomType, roomAvailability.get(roomType) - 1);
    }

    public int getAvailability(String roomType) {
        return roomAvailability.getOrDefault(roomType, 0);
    }
}

class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    public void submitRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation pollRequest() {
        return queue.poll();
    }

    public boolean hasPendingRequests() {
        return !queue.isEmpty();
    }
}

class AllocationService {
    private Inventory inventory;
    private Map<String, Set<String>> allocatedRooms;
    private Random random;

    public AllocationService(Inventory inventory) {
        this.inventory = inventory;
        this.allocatedRooms = new HashMap<>();
        this.random = new Random();
    }

    public String allocateRoom(Reservation r) {
        String type = r.getRoomType();
        if (!inventory.isAvailable(type)) {
            return "No available rooms for " + type;
        }

        allocatedRooms.putIfAbsent(type, new HashSet<>());
        String roomId;
        do {
            roomId = type.substring(0,3).toUpperCase() + "-" + (100 + random.nextInt(900));
        } while (allocatedRooms.get(type).contains(roomId));

        allocatedRooms.get(type).add(roomId);
        inventory.decrementRoom(type);

        return "Reservation confirmed for " + r.getGuestName() + ", Room ID: " + roomId;
    }
}

public class UseCase6RoomAllocationService {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        inventory.addRoomType("Standard", 2);
        inventory.addRoomType("Deluxe", 1);

        BookingRequestQueue queue = new BookingRequestQueue();
        queue.submitRequest(new Reservation("Alice", "Standard"));
        queue.submitRequest(new Reservation("Bob", "Deluxe"));
        queue.submitRequest(new Reservation("Charlie", "Standard"));
        queue.submitRequest(new Reservation("David", "Standard"));

        AllocationService allocator = new AllocationService(inventory);

        while (queue.hasPendingRequests()) {
            Reservation r = queue.pollRequest();
            System.out.println(allocator.allocateRoom(r));
        }

        System.out.println("\nFinal Inventory:");
        System.out.println("Standard: " + inventory.getAvailability("Standard"));
        System.out.println("Deluxe: " + inventory.getAvailability("Deluxe"));
    }
}