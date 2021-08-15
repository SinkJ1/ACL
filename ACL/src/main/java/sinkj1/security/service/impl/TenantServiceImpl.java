package sinkj1.security.service.impl;

import java.sql.*;
import java.util.Optional;
import javax.sql.DataSource;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sinkj1.security.domain.Tenant;
import sinkj1.security.repository.TenantRepository;
import sinkj1.security.service.TenantCreationException;
import sinkj1.security.service.TenantService;
import sinkj1.security.service.dto.TenantDTO;
import sinkj1.security.service.mapper.TenantMapper;

@Service
public class TenantServiceImpl implements TenantService {

    private final Logger log = LoggerFactory.getLogger(TenantServiceImpl.class);
    private final TenantMapper tenantMapper;

    private final DataSource dataSource;
    private final LiquibaseProperties liquibaseProperties;
    private final ResourceLoader resourceLoader;
    private final TenantRepository tenantRepository;

    @Autowired
    public TenantServiceImpl(
        TenantMapper tenantMapper,
        DataSource dataSource,
        @Qualifier("tenantLiquibaseProperties") LiquibaseProperties liquibaseProperties,
        ResourceLoader resourceLoader,
        TenantRepository tenantRepository
    ) {
        this.tenantMapper = tenantMapper;
        this.dataSource = dataSource;
        this.liquibaseProperties = liquibaseProperties;
        this.resourceLoader = resourceLoader;
        this.tenantRepository = tenantRepository;
    }

    private static final String VALID_SCHEMA_NAME_REGEXP = "[A-Za-z0-9_]*";

    @Override
    public TenantDTO save(TenantDTO tenantDTO) throws SQLException {
        log.debug("Request to save Tenant : {}", tenantDTO);

        Tenant oldTenant = tenantRepository.findById(tenantDTO.getId()).get();

        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "ALTER SCHEMA " + oldTenant.getSchema() + " RENAME TO " + tenantDTO.getSchema()
        );
        statement.execute();
        connection.commit();
        connection.close();

        Tenant tenant = tenantMapper.toEntity(tenantDTO);
        tenant = tenantRepository.save(tenant);
        return tenantMapper.toDto(tenant);
    }

    @Override
    public TenantDTO createTenant(TenantDTO tenantDTO) {
        // Verify schema string to prevent SQL injection
        Tenant savedTenant = new Tenant();
        String schema = tenantDTO.getSchema();
        String tenantId = tenantDTO.getTenantId();

        if (!schema.matches(VALID_SCHEMA_NAME_REGEXP)) {
            throw new TenantCreationException("Invalid schema name: " + schema);
        }

        try {
            createSchema(schema);
            Tenant tenant = new Tenant(tenantId, schema);
            savedTenant = tenantRepository.save(tenant);
            runLiquibase(dataSource, schema);
        } catch (DataAccessException e) {
            throw new TenantCreationException("Error when creating schema: " + schema, e);
        } catch (LiquibaseException e) {
            throw new TenantCreationException("Error when populating schema: ", e);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return tenantMapper.toDto(savedTenant);
    }

    private void runLiquibase(DataSource dataSource, String schema) throws LiquibaseException, SQLException {
        SpringLiquibase liquibase = getSpringLiquibase(dataSource, schema);
        liquibase.afterPropertiesSet();
    }

    private void createSchema(String schema) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("CREATE SCHEMA " + schema.toLowerCase());
        statement.execute();
        connection.commit();
        connection.close();
    }

    protected SpringLiquibase getSpringLiquibase(DataSource dataSource, String schema) throws SQLException {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setResourceLoader(resourceLoader);
        liquibase.setDataSource(dataSource);
        liquibase.setDefaultSchema(schema.toLowerCase());
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
    public Tenant findByTenantId(String tenantId) {
        return tenantRepository.findByTenantId(tenantId).orElseThrow(() -> new RuntimeException("No such tenant: " + tenantId));
    }

    @Override
    public Optional<TenantDTO> partialUpdate(TenantDTO tenantDTO) {
        log.debug("Request to partially update Tenant : {}", tenantDTO);

        return tenantRepository
            .findById(tenantDTO.getId())
            .map(
                existingTenant -> {
                    tenantMapper.partialUpdate(existingTenant, tenantDTO);

                    return existingTenant;
                }
            )
            .map(tenantRepository::save)
            .map(tenantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TenantDTO> findOne(Long id) {
        log.debug("Request to get Tenant : {}", id);
        return tenantRepository.findById(id).map(tenantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TenantDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tenants");
        return tenantRepository.findAll(pageable).map(tenantMapper::toDto);
    }

    @Override
    public void delete(Long id) throws SQLException {
        log.debug("Request to delete Tenant : {}", id);
        Tenant tenant = tenantRepository.findById(id).orElseThrow(() -> new RuntimeException("No such tenant: " + id));
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("drop SCHEMA " + tenant.getSchema().toLowerCase() + " cascade");
        statement.execute();
        connection.commit();
        connection.close();
        tenantRepository.deleteById(id);
    }
}
