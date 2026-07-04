# VERTEX - Premium Movie Ticket Booking System

A premium, interactive desktop-based Movie Ticket Booking Application engineered using Java Swing for a rich graphical experience and backed by a relational MySQL database ledger.

## 🌟 Key Features
- **Modern Dark Aesthetic:** Complete UI redesign incorporating custom deep canvas gradients, glowing accent borders, and glassmorphic card layouts.
- **Dynamic Data Sync:** Real-time synchronization fetching available listings directly from local active relational structures.
- **Transaction Ledger:** Live monospace monitoring viewport rendering transactional changes instantly upon safe record insertion or dropping.
- **Encapsulated Controls:** Full error checking arrays handling type mismatches, empty validations, and invalid seat thresholds natively.

## 🛠️ Built With
- **Language:** Java 21 / Swing AWT Framework
- **Database Engine:** MySQL Server
- **Driver Connector:** MySQL Connector/J JDBC Driver

## 🚀 Getting Started

### Prerequisites
Ensure your machine runs local database ports on `3306` and contains the required schema tables:
```sql
CREATE DATABASE MovieBookingDB;
USE MovieBookingDB;

CREATE TABLE movies (
    movie_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    genre VARCHAR(100),
    price DOUBLE NOT NULL
);

CREATE TABLE bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    movie_id INT,
    customer_name VARCHAR(255) NOT NULL,
    seats_booked INT NOT NULL,
    total_amount DOUBLE NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id)
);
