package sinkj1.security.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sinkj1.security.domain.AclClass;
import sinkj1.security.repository.AclClassRepository;
import sinkj1.security.service.AclClassService;
import sinkj1.security.service.dto.AclClassDTO;
import sinkj1.security.service.mapper.AclClassMapper;

/**
 * Service Implementation for managing {@link AclClass}.
 */
@Service
@Transactional
public class AclClassServiceImpl implements AclClassService {

    private final Logger log = LoggerFactory.getLogger(AclClassServiceImpl.class);

    private final AclClassRepository aclClassRepository;

    private final AclClassMapper aclClassMapper;

    public AclClassServiceImpl(AclClassRepository aclClassRepository, AclClassMapper aclClassMapper) {
        this.aclClassRepository = aclClassRepository;
        this.aclClassMapper = aclClassMapper;
    }

    @Override
    public AclClassDTO save(AclClassDTO aclClassDTO) {
        log.debug("Request to save AclClass : {}", aclClassDTO);
        AclClass aclClass = aclClassMapper.toEntity(aclClassDTO);
        aclClass = aclClassRepository.save(aclClass);
        return aclClassMapper.toDto(aclClass);
    }

    @Override
    public Optional<AclClassDTO> partialUpdate(AclClassDTO aclClassDTO) {
        log.debug("Request to partially update AclClass : {}", aclClassDTO);

        return aclClassRepository
            .findById(aclClassDTO.getId())
            .map(
                existingAclClass -> {
                    aclClassMapper.partialUpdate(existingAclClass, aclClassDTO);

                    return existingAclClass;
                }
            )
            .map(aclClassRepository::save)
            .map(aclClassMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AclClassDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AclClasses");
        return aclClassRepository.findAll(pageable).map(aclClassMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AclClassDTO> findOne(Long id) {
        log.debug("Request to get AclClass : {}", id);
        return aclClassRepository.findById(id).map(aclClassMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AclClass : {}", id);
        aclClassRepository.deleteById(id);
    }
}
