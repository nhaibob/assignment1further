/**
 * @author <Nguyen Pham Hai Anh - s3978692>
 */
package assignment1;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class InsuranceClaimsManagementSystem {

    private static final ClaimProcessManager claimProcessManager = new ClaimProcessManagerImpl();

    public static void main(String[] args) {
        try {
            List<Customer> customers = loadCustomersFromFile();
            List<Claim> claims = loadClaimsFromFile();

            for (Customer customer : customers) {
                claimProcessManager.addCustomer(customer);
            }

            for (Claim claim : claims) {
                claimProcessManager.add(claim);
            }

            startUserInterface();
        } catch (IOException | ParseException e) {
            System.err.println("Error during system initialization: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void startUserInterface() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nInsurance Claims Management System");
            System.out.println("1. Add Claim\n2. Update Claim\n3. Delete Claim\n4. View Claims\n5. Exit");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over

            switch (choice) {
                case 1:
                    addClaim(scanner);
                    break;
                case 2:
                    updateClaim(scanner);
                    break;
                case 3:
                    deleteClaim(scanner);
                    break;
                case 4:
                    viewClaims();
                    break;
                case 5:
                    running = false;
                    saveAllData();
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }

        scanner.close();
        System.out.println("System exited.");
    }

    private static void addClaim(Scanner scanner) {
        System.out.println("Enter claim details:");

        System.out.print("Enter claim ID (numbers; 10 numbers): ");
        String id = scanner.nextLine();

        System.out.print("Enter claim date (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();

        System.out.print("Enter insured person ID: ");
        String insuredPersonId = scanner.nextLine();

        System.out.print("Enter card number: ");
        String cardNumber = scanner.nextLine();

        System.out.print("Enter exam date (yyyy-MM-dd): ");
        String examDateStr = scanner.nextLine();

        System.out.print("Enter claim amount: ");
        BigDecimal claimAmount = new BigDecimal(scanner.nextLine());

        System.out.print("Enter status (New, Processing, Done): ");
        String status = scanner.nextLine();

        System.out.print("Enter receiver bank info: ");
        String receiverBankInfo = scanner.nextLine();

        try {
            Date claimDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            Date examDate = new SimpleDateFormat("yyyy-MM-dd").parse(examDateStr);
            Claim claim = new Claim(id, claimDate, insuredPersonId, cardNumber, examDate, new ArrayList<>(), claimAmount, status, receiverBankInfo);
            claimProcessManager.add(claim);
            System.out.println("Claim added successfully.");
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
        }
    }

    private static void updateClaim(Scanner scanner) {
        System.out.print("Enter the ID of the claim to update: ");
        String claimId = scanner.nextLine();
        Claim claim = claimProcessManager.getOne(claimId);

        if (claim != null) {
            // Example of setting new values - you would prompt for each value as in addClaim
            System.out.println("Enter new details for the claim (press Enter to keep current value):");

            System.out.print("Enter new status (New, Processing, Done): ");
            String newStatus = scanner.nextLine();
            if (!newStatus.isEmpty()) {
                claim.setStatus(newStatus);
            }

            claimProcessManager.update(claim);
            System.out.println("Claim updated successfully.");
        } else {
            System.out.println("No claim found with ID: " + claimId);
        }
    }

    private static void deleteClaim(Scanner scanner) {
        System.out.print("Enter the ID of the claim to delete: ");
        String claimId = scanner.nextLine();
        claimProcessManager.delete(claimId);
        System.out.println("Claim deleted successfully.");
    }

    private static void viewClaims() {
        List<Claim> claims = claimProcessManager.getAll();
        if (claims.isEmpty()) {
            System.out.println("No claims to display.");
        } else {
            for (Claim claim : claims) {
                System.out.println(claim);
            }
        }
    }

    private static void saveAllData() {
        try {
            saveClaimsToFile(claimProcessManager.getAll());
            System.out.println("Data saved to claims.csv successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while saving data: " + e.getMessage());
        }
    }

    // Load customers from file
    private static List<Customer> loadCustomersFromFile() throws IOException, ParseException {
        List<Customer> customers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/assignment1/customers.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip headers and empty lines
                if (line.trim().isEmpty() || line.startsWith("ID")) continue;
                String[] data = line.split(",");
                if (data.length >= 3) {
                    customers.add(Customer.parseCustomerData(data));
                } else {
                    System.err.println("Skipping invalid line: " + line);
                }
            }
        }
        return customers;
    }

    // Load claims from file
    private static List<Claim> loadClaimsFromFile() throws IOException, ParseException {
        List<Claim> claims = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/assignment1/claims.csv"))) {
            String line = reader.readLine(); // This line skips the header row
            while ((line = reader.readLine()) != null) {
                // Skip headers and empty lines
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                if (data.length >= 9) {
                    claims.add(Claim.fromCsvString(line));
                } else {
                    System.err.println("Skipping invalid line: " + line);
                }
            }
        }
        return claims;
    }

    // Save claims to file
    private static void saveClaimsToFile(List<Claim> claims) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("claims.csv"))) {
            writer.write("ID,Claim Date,Insured Person ID,Card Number,Exam Date,Documents,Claim Amount,Status,Receiver Bank Info\n");
            for (Claim claim : claims) {
                writer.write(claim.toCsvString() + "\n");
            }
        }
    }
}
