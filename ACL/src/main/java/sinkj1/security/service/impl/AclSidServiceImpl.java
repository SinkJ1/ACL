package sinkj1.security.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sinkj1.security.domain.AclSid;
import sinkj1.security.repository.AclSidRepository;
import sinkj1.security.service.AclSidService;
import sinkj1.security.service.dto.AclSidDTO;
import sinkj1.security.service.mapper.AclSidMapper;

/**
 * Service Implementation for managing {@link AclSid}.
 */
@Service
@Transactional
public class AclSidServiceImpl implements AclSidService {

    private final Logger log = LoggerFactory.getLogger(AclSidServiceImpl.class);

    private final AclSidRepository aclSidRepository;

    private final AclSidMapper aclSidMapper;

    public AclSidServiceImpl(AclSidRepository aclSidRepository, AclSidMapper aclSidMapper) {
        this.aclSidRepository = aclSidRepository;
        this.aclSidMapper = aclSidMapper;
    }

    @Override
    public AclSidDTO save(AclSidDTO aclSidDTO) {
        log.debug("Request to save AclSid : {}", aclSidDTO);
        AclSid aclSid = aclSidMapper.toEntity(aclSidDTO);
        aclSid = aclSidRepository.save(aclSid);
        return aclSidMapper.toDto(aclSid);
    }

    @Override
    public Optional<AclSidDTO> partialUpdate(AclSidDTO aclSidDTO) {
        log.debug("Request to partially update AclSid : {}", aclSidDTO);

        return aclSidRepository
            .findById(aclSidDTO.getId())
            .map(
                existingAclSid -> {
                    aclSidMapper.partialUpdate(existingAclSid, aclSidDTO);

                    return existingAclSid;
                }
            )
            .map(aclSidRepository::save)
            .map(aclSidMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AclSidDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AclSids");
        return aclSidRepository.findAll(pageable).map(aclSidMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AclSidDTO> findOne(Long id) {
        log.debug("Request to get AclSid : {}", id);
        return aclSidRepository.findById(id).map(aclSidMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AclSid : {}", id);
        aclSidRepository.deleteById(id);
    }
}
