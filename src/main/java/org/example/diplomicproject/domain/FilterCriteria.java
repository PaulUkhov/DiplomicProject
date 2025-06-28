package org.example.diplomicproject.domain;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;



@Data
@Schema(description = "Критерии фильтрации товаров")
public class FilterCriteria {

    @Schema(description = "Минимальная цена", example = "100.00")
    private BigDecimal minPrice;

    @Schema(description = "Максимальная цена", example = "1000.00")
    private BigDecimal maxPrice;

    @Schema(description = "Категория товара", example = "Электроника")
    private String category;

    @Schema(description = "Минимальный рейтинг", example = "3")
    private int minRating;
}
