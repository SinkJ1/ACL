package sinkj1.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Service;
import sinkj1.security.domain.*;
import sinkj1.security.repository.*;
import sinkj1.security.service.AclService;

import java.util.Date;
import java.util.Optional;

@Service
public class AclServiceImpl implements AclService {

    @Autowired
    private final AclEntryRepository aclEntryRepository;

    @Autowired
    private final AclClassRepository aclClassRepository;

    @Autowired
    private final AclSidRepository aclSidRepository;
    private final AclMaskRepository aclMaskRepository;

    @Autowired
    private final AclObjectIdentityRepository aclObjectIdentityRepository;

    public AclServiceImpl(AclEntryRepository aclEntryRepository, AclClassRepository aclClassRepository, AclSidRepository aclSidRepository, AclMaskRepository aclMaskRepository, AclObjectIdentityRepository aclObjectIdentityRepository) {
        this.aclEntryRepository = aclEntryRepository;
        this.aclClassRepository = aclClassRepository;
        this.aclSidRepository = aclSidRepository;
        this.aclMaskRepository = aclMaskRepository;
        this.aclObjectIdentityRepository = aclObjectIdentityRepository;
    }

    public void createPermission(Long id, String className, Permission permission, AclSid sid) {

        Optional<AclClass> findAclClass = aclClassRepository.findByName(className);
        Optional<AclSid> findAclSid = aclSidRepository.findByName(sid.getSid());
        AclMask aclMask = aclMaskRepository.findById((long) permission.getMask()).get();

        if (!findAclClass.isPresent()) {
            aclClassRepository.save(new AclClass(className));
        }

        if (!findAclSid.isPresent()) {
            aclSidRepository.save(sid);
        }
        Optional<AclObjectIdentity> aclObjectIdentity = aclObjectIdentityRepository.getByClassIdAndIdIdentityFields(id.toString(), (int) (long) aclClassRepository.findByName(className).get().getId());

        if (!aclObjectIdentity.isPresent()) {
            AclObjectIdentity aclObjectIdentity1 = new AclObjectIdentity(id.toString(), (int) (long) aclSidRepository.findByName(sid.getSid()).get().getId(), true, aclClassRepository.findByName(className).get());
            aclObjectIdentityRepository.save(aclObjectIdentity1);
        }
        Optional<AclObjectIdentity> aclObjectIdentityOptional = aclObjectIdentityRepository.getByClassIdAndIdIdentityFields(id.toString(), (int) (long) aclClassRepository.findByName(className).get().getId());

        Optional<AclEntry> aclEntry = aclEntryRepository.findByObjectIdentitySidAndMask((int)(long)aclObjectIdentityOptional.get().getId(),(int)(long)aclSidRepository.findByName(sid.getSid()).get().getId(),permission.getMask());

        if(!aclEntry.isPresent()){
            aclEntryRepository.save(new AclEntry(0, aclMask, true, false, false, aclSidRepository.findByName(sid.getSid()).get(), aclObjectIdentityRepository.getByClassIdAndIdIdentityFields(id.toString(), (int) (long) aclClassRepository.findByName(className).get().getId()).get()));
        }

    }


}
