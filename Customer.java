/**
 * @author <Nguyen Pham Hai Anh - s3978692>
 */
package assignment1;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Customer {
    private final String id; // Format: c-numbers; 7 digits
    private String fullName;
    private String policyHolderId; // New field for policy holder ID
    private List<Claim> claims;
    private InsuranceCard insuranceCard;

    // Constructor
    public Customer(String id, String fullName, String policyHolderId, InsuranceCard insuranceCard) {
        this.id = id;
        this.fullName = fullName;
        this.policyHolderId = policyHolderId;
        this.insuranceCard = insuranceCard;
        this.claims = new ArrayList<>();
    }

    // Getters
    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public String getPolicyHolderId() { return policyHolderId; }
    public InsuranceCard getInsuranceCard() { return insuranceCard; }
    public List<Claim> getClaims() { return new ArrayList<>(claims); } // Defensive copy

    // Setters
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setPolicyHolderId(String policyHolderId) { this.policyHolderId = policyHolderId; }
    public void setInsuranceCard(InsuranceCard insuranceCard) { this.insuranceCard = insuranceCard; }
    public void addClaim(Claim claim) { this.claims.add(claim); }

    // Load customer data from a CSV file
    public static List<Customer> loadCustomersFromFile(String fileName) throws IOException, ParseException {
        List<Customer> customers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("ID")) continue; // Skip headers or empty lines
                String[] data = line.split(",");
                if (data.length >= 3) {
                    Customer customer = Customer.parseCustomerData(data);
                    customers.add(customer);
                } else {
                    System.err.println("Skipping invalid line: " + line);
                }
            }
        }
        return customers;
    }

    // Parse data from a CSV line and create a Customer object
    public static Customer parseCustomerData(String[] data) throws ParseException {
        String id = data[0].trim();
        String fullName = data[1].trim();
        String policyHolderId = data.length > 2 ? data[2].trim() : null;
        InsuranceCard insuranceCard = data.length > 3 ? InsuranceCard.parseInsuranceCardData(data[3].trim()) : null;

        return new Customer(id, fullName, policyHolderId, insuranceCard);
    }

    // Save customer data to a CSV file
    public static void saveCustomersToFile(List<Customer> customers, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("ID,Full Name,Policy Holder ID,Insurance Card\n");
            for (Customer customer : customers) {
                writer.write(customer.toCsvString() + "\n");
            }
        }
    }

    // Convert Customer details to a CSV-formatted string
    public String toCsvString() {
        String insuranceCardData = insuranceCard != null ? insuranceCard.toCsvString() : "";
        return String.format("%s,%s,%s,%s", id, fullName, policyHolderId, insuranceCardData);
    }
}
