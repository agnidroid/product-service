package in.stackroute.productservicebackend.controller;

import in.stackroute.productservicebackend.dto.ProductDto;
import in.stackroute.productservicebackend.exceptions.ProductAlreadyExists;
import in.stackroute.productservicebackend.exceptions.ProductNotFoundException;
import in.stackroute.productservicebackend.service.ProductService;
import in.stackroute.productservicebackend.util.ServiceUtility;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ServiceUtility util;
    private final ProductService productService;
    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    // Create product
    // POST http://localhost:8080/api/v1/product
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto dto) {
        var product = util.toEntity(dto);
        productService.findBySkuCode(product.getSkuCode()).ifPresent(p -> {
            throw new ProductAlreadyExists("Product with skuCode " + product.getSkuCode() + " already exists");
        });
        var savedProduct = productService.save(product);
        var savedProductDto = util.toDto(savedProduct);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(savedProductDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDto>> getAllProduct(){
        var productList=productService.getAll();
        var productDtoList = productList.stream().map(product -> util.toDto(product)).toList();
        return ResponseEntity.ok(productDtoList);
    }

    // GET http://localhost:8080/api/v1/products/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable("id") int id) {
        var product = productService.findById(id);
        var productDto = util.toDto(product);
        return ResponseEntity.ok(productDto);
    }

    // GET GET http://localhost:8080/api/v1/products?name=Apple
    @GetMapping
    public ResponseEntity<List<ProductDto>> getProductByName(@RequestParam("name") String name) {
        var productList = productService.findByProductName(name);
        if (productList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var productDtoList = productList.stream().map(product -> util.toDto(product)).toList();
        return ResponseEntity.ok(productDtoList);
    }

    // GET http://localhost:8080/api/v1/products?skuCode=ABC123
    @GetMapping(params = "skuCode")
    public ResponseEntity<ProductDto> getProductBySkuCode(@RequestParam("skuCode") String skuCode) {
        var product = productService.findBySkuCode(skuCode)
                .orElseThrow(() -> new ProductNotFoundException("Product with skuCode " + skuCode + " not found"));
        var productDto = util.toDto(product);
        return ResponseEntity.ok(productDto);
    }

    // PUT http://localhost:8080/api/v1/products/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("id") int id, @RequestBody ProductDto dto) {
        var product = util.toEntity(dto);
        product.setProductId(id);
        var updatedProduct = productService.update(product);
        var updatedProductDto = util.toDto(updatedProduct);
        return ResponseEntity.ok(updatedProductDto);
    }

    // DELETE http://localhost:8080/api/v1/products/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") int id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
