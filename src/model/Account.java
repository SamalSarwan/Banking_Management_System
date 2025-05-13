package model;

public class Account {
    private String accountNo;
    private String custNo;
    private double balance;
    
    public Account(String accountNo, String custNo, double balance) {
        this.accountNo = accountNo;
        this.custNo = custNo;
        this.balance = balance;
    }
    
    // Getters
    public String getAccountNo() { return accountNo; }
    public String getCustNo() { return custNo; }
    public double getBalance() { return balance; }
    
    @Override
    public String toString() {
        return String.format("%-10s %-10s %-15.2f", accountNo, custNo, balance);
    }
}