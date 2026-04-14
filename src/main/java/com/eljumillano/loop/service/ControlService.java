package com.eljumillano.loop.service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eljumillano.loop.dtos.control.ControlDto;
import com.eljumillano.loop.exception.ResourceNotFoundException;
import com.eljumillano.loop.mapper.ControlMapper;
import com.eljumillano.loop.model.Product;
import com.eljumillano.loop.repository.ControlRepository;
import com.eljumillano.loop.repository.ProductRepository;
import com.eljumillano.loop.service.iservice.IControlService;

@Service
public class ControlService implements IControlService {

private static final String MESSAGE_ENTITY_NAME = "Control";

@Autowired
private ControlMapper     controlMapper;

@Autowired
private ControlRepository controlRepository;

@Autowired
private ProductRepository productRepository;


    @Override
    public List<ControlDto> getAllControls() {
        return controlRepository.findAll().stream().map(controlMapper::toDto).toList();
    }

    
    @Override
    public ControlDto getControlById(Long id) {
        return controlRepository.findById(id)
                .map(controlMapper::toDto).orElseThrow(() -> new ResourceNotFoundException(MESSAGE_ENTITY_NAME, id));
    }


    @Override
    public ControlDto createControl(ControlDto control) {
        Map<Long, Product> productsMap = loadProducts(control);
        var entity = controlMapper.toEntity(control, productsMap);
        return controlMapper.toDto(controlRepository.save(entity));
    }


    @Override
    public ControlDto updateControl(Long id, ControlDto control) {
        var existingControl = controlRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE_ENTITY_NAME, id));
        Map<Long, Product> productsMap = loadProducts(control);
        controlMapper.updateEntity(control, existingControl, productsMap);
        return controlMapper.toDto(controlRepository.save(existingControl));
    }


    @Override
    public void deleteControl(Long id) {
        var existingControl = controlRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE_ENTITY_NAME, id));
        controlRepository.delete(existingControl);
    }

    private Map<Long, Product> loadProducts(ControlDto control) {
        return (control.getProducts() == null || control.getProducts().isEmpty())
                ? Map.of()
                : productRepository.findAllById(
                        control.getProducts().stream()
                                .map(p -> p.getProductId())
                                .toList()
                  ).stream()
                  .collect(Collectors.toMap(Product::getId, p -> p));
    }
}
