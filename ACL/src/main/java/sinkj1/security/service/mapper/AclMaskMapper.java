package sinkj1.security.service.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import sinkj1.security.domain.AclMask;
import sinkj1.security.service.dto.AclMaskDTO;

/**
 * Mapper for the entity {@link AclMask} and its DTO {@link AclMaskDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AclMaskMapper extends EntityMapper<AclMaskDTO, AclMask> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    AclMaskDTO toDtoId(AclMask aclMask);
}
