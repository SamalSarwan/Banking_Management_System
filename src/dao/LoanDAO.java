package dao;

import model.Loan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import oracle.jdbc.OraclePreparedStatement;

public class LoanDAO {
    // Create new loan with auto-generated loan number and branch details
    public String createLoan(String custNo, double amount, 
                           String branchCode, String branchName, String branchCity) throws SQLException {
        String sql = "INSERT INTO loan (loan_no, cust_no, amount, branch_code, branch_name, branch_city) " +
                    "VALUES ('LN'||LPAD(loan_seq.NEXTVAL, 4, '0'), ?, ?, ?, ?, ?) " +
                    "RETURNING loan_no INTO ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             OraclePreparedStatement pstmt = (OraclePreparedStatement) conn.prepareStatement(sql)) {
            
            pstmt.setString(1, custNo);
            pstmt.setDouble(2, amount);
            pstmt.setString(3, branchCode);
            pstmt.setString(4, branchName);
            pstmt.setString(5, branchCity);
            pstmt.registerReturnParameter(6, Types.VARCHAR);
            
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getReturnResultSet();
            if (rs.next()) {
                return rs.getString(1);
            }
            return null;
        }
    }

    // Get all loans for a customer with full branch details
    public List<Loan> getLoansByCustomer(String custNo) throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT loan_no, cust_no, amount, branch_code, branch_name, branch_city " +
                     "FROM loan WHERE cust_no = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, custNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    loans.add(new Loan(
                        rs.getString("loan_no"),
                        rs.getString("cust_no"),
                        rs.getDouble("amount"),
                        rs.getString("branch_code"),
                        rs.getString("branch_name"),
                        rs.getString("branch_city")
                    ));
                }
            }
        }
        return loans;
    }

    // Get loan by loan number with full details
    public Loan getLoanByNumber(String loanNo) throws SQLException {
        String sql = "SELECT loan_no, cust_no, amount, branch_code, branch_name, branch_city " +
                     "FROM loan WHERE loan_no = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, loanNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Loan(
                        rs.getString("loan_no"),
                        rs.getString("cust_no"),
                        rs.getDouble("amount"),
                        rs.getString("branch_code"),
                        rs.getString("branch_name"),
                        rs.getString("branch_city")
                    );
                }
            }
        }
        return null;
    }

    // Update loan amount
    public boolean updateLoanAmount(String loanNo, double newAmount) throws SQLException {
        String sql = "UPDATE loan SET amount = ? WHERE loan_no = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, newAmount);
            pstmt.setString(2, loanNo);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Delete loan
    public boolean deleteLoan(String loanNo) throws SQLException {
        String sql = "DELETE FROM loan WHERE loan_no = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, loanNo);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Get total loan amount for a customer
    public double getTotalLoanAmount(String custNo) throws SQLException {
        String sql = "SELECT NVL(SUM(amount), 0) FROM loan WHERE cust_no = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, custNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        }
        return 0;
    }

    // Check if customer has any loans
    public boolean hasLoans(String custNo) throws SQLException {
        String sql = "SELECT 1 FROM loan WHERE cust_no = ? AND ROWNUM = 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, custNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Get all loans with full branch details (admin function)
    public List<Loan> getAllLoans() throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT loan_no, cust_no, amount, branch_code, branch_name, branch_city FROM loan";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                loans.add(new Loan(
                    rs.getString("loan_no"),
                    rs.getString("cust_no"),
                    rs.getDouble("amount"),
                    rs.getString("branch_code"),
                    rs.getString("branch_name"),
                    rs.getString("branch_city")
                ));
            }
        }
        return loans;
    }
}