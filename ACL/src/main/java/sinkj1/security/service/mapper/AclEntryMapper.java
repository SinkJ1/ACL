package sinkj1.security.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sinkj1.security.domain.AclEntry;
import sinkj1.security.service.dto.AclEntryDTO;

/**
 * Mapper for the entity {@link AclEntry} and its DTO {@link AclEntryDTO}.
 */
@Mapper(componentModel = "spring", uses = { AclSidMapper.class, AclObjectIdentityMapper.class, AclMaskMapper.class })
public interface AclEntryMapper extends EntityMapper<AclEntryDTO, AclEntry> {
    @Mapping(target = "aclSid", source = "aclSid", qualifiedByName = "sid")
    @Mapping(target = "aclObjectIdentity", source = "aclObjectIdentity", qualifiedByName = "id")
    @Mapping(target = "mask", source = "aclMask", qualifiedByName = "id")
    AclEntryDTO toDto(AclEntry s);
}
