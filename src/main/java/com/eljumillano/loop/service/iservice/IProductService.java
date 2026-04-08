package com.eljumillano.loop.service.iservice;
import java.util.List;
import com.eljumillano.loop.dtos.product.ProductDto;

public interface IProductService {
    List<ProductDto> getAllProducts();
    ProductDto getProductById(Long id);
    ProductDto createProduct(ProductDto product);
    ProductDto updateProduct(Long id, ProductDto product);
    void deleteProduct(Long id);
}
