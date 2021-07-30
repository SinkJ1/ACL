package sinkj1.security.service.mapper;

import org.mapstruct.*;
import sinkj1.security.domain.*;
import sinkj1.security.service.dto.AclEntryDTO;

/**
 * Mapper for the entity {@link AclEntry} and its DTO {@link AclEntryDTO}.
 */
@Mapper(componentModel = "spring", uses = { AclSidMapper.class, AclObjectIdentityMapper.class })
public interface AclEntryMapper extends EntityMapper<AclEntryDTO, AclEntry> {
    @Mapping(target = "aclSid", source = "aclSid", qualifiedByName = "sid")
    @Mapping(target = "aclObjectIdentity", source = "aclObjectIdentity", qualifiedByName = "id")
    AclEntryDTO toDto(AclEntry s);
}
