package org.example.controller;

import org.example.config.DatabaseConnection;
import org.example.model.User;
import org.example.validator.UserValidator;

import java.math.BigDecimal;
import java.sql.*;

public class AdminOperations {

    public static void showAllProducts(User user) throws SQLException {
        if (!UserValidator.isAdmin(user)) {
            System.out.println("Access Denied: Only Admins can perform this operation.");
            return;
        }

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

    public static void addProduct(User user,String productName, double price, int stockCount, String description, String category, String deliveryFrequency) throws SQLException {
        if (!UserValidator.isAdmin(user)) {
            System.out.println("Access Denied: Only Admins can perform this operation.");
            return;
        }

        String query = "INSERT INTO Products (ProductName, Price, StockCount, Description, Category, deliveryFrequency) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, productName);
            pstmt.setBigDecimal(2, BigDecimal.valueOf(price));
            pstmt.setInt(3, stockCount);
            pstmt.setString(4, description);
            pstmt.setString(5, category);
            pstmt.setString(6, deliveryFrequency);
            pstmt.executeUpdate();
            System.out.println("Product added successfully.");
        }
    }

    public static void editProduct(User user,int productId, String productName, double price, int stockCount, String description, String category, String deliveryFrequency) throws SQLException {
        if (!UserValidator.isAdmin(user)) {
            System.out.println("Access Denied: Only Admins can perform this operation.");
            return;
        }

        String query = "UPDATE Products SET ProductName = ?, Price = ?, StockCount = ?, Description = ?, Category = ?, deliveryFrequency = ? WHERE ProductID = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, productName);
            pstmt.setBigDecimal(2, BigDecimal.valueOf(price));
            pstmt.setInt(3, stockCount);
            pstmt.setString(4, description);
            pstmt.setString(5, category);
            pstmt.setString(6, deliveryFrequency);
            pstmt.setInt(7, productId);
            pstmt.executeUpdate();
            System.out.println("Product updated successfully.");
        }
    }

    public static void deleteProduct(User user,int productId) throws SQLException {
        if (!UserValidator.isAdmin(user)) {
            System.out.println("Access Denied: Only Admins can perform this operation.");
            return;
        }

        String query = "DELETE FROM Products WHERE ProductID = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, productId);
            pstmt.executeUpdate();
            System.out.println("Product deleted successfully.");
        }
    }

    // Method to search a product by ProductID
    public static void searchProductById(User user,int productId) throws SQLException {
        if (!UserValidator.isAdmin(user)) {
            System.out.println("Access Denied: Only Admins can perform this operation.");
            return;
        }

        String query = "SELECT * FROM Products WHERE ProductID = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    displayProductDetails(rs);
                } else {
                    System.out.println("Product with ID " + productId + " not found.");
                }
            }
        }
    }

    // Method to show all order history
    public static void showAllOrderHistory(User user) throws SQLException {
        if (!UserValidator.isAdmin(user)) {
            System.out.println("Access Denied: Only Admins can perform this operation.");
            return;
        }

        String query = "SELECT * FROM Orders";
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                displayOrderDetails(rs);
            }
        }
    }

    // Method to show order history by product ID
    public static void showOrderHistoryByProductId(User user,int productId) throws SQLException {
        if (!UserValidator.isAdmin(user)) {
            System.out.println("Access Denied: Only Admins can perform this operation.");
            return;
        }

        String query = "SELECT o.* FROM Orders o " +
                "JOIN Subscriptions s ON o.SubscriptionID = s.SubscriptionID " +
                "JOIN SubscriptionProducts sp ON s.SubscriptionID = sp.SubscriptionID " +
                "WHERE sp.ProductID = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setInt(1, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    displayOrderDetails(rs);
                }
            }
        }
    }

    // Method to show order history by user ID
    public static void showOrderHistoryByUserId(User user,String userId) throws SQLException {
        if (!UserValidator.isAdmin(user)) {
            System.out.println("Access Denied: Only Admins can perform this operation.");
            return;
        }

        String query = "SELECT * FROM Orders WHERE UserID = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    displayOrderDetails(rs);
                }
            }
        }
    }

    // Helper method to display order details
    private static void displayOrderDetails(ResultSet rs) throws SQLException {
        System.out.println("OrderID: " + rs.getInt("OrderID"));
        System.out.println("UserID: " + rs.getString("UserID"));
        System.out.println("SubscriptionID: " + rs.getInt("SubscriptionID"));
        System.out.println("OrderDate: " + rs.getDate("OrderDate"));
        System.out.println("StartDate: " + rs.getDate("StartDate"));
        System.out.println("EndDate: " + rs.getDate("EndDate"));
        System.out.println("OrderStatus: " + rs.getString("OrderStatus"));
        System.out.println("----");
    }

    // Helper method to display product details from ResultSet
    private static void displayProductDetails(ResultSet rs) throws SQLException {
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

