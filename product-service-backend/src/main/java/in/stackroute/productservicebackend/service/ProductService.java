package in.stackroute.productservicebackend.service;

import in.stackroute.productservicebackend.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Product save(Product product);

    List<Product> getAll();

    Product findById(int productId);

    Product update(Product product);

    void deleteById(int productId);

    Optional<Product> findBySkuCode(String skuCode);

    List<Product> findByProductName(String productName);

}
