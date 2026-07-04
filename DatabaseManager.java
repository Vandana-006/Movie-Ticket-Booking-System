import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/MovieBookingDB?useSSL=false&allowPublicKeyRetrieval=true"; 
    private static final String USER = "root"; 
   
    private static final String PASSWORD = "Vand@2006"; 

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Fetch all available movies from database
    public List<Movie> getAllMovies() throws SQLException {
        List<Movie> list = new ArrayList<>();
        String query = "SELECT * FROM movies";
        try (Connection conn = getConnection(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new Movie(rs.getInt("movie_id"), rs.getString("title"), rs.getString("genre"), rs.getDouble("price")));
            }
        }
        return list;
    }

    // Insert a new Booking
    public boolean insertBooking(int movieId, String customerName, int seats, double total) throws Exception {
        if (customerName.trim().isEmpty()) {
            throw new Exception("Validation Error: Customer name cannot be empty.");
        }
        if (seats <= 0) {
            throw new Exception("Validation Error: Seats must be greater than 0.");
        }
        
        String query = "INSERT INTO bookings (movie_id, customer_name, seats_booked, total_amount) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, movieId);
            pstmt.setString(2, customerName);
            pstmt.setInt(3, seats);
            pstmt.setDouble(4, total);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Cancel Booking by ID
    public boolean cancelBooking(int bookingId) {
        String query = "DELETE FROM bookings WHERE booking_id = ?";
        try (Connection conn = getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookingId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Generate plain-text layout for the Booking Summary
    public String getBookingSummary() {
        StringBuilder summary = new StringBuilder("ID\tName\tMovie\tSeats\tTotal Price\n");
        summary.append("----------------------------------------------------------------------------------------\n");
        String query = "SELECT b.booking_id, b.customer_name, m.title, b.seats_booked, b.total_amount " +
                       "FROM bookings b JOIN movies m ON b.movie_id = m.movie_id";
        try (Connection conn = getConnection(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                summary.append(rs.getInt("booking_id")).append("\t")
                       .append(rs.getString("customer_name")).append("\t")
                       .append(rs.getString("title")).append("\t")
                       .append(rs.getInt("seats_booked")).append("\t")
                       .append("Rs. ").append(rs.getDouble("total_amount")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return summary.toString();
    }
}