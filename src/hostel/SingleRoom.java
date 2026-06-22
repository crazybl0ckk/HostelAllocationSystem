package hostel;

/**
 * Demonstrates INHERITANCE: SingleRoom extends abstract Room.
 */
public class SingleRoom extends Room {
    private boolean hasAirConditioning;

    public SingleRoom(String roomNumber, double baseFee, boolean hasAirConditioning) {
        super(roomNumber, baseFee, 1); // Capacity for single room is 1
        this.hasAirConditioning = hasAirConditioning;
    }

    /**
     * Demonstrates POLYMORPHISM & CALCULATION METHOD: Overriding fee logic.
     */
    @Override
    public double calculateTotalFee(int durationMonths) {
        double total = baseFee * durationMonths;
        if (hasAirConditioning) {
            total += (50.0 * durationMonths); // Additional AC surcharge per month
        }
        return total;
    }

    @Override
    public String getRoomType() {
        return "Single Room" + (hasAirConditioning ? " (AC)" : " (No AC)");
    }
}