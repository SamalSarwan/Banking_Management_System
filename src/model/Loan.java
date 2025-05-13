package model;

public class Loan {
    private String loanNo;
    private String custNo;
    private double amount;
    private String branchCode;
    private String branchName;
    private String branchCity;
    
    // Constructor, getters, and setters
    public Loan(String loanNo, String custNo, double amount, 
               String branchCode, String branchName, String branchCity) {
        this.loanNo = loanNo;
        this.custNo = custNo;
        this.amount = amount;
        this.branchCode = branchCode;
        this.branchName = branchName;
        this.branchCity = branchCity;
    }

    // Getters and setters
    public String getLoanNo() { return loanNo; }
    public String getCustNo() { return custNo; }
    public double getAmount() { return amount; }
    public String getBranchCode() { return branchCode; }
    public String getBranchName() { return branchName; }
    public String getBranchCity() { return branchCity; }
    
    @Override
    public String toString() {
        return String.format("%-10s %-10s %-15.2f %-10s %-20s %-15s", 
                loanNo, custNo, amount, branchCode, branchName, branchCity);
    }
}