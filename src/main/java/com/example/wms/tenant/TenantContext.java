package com.example.wms.tenant;


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


    public static void clear() {
        currentGroupId.remove();
        currentCompanyId.remove();
    }
}