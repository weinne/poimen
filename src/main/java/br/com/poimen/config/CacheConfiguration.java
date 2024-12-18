package br.com.poimen.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, br.com.poimen.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, br.com.poimen.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, br.com.poimen.domain.User.class.getName());
            createCache(cm, br.com.poimen.domain.Authority.class.getName());
            createCache(cm, br.com.poimen.domain.User.class.getName() + ".authorities");
            createCache(cm, br.com.poimen.domain.Church.class.getName());
            createCache(cm, br.com.poimen.domain.Church.class.getName() + ".users");
            createCache(cm, br.com.poimen.domain.Church.class.getName() + ".members");
            createCache(cm, br.com.poimen.domain.Church.class.getName() + ".ministryGroups");
            createCache(cm, br.com.poimen.domain.Church.class.getName() + ".worshipEvents");
            createCache(cm, br.com.poimen.domain.Church.class.getName() + ".tasks");
            createCache(cm, br.com.poimen.domain.Church.class.getName() + ".counselingSessions");
            createCache(cm, br.com.poimen.domain.Church.class.getName() + ".invoices");
            createCache(cm, br.com.poimen.domain.Church.class.getName() + ".transactions");
            createCache(cm, br.com.poimen.domain.Church.class.getName() + ".planSubscriptions");
            createCache(cm, br.com.poimen.domain.CounselingSession.class.getName());
            createCache(cm, br.com.poimen.domain.Hymn.class.getName());
            createCache(cm, br.com.poimen.domain.Hymn.class.getName() + ".worshipEvents");
            createCache(cm, br.com.poimen.domain.Invoice.class.getName());
            createCache(cm, br.com.poimen.domain.Member.class.getName());
            createCache(cm, br.com.poimen.domain.Member.class.getName() + ".counselingSessions");
            createCache(cm, br.com.poimen.domain.Member.class.getName() + ".ministryMemberships");
            createCache(cm, br.com.poimen.domain.Member.class.getName() + ".tasks");
            createCache(cm, br.com.poimen.domain.Member.class.getName() + ".transactions");
            createCache(cm, br.com.poimen.domain.Member.class.getName() + ".schedules");
            createCache(cm, br.com.poimen.domain.MinistryGroup.class.getName());
            createCache(cm, br.com.poimen.domain.MinistryGroup.class.getName() + ".ministryMemberships");
            createCache(cm, br.com.poimen.domain.MinistryMembership.class.getName());
            createCache(cm, br.com.poimen.domain.Plan.class.getName());
            createCache(cm, br.com.poimen.domain.Plan.class.getName() + ".planSubscriptions");
            createCache(cm, br.com.poimen.domain.PlanSubscription.class.getName());
            createCache(cm, br.com.poimen.domain.Schedule.class.getName());
            createCache(cm, br.com.poimen.domain.Schedule.class.getName() + ".members");
            createCache(cm, br.com.poimen.domain.Schedule.class.getName() + ".worshipEvents");
            createCache(cm, br.com.poimen.domain.Task.class.getName());
            createCache(cm, br.com.poimen.domain.Transaction.class.getName());
            createCache(cm, br.com.poimen.domain.Transaction.class.getName() + ".invoices");
            createCache(cm, br.com.poimen.domain.WorshipEvent.class.getName());
            createCache(cm, br.com.poimen.domain.WorshipEvent.class.getName() + ".hymns");
            createCache(cm, br.com.poimen.domain.WorshipEvent.class.getName() + ".schedules");
            createCache(cm, br.com.poimen.domain.Member.class.getName() + ".worshipEvents");
            createCache(cm, br.com.poimen.domain.Schedule.class.getName() + ".users");
            createCache(cm, br.com.poimen.domain.WorshipEvent.class.getName() + ".musicians");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
