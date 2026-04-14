package com.eljumillano.loop.dtos.delivery;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO que representa un item de stock del servicio externo
 */
public class StockItemDTO {
    
    @JsonProperty("codproducto")
    private String codigoProducto;
    
    @JsonProperty("cantunidadescargo")
    private String cantidadUnidadesCargo;
    
    @JsonProperty("abrevproducto")
    private String abreviaturaProducto;
    
    @JsonProperty("cantbultoscargo")
    private String cantidadBultosCargo;

    public StockItemDTO() {
    }

    public StockItemDTO(String codigoProducto, String cantidadUnidadesCargo, 
                        String abreviaturaProducto, String cantidadBultosCargo) {
        this.codigoProducto = codigoProducto;
        this.cantidadUnidadesCargo = cantidadUnidadesCargo;
        this.abreviaturaProducto = abreviaturaProducto;
        this.cantidadBultosCargo = cantidadBultosCargo;
    }

    // Getters y Setters
    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getCantidadUnidadesCargo() {
        return cantidadUnidadesCargo;
    }

    public void setCantidadUnidadesCargo(String cantidadUnidadesCargo) {
        this.cantidadUnidadesCargo = cantidadUnidadesCargo;
    }

    public String getAbreviaturaProducto() {
        return abreviaturaProducto;
    }

    public void setAbreviaturaProducto(String abreviaturaProducto) {
        this.abreviaturaProducto = abreviaturaProducto;
    }

    public String getCantidadBultosCargo() {
        return cantidadBultosCargo;
    }

    public void setCantidadBultosCargo(String cantidadBultosCargo) {
        this.cantidadBultosCargo = cantidadBultosCargo;
    }

    @Override
    public String toString() {
        return "StockItemDTO{" +
                "codigoProducto='" + codigoProducto + '\'' +
                ", cantidadUnidadesCargo='" + cantidadUnidadesCargo + '\'' +
                ", abreviaturaProducto='" + abreviaturaProducto + '\'' +
                ", cantidadBultosCargo='" + cantidadBultosCargo + '\'' +
                '}';
    }
}
