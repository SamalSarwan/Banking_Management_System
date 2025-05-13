package service;

import dao.*;
import model.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class BankingService {
    private CustomerDAO customerDAO = new CustomerDAO();
    private AccountDAO accountDAO = new AccountDAO();
    private LoanDAO loanDAO = new LoanDAO();
    private Scanner scanner = new Scanner(System.in);

    // Option 1: Show all customers
    public void showAllCustomers() {
        try {
            List<Customer> customers = customerDAO.getAllCustomers();
            System.out.println("\nCustomer Records:");
            System.out.println("-------------------------------------------------------------------------------");
            System.out.printf("%-10s %-20s %-15s %-15s %-10s %-15s\n", 
                "Cust No", "Name", "Phone", "City", "Acc Type", "Branch");
            System.out.println("-------------------------------------------------------------------------------");
            
            for (Customer customer : customers) {
                System.out.println(customer);
            }
            System.out.println("-------------------------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Error retrieving customer records: " + e.getMessage());
        }
    }

    // Option 2: Add customer with auto-generated account
    public void addCustomer() {
        try {
            System.out.println("\nAdd New Customer with Account");
            System.out.println("-----------------------------");
            
            System.out.print("Enter Customer Number (e.g., C1001): ");
            String custNo = scanner.nextLine();
            
            System.out.print("Enter Full Name: ");
            String name = scanner.nextLine();
            
            System.out.print("Enter Phone Number: ");
            String phoneNo = scanner.nextLine();
            
            System.out.print("Enter City: ");
            String city = scanner.nextLine();
            
            System.out.print("Enter Account Type (Savings/Current): ");
            String accountType = scanner.nextLine();
            
            System.out.print("Enter Branch Name: ");
            String branchName = scanner.nextLine();
            
            Customer customer = new Customer(custNo, name, phoneNo, city, accountType, branchName);
            
            if (customerDAO.addCustomer(customer)) {
                String accountNo = accountDAO.createAccount(custNo);
                System.out.println("\nCustomer and account created successfully!");
                System.out.println("Account Number: " + accountNo);
                System.out.println("Initial Balance: 0.00");
            } else {
                System.out.println("Failed to add customer.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding customer: " + e.getMessage());
        }
    }

    // Option 3: Delete customer
    public void deleteCustomer() {
        try {
            System.out.println("\nDelete Customer");
            System.out.println("--------------");
            
            System.out.print("Enter Customer Number to delete: ");
            String custNo = scanner.nextLine();
            
            boolean success = customerDAO.deleteCustomer(custNo);
            
            if (success) {
                System.out.println("Customer deleted successfully!");
            } else {
                System.out.println("Customer not found or could not be deleted.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting customer: " + e.getMessage());
        }
    }

    // Option 4: Update customer
    public void updateCustomer() {
        try {
            System.out.println("\nUpdate Customer Information");
            System.out.println("--------------------------");
            
            System.out.print("Enter Customer Number to update: ");
            String custNo = scanner.nextLine();
            
            Customer customer = customerDAO.getCustomerById(custNo);
            if (customer == null) {
                System.out.println("Customer not found!");
                return;
            }
            
            System.out.println("\nCurrent Customer Details:");
            System.out.println(customer);
            
            System.out.println("\nWhat would you like to update?");
            System.out.println("1. Name");
            System.out.println("2. Phone Number");
            System.out.println("3. City");
            System.out.println("4. Account Type");
            System.out.println("5. Branch Name");
            System.out.print("Enter your choice (1-5): ");
            int choice = Integer.parseInt(scanner.nextLine());
            
            boolean success = false;
            switch (choice) {
                case 1:
                    System.out.print("Enter new Name: ");
                    success = customerDAO.updateCustomerName(custNo, scanner.nextLine());
                    break;
                case 2:
                    System.out.print("Enter new Phone Number: ");
                    success = customerDAO.updateCustomerPhone(custNo, scanner.nextLine());
                    break;
                case 3:
                    System.out.print("Enter new City: ");
                    success = customerDAO.updateCustomerCity(custNo, scanner.nextLine());
                    break;
                case 4:
                    System.out.print("Enter new Account Type: ");
                    success = customerDAO.updateAccountType(custNo, scanner.nextLine());
                    break;
                case 5:
                    System.out.print("Enter new Branch Name: ");
                    success = customerDAO.updateBranchName(custNo, scanner.nextLine());
                    break;
                default:
                    System.out.println("Invalid choice!");
                    return;
            }
            
            if (success) {
                System.out.println("Customer updated successfully!");
            } else {
                System.out.println("Failed to update customer.");
            }
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Error updating customer: " + e.getMessage());
        }
    }

    // Option 5: Show account details
    public void showAccountDetails() {
        try {
            System.out.println("\nAccount Details");
            System.out.println("--------------");
            
            System.out.print("Enter Customer Number: ");
            String custNo = scanner.nextLine();
            
            List<Account> accounts = accountDAO.getAccountsByCustomer(custNo);
            
            if (accounts.isEmpty()) {
                System.out.println("No accounts found for this customer.");
                return;
            }
            
            System.out.println("\nAccount Details for Customer " + custNo + ":");
            System.out.println("----------------------------------------");
            System.out.printf("%-12s %-12s %-15s\n", "Account No", "Cust No", "Balance");
            System.out.println("----------------------------------------");
            
            for (Account account : accounts) {
                System.out.println(account);
            }
            System.out.println("----------------------------------------");
        } catch (SQLException e) {
            System.out.println("Error retrieving account details: " + e.getMessage());
        }
    }

    // Option 6: Add loan with auto-generated number
    // Option 6: Add loan with auto-generated number and full branch details
public void addLoan() {
    try {
        System.out.println("\nAdd New Loan");
        System.out.println("------------");
        
        System.out.print("Enter Customer Number: ");
        String custNo = scanner.nextLine();
        
        System.out.print("Enter Loan Amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        
        System.out.print("Enter Branch Code: ");
        String branchCode = scanner.nextLine();
        
        System.out.print("Enter Branch Name: ");
        String branchName = scanner.nextLine();
        
        System.out.print("Enter Branch City: ");
        String branchCity = scanner.nextLine();
        
        String loanNo = loanDAO.createLoan(custNo, amount, branchCode, branchName, branchCity);
        
        if (loanNo != null) {
            System.out.println("\nLoan created successfully!");
            System.out.println("Loan Number: " + loanNo);
            System.out.printf("Branch: %s (%s, %s)\n", branchName, branchCode, branchCity);
        } else {
            System.out.println("Failed to create loan.");
        }
    } catch (SQLException | NumberFormatException e) {
        System.out.println("Error adding loan: " + e.getMessage());
    }
}

    // Option 7: Show loan details
    // Option 7: Show loan details with full branch information
public void showLoanDetails() {
    try {
        System.out.println("\nLoan Details");
        System.out.println("------------");
        
        System.out.print("Enter Customer Number: ");
        String custNo = scanner.nextLine();
        
        List<Loan> loans = loanDAO.getLoansByCustomer(custNo);
        
        if (loans.isEmpty()) {
            System.out.println("This customer has no loans.");
            return;
        }
        
        System.out.println("\nLoan Details for Customer " + custNo + ":");
        System.out.println("----------------------------------------------------------------------------------------");
        System.out.printf("%-10s %-10s %-15s %-10s %-20s %-15s\n", 
            "Loan No", "Cust No", "Amount", "Branch Code", "Branch Name", "Branch City");
        System.out.println("----------------------------------------------------------------------------------------");
        
        for (Loan loan : loans) {
            System.out.println(loan);
        }
        System.out.println("----------------------------------------------------------------------------------------");
    } catch (SQLException e) {
        System.out.println("Error retrieving loan details: " + e.getMessage());
    }
}

    // Option 8: Deposit money
    public void depositMoney() {
        try {
            System.out.println("\nDeposit Money");
            System.out.println("-------------");
            
            System.out.print("Enter Account Number: ");
            String accountNo = scanner.nextLine();
            
            System.out.print("Enter Amount to Deposit: ");
            double amount = Double.parseDouble(scanner.nextLine());
            
            boolean success = accountDAO.deposit(accountNo, amount);
            
            if (success) {
                System.out.println("Amount deposited successfully!");
                System.out.println("New Balance: " + accountDAO.getBalance(accountNo));
            } else {
                System.out.println("Failed to deposit amount. Account may not exist.");
            }
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Error depositing money: " + e.getMessage());
        }
    }

    // Option 9: Withdraw money
    public void withdrawMoney() {
        try {
            System.out.println("\nWithdraw Money");
            System.out.println("--------------");
            
            System.out.print("Enter Account Number: ");
            String accountNo = scanner.nextLine();
            
            System.out.print("Enter Amount to Withdraw: ");
            double amount = Double.parseDouble(scanner.nextLine());
            
            boolean success = accountDAO.withdraw(accountNo, amount);
            
            if (success) {
                System.out.println("Amount withdrawn successfully!");
                System.out.println("New Balance: " + accountDAO.getBalance(accountNo));
            } else {
                System.out.println("Failed to withdraw. Insufficient balance or account doesn't exist.");
            }
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Error withdrawing money: " + e.getMessage());
        }
    }
}