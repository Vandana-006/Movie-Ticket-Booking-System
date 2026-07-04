public class Booking extends AbstractItem {
    private int movieId;
    private String customerName;
    private int seatsBooked;
    private double totalAmount;

    public Booking(int id, int movieId, String customerName, int seatsBooked, double totalAmount) {
        this.id = id;
        this.movieId = movieId;
        this.customerName = customerName;
        this.seatsBooked = seatsBooked;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public int getMovieId() { return movieId; }
    public String getCustomerName() { return customerName; }
    public int getSeatsBooked() { return seatsBooked; }
    public double getTotalAmount() { return totalAmount; }
}