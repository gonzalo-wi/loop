package com.eljumillano.loop.service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eljumillano.loop.dtos.control.ControlDto;
import com.eljumillano.loop.dtos.packingslip.PackingSlipDto;
import com.eljumillano.loop.exception.ResourceNotFoundException;
import com.eljumillano.loop.mapper.ControlMapper;
import com.eljumillano.loop.model.Control;
import com.eljumillano.loop.model.enums.TypeControl;
import com.eljumillano.loop.repository.postgres.ControlRepository;
import com.eljumillano.loop.repository.postgres.ProductRepository;
import com.eljumillano.loop.model.Product;
import com.eljumillano.loop.service.iservice.IControlService;
import com.eljumillano.loop.service.iservice.IPackingSlipService;

@Service
public class ControlService implements IControlService {

private static final String MESSAGE_ENTITY_NAME        = "Control";
private static final String DELIVERY_DATE_INVALID_PAST = "La fecha de entrega no puede ser anterior a hoy";
private static final String DELIVERY_DATE_INVALID_IN   = "Los controles IN no deben tener fecha de entrega";

// Fecha de trabajo global para controles OUT (en memoria, resetea al reiniciar)
private volatile LocalDate globalDeliveryDate = null;

@Autowired
private ControlMapper     controlMapper;

@Autowired
private ControlRepository controlRepository;

@Autowired
private ProductRepository productRepository;

@Autowired
private IPackingSlipService packingSlipService;


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
        validateAndSetDeliveryDate(control);
        Map<Long, Product> productsMap = loadProducts(control);
        var entity = controlMapper.toEntity(control, productsMap);
        var savedControl = controlRepository.save(entity);
        
        // Si es control OUT, crear automáticamente los packing slips para las órdenes del día
        if (savedControl.getTypeControl() == TypeControl.OUT) {
            createPackingSlipsForControl(savedControl);
        }
        
        return controlMapper.toDto(savedControl);
    }


    @Override
    public ControlDto updateControl(Long id, ControlDto control) {
        var existingControl = controlRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE_ENTITY_NAME, id));
        validateAndSetDeliveryDate(control);
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

    private void validateAndSetDeliveryDate(ControlDto control) {
        if (control.getTypeControl() == TypeControl.OUT) {
            // Si no viene fecha para control OUT, usar fecha global configurada o mañana
            if (control.getDeliveryDate() == null) {
                control.setDeliveryDate(globalDeliveryDate != null ? globalDeliveryDate : LocalDate.now().plusDays(1));
            } else if (control.getDeliveryDate().isBefore(LocalDate.now())) {
                // Validar que la fecha no sea en el pasado
                throw new IllegalArgumentException(DELIVERY_DATE_INVALID_PAST);
            }
        } else if (control.getTypeControl() == TypeControl.IN) {
            // Los controles IN no deben tener fecha de entrega
            if (control.getDeliveryDate() != null) {
                throw new IllegalArgumentException(DELIVERY_DATE_INVALID_IN);
            }
        }
    }

    @Override
    public void setGlobalDeliveryDate(LocalDate date) {
        if (date != null && date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(DELIVERY_DATE_INVALID_PAST);
        }
        this.globalDeliveryDate = date;
    }

    @Override
    public LocalDate getGlobalDeliveryDate() {
        return globalDeliveryDate != null ? globalDeliveryDate : LocalDate.now().plusDays(1);
    }

    /**
     * Crea automáticamente el packing slip para el control OUT
     */
    private void createPackingSlipsForControl(Control control) {
        System.out.println("=== INICIO createPackingSlipsForControl ===");
        System.out.println("Control ID: " + control.getId());
        System.out.println("Delivery ID: " + control.getDeliveryId());
        System.out.println("Sucursal ID: " + control.getSucursalId());
        System.out.println("Type: " + control.getTypeControl());
        
        if (control.getDeliveryId() == null) {
            System.out.println("❌ No hay delivery ID, saliendo...");
            return; // No hay delivery, no se puede crear packing slip
        }
        
        try {
            System.out.println("➡️ Creando packing slip para control ID: " + control.getId());
            PackingSlipDto packingSlip = packingSlipService.createPackingSlipFromControl(control.getId());
            System.out.println("✅ Packing slip creado exitosamente!");
            System.out.println("   - ID: " + packingSlip.getId());
            System.out.println("   - Número: " + packingSlip.getSlipNumber());
        } catch (Exception e) {
            System.err.println("❌ ERROR creando packing slip para control " + control.getId());
            System.err.println("   Tipo de error: " + e.getClass().getSimpleName());
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=== FIN createPackingSlipsForControl ===");
    }
}
