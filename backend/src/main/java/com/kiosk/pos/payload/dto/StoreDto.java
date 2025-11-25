package com.kiosk.pos.payload.dto;

import com.kiosk.pos.domain.StoreStatus;
import com.kiosk.pos.model.StoreContact;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoreDto {

    private Long id;
    private String Brand;
    private UserDto storeAdmin;
    private String description;
    private String storeType;
    private StoreStatus storeStatus;
    private StoreContact contact;
}
