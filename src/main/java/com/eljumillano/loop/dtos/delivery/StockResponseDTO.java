package com.eljumillano.loop.dtos.delivery;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * DTO que representa la respuesta completa del servicio de stock
 */
public class StockResponseDTO {
    
    @JsonProperty("data")
    private List<StockItemDTO> data;
    
    @JsonProperty("cantidad")
    private Integer cantidad;

    public StockResponseDTO() {
    }

    public StockResponseDTO(List<StockItemDTO> data, Integer cantidad) {
        this.data = data;
        this.cantidad = cantidad;
    }

    public List<StockItemDTO> getData() {
        return data;
    }

    public void setData(List<StockItemDTO> data) {
        this.data = data;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "StockResponseDTO{" +
                "data=" + data +
                ", cantidad=" + cantidad +
                '}';
    }
}
