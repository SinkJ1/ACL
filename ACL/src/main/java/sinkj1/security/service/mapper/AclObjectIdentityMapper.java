package sinkj1.security.service.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import sinkj1.security.domain.AclObjectIdentity;
import sinkj1.security.service.dto.AclObjectIdentityDTO;

/**
 * Mapper for the entity {@link AclObjectIdentity} and its DTO {@link AclObjectIdentityDTO}.
 */
@Mapper(componentModel = "spring", uses = { AclClassMapper.class })
public interface AclObjectIdentityMapper extends EntityMapper<AclObjectIdentityDTO, AclObjectIdentity> {
    @Mapping(target = "aclClass", source = "aclClass", qualifiedByName = "className")
    AclObjectIdentityDTO toDto(AclObjectIdentity s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AclObjectIdentityDTO toDtoId(AclObjectIdentity aclObjectIdentity);
}
