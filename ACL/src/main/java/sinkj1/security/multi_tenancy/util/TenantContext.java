package sinkj1.security.multi_tenancy.util;


public final class TenantContext {

    private TenantContext() {}

    private static final InheritableThreadLocal<String> currentTenant = new InheritableThreadLocal<>();

    public static void setTenantId(String tenantId) {
//        log.debug("Setting tenantId to " + tenantId);
        currentTenant.set(tenantId);
    }

    public static String getTenantId() {
        return currentTenant.get();
    }

    public static void clear(){
        currentTenant.remove();
    }
}
