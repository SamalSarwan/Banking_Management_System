package dao;

import model.Account;
import oracle.jdbc.OraclePreparedStatement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    
    public String createAccount(String custNo) throws SQLException {
        String sql = "INSERT INTO account (account_no, cust_no) VALUES ('AC'||LPAD(account_seq.NEXTVAL, 4, '0'), ?) RETURNING account_no INTO ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             OraclePreparedStatement pstmt = (OraclePreparedStatement) conn.prepareStatement(sql)) {
            
            pstmt.setString(1, custNo);
            pstmt.registerReturnParameter(2, Types.VARCHAR);
            
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getReturnResultSet();
            if (rs.next()) {
                return rs.getString(1);
            }
            return null;
        }
    }

    // Get all accounts for a customer
    public List<Account> getAccountsByCustomer(String custNo) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT account_no, cust_no, balance FROM account WHERE cust_no = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, custNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    accounts.add(new Account(
                        rs.getString("account_no"),
                        rs.getString("cust_no"),
                        rs.getDouble("balance")
                    ));
                }
            }
        }
        return accounts;
    }

    // Deposit money into account
    public boolean deposit(String accountNo, double amount) throws SQLException {
        String sql = "UPDATE account SET balance = balance + ? WHERE account_no = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, amount);
            pstmt.setString(2, accountNo);
            
            return pstmt.executeUpdate() > 0;
        }
    }

    // Withdraw money from account
    public boolean withdraw(String accountNo, double amount) throws SQLException {
        String sql = "UPDATE account SET balance = balance - ? WHERE account_no = ? AND balance >= ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, amount);
            pstmt.setString(2, accountNo);
            pstmt.setDouble(3, amount);
            
            return pstmt.executeUpdate() > 0;
        }
    }

    // Get current balance
    public double getBalance(String accountNo) throws SQLException {
        String sql = "SELECT balance FROM account WHERE account_no = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("balance");
                }
            }
        }
        throw new SQLException("Account not found");
    }

    // Delete account (cascade when customer is deleted)
    public boolean deleteAccount(String accountNo) throws SQLException {
        String sql = "DELETE FROM account WHERE account_no = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNo);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Transfer money between accounts
    public boolean transfer(String fromAccount, String toAccount, double amount) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Withdraw from source account
            String withdrawSql = "UPDATE account SET balance = balance - ? WHERE account_no = ? AND balance >= ?";
            try (PreparedStatement withdrawStmt = conn.prepareStatement(withdrawSql)) {
                withdrawStmt.setDouble(1, amount);
                withdrawStmt.setString(2, fromAccount);
                withdrawStmt.setDouble(3, amount);
                
                if (withdrawStmt.executeUpdate() == 0) {
                    conn.rollback();
                    return false;
                }
            }
            
            // Deposit to target account
            String depositSql = "UPDATE account SET balance = balance + ? WHERE account_no = ?";
            try (PreparedStatement depositStmt = conn.prepareStatement(depositSql)) {
                depositStmt.setDouble(1, amount);
                depositStmt.setString(2, toAccount);
                depositStmt.executeUpdate();
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
        }
    }
}