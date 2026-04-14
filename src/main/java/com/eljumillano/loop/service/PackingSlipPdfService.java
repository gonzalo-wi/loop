package com.eljumillano.loop.service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eljumillano.loop.exception.ResourceNotFoundException;
import com.eljumillano.loop.model.ControlProduct;
import com.eljumillano.loop.model.PackingSlip;
import com.eljumillano.loop.model.Sucursal;
import com.eljumillano.loop.model.User;
import com.eljumillano.loop.repository.PackingSlipRepository;
import com.eljumillano.loop.repository.SucursalRepository;
import com.eljumillano.loop.repository.UserRepository;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;

@Service
public class PackingSlipPdfService {

    private static final String PACKING_SLIP_NOT_FOUND = "Packing slip not found with id: ";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yy");

    @Autowired
    private PackingSlipRepository packingSlipRepository;

    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private UserRepository userRepository;

    public byte[] generatePdf(Long packingSlipId) {
        PackingSlip packingSlip = packingSlipRepository.findById(packingSlipId)
                .orElseThrow(() -> new ResourceNotFoundException(PACKING_SLIP_NOT_FOUND + packingSlipId));

        Sucursal sucursal = sucursalRepository.findById(packingSlip.getSucursalId())
                .orElse(null);
        
        User delivery = userRepository.findById(packingSlip.getDeliveryId())
                .orElse(null);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(30, 30, 30, 30);

            // Cabecera
            addHeader(document, packingSlip, sucursal);

            // Tabla de productos
            addProductTable(document, packingSlip);

            // Pie con campos de control y repartidor
            addFooter(document, packingSlip, delivery);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating packing slip PDF", e);
        }
    }

    private void addHeader(Document document, PackingSlip packingSlip, Sucursal sucursal) {
        // Tabla para layout de cabecera (3 columnas)
        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{35, 30, 35}))
                .useAllAvailableWidth()
                .setMarginBottom(15);

        // Columna izquierda - Datos de la empresa
        Paragraph empresaInfo = new Paragraph()
                .add(new Text("El Jumillano S.A.\n").setBold().setFontSize(16).setFontColor(ColorConstants.DARK_GRAY))
                .add(new Text("\nLiniers 3241\n").setFontSize(10))
                .add(new Text("(1702) Ciudadela\n").setFontSize(10))
                .add(new Text("Provincia de Bs. As.\n").setFontSize(10))
                .add(new Text("Tel. 0800-333-3090\n").setFontSize(10).setBold());

        if (sucursal != null && sucursal.getCuit() != null) {
            empresaInfo.add(new Text("\nCUIT: " + sucursal.getCuit()).setFontSize(9).setItalic());
        }

        headerTable.addCell(new Cell().add(empresaInfo)
                .setBorder(null)
                .setPaddingRight(10));

        // Columna central - Logo/Código IVESS con borde
        Paragraph logoCell = new Paragraph()
                .add(new Text("IVESS\n").setBold().setFontSize(14))
                .add(new Text("R\n").setBold().setFontSize(32).setFontColor(ColorConstants.RED))
                .add(new Text("\nCódigo ").setFontSize(9))
                .add(new Text("91").setFontSize(10).setBold())
                .setTextAlignment(TextAlignment.CENTER);

        headerTable.addCell(new Cell().add(logoCell)
                .setBorder(new SolidBorder(ColorConstants.BLACK, 2))
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(10));

        // Columna derecha - Packing Slip y fecha con fondo
        Paragraph packingSlipInfo = new Paragraph()
                .add(new Text("PACKING SLIP\n").setBold().setFontSize(18).setFontColor(ColorConstants.WHITE))
                .add(new Text("\nNº " + packingSlip.getSlipNumber() + "\n\n").setFontSize(14).setBold().setFontColor(ColorConstants.WHITE))
                .add(new Text("Fecha: ").setFontSize(11).setFontColor(ColorConstants.WHITE))
                .add(new Text(packingSlip.getCreatedAt().format(DATE_FORMATTER)).setFontSize(11).setBold().setFontColor(ColorConstants.WHITE))
                .setTextAlignment(TextAlignment.CENTER);

        headerTable.addCell(new Cell().add(packingSlipInfo)
                .setBorder(null)
                .setBackgroundColor(ColorConstants.DARK_GRAY)
                .setPadding(10));

        document.add(headerTable);
        
        // Línea separadora
        document.add(new Paragraph("\n").setMarginTop(0).setMarginBottom(5));
    }

    private void addProductTable(Document document, PackingSlip packingSlip) {
        // Tabla de productos (2 columnas: CANTIDAD y DESCRIPCION)
        Table table = new Table(UnitValue.createPercentArray(new float[]{20, 80}))
                .useAllAvailableWidth()
                .setMarginTop(10);

        // Encabezados con estilo mejorado
        table.addHeaderCell(new Cell()
                .add(new Paragraph("CANTIDAD").setBold().setFontSize(11))
                .setTextAlignment(TextAlignment.CENTER)
                .setBackgroundColor(ColorConstants.DARK_GRAY)
                .setFontColor(ColorConstants.WHITE)
                .setPadding(8)
                .setBorder(new com.itextpdf.layout.borders.SolidBorder(ColorConstants.BLACK, 1)));

        table.addHeaderCell(new Cell()
                .add(new Paragraph("DESCRIPCIÓN").setBold().setFontSize(11))
                .setTextAlignment(TextAlignment.CENTER)
                .setBackgroundColor(ColorConstants.DARK_GRAY)
                .setFontColor(ColorConstants.WHITE)
                .setPadding(8)
                .setBorder(new com.itextpdf.layout.borders.SolidBorder(ColorConstants.BLACK, 1)));

        // Productos del control con colores alternados
        boolean alternate = false;
        if (packingSlip.getControl() != null && packingSlip.getControl().getProducts() != null) {
            for (ControlProduct cp : packingSlip.getControl().getProducts()) {
                int totalQuantity = cp.getFullCount() + cp.getTotalCount();
                
                com.itextpdf.kernel.colors.Color bgColor = alternate ? 
                    new com.itextpdf.kernel.colors.DeviceRgb(245, 245, 245) : ColorConstants.WHITE;
                
                table.addCell(new Cell()
                        .add(new Paragraph(String.valueOf(totalQuantity)).setBold().setFontSize(12))
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBackgroundColor(bgColor)
                        .setPadding(6)
                        .setBorder(new com.itextpdf.layout.borders.SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f)));

                table.addCell(new Cell()
                        .add(new Paragraph(cp.getProduct().getName()).setFontSize(11))
                        .setBackgroundColor(bgColor)
                        .setPaddingLeft(10)
                        .setPadding(6)
                        .setBorder(new com.itextpdf.layout.borders.SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f)));
                
                alternate = !alternate;
            }
        }

        // Agregar filas vacías para llenar la página (mínimo 12 filas)
        int currentRows = packingSlip.getControl() != null && packingSlip.getControl().getProducts() != null 
                ? packingSlip.getControl().getProducts().size() : 0;
        
        for (int i = currentRows; i < 12; i++) {
            table.addCell(new Cell()
                    .add(new Paragraph(" ").setFontSize(11))
                    .setPadding(6)
                    .setBorder(new com.itextpdf.layout.borders.SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f)));
            table.addCell(new Cell()
                    .add(new Paragraph(" ").setFontSize(11))
                    .setPadding(6)
                    .setBorder(new com.itextpdf.layout.borders.SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f)));
        }

        document.add(table);
    }

    private void addFooter(Document document, PackingSlip packingSlip, User delivery) {
        // Espacio antes del footer
        document.add(new Paragraph("\n"));
        
        // Tabla para footer (2 columnas principales)
        Table footerTable = new Table(UnitValue.createPercentArray(new float[]{50, 50}))
                .useAllAvailableWidth()
                .setMarginTop(20);

        // Campo CONTROLÓ con línea para firma
        Paragraph controlo = new Paragraph()
                .add(new Text("CONTROLÓ:\n\n").setBold().setFontSize(10))
                .add(new Text("________________________________\n").setFontSize(10))
                .add(new Text("Firma y aclaración").setFontSize(8).setItalic().setFontColor(ColorConstants.GRAY));
        
        footerTable.addCell(new Cell()
                .add(controlo)
                .setBorder(null)
                .setPaddingRight(10)
                .setVerticalAlignment(VerticalAlignment.MIDDLE));

        // Campo REPARTIDOR con nombre
        String repartidorName = delivery != null ? delivery.getName() + " " + delivery.getLastName() : "___________________________";
        Paragraph repartidor = new Paragraph()
                .add(new Text("REPARTIDOR:\n\n").setBold().setFontSize(10))
                .add(new Text(repartidorName + "\n").setFontSize(11).setBold())
                .add(new Text("________________________________\n").setFontSize(10))
                .add(new Text("Firma").setFontSize(8).setItalic().setFontColor(ColorConstants.GRAY));
        
        footerTable.addCell(new Cell()
                .add(repartidor)
                .setBorder(null)
                .setPaddingLeft(10)
                .setVerticalAlignment(VerticalAlignment.MIDDLE));

        document.add(footerTable);

        // Información adicional de impresión con diseño mejorado
        Table printInfoTable = new Table(1).useAllAvailableWidth().setMarginTop(30);
        
        Paragraph printInfo = new Paragraph()
                .add(new Text("Nº " + packingSlip.getSlipNumber() + " | ").setFontSize(8).setBold())
                .add(new Text("Impreso: " + packingSlip.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))
                        .setFontSize(8))
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.GRAY);
        
        printInfoTable.addCell(new Cell()
                .add(printInfo)
                .setBorder(null)
                .setBorderTop(new com.itextpdf.layout.borders.SolidBorder(ColorConstants.LIGHT_GRAY, 1))
                .setPaddingTop(10));
        
        document.add(printInfoTable);
        
        // Marca de agua "DUPLICADO" en diagonal
        Paragraph duplicado = new Paragraph("DUPLICADO")
                .setFontSize(60)
                .setBold()
                .setFontColor(ColorConstants.LIGHT_GRAY)
                .setOpacity(0.3f)
                .setRotationAngle(Math.toRadians(45))
                .setFixedPosition(150, 400, 400)
                .setTextAlignment(TextAlignment.CENTER);
        
        document.add(duplicado);
    }
}
