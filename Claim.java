/**
 * @author <Nguyen Pham Hai Anh - s3978692>
 */
package assignment1;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class ClaimProcessManagerImpl implements ClaimProcessManager {
    private final String claimsFilePath = "claims.csv";
    private final String customersFilePath = "customers.csv";

    public ClaimProcessManagerImpl() {
        createFileIfNeeded(claimsFilePath);
        createFileIfNeeded(customersFilePath);
    }

    private void createFileIfNeeded(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                boolean created = file.createNewFile();
                if (!created) {
                    System.err.println("Failed to create the file: " + filePath);
                }
            } catch (IOException e) {
                System.err.println("An error occurred while creating the file: " + filePath);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void add(Claim claim) {
        try (PrintWriter out = new PrintWriter(new FileWriter(claimsFilePath, true))) {
            out.println(claim.toCsvString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Claim claim) {
        List<Claim> allClaims = getAll();
        for (int i = 0; i < allClaims.size(); i++) {
            if (allClaims.get(i).getId().equals(claim.getId())) {
                allClaims.set(i, claim);
                break;
            }
        }
        saveAllClaims(allClaims);
    }

    @Override
    public void delete(String claimId) {
        List<Claim> allClaims = getAll();
        for (int i = 0; i < allClaims.size(); i++) {
            if (allClaims.get(i).getId().equals(claimId)) {
                allClaims.remove(i);
                break;
            }
        }
        saveAllClaims(allClaims);
    }

    @Override
    public Claim getOne(String claimId) {
        List<Claim> allClaims = getAll();
        for (Claim claim : allClaims) {
            if (claim.getId().equals(claimId)) {
                return claim;
            }
        }
        return null;
    }

    @Override
    public List<Claim> getAll() {
        List<Claim> claims = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(claimsFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                claims.add(Claim.fromCsvString(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return claims;
    }

    @Override
    public void addCustomer(Customer customer) {
        try (PrintWriter out = new PrintWriter(new FileWriter(customersFilePath, true))) {
            out.println(customer.toCsvString());
        } catch (IOException e) {
            System.err.println("Failed to add customer to file: " + e.getMessage());
        }
    }

    private void saveAllClaims(List<Claim> claims) {
        try (PrintWriter out = new PrintWriter(new FileWriter(claimsFilePath))) {
            for (Claim claim : claims) {
                out.println(claim.toCsvString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
