package com.eljumillano.loop.repository.sqlserver;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
public class AguasInRepository {
    
    @PersistenceContext(unitName = "sqlserver")
    private EntityManager entityManager;
    
    /**
     * Prueba la conexión ejecutando una consulta simple
     */
    @Transactional(value = "sqlServerTransactionManager", readOnly = true)
    public boolean testConnection() {
        try {
            Integer result = (Integer) entityManager
                .createNativeQuery("SELECT 1")
                .getSingleResult();
            return result != null && result == 1;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Ejecuta una consulta SQL personalizada y devuelve los resultados
     */
    @Transactional(value = "sqlServerTransactionManager", readOnly = true)
    public List<?> executeQuery(String sqlQuery) {
        return entityManager.createNativeQuery(sqlQuery).getResultList();
    }
    
    /**
     * Obtiene información de la base de datos
     */
    @Transactional(value = "sqlServerTransactionManager", readOnly = true)
    public Map<String, String> getDatabaseInfo() {
        String query = "SELECT @@VERSION as version, DB_NAME() as database_name, @@SERVERNAME as server_name";
        Object[] result = (Object[]) entityManager.createNativeQuery(query).getSingleResult();
        return Map.of(
            "version", result[0] != null ? result[0].toString() : "N/A",
            "database_name", result[1] != null ? result[1].toString() : "N/A",
            "server_name", result[2] != null ? result[2].toString() : "N/A"
        );
    }
}
