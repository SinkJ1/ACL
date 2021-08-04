package sinkj1.security.domain;

public class PermissionVM {

    private Long entityId;
    private String permission;
    private String userCredentional;
    private BaseEntity baseEntity;


    public PermissionVM(Long entityId, String permission, String userCredentional, BaseEntity baseEntity) {
        this.entityId = entityId;
        this.permission = permission;
        this.userCredentional = userCredentional;
        this.baseEntity = baseEntity;
    }

    public PermissionVM() {
    }

    public Long getEntityId() {
        return entityId;
    }

    public String getPermission() {
        return permission;
    }

    public String getUserCredentional() {
        return userCredentional;
    }

    public BaseEntity getBaseEntity() {
        return baseEntity;
    }
}
