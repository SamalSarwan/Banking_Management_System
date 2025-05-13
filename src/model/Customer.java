package model;

public class Customer {
    private String custNo;
    private String name;
    private String phoneNo;
    private String city;
    private String accountType;
    private String branchName;
    
    public Customer(String custNo, String name, String phoneNo, String city, 
                   String accountType, String branchName) {
        this.custNo = custNo;
        this.name = name;
        this.phoneNo = phoneNo;
        this.city = city;
        this.accountType = accountType;
        this.branchName = branchName;
    }
    
    // Getters and setters
    public String getCustNo() { return custNo; }
    public String getName() { return name; }
    public String getPhoneNo() { return phoneNo; }
    public String getCity() { return city; }
    public String getAccountType() { return accountType; }
    public String getBranchName() { return branchName; }
    
    @Override
    public String toString() {
        return String.format("%-10s %-20s %-15s %-15s %-10s %-15s", 
            custNo, name, phoneNo, city, accountType, branchName);
    }
}