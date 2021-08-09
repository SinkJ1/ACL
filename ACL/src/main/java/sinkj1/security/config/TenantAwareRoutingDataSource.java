package sinkj1.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class TenantAwareRoutingDataSource /*extends AbstractRoutingDataSource */{

    /*@Override
    protected Object determineCurrentLookupKey() {
        return ThreadLocalStorage.getTenantName();
    }*/
}
