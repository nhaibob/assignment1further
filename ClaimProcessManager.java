/**
 * @author <Nguyen Pham Hai Anh - s3978692>
 */
package assignment1;

import java.util.List;

public interface ClaimProcessManager {
    void add(Claim claim);
    void update(Claim claim);
    void delete(String claimId);
    Claim getOne(String claimId);
    List<Claim> getAll();

    void addCustomer(Customer customer);
}
