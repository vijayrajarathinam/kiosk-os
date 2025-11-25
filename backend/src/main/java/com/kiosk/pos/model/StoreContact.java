package com.kiosk.pos.model;


import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class StoreContact {

    private String address;
    private String phone;

    @Email(message = "Invalid email format")
    private String email;
}
