package com.eljumillano.loop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eljumillano.loop.dtos.packingslip.PackingSlipDto;
import com.eljumillano.loop.exception.ResourceNotFoundException;
import com.eljumillano.loop.mapper.PackingSlipMapper;
import com.eljumillano.loop.model.Control;
import com.eljumillano.loop.model.Order;
import com.eljumillano.loop.model.PackingSlip;
import com.eljumillano.loop.model.Sucursal;
import com.eljumillano.loop.model.enums.PackingSlipStatus;
import com.eljumillano.loop.repository.ControlRepository;
import com.eljumillano.loop.repository.OrderRepository;
import com.eljumillano.loop.repository.PackingSlipRepository;
import com.eljumillano.loop.repository.SucursalRepository;
import com.eljumillano.loop.service.iservice.IPackingSlipService;

@Service
public class PackingSlipService implements IPackingSlipService {

    private static final String PACKING_SLIP_NOT_FOUND_ID = "Packing slip not found with id: ";
    private static final String PACKING_SLIP_NOT_FOUND_NUMBER = "Packing slip not found with number: ";
    private static final String CONTROL_NOT_FOUND = "Control no encontrado con id: ";
    private static final String ORDER_NOT_FOUND = "Order no encontrada con id: ";
    private static final String SUCURSAL_NOT_FOUND = "Sucursal no encontrada con id: ";
    private static final String SUCURSAL_NO_CODE = "La sucursal no tiene código de packing slip configurado";

    @Autowired
    private PackingSlipRepository packingSlipRepository;

    @Autowired
    private ControlRepository controlRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private PackingSlipMapper packingSlipMapper;

    @Autowired
    private PackingSlipPdfService packingSlipPdfService;

    @Override
    @Transactional(readOnly = true)
    public PackingSlipDto getPackingSlipById(Long id) {
        PackingSlip packingSlip = packingSlipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PACKING_SLIP_NOT_FOUND_ID + id));
        return packingSlipMapper.toDto(packingSlip);
    }

    @Override
    @Transactional(readOnly = true)
    public PackingSlipDto getPackingSlipByNumber(String slipNumber) {
        PackingSlip packingSlip = packingSlipRepository.findBySlipNumber(slipNumber)
                .orElseThrow(() -> new ResourceNotFoundException(PACKING_SLIP_NOT_FOUND_NUMBER + slipNumber));
        return packingSlipMapper.toDto(packingSlip);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PackingSlipDto> getPackingSlipsByDeliveryId(Long deliveryId) {
        return packingSlipRepository.findByDeliveryId(deliveryId).stream()
                .map(packingSlipMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PackingSlipDto> getPackingSlipsByControlId(Long controlId) {
        return packingSlipRepository.findByControlId(controlId).stream()
                .map(packingSlipMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public PackingSlipDto createPackingSlipFromControl(Long controlId, Long orderId) {
        Control control = controlRepository.findById(controlId)
                .orElseThrow(() -> new ResourceNotFoundException(CONTROL_NOT_FOUND + controlId));
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(ORDER_NOT_FOUND + orderId));

        PackingSlip packingSlip = new PackingSlip();
        packingSlip.setControl(control);
        packingSlip.setOrder(order);
        packingSlip.setDeliveryId(control.getDeliveryId());
        packingSlip.setSupervisorId(control.getSupervisorId());
        packingSlip.setSucursalId(control.getSucursalId());
        packingSlip.setStatus(PackingSlipStatus.ACTIVE);
        
        // Generar número de packing slip
        String slipNumber = generateSlipNumber(control.getSucursalId());
        packingSlip.setSlipNumber(slipNumber);

        PackingSlip savedPackingSlip = packingSlipRepository.save(packingSlip);
        return packingSlipMapper.toDto(savedPackingSlip);
    }

    @Override
    @Transactional
    public PackingSlipDto createPackingSlipFromControl(Long controlId) {
        System.out.println("📦 Creando packing slip para control ID: " + controlId);
        
        Control control = controlRepository.findById(controlId)
                .orElseThrow(() -> new ResourceNotFoundException(CONTROL_NOT_FOUND + controlId));

        System.out.println("✓ Control encontrado - Sucursal ID: " + control.getSucursalId());

        PackingSlip packingSlip = new PackingSlip();
        packingSlip.setControl(control);
        packingSlip.setOrder(null);  // Sin orden asociada
        packingSlip.setDeliveryId(control.getDeliveryId());
        packingSlip.setSupervisorId(control.getSupervisorId());
        packingSlip.setSucursalId(control.getSucursalId());
        packingSlip.setStatus(PackingSlipStatus.ACTIVE);
        
        // Generar número de packing slip
        System.out.println("🔢 Generando número de packing slip...");
        String slipNumber = generateSlipNumber(control.getSucursalId());
        System.out.println("✓ Número generado: " + slipNumber);
        packingSlip.setSlipNumber(slipNumber);

        System.out.println("💾 Guardando packing slip...");
        PackingSlip savedPackingSlip = packingSlipRepository.save(packingSlip);
        System.out.println("✓ Packing slip guardado con ID: " + savedPackingSlip.getId());
        
        return packingSlipMapper.toDto(savedPackingSlip);
    }

    @Override
    @Transactional
    public void cancelPackingSlip(Long id) {
        PackingSlip packingSlip = packingSlipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PACKING_SLIP_NOT_FOUND_ID + id));
        packingSlip.setStatus(PackingSlipStatus.CANCELLED);
        packingSlipRepository.save(packingSlip);
    }

    @Override
    public byte[] generatePackingSlipPdf(Long packingSlipId) {
        return packingSlipPdfService.generatePdf(packingSlipId);
    }

    private String generateSlipNumber(Long sucursalId) {
        // Obtener la sucursal para usar su código
        Sucursal sucursal = sucursalRepository.findById(sucursalId)
                .orElseThrow(() -> new ResourceNotFoundException(SUCURSAL_NOT_FOUND + sucursalId));
        
        // Usar código de sucursal o valor por defecto
        String sucursalCode = sucursal.getPackingSlipCode();
        if (sucursalCode == null || sucursalCode.trim().isEmpty()) {
            System.err.println("⚠️ WARNING: Sucursal " + sucursalId + " no tiene packingSlipCode configurado. Usando código por defecto: SUC" + sucursalId);
            sucursalCode = "SUC" + String.format("%03d", sucursalId);
        }
        
        // Obtener el último consecutivo para la sucursal
        Long maxConsecutivo = packingSlipRepository.findMaxConsecutivoBySucursal(sucursalId);
        long nextConsecutivo = (maxConsecutivo != null ? maxConsecutivo : 0) + 1;
        
        // Formato: 00202-00345476 o SUC001-00000001 (código sucursal-consecutivo)
        String consecutivo = String.format("%08d", nextConsecutivo);
        
        return sucursalCode + "-" + consecutivo;
    }
}
