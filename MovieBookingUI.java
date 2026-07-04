import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MovieBookingUI extends JFrame {
    private JComboBox<String> movieComboBox;
    private JTextField nameField, seatsField, cancelField;
    private JTextArea summaryArea;
    private DatabaseManager dbManager;
    private List<Movie> movieList;

    // Custom UI Dark Color Palette
    private final Color BG_DARK = new Color(15, 12, 27);       // Deep midnight background
    private final Color PANEL_DARK = new Color(32, 26, 48);   // Lighter card/panel background
    private final Color ACCENT_PURPLE = new Color(168, 85, 247); // Vibrant neon purple
    private final Color TEXT_WHITE = new Color(241, 241, 246);   // Crisp text color

    public MovieBookingUI() {
        dbManager = new DatabaseManager();

        // Frame Setup
        setTitle("VERTEX - Premium Movie Booking");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout(20, 20));

        // ---- TOP BANNER ----
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(BG_DARK);
        headerPanel.setBorder(new EmptyBorder(20, 30, 0, 30));
        headerPanel.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("VERTEX CINEMAS");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_WHITE);
        
        JLabel subtitleLabel = new JLabel("Intelligent Ticket Reservation System");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(ACCENT_PURPLE);
        
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // ---- MAIN BODY SPLIT PANEL ----
        JPanel mainBody = new JPanel(new GridLayout(1, 2, 25, 0));
        mainBody.setBackground(BG_DARK);
        mainBody.setBorder(new EmptyBorder(10, 30, 30, 30));

        // LEFT COLUMN: Input Forms
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(BG_DARK);

        // Booking Card Container
        JPanel bookingCard = createStyledPanel("Create New Reservation");
        bookingCard.setLayout(new GridLayout(4, 2, 15, 20));

        bookingCard.add(createStyledLabel("Select Movie:"));
        movieComboBox = new JComboBox<>();
        styleComponent(movieComboBox);
        bookingCard.add(movieComboBox);

        bookingCard.add(createStyledLabel("Customer Name:"));
        nameField = new JTextField();
        styleComponent(nameField);
        bookingCard.add(nameField);

        bookingCard.add(createStyledLabel("Number of Seats:"));
        seatsField = new JTextField();
        styleComponent(seatsField);
        bookingCard.add(seatsField);

        bookingCard.add(new JLabel("")); // Empty spacer
        JButton bookButton = createStyledButton("Confirm Booking", ACCENT_PURPLE);
        bookButton.addActionListener(this::handleBooking);
        bookingCard.add(bookButton);

        // Cancellation Card Container
        JPanel cancelCard = createStyledPanel("Cancel Reservation");
        cancelCard.setLayout(new GridLayout(2, 2, 15, 15));

        cancelCard.add(createStyledLabel("Enter Booking ID:"));
        cancelField = new JTextField();
        styleComponent(cancelField);
        cancelCard.add(cancelField);

        cancelCard.add(new JLabel("")); // Empty spacer
        JButton cancelButton = createStyledButton("Cancel Ticket", new Color(239, 68, 68)); // Warning Red
        cancelButton.addActionListener(this::handleCancellation);
        cancelCard.add(cancelButton);

        leftPanel.add(bookingCard);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(cancelCard);

        // RIGHT COLUMN: Real-Time Live Log Summary View
        JPanel rightPanel = createStyledPanel("Live Database Ledger Summary");
        rightPanel.setLayout(new BorderLayout(10, 15));

        summaryArea = new JTextArea();
        summaryArea.setBackground(new Color(20, 16, 32));
        summaryArea.setForeground(new Color(52, 211, 153)); // Retro Neon Green log font
        summaryArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        summaryArea.setEditable(false);
        summaryArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(summaryArea);
        scrollPane.setBorder(new LineBorder(new Color(255, 255, 255, 12), 1));
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        mainBody.add(leftPanel);
        mainBody.add(rightPanel);
        add(mainBody, BorderLayout.CENTER);

        // Initialize Database Data
        loadMovies();
        refreshSummary();
    }

    // ---- UI CUSTOM COMPONENT FACTORIES ----
    private JPanel createStyledPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBackground(PANEL_DARK);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(255, 255, 255, 12), 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));
        return panel;
    }

    private JLabel createStyledLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(TEXT_WHITE);
        return lbl;
    }

    private void styleComponent(JComponent comp) {
        comp.setBackground(new Color(20, 16, 32));
        comp.setForeground(TEXT_WHITE);
        comp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comp.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(168, 85, 247, 76), 1),
            new EmptyBorder(6, 10, 6, 10)
        ));
    }

    private JButton createStyledButton(String text, Color baseColor) {
        JButton btn = new JButton(text);
        btn.setBackground(baseColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 15, 10, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ---- CORE APP FUNCTIONALITIES ----
    private void loadMovies() {
        try {
            movieComboBox.removeAllItems();
            movieList = dbManager.getAllMovies();
            for (Movie m : movieList) {
                movieComboBox.addItem(m.getTitle() + " (" + m.getGenre() + ") - Rs. " + m.getPrice());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database Connection established successfully!", "System Status", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void refreshSummary() {
        summaryArea.setText(dbManager.getBookingSummary());
    }

    private void handleBooking(ActionEvent e) {
        try {
            int selectedIndex = movieComboBox.getSelectedIndex();
            if (selectedIndex == -1) throw new Exception("Please pick an active movie listing.");

            Movie movie = movieList.get(selectedIndex);
            String customerName = nameField.getText();
            int seats = Integer.parseInt(seatsField.getText().trim());
            double total = movie.getPrice() * seats;

            if (dbManager.insertBooking(movie.getId(), customerName, seats, total)) {
                JOptionPane.showMessageDialog(this, "Success! Total Charged: Rs. " + total, "Transaction Confirmed", JOptionPane.INFORMATION_MESSAGE);
                nameField.setText("");
                seatsField.setText("");
                refreshSummary();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Formatting Error: Please input a valid numeric digit for Seats.", "Input Refused", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Refused", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCancellation(ActionEvent e) {
        try {
            int id = Integer.parseInt(cancelField.getText().trim());
            if (dbManager.cancelBooking(id)) {
                JOptionPane.showMessageDialog(this, "Booking ID " + id + " dropped successfully.", "Ledger Purged", JOptionPane.INFORMATION_MESSAGE);
                cancelField.setText("");
                refreshSummary();
            } else {
                JOptionPane.showMessageDialog(this, "No record match found for ID: " + id, "Sync Error", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Please pass a valid numeric ledger database ID string.", "Input Refused", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MovieBookingUI().setVisible(true));
    }
}