package in.stackroute.productservicebackend.service;

import in.stackroute.productservicebackend.model.Product;
import in.stackroute.productservicebackend.exceptions.ProductNotFoundException;
import in.stackroute.productservicebackend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product save(Product product) {
        log.debug("Saving product to database {} ", product);
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAll(){
        return productRepository.findAll();
    }

    @Override
    public Product findById(int productId) {
        log.debug("Finding product with id {}", productId);
        return productRepository
                .findById(productId)
                .orElseThrow(
                        () -> new ProductNotFoundException("Cannot find product with id " + productId
                                + " in the database"));
    }

    @Override
    public Product update(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteById(int productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public Optional<Product> findBySkuCode(String skuCode) {
        log.debug("Finding product with skuCode {}", skuCode);
        return productRepository.findBySkuCode(skuCode);
    }

    @Override
    public List<Product> findByProductName(String productName) {
        return productRepository.findByProductName(productName);
    }
}
