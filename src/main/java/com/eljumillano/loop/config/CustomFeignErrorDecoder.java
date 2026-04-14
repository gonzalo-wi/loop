package com.eljumillano.loop.config;

import com.eljumillano.loop.exception.ResourceNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CustomFeignErrorDecoder implements ErrorDecoder {

    private static final Logger log = LoggerFactory.getLogger(CustomFeignErrorDecoder.class);
    private static final String MSG_ERROR_FEIGN              = "Error en llamada Feign: {} - Status: {} - Reason: {}";
    private static final String ERROR_SOLICITUD_INVALIDA     = "Solicitud inválida: %s";
    private static final String ERROR_RECURSO_NO_ENCONTRADO  = "Recurso no encontrado: %s";
    private static final String ERROR_INTERNO_SERVIDOR       = "Error interno del servidor: %s";
    private static final String ERROR_SERVICIO_NO_DISPONIBLE = "Servicio no disponible: %s";
    
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        log.error(MSG_ERROR_FEIGN, methodKey, response.status(), response.reason());

        switch (response.status()) {
            case 400:
                return new IllegalArgumentException(String.format(ERROR_SOLICITUD_INVALIDA, response.reason()));
            case 404:
                return new ResourceNotFoundException(String.format(ERROR_RECURSO_NO_ENCONTRADO, response.reason()));
            case 500:
                return new RuntimeException(String.format(ERROR_INTERNO_SERVIDOR, response.reason()));
            case 503:
                return new RuntimeException(String.format(ERROR_SERVICIO_NO_DISPONIBLE, response.reason()));
            default:
                return defaultErrorDecoder.decode(methodKey, response);
        }
    }
}
