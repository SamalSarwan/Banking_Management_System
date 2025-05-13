package dao;

import model.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    // Add new customer with account type and branch information
    public boolean addCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO customer (cust_no, name, phoneno, city, account_type, branch_name) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customer.getCustNo());
            pstmt.setString(2, customer.getName());
            pstmt.setString(3, customer.getPhoneNo());
            pstmt.setString(4, customer.getCity());
            pstmt.setString(5, customer.getAccountType());
            pstmt.setString(6, customer.getBranchName());
            
            return pstmt.executeUpdate() > 0;
        }
    }

    // Get all customers
    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT cust_no, name, phoneno, city, account_type, branch_name FROM customer";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                customers.add(new Customer(
                    rs.getString("cust_no"),
                    rs.getString("name"),
                    rs.getString("phoneno"),
                    rs.getString("city"),
                    rs.getString("account_type"),
                    rs.getString("branch_name")
                ));
            }
        }
        return customers;
    }

    // Get customer by ID
    public Customer getCustomerById(String custNo) throws SQLException {
        String sql = "SELECT cust_no, name, phoneno, city, account_type, branch_name FROM customer WHERE cust_no = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, custNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                        rs.getString("cust_no"),
                        rs.getString("name"),
                        rs.getString("phoneno"),
                        rs.getString("city"),
                        rs.getString("account_type"),
                        rs.getString("branch_name")
                    );
                }
            }
        }
        return null;
    }

    // Delete customer (will cascade to accounts via FK)
    public boolean deleteCustomer(String custNo) throws SQLException {
        String sql = "DELETE FROM customer WHERE cust_no = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, custNo);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Update customer name
    public boolean updateCustomerName(String custNo, String newName) throws SQLException {
        String sql = "UPDATE customer SET name = ? WHERE cust_no = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newName);
            pstmt.setString(2, custNo);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Update customer phone
    public boolean updateCustomerPhone(String custNo, String newPhone) throws SQLException {
        String sql = "UPDATE customer SET phoneno = ? WHERE cust_no = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newPhone);
            pstmt.setString(2, custNo);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Update customer city
    public boolean updateCustomerCity(String custNo, String newCity) throws SQLException {
        String sql = "UPDATE customer SET city = ? WHERE cust_no = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newCity);
            pstmt.setString(2, custNo);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Update account type
    public boolean updateAccountType(String custNo, String newType) throws SQLException {
        String sql = "UPDATE customer SET account_type = ? WHERE cust_no = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newType);
            pstmt.setString(2, custNo);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Update branch name
    public boolean updateBranchName(String custNo, String newBranch) throws SQLException {
        String sql = "UPDATE customer SET branch_name = ? WHERE cust_no = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newBranch);
            pstmt.setString(2, custNo);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Check if customer exists
    public boolean customerExists(String custNo) throws SQLException {
        String sql = "SELECT 1 FROM customer WHERE cust_no = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, custNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Get customers by city
    public List<Customer> getCustomersByCity(String city) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT cust_no, name, phoneno, city, account_type, branch_name FROM customer WHERE city = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, city);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(new Customer(
                        rs.getString("cust_no"),
                        rs.getString("name"),
                        rs.getString("phoneno"),
                        rs.getString("city"),
                        rs.getString("account_type"),
                        rs.getString("branch_name")
                    ));
                }
            }
        }
        return customers;
    }
}