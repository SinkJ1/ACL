package sinkj1.security.multi_tenancy.config.tenant.liquibase;

import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import sinkj1.security.domain.Tenant;
import sinkj1.security.multi_tenancy.repository.TenantRepository;
import sinkj1.security.repository.TenantCrudRepository;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;

/**
 * Based on MultiTenantSpringLiquibase, this class provides Liquibase support for
 * multi-tenancy based on a dynamic collection of DataSources.
 */
public class DynamicSchemaBasedMultiTenantSpringLiquibase implements InitializingBean, ResourceLoaderAware {

    @Autowired
    private TenantCrudRepository masterTenantRepository;

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier("tenantLiquibaseProperties")
    private LiquibaseProperties liquibaseProperties;

    private ResourceLoader resourceLoader;

    @Override
    public void afterPropertiesSet() throws Exception {
       // log.info("Schema based multitenancy enabled");
        this.runOnAllSchemas(dataSource, masterTenantRepository.findAll());
    }

    protected void runOnAllSchemas(DataSource dataSource, List<sinkj1.security.domain.Tenant> tenants) throws LiquibaseException {
        for(Tenant tenant : tenants) {
          //  log.info("Initializing Liquibase for tenant " + tenant.getTenantId());
            SpringLiquibase liquibase = this.getSpringLiquibase(dataSource, tenant.getSchema());
            liquibase.afterPropertiesSet();
            //log.info("Liquibase ran for tenant " + tenant.getTenantId());
        }
    }

    protected SpringLiquibase getSpringLiquibase(DataSource dataSource, String schema) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setResourceLoader(getResourceLoader());
        liquibase.setDataSource(dataSource);
        liquibase.setDefaultSchema(schema);
        liquibase.setChangeLog(liquibaseProperties.getChangeLog());
        liquibase.setContexts(liquibaseProperties.getContexts());
        liquibase.setLiquibaseSchema(liquibaseProperties.getLiquibaseSchema());
        liquibase.setLiquibaseTablespace(liquibaseProperties.getLiquibaseTablespace());
        liquibase.setDatabaseChangeLogTable(liquibaseProperties.getDatabaseChangeLogTable());
        liquibase.setDatabaseChangeLogLockTable(liquibaseProperties.getDatabaseChangeLogLockTable());
        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
        liquibase.setShouldRun(liquibaseProperties.isEnabled());
        liquibase.setLabels(liquibaseProperties.getLabels());
        liquibase.setChangeLogParameters(liquibaseProperties.getParameters());
        liquibase.setRollbackFile(liquibaseProperties.getRollbackFile());
        liquibase.setTestRollbackOnUpdate(liquibaseProperties.isTestRollbackOnUpdate());
        return liquibase;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

}
