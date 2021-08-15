package sinkj1.security.service.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import sinkj1.security.domain.AclClass;
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
