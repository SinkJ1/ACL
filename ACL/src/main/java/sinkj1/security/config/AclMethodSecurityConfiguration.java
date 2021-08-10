package sinkj1.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionCacheOptimizer;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class AclMethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    @Autowired
    private DataSource dataSource;

    private String deleteEntryByObjectIdentityForeignKey = "delete from acl_entry where acl_object_identity=?";
    private String deleteObjectIdentityByPrimaryKey = "delete from acl_object_identity where id=?";
    private String insertClass = "insert into acl_class (class) values (?)";
    private String insertEntry = "insert into acl_entry "
        + "(acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)"
        + "values (?, ?, ?, ?, ?, ?, ?)";
    private String insertObjectIdentity = "insert into acl_object_identity "
        + "(object_id_class, object_id_identity, owner_sid, entries_inheriting) "
        + "values (?, ?, ?, ?)";
    private String insertSid = "insert into acl_sid (principal, sid) values (?, ?)";
    private String selectClassPrimaryKey = "select id from acl_class where class=?";
    private String selectObjectIdentityPrimaryKey = "select acl_object_identity.id from acl_object_identity, acl_class "
        + "where acl_object_identity.object_id_class = acl_class.id and acl_class.class=? "
        + "and acl_object_identity.object_id_identity = ?";
    private String selectSidPrimaryKey = "select acl_sid.id from acl_sid where principal=? and sid=?";
    private String updateObjectIdentity = "update acl_object_identity set "
        + "parent_object = ?, owner_sid = ?, entries_inheriting = ?"
        + " where id = ?";

    private final static String DEFAULT_LOOKUP_KEYS_WHERE_CLAUSE = "(acl_object_identity.id = ?)";

    public final static String DEFAULT_ORDER_BY_CLAUSE = ") order by acl_object_identity.object_id_identity"
        + " asc, acl_entry.ace_order asc";

    @Bean
    public MutableAclService aclService() {

        JdbcMutableAclService jdbcMutableAclService = new JdbcMutableAclService(dataSource, lookupStrategy(), aclCache());
        jdbcMutableAclService.setSidIdentityQuery("SELECT currval('acl_sid_id_seq')");
        jdbcMutableAclService.setClassIdentityQuery("SELECT currval('acl_class_id_seq')");
        jdbcMutableAclService.setDeleteObjectIdentityByPrimaryKeySql(deleteObjectIdentityByPrimaryKey);
        jdbcMutableAclService.setDeleteEntryByObjectIdentityForeignKeySql(deleteEntryByObjectIdentityForeignKey);
        jdbcMutableAclService.setInsertClassSql(insertClass);

        jdbcMutableAclService.setInsertEntrySql(insertEntry);
        jdbcMutableAclService.setInsertObjectIdentitySql(insertObjectIdentity);
        jdbcMutableAclService.setInsertSidSql(insertSid);
        jdbcMutableAclService.setClassPrimaryKeyQuery(selectClassPrimaryKey);
        jdbcMutableAclService.setObjectIdentityPrimaryKeyQuery(selectObjectIdentityPrimaryKey);
        jdbcMutableAclService.setSidPrimaryKeyQuery(selectSidPrimaryKey);
        jdbcMutableAclService.setAclClassIdSupported(true);

        return jdbcMutableAclService;
    }

    @Bean
    public LookupStrategy lookupStrategy() {

        BasicLookupStrategy basicLookupStrategy = new BasicLookupStrategy(dataSource, aclCache(),
            aclAuthorizationStrategy(), new ConsoleAuditLogger());

        String lookupObjectIdentitiesWhereClause = "(acl_object_identity.object_id_identity::varchar(36) = ? and acl_class.class = ?)";
        basicLookupStrategy.setLookupObjectIdentitiesWhereClause(lookupObjectIdentitiesWhereClause);
        basicLookupStrategy.setLookupPrimaryKeysWhereClause(DEFAULT_LOOKUP_KEYS_WHERE_CLAUSE);
        basicLookupStrategy.setOrderByClause(DEFAULT_ORDER_BY_CLAUSE);
        basicLookupStrategy.setAclClassIdSupported(true);

        return basicLookupStrategy;
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        AclPermissionEvaluator permissionEvaluator = new AclPermissionEvaluator(aclService());
        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        expressionHandler.setPermissionCacheOptimizer(new AclPermissionCacheOptimizer(aclService()));
        return expressionHandler;
    }

    @Bean
    public EhCacheBasedAclCache aclCache() {
        return new EhCacheBasedAclCache(
            Objects.requireNonNull(aclEhCacheFactoryBean().getObject()),
            permissionGrantingStrategy(),
            aclAuthorizationStrategy()
        );
    }

    @Bean
    public EhCacheFactoryBean aclEhCacheFactoryBean() {
        EhCacheFactoryBean ehCacheFactoryBean = new EhCacheFactoryBean();
        ehCacheFactoryBean.setCacheManager(aclCacheManager().getObject());
        ehCacheFactoryBean.setCacheName("aclCache");
        return ehCacheFactoryBean;
    }

    @Bean
    public EhCacheManagerFactoryBean aclCacheManager() {
        return new EhCacheManagerFactoryBean();
    }

    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy() {
        return new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
    }

    @Bean
    AclAuthorizationStrategyImpl aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}
