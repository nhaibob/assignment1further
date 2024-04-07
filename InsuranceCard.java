/**
 * @author <Nguyen Pham Hai Anh - s3978692>
 */
package assignment1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InsuranceCard {
    private String cardNumber;
    private String cardHolderId;
    private String policyOwnerId;
    private Date expirationDate;

    // Constructor
    public InsuranceCard(String cardNumber, String cardHolderId, String policyOwnerId, Date expirationDate) {
        this.cardNumber = cardNumber;
        this.cardHolderId = cardHolderId;
        this.policyOwnerId = policyOwnerId;
        this.expirationDate = expirationDate;
    }

    // Getters and setters
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public String getCardHolderId() { return cardHolderId; }
    public void setCardHolderId(String cardHolderId) { this.cardHolderId = cardHolderId; }
    public String getPolicyOwnerId() { return policyOwnerId; }
    public void setPolicyOwnerId(String policyOwnerId) { this.policyOwnerId = policyOwnerId; }
    public Date getExpirationDate() { return expirationDate; }
    public void setExpirationDate(Date expirationDate) { this.expirationDate = expirationDate; }

    // Convert insurance card details to a CSV-formatted string
    public String toCsvString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return String.format("%s,%s,%s,%s",
                this.cardNumber,
                this.cardHolderId,
                this.policyOwnerId,
                sdf.format(this.expirationDate));
    }

    // Static method to parse a CSV string and return an InsuranceCard object
    public static InsuranceCard parseInsuranceCardData(String data) throws ParseException {
        String[] parts = data.split(",");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid number of fields for insurance card data.");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String cardNumber = parts[0].trim();
        String cardHolderId = parts[1].trim();
        String policyOwnerId = parts[2].trim();
        Date expirationDate = sdf.parse(parts[3].trim());

        return new InsuranceCard(cardNumber, cardHolderId, policyOwnerId, expirationDate);
    }
}
