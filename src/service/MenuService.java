package service;

import java.util.Scanner;

public class MenuService {
    private BankingService bankingService = new BankingService();
    private Scanner scanner = new Scanner(System.in);

    public void displayMenu() {
        System.out.println("\n***** Banking Management System *****");
        System.out.println("1. Show Customer Records");
        System.out.println("2. Add Customer Record (with auto-generated account)");
        System.out.println("3. Delete Customer Record");
        System.out.println("4. Update Customer Information");
        System.out.println("5. Show Account Details of a Customer");
        System.out.println("6. Add Loan to Customer (with auto-generated loan number)");
        System.out.println("7. Show Loan Details of a Customer");
        System.out.println("8. Deposit Money to an Account");
        System.out.println("9. Withdraw Money from an Account");
        System.out.println("10. Exit the Program");
    }

    public void processChoice(int choice) {
        switch (choice) {
            case 1:
                bankingService.showAllCustomers();
                break;
            case 2:
                bankingService.addCustomer();
                break;
            case 3:
                bankingService.deleteCustomer();
                break;
            case 4:
                bankingService.updateCustomer();
                break;
            case 5:
                bankingService.showAccountDetails();
                break;
            case 6:
                bankingService.addLoan();
                break;
            case 7:
                bankingService.showLoanDetails();
                break;
            case 8:
                bankingService.depositMoney();
                break;
            case 9:
                bankingService.withdrawMoney();
                break;
            case 10:
                System.out.println("Exiting the program. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice! Please enter a number between 1-10.");
        }
    }

    public void run() {
        while (true) {
            displayMenu();
            System.out.print("\nEnter your choice (1-10): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                processChoice(choice);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
            
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }
}