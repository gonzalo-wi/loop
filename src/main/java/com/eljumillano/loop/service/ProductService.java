package com.eljumillano.loop.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eljumillano.loop.dtos.product.ProductDto;
import com.eljumillano.loop.exception.ResourceNotFoundException;
import com.eljumillano.loop.mapper.ProductMapper;
import com.eljumillano.loop.model.Product;
import com.eljumillano.loop.repository.postgres.ProductRepository;
import com.eljumillano.loop.service.iservice.IProductService;

@Service
public class ProductService implements IProductService {

    private static final String MESSAGE_ENTITY_NAME = "Product";

    @Autowired
    private  ProductRepository productRepository;

    @Autowired
    private  ProductMapper     productMapper;



    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAllByOrderByOrderAsc()
                .stream()
                .map(productMapper::toDto)
                .toList();
    }


    @Override
    public ProductDto getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE_ENTITY_NAME, id));
    }


    @Override
    public ProductDto createProduct(ProductDto dto) {
        var product = productMapper.toEntity(dto);
        return productMapper.toDto(productRepository.save(product));
    }

    
    @Override
    public ProductDto updateProduct(Long id, ProductDto dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE_ENTITY_NAME, id));
        productMapper.updateEntity(dto, product);
        return productMapper.toDto(productRepository.save(product));
    }


    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException(MESSAGE_ENTITY_NAME, id);
        }
        productRepository.deleteById(id);
    }
    
}
