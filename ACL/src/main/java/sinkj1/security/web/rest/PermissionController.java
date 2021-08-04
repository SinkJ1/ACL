package sinkj1.security.web.rest;


import org.springframework.http.ResponseEntity;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    public PermissionController(PermissionService permissionService, AclPermissionEvaluator aclPermissionEvaluator, CastingService castingService) {
        this.permissionService = permissionService;
        this.aclPermissionEvaluator = aclPermissionEvaluator;
        this.castingService = castingService;
    }

    @PostMapping("/permission/authority")
    public ResponseEntity<String> addPermissionForAuthority(@RequestBody PermissionDto permissionDto) {
        permissionService.addPermissionForUser(permissionDto.getId(),permissionDto.getClassName(), convertFromStringToBasePermission(permissionDto.getPermission()), permissionDto.getSid());
        return ResponseEntity
            .noContent()
            .build();
    }

    @PostMapping("/permission/check")
    public ResponseEntity<Boolean> checkPermission(@RequestBody CheckPermissionDto test) throws IllegalAccessException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Boolean ans = aclPermissionEvaluator.hasPermission(authentication,test.getCustomObjectIdentity(),test.getPermission());
        return ResponseEntity.ok(ans);
    }



    private Permission convertFromStringToBasePermission(String permission){

        switch (permission) {
            case "WRITE":
                return BasePermission.WRITE;
            case "ADMINISTRATION":
                return BasePermission.ADMINISTRATION;
            case "CREATE":
                return BasePermission.CREATE;
            case "DELETE":
                return BasePermission.DELETE;
            default:
                return BasePermission.READ;
        }
    }


}
