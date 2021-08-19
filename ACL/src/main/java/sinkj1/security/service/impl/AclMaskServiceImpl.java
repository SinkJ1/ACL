package sinkj1.security.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sinkj1.security.domain.AclMask;
import sinkj1.security.repository.AclMaskRepository;
import sinkj1.security.service.AclMaskService;
import sinkj1.security.service.dto.AclMaskDTO;
import sinkj1.security.service.mapper.AclMaskMapper;

/**
 * Service Implementation for managing {@link AclMask}.
 */
@Service
@Transactional
public class AclMaskServiceImpl implements AclMaskService {

    private final Logger log = LoggerFactory.getLogger(AclMaskServiceImpl.class);

    private final AclMaskRepository aclMaskRepository;

    private final AclMaskMapper aclMaskMapper;

    public AclMaskServiceImpl(AclMaskRepository aclMaskRepository, AclMaskMapper aclMaskMapper) {
        this.aclMaskRepository = aclMaskRepository;
        this.aclMaskMapper = aclMaskMapper;
    }

    @Override
    public AclMaskDTO save(AclMaskDTO aclMaskDTO) {
        log.debug("Request to save AclMask : {}", aclMaskDTO);
        AclMask aclMask = aclMaskMapper.toEntity(aclMaskDTO);
        aclMask = aclMaskRepository.save(aclMask);
        return aclMaskMapper.toDto(aclMask);
    }

    @Override
    public Optional<AclMaskDTO> partialUpdate(AclMaskDTO aclMaskDTO) {
        log.debug("Request to partially update AclMask : {}", aclMaskDTO);

        return aclMaskRepository
            .findById(aclMaskDTO.getId())
            .map(
                existingAclMask -> {
                    aclMaskMapper.partialUpdate(existingAclMask, aclMaskDTO);

                    return existingAclMask;
                }
            )
            .map(aclMaskRepository::save)
            .map(aclMaskMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AclMaskDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AclMasks");
        return aclMaskRepository.findAll(pageable).map(aclMaskMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AclMaskDTO> findOne(Long id) {
        log.debug("Request to get AclMask : {}", id);
        return aclMaskRepository.findById(id).map(aclMaskMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AclMask : {}", id);
        aclMaskRepository.deleteById(id);
    }
}
