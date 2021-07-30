package sinkj1.security.service.mapper;

import org.mapstruct.*;
import sinkj1.security.domain.*;
import sinkj1.security.service.dto.AclClassDTO;

/**
 * Mapper for the entity {@link AclClass} and its DTO {@link AclClassDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AclClassMapper extends EntityMapper<AclClassDTO, AclClass> {
    @Named("className")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "className", source = "className")
    AclClassDTO toDtoClassName(AclClass aclClass);
}
