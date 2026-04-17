package com.eljumillano.loop.service;

import com.eljumillano.loop.repository.sqlserver.AguasInRepository;
import com.eljumillano.loop.service.iservice.IAguasInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AguasInService implements IAguasInService {
    
    @Autowired
    private AguasInRepository aguasInRepository;
    
    @Override
    public boolean testConnection() {
        try {
            return aguasInRepository.testConnection();
        } catch (Exception e) {
            System.err.println("Error al probar conexión SQL Server: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public Map<String, String> getDatabaseInfo() {
        try {
            return aguasInRepository.getDatabaseInfo();
        } catch (Exception e) {
            System.err.println("Error al obtener información de la base de datos: " + e.getMessage());
            return Map.of(
                "error", e.getMessage(),
                "status", "failed"
            );
        }
    }
    
    @Override
    public List<?> executeCustomQuery(String sqlQuery) {
        return aguasInRepository.executeQuery(sqlQuery);
    }
}
