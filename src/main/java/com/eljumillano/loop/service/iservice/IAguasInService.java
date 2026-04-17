package com.eljumillano.loop.service.iservice;

import java.util.List;
import java.util.Map;

public interface IAguasInService {
    
    boolean testConnection();
    
    Map<String, String> getDatabaseInfo();
    
    List<?> executeCustomQuery(String sqlQuery);
}
