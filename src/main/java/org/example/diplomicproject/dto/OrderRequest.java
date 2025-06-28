package org.example.diplomicproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private Long userId;
    private Long productId;
    private int quantity;
    private double totalAmount;
}
