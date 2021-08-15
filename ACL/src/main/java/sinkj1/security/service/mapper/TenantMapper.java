package sinkj1.security.service.mapper;

import org.mapstruct.Mapper;
import sinkj1.security.domain.Tenant;
import sinkj1.security.service.dto.TenantDTO;

/**
 * Mapper for the entity {@link Tenant} and its DTO {@link TenantDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TenantMapper extends EntityMapper<TenantDTO, Tenant> {}
