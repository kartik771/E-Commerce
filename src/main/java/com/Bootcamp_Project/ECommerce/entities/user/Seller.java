package com.Bootcamp_Project.ECommerce.entities.user;


import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "user_id")
public class Seller extends User {

    private String gst;

    private String companyContact;
    private String CompanyName;

//    @OneToOne(mappedBy = "seller" , cascade = CascadeType.ALL)
//    private Address companyAddress;

}
