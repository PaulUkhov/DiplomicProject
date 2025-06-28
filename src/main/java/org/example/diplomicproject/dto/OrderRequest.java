package org.example.diplomicproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на оформление заказа")
public class OrderRequest {

    @Schema(description = "ID пользователя, оформляющего заказ", example = "1", required = true)
    private Long userId;

    @Schema(description = "ID продукта, добавленного в заказ", example = "10", required = true)
    private Long productId;

    @Schema(description = "Количество заказываемого товара", example = "2", required = true)
    private int quantity;

    @Schema(description = "Общая сумма заказа", example = "1999.99", required = true)
    private double totalAmount;
}
