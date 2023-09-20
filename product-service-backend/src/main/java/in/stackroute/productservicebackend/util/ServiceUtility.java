package in.stackroute.productservicebackend.util;

import in.stackroute.productservicebackend.dto.ProductDto;
import in.stackroute.productservicebackend.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ServiceUtility {

    public Product toEntity(ProductDto dto) {
        return Product
                .builder()
                .productName(dto.productName())
                .skuCode(dto.skuCode())
                .description(dto.description())
                .unitPrice(dto.unitPrice())
                .build();
    }

    public ProductDto toDto(Product p) {
        return new ProductDto(p.getProductId(), p.getProductName(),p.getSkuCode(),
                p.getDescription(), p.getUnitPrice());
    }
}
