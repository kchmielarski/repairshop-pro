package com.wsiiz.repairshop.enterprise.branch;

import com.wsiiz.repairshop.foundation.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode
public class Branch extends BaseEntity {
    @Id
    @GeneratedValue
    int brachId;

    String branchName;
    @Enumerated(EnumType.STRING)
    OperationType type;

    @Data
    @Embeddable
    public static class Address{
    String streetName;
    int houseNumber;
    String postcode;
    String city;

        public Address(String streetName, int houseNumber, String postcode, String city) {
            this.streetName = streetName;
            this.houseNumber = houseNumber;
            this.postcode = postcode;
            this.city = city;
        }
     }
}
