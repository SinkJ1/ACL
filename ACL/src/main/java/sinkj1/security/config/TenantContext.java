package sinkj1.security.config;


public final class TenantContext {

    private TenantContext() {}

    private static final InheritableThreadLocal<String> currentTenant = new InheritableThreadLocal<>();

    public static void setTenantId(String tenantId) {
        //log.debug("Setting tenantId to " + tenantId);
        currentTenant.set(tenantId);
    }

    public static String getTenantId() {
        return currentTenant.get() == null ? "public" : currentTenant.get();
    }

    public static void clear(){
        currentTenant.remove();
    }
}
