import java.util.*;

class Room {
    private String type;
    private String amenities;
    private double price;

    public Room(String type, String amenities, double price) {
        this.type = type;
        this.amenities = amenities;
        this.price = price;
    }

    public String getType() { return type; }
    public String getAmenities() { return amenities; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return "Room Type: " + type + ", Amenities: " + amenities + ", Price: $" + price;
    }
}

class Inventory {
    private Map<String, Integer> roomAvailability;
    private Map<String, Room> rooms;

    public Inventory() {
        roomAvailability = new HashMap<>();
        rooms = new HashMap<>();
    }

    public void addRoom(Room room, int quantity) {
        rooms.put(room.getType(), room);
        roomAvailability.put(room.getType(), quantity);
    }

    public int getAvailability(String roomType) {
        return roomAvailability.getOrDefault(roomType, 0);
    }

    public Room getRoom(String roomType) {
        return rooms.get(roomType);
    }

    public Set<String> getAllRoomTypes() {
        return rooms.keySet();
    }
}

class SearchService {
    private Inventory inventory;

    public SearchService(Inventory inventory) {
        this.inventory = inventory;
    }

    public List<Room> searchAvailableRooms() {
        List<Room> availableRooms = new ArrayList<>();
        for (String type : inventory.getAllRoomTypes()) {
            if (inventory.getAvailability(type) > 0) {
                availableRooms.add(inventory.getRoom(type));
            }
        }
        return availableRooms;
    }
}

public class UseCase4RoomSearch {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        inventory.addRoom(new Room("Standard", "WiFi, TV", 100), 5);
        inventory.addRoom(new Room("Deluxe", "WiFi, TV, Mini Bar", 200), 2);
        inventory.addRoom(new Room("Suite", "WiFi, TV, Mini Bar, Kitchen", 500), 0);

        SearchService searchService = new SearchService(inventory);
        List<Room> availableRooms = searchService.searchAvailableRooms();

        System.out.println("Available Rooms:");
        for (Room room : availableRooms) {
            System.out.println(room);
        }
    }
}