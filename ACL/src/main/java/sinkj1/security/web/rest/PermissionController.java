package sinkj1.security.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sinkj1.security.config.AclPermissionEvaluator;
import sinkj1.security.service.CastingService;
import sinkj1.security.service.PermissionService;
import sinkj1.security.service.dto.CheckPermissionDto;
import sinkj1.security.service.dto.PermissionDto;

@RestController
@RequestMapping("/api")
public class PermissionController {

    private final PermissionService permissionService;
    private final AclPermissionEvaluator aclPermissionEvaluator;
    private final CastingService castingService;

    public PermissionController(
        PermissionService permissionService,
        AclPermissionEvaluator aclPermissionEvaluator,
        CastingService castingService
    ) {
        this.permissionService = permissionService;
        this.aclPermissionEvaluator = aclPermissionEvaluator;
        this.castingService = castingService;
    }

    @PostMapping("/permission/authority")
    public ResponseEntity<String> addPermissionForAuthority(
        @RequestHeader(value = "X-TENANT-ID", required = false) String tenantId,
        @RequestBody PermissionDto permissionDto
    ) {
        permissionService.addPermissionForAuthority(
            permissionDto.getId(),
            permissionDto.getClassName(),
            convertFromIntToBasePermission(permissionDto.getPermission()),
            permissionDto.getSid()
        );
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/permission/user")
    public ResponseEntity<String> addPermissionForUser(
        @RequestHeader(value = "X-TENANT-ID", required = false) String tenantId,
        @RequestBody PermissionDto permissionDto
    ) {
        permissionService.addPermissionForUser(
            permissionDto.getId(),
            permissionDto.getClassName(),
            convertFromIntToBasePermission(permissionDto.getPermission()),
            permissionDto.getSid()
        );
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/permission/check")
    public ResponseEntity<Boolean> checkPermission(
        @RequestHeader(value = "X-TENANT-ID", required = false) String tenantId,
        @RequestBody CheckPermissionDto test
    ) throws IllegalAccessException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Boolean ans = aclPermissionEvaluator.hasPermission(authentication, test.getCustomObjectIdentity(), test.getPermission());
        return ResponseEntity.ok(ans);
    }

    private Permission convertFromIntToBasePermission(int permission) {
        switch (permission) {
            case 2:
                return BasePermission.WRITE;
            case 4:
                return BasePermission.CREATE;
            case 8:
                return BasePermission.DELETE;
            case 16:
                return BasePermission.ADMINISTRATION;
            default:
                return BasePermission.READ;
        }
    }
}
