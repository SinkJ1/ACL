package sinkj1.security.service.impl;

import io.swagger.models.auth.In;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Service;
import sinkj1.security.domain.*;
import sinkj1.security.repository.*;
import sinkj1.security.service.AclService;
import sinkj1.security.service.dto.DeletePermissionDto;
import sinkj1.security.service.dto.PermissionDto;

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

    public AclServiceImpl(
        AclEntryRepository aclEntryRepository,
        AclClassRepository aclClassRepository,
        AclSidRepository aclSidRepository,
        AclMaskRepository aclMaskRepository,
        AclObjectIdentityRepository aclObjectIdentityRepository
    ) {
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
        if (permission.getMask() == 4) {
            id = 0000L;
        }
        Optional<AclObjectIdentity> aclObjectIdentity = aclObjectIdentityRepository.getByClassIdAndIdIdentityFields(
            (int) (long) id,
            (int) (long) aclClassRepository.findByName(className).get().getId()
        );

        if (!aclObjectIdentity.isPresent()) {
            AclObjectIdentity aclObjectIdentity1 = new AclObjectIdentity(
                (int) (long) id,
                (int) (long) aclSidRepository.findByName(sid.getSid()).get().getId(),
                true,
                aclClassRepository.findByName(className).get()
            );
            aclObjectIdentityRepository.save(aclObjectIdentity1);
        }
        Optional<AclObjectIdentity> aclObjectIdentityOptional = aclObjectIdentityRepository.getByClassIdAndIdIdentityFields(
            (int) (long) id,
            (int) (long) aclClassRepository.findByName(className).get().getId()
        );

        Optional<AclEntry> aclEntry = aclEntryRepository.findByObjectIdentitySidAndMask(
            (int) (long) aclObjectIdentityOptional.get().getId(),
            (int) (long) aclSidRepository.findByName(sid.getSid()).get().getId(),
            permission.getMask()
        );

        if (!aclEntry.isPresent()) {
            aclEntryRepository.save(
                new AclEntry(
                    true,
                    aclSidRepository.findByName(sid.getSid()).get(),
                    aclObjectIdentityRepository
                        .getByClassIdAndIdIdentityFields(
                            (int) (long) id,
                            (int) (long) aclClassRepository.findByName(className).get().getId()
                        )
                        .get(),
                    aclMask
                )
            );
        }
    }

    @Override
    public void createPermissions(List<PermissionDto> permissionDtos) {
        List<String> classNames = permissionDtos.stream().map(PermissionDto::getClassName).collect(Collectors.toList());
        List<String> sidsNames = permissionDtos.stream().map(PermissionDto::getSid).collect(Collectors.toList());
        List<Long> entitiesIds = permissionDtos.stream().map(PermissionDto::getId).collect(Collectors.toList());
        List<Integer> masks = permissionDtos.stream().map(PermissionDto::getPermission).collect(Collectors.toList());

        List<AclClass> aclClasses = saveAclClasses(classNames);
        List<AclSid> aclSids = saveAclSids(sidsNames);
        List<AclObjectIdentity> aclObjectIdentities = saveAclObjectIdentities(entitiesIds, classNames);
        saveAclEntry(entitiesIds, masks, sidsNames, classNames);
    }

    @Override
    public void deletePermission(DeletePermissionDto deletePermissionDto) {
        Optional<AclMask> aclMask = aclMaskRepository.findById((long) deletePermissionDto.getPermission());
        List<Integer> aclMasks = new ArrayList<>();
        aclMasks.add(deletePermissionDto.getPermission());
        List<String> userAuthority = new ArrayList<>();
        userAuthority.add(deletePermissionDto.getUser());
        Optional<AclEntry> aclEntry = aclEntryRepository.findEntryForUser(
            aclMasks,
            deletePermissionDto.getEntityId(),
            deletePermissionDto.getEntityClassName(),
            userAuthority
        );

        if (aclEntry.isPresent()) {
            aclEntryRepository.deleteById(aclEntry.get().getId());
        }
    }

    private List<AclClass> saveAclClasses(List<String> classNames) {
        Set<String> aclClassSet = new HashSet<>(classNames);
        Set<AclClass> aclClasses = aclClassRepository.findByNames(aclClassSet);

        Set<AclClass> newAclClasses = aclClassSet.stream().map(AclClass::new).collect(Collectors.toSet());
        Set<AclClass> aclClassToSave = new HashSet<>();
        if (aclClasses.size() == 0) {
            aclClassRepository.saveAll(newAclClasses);
        } else {
            Set<AclClass> bufferSet = new HashSet<>();
            for (AclClass aclClass : newAclClasses) {
                for (AclClass oldAclClass : aclClasses) {
                    if (oldAclClass.getClassName().equalsIgnoreCase(aclClass.getClassName())) {
                        bufferSet.add(oldAclClass);
                    }
                }
                if (bufferSet.isEmpty()) {
                    aclClassToSave.add(aclClass);
                }
                bufferSet.clear();
            }
        }
        return aclClassRepository.saveAll(aclClassToSave);
    }

    private List<AclSid> saveAclSids(List<String> aclSidsNamesString) {
        Set<String> aclSidsNamesSet = new HashSet<>(aclSidsNamesString);
        Set<AclSid> aclSids = aclSidRepository.findByNames(aclSidsNamesSet);

        Set<AclSid> newAclSids = aclSidsNamesSet.stream().map(AclSid::new).collect(Collectors.toSet());
        Set<AclSid> aclSidToSave = new HashSet<>();
        if (aclSids.size() == 0) {
            aclSidRepository.saveAll(newAclSids);
        } else {
            Set<AclSid> bufferSet = new HashSet<>();
            for (AclSid aclSid : newAclSids) {
                for (AclSid oldAclSid : aclSids) {
                    if (oldAclSid.getSid().equalsIgnoreCase(aclSid.getSid())) {
                        bufferSet.add(oldAclSid);
                    }
                }
                if (bufferSet.isEmpty()) {
                    aclSidToSave.add(aclSid);
                }
                bufferSet.clear();
            }
        }
        return aclSidRepository.saveAll(aclSidToSave);
    }

    private List<AclObjectIdentity> saveAclObjectIdentities(List<Long> entitiesIds, List<String> classNames) {
        List<Long> aclClassIds = aclClassRepository.getIdsByNames(classNames);
        List<AclObjectIdentity> aclObjectIdentities = aclObjectIdentityRepository.getByClassIdAndIdIdentityFieldsList(
            entitiesIds,
            aclClassIds
        );
        List<AclObjectIdentity> newAclObjectIdentities = new ArrayList<>();

        for (int i = 0; i < entitiesIds.size(); i++) {
            Optional<AclClass> aclClass = aclClassRepository.findByName(classNames.get(i));
            newAclObjectIdentities.add(new AclObjectIdentity((int) (long) entitiesIds.get(i), 1, true, aclClass.get()));
        }

        List<AclObjectIdentity> forSaveList = new ArrayList<>();
        if (aclObjectIdentities.isEmpty()) {
            aclObjectIdentityRepository.saveAll(newAclObjectIdentities);
        } else {
            List<AclObjectIdentity> bufferList = new ArrayList<>();
            for (AclObjectIdentity newAclObjectIdentity : newAclObjectIdentities) {
                for (AclObjectIdentity oldAclObjectIdentity : aclObjectIdentities) {
                    if (
                        Objects.equals(newAclObjectIdentity.getObjectIdIdentity(), oldAclObjectIdentity.getObjectIdIdentity()) &&
                        Objects.equals(newAclObjectIdentity.getAclClass().getId(), oldAclObjectIdentity.getAclClass().getId())
                    ) {
                        bufferList.add(newAclObjectIdentity);
                    }
                }
                if (bufferList.isEmpty()) {
                    forSaveList.add(newAclObjectIdentity);
                }
                bufferList.clear();
            }
        }

        return aclObjectIdentityRepository.saveAll(forSaveList);
    }

    private List<AclEntry> saveAclEntry(List<Long> entitiesId, List<Integer> masks, List<String> sidsNames, List<String> classNames) {
        Set<AclSid> aclSids = aclSidRepository.findByNames(new HashSet<>(sidsNames));
        List<AclSid> aclSidList = new ArrayList<>(aclSids);
        List<Integer> aclSidsIds = aclSids.stream().map(aclSid -> (int) (long) aclSid.getId()).collect(Collectors.toList());
        List<Integer> entitiesIdsInt = entitiesId.stream().map(aLong -> (int) (long) aLong).collect(Collectors.toList());
        List<AclEntry> aclEntries = aclEntryRepository.findByObjectIdentitySidAndMaskList(entitiesIdsInt, aclSidsIds, masks);
        List<Long> aclClassIds = aclClassRepository.getIdsByNames(classNames);
        List<AclObjectIdentity> aclObjectIdentities = aclObjectIdentityRepository.getByClassIdAndIdIdentityFieldsList(
            entitiesId,
            aclClassIds
        );

        List<AclEntry> toSave = new ArrayList<>();

        List<AclMask> aclMasks = aclMaskRepository.findAll();

        for (int i = 0; i < masks.size(); i++) {
            int finalI = i;
            toSave.add(
                new AclEntry(
                    true,
                    aclSidList.get(i),
                    aclObjectIdentities.get(i),
                    aclMasks.stream().filter(aclMask1 -> aclMask1.getId() == (long) (int) masks.get(finalI)).findFirst().get()
                )
            );
        }

        List<AclEntry> newAclEntries = new ArrayList<>();

        if (aclEntries.isEmpty()) {
            aclEntryRepository.saveAll(toSave);
        } else {
            List<AclEntry> bufferEntries = new ArrayList<>();
            for (AclEntry newAclEntry : toSave) {
                for (AclEntry oldAclEntry : aclEntries) {
                    if (
                        newAclEntry.getAclMask() == oldAclEntry.getAclMask() &&
                        newAclEntry.getAclSid() == oldAclEntry.getAclSid() &&
                        newAclEntry.getAclObjectIdentity() == oldAclEntry.getAclObjectIdentity()
                    ) {
                        bufferEntries.add(newAclEntry);
                    }
                }
                if (bufferEntries.isEmpty()) {
                    newAclEntries.add(newAclEntry);
                }
                bufferEntries.clear();
            }
        }
        return aclEntryRepository.saveAll(newAclEntries);
    }
}
