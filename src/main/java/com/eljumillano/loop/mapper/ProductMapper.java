package com.eljumillano.loop.mapper;
import org.springframework.stereotype.Component;
import com.eljumillano.loop.dtos.product.ProductDto;
import com.eljumillano.loop.model.Product;

@Component
public class ProductMapper {

    public ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setCodProduct(product.getCodProduct());
        dto.setOrder(product.getOrder());
        dto.setPack(product.getPack());
        dto.setReturnable(product.isReturnable());
        dto.setCreatedAt(product.getCreatedAt());
        return dto;
    }


    public Product toEntity(ProductDto dto) {
        if (dto == null) {
            return null;
        }
        Product product = new Product();
        product.setName(dto.getName());
        product.setCodProduct(dto.getCodProduct());
        product.setOrder(dto.getOrder());
        product.setPack(dto.getPack());
        product.setReturnable(dto.isReturnable());
        return product;
    }

    
    public void updateEntity(ProductDto dto, Product product) {
        if (dto == null || product == null) {
            return;
        }
        product.setName(dto.getName());
        product.setCodProduct(dto.getCodProduct());
        product.setOrder(dto.getOrder());
        product.setPack(dto.getPack());
        product.setReturnable(dto.isReturnable());
    }
}
