package com.kiosk.pos.payload.dto;


import com.kiosk.pos.model.Category;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDto {

    private Long id;
    private String name;
    private String sku;
    private String description;
    private Double mrp;
    private Double sellingPrice;
    private String brand;
    private String image;
    private Long storeId;
    private Long categoryId;

}
