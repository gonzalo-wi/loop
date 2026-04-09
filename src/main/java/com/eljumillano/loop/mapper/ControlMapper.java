package com.eljumillano.loop.mapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.eljumillano.loop.dtos.control.ControlDto;
import com.eljumillano.loop.dtos.control.ControlProductDto;
import com.eljumillano.loop.model.Control;
import com.eljumillano.loop.model.ControlProduct;
import com.eljumillano.loop.model.Product;
import com.eljumillano.loop.repository.ProductRepository;

@Component
public class ControlMapper {

    @Autowired
    private ProductRepository productRepository;

    public ControlDto toDto(Control control) {
        if (control == null) {
            return null;
        }
        ControlDto dto = new ControlDto();
        dto.setId(control.getId());
        dto.setDeliveryId(control.getDeliveryId());
        dto.setSupervisorId(control.getSupervisorId());
        dto.setSucursalId(control.getSucursalId());
        dto.setTypeControl(control.getTypeControl());
        dto.setCreatedAt(control.getCreatedAt());
        if (control.getProducts() != null) {
            List<ControlProductDto> products = control.getProducts().stream()
                    .map(this::toProductDto)
                    .toList();
            dto.setProducts(products);
        }
        return dto;
    }

    public Control toEntity(ControlDto dto) {
        if (dto == null) {
            return null;
        }
        Control control = new Control();
        control.setDeliveryId(dto.getDeliveryId());
        control.setSupervisorId(dto.getSupervisorId());
        control.setSucursalId(dto.getSucursalId());
        control.setTypeControl(dto.getTypeControl());
        if (dto.getProducts() != null) {
            List<ControlProduct> products = dto.getProducts().stream()
                    .map(p -> toProductEntity(p, control))
                    .toList();
            control.setProducts(products);
        }
        return control;
    }

    public void updateEntity(ControlDto dto, Control control) {
        if (dto == null || control == null) {
            return;
        }
        control.setDeliveryId(dto.getDeliveryId());
        control.setSupervisorId(dto.getSupervisorId());
        control.setSucursalId(dto.getSucursalId());
        control.setTypeControl(dto.getTypeControl());
        if (dto.getProducts() != null) {
            control.getProducts().clear();
            dto.getProducts().stream()
                    .map(p -> toProductEntity(p, control))
                    .forEach(control.getProducts()::add);
        }
    }

    private ControlProductDto toProductDto(ControlProduct cp) {
        ControlProductDto dto = new ControlProductDto();
        dto.setId(cp.getId());
        dto.setProductId(cp.getProduct().getId());
        dto.setProductName(cp.getProduct().getName());
        dto.setFullCount(cp.getFullCount());
        dto.setTotalCount(cp.getTotalCount());
        dto.setReplacements(cp.getReplacements());
        return dto;
    }

    private ControlProduct toProductEntity(ControlProductDto dto, Control control) {
        var controlProduct = new ControlProduct();
        controlProduct.setControl(control);
        Product product = productRepository.getReferenceById(dto.getProductId());
        controlProduct.setProduct(product);
        controlProduct.setFullCount(dto.getFullCount());
        controlProduct.setTotalCount(dto.getTotalCount());
        controlProduct.setReplacements(dto.getReplacements());
        return controlProduct;
    }
}
