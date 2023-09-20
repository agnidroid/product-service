package in.stackroute.productservicebackend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record ProductDto(
        int productId,
        @NotBlank(message = "Product must have a name") String productName,
        @NotBlank(message = "Product must have a name") String skuCode,
        @NotBlank(message = "Product must have a description") String description,
        @DecimalMin(message = "Unit price must be at least 1.0", value = "1.0") float unitPrice
) {
}
