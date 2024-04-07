/**
 * @author <Nguyen Pham Hai Anh - s3978692>
 */
package assignment1;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class Claim {
    private String id;
    private Date claimDate;
    private String insuredPersonId;
    private String cardNumber;
    private Date examDate;
    private List<String> documents;
    private BigDecimal claimAmount;
    private String status;
    private String receiverBankInfo;

    public Claim(String id, Date claimDate, String insuredPersonId, String cardNumber,
                 Date examDate, List<String> documents, BigDecimal claimAmount, String status, String receiverBankInfo) {
        setId(id);
        setClaimDate(claimDate);
        setInsuredPersonId(insuredPersonId);
        setCardNumber(cardNumber);
        setExamDate(examDate);
        setDocuments(documents);
        setClaimAmount(claimAmount);
        setStatus(status);
        setReceiverBankInfo(receiverBankInfo);
    }

    public Claim(String claimId, Date claimDate, String insuredPersonId, String cardNumber,
                 Date examDate, BigDecimal claimAmount, String status) {
        this(claimId, claimDate, insuredPersonId, cardNumber, examDate, new ArrayList<>(), claimAmount, status, null);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || !id.matches("\\d{10}")) {
            throw new IllegalArgumentException("Invalid ID format. ID must be in the format 'xxxxxxxxxx' where x is a digit.");
        }
        this.id = id;
    }

    public Date getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(Date claimDate) {
        this.claimDate = claimDate;
    }

    public String getInsuredPersonId() {
        return insuredPersonId;
    }

    public void setInsuredPersonId(String insuredPersonId) {
        this.insuredPersonId = insuredPersonId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    public List<String> getDocuments() {
        return documents != null ? new ArrayList<>(documents) : new ArrayList<>();
    }

    public void setDocuments(List<String> documents) {
        this.documents = documents != null ? new ArrayList<>(documents) : new ArrayList<>();
    }

    public BigDecimal getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(BigDecimal claimAmount) {
        this.claimAmount = claimAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceiverBankInfo() {
        return receiverBankInfo;
    }

    public void setReceiverBankInfo(String receiverBankInfo) {
        this.receiverBankInfo = receiverBankInfo;
    }

    public void addDocument(String document) {
        if (this.documents == null) {
            this.documents = new ArrayList<>();
        }
        this.documents.add(document);
    }

    public void removeDocument(String document) {
        if (this.documents != null) {
            this.documents.remove(document);
        }
    }

    public String toCsvString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                getId(), dateFormat.format(getClaimDate()), getInsuredPersonId(), getCardNumber(),
                dateFormat.format(getExamDate()), String.join(";", getDocuments()),
                getClaimAmount().toPlainString(), getStatus(), getReceiverBankInfo());
    }

    public static Claim fromCsvString(String csvString) {
        String[] parts = csvString.split(",");
        if (parts.length != 9) {
            throw new IllegalArgumentException("Invalid CSV string format for Claim.");
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date claimDate = dateFormat.parse(parts[1]);
            Date examDate = dateFormat.parse(parts[4]);
            BigDecimal claimAmount = new BigDecimal(parts[6].trim()); // Trim whitespace before parsing
            return new Claim(parts[0], claimDate, parts[2], parts[3], examDate,
                    new ArrayList<>(), claimAmount, parts[7], parts[8]);
        } catch (ParseException | NumberFormatException e) {
            throw new IllegalArgumentException("Error parsing CSV string for Claim: " + e.getMessage());
        }
    }

}
