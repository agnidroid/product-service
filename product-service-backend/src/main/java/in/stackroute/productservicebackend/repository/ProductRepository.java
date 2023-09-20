package in.stackroute.productservicebackend.repository;

import in.stackroute.productservicebackend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    // select p from Product p where p.skuCode = ?1
    Optional<Product> findBySkuCode(String skuCode);

    @Query("select p from Product p where p.productName like %?1%")
    List<Product> findByProductName(String productName);

}
