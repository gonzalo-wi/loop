package com.eljumillano.loop.controller;

import com.eljumillano.loop.service.iservice.IAguasInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/aguas-in")
@CrossOrigin(origins = "*")
public class AguasInController {
    
    @Autowired
    private IAguasInService aguasInService;
    
    /**
     * Endpoint de prueba para verificar la conexión con SQL Server
     * GET /api/aguas-in/test-connection
     */
    @GetMapping("/test-connection")
    public ResponseEntity<Map<String, Object>> testConnection() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean isConnected = aguasInService.testConnection();
            
            if (isConnected) {
                response.put("status", "success");
                response.put("message", "Conexión exitosa con SQL Server (H2O_JUMI)");
                response.put("connected", true);
                
                // Obtener información adicional de la base de datos
                try {
                    Map<String, String> dbInfo = aguasInService.getDatabaseInfo();
                    response.put("databaseInfo", dbInfo);
                } catch (Exception e) {
                    response.put("databaseInfo", "No disponible");
                }
                
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "No se pudo establecer conexión con SQL Server");
                response.put("connected", false);
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error al probar la conexión: " + e.getMessage());
            response.put("connected", false);
            response.put("errorDetails", e.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Ejecutar una consulta SQL personalizada
     * POST /api/aguas-in/query
     */
    @PostMapping("/query")
    public ResponseEntity<Map<String, Object>> executeQuery(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String sqlQuery = request.get("query");
            
            if (sqlQuery == null || sqlQuery.trim().isEmpty()) {
                response.put("status", "error");
                response.put("message", "La consulta SQL es requerida");
                return ResponseEntity.badRequest().body(response);
            }
            
            List<?> results = aguasInService.executeCustomQuery(sqlQuery);
            
            response.put("status", "success");
            response.put("message", "Consulta ejecutada correctamente");
            response.put("results", results);
            response.put("count", results.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error al ejecutar la consulta: " + e.getMessage());
            response.put("errorDetails", e.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
