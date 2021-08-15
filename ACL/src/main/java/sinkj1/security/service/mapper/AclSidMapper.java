package sinkj1.security.service.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import sinkj1.security.domain.AclSid;
import sinkj1.security.service.dto.AclSidDTO;

/**
 * Mapper for the entity {@link AclSid} and its DTO {@link AclSidDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AclSidMapper extends EntityMapper<AclSidDTO, AclSid> {
    @Named("sid")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "sid", source = "sid")
    AclSidDTO toDtoSid(AclSid aclSid);
}
