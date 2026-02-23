package com.example.wms.tenant;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Aspect
@Component
public class TenantAspect {

    @PersistenceContext
    private EntityManager entityManager;

    // Pointcut for all methods in classes within the repository package
    @Pointcut("execution(* com.example.wms.repository..*(..))")
    public void repositoryMethods() {}

    @Before("repositoryMethods()")
    public void beforeRepositoryMethod() {
        String companyId = TenantContext.getCompanyId();
        if (companyId != null) {
            Session session = entityManager.unwrap(Session.class);
            session.enableFilter("tenantFilter").setParameter("companyId", companyId);
        }
    }
}