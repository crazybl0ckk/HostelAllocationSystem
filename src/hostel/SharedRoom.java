package hostel;

/**
 * Demonstrates INHERITANCE: SharedRoom extends abstract Room.
 */
public class SharedRoom extends Room {
    private int maxRoommates;

    public SharedRoom(String roomNumber, double baseFee, int maxRoommates) {
        super(roomNumber, baseFee, maxRoommates);
        this.maxRoommates = maxRoommates;
    }

    /**
     * Demonstrates POLYMORPHISM & CALCULATION METHOD: Shared utility discount logic.
     */
    @Override
    public double calculateTotalFee(int durationMonths) {
        // Shared rooms get a 10% discount on the base fee rate due to shared utility scaling
        return (baseFee * 0.9) * durationMonths;
    }

    @Override
    public String getRoomType() {
        return "Shared Room (" + maxRoommates + " Pax)";
    }
}