package org.example.controller;

import org.example.config.DatabaseConnection;
import org.example.model.User;

import java.sql.*;

public class UserOperations {
    // Method to search products by name
    public static void searchProductByName(String productName) throws SQLException {
        String query = "SELECT * FROM Products WHERE ProductName LIKE ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, "%" + productName + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("ProductID: " + rs.getInt("ProductID"));
                    System.out.println("ProductName: " + rs.getString("ProductName"));
                    System.out.println("Price: " + rs.getBigDecimal("Price"));
                    System.out.println("StockCount: " + rs.getInt("StockCount"));
                    System.out.println("Description: " + rs.getString("Description"));
                    System.out.println("Category: " + rs.getString("Category"));
                    System.out.println("Delivery Frequency: " + rs.getString("deliveryFrequency"));
                    System.out.println("----");
                }
            }
        }
    }

    // Method to show all products
    public static void showAllProducts() throws SQLException {
        String query = "SELECT * FROM Products";

        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                System.out.println("ProductID: " + rs.getInt("ProductID"));
                System.out.println("ProductName: " + rs.getString("ProductName"));
                System.out.println("Price: " + rs.getBigDecimal("Price"));
                System.out.println("StockCount: " + rs.getInt("StockCount"));
                System.out.println("Description: " + rs.getString("Description"));
                System.out.println("Category: " + rs.getString("Category"));
                System.out.println("Delivery Frequency: " + rs.getString("deliveryFrequency"));
                System.out.println("----");
            }
        }
    }

    // Method to view order history
    public static void viewOrderHistory(User user) throws SQLException {
        String query = "SELECT * FROM Orders WHERE UserID = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, user.getUserId());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("OrderID: " + rs.getInt("OrderID"));
                    System.out.println("SubscriptionID: " + rs.getInt("SubscriptionID"));
                    System.out.println("OrderDate: " + rs.getDate("OrderDate"));
                    System.out.println("StartDate: " + rs.getDate("StartDate"));
                    System.out.println("EndDate: " + rs.getDate("EndDate"));
                    System.out.println("OrderStatus: " + rs.getString("OrderStatus"));
                    System.out.println("----");
                }
            }
        }
    }

    // Method to view user profile including subscriptions
    public static void viewUserProfile(User user) throws SQLException {
        System.out.println("User Profile:");
        System.out.println("UserID: " + user.getUserId());
        System.out.println("Name: " + user.getFirstName() + " " + user.getLastName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Address: " + user.getAddress());

        String query = "SELECT * FROM Subscriptions WHERE SubscriptionID IN (SELECT SubscriptionID FROM Orders WHERE UserID = ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, user.getUserId());

            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("Subscriptions:");
                while (rs.next()) {
                    System.out.println("Subscription ID: " + rs.getInt("SubscriptionID"));
                    System.out.println("Name: " + rs.getString("SubscriptionName"));
                    System.out.println("Description: " + rs.getString("SubscriptionDescription"));
                    System.out.println("Category: " + rs.getString("SubscriptionCategory"));
                    System.out.println("Status: " + rs.getString("SubscriptionStatus"));
                    System.out.println("Count: " + rs.getInt("SubscriptionCount"));
                    System.out.println("----");
                }
            }
        }
    }

    // Function to show products by category
    public static void showProductsByCategory(String category) throws SQLException {
        String query = "SELECT * FROM Products WHERE Category = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, category);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("ProductID: " + rs.getInt("ProductID"));
                    System.out.println("ProductName: " + rs.getString("ProductName"));
                    System.out.println("Price: " + rs.getBigDecimal("Price"));
                    System.out.println("StockCount: " + rs.getInt("StockCount"));
                    System.out.println("Description: " + rs.getString("Description"));
                    System.out.println("Category: " + rs.getString("Category"));
                    System.out.println("Delivery Frequency: " + rs.getString("deliveryFrequency"));
                    System.out.println("----");
                }
            }
        }
    }

    // Function to view the user's cart
    public static void viewCart(User user) throws SQLException {
        String query = "SELECT c.ID, s.SubscriptionName, s.SubscriptionDescription, s.SubscriptionCategory " +
                "FROM Cart c JOIN Subscriptions s ON c.SubscriptionID = s.SubscriptionID " +
                "WHERE c.UserID = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, user.getUserId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("CartID: " + rs.getInt("ID"));
                    System.out.println("SubscriptionName: " + rs.getString("SubscriptionName"));
                    System.out.println("SubscriptionDescription: " + rs.getString("SubscriptionDescription"));
                    System.out.println("SubscriptionCategory: " + rs.getString("SubscriptionCategory"));
                    System.out.println("----");
                }
            }
        }
    }
}
