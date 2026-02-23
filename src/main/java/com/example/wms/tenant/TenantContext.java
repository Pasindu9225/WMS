package com.example.wms.tenant;

/**
 * Requirement 3: Implement ThreadLocal-based TenantContext class
 */
public class TenantContext {
    private static final ThreadLocal<String> currentGroupId = new ThreadLocal<>();
    private static final ThreadLocal<String> currentCompanyId = new ThreadLocal<>();

    public static void setGroupId(String groupId) {
        currentGroupId.set(groupId);
    }

    public static String getGroupId() {
        return currentGroupId.get();
    }

    public static void setCompanyId(String companyId) {
        currentCompanyId.set(companyId);
    }

    public static String getCompanyId() {
        return currentCompanyId.get();
    }

    /**
     * Requirement 15: Ensure tenant context is cleared after each request
     */
    public static void clear() {
        currentGroupId.remove();
        currentCompanyId.remove();
    }
}