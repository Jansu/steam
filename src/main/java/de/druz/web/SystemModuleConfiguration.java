package de.druz.web;

import java.sql.Driver;
import java.util.Arrays;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.github.koraktor.steamcondenser.exceptions.WebApiException;
import com.github.koraktor.steamcondenser.steam.community.WebApi;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@PropertySource("system.properties")
@ComponentScan(basePackages = { "de.druz.web" })
@EnableJpaRepositories("de.druz.web.persistance")
public class SystemModuleConfiguration {

	@Inject
	protected Environment env;
	
	private String apiKey;
	private String steamId;
	private String steamIdLong;

	@Bean
	public DataSource dataSource() throws WebApiException {
		this.apiKey = env.getProperty("api.key");
		WebApi.setApiKey(apiKey);
		this.steamId = env.getProperty("steam.id");
		this.steamIdLong = env.getProperty("steam.id.long");
		
		if (isTestProfileActive()) {
			// We use an embedded H2 database instance for testing
			return new EmbeddedDatabaseBuilder()
					.setType(EmbeddedDatabaseType.H2)
					.addScript("classpath:/db/schema.sql").build();
		} else {
			SimpleDriverDataSource ds = new SimpleDriverDataSource();
			ds.setDriverClass(env.getPropertyAsClass("persistence.db.driverClass", Driver.class));
			ds.setUrl(env.getProperty("persistence.db.url"));
			ds.setUsername(env.getProperty("persistence.db.username"));
			ds.setPassword(env.getProperty("persistence.db.password"));
			return ds;
		}
	}

	@Bean
	public JpaTransactionManager transactionManager() throws WebApiException {
		return new JpaTransactionManager(entityManagerFactory());
	}

	@Bean
	public EntityManagerFactory entityManagerFactory() throws WebApiException {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource());
		em.setJpaVendorAdapter(jpaVendorAdapter());

		/*
		 * this implicitly generates an appropriate persistence.xml for us at
		 * runtime.
		 */
		em.setPackagesToScan("de.druz.web");

		em.afterPropertiesSet();
		return em.getObject();
	}

	@Bean
	public PersistenceExceptionTranslator hibernateExceptionTranslator() {
		return new HibernateExceptionTranslator();
	}

	@Bean
	public HibernateJpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setShowSql(env.getProperty("persistence.hibernate.showSql",
				Boolean.class));
		adapter.setGenerateDdl(env.getProperty(
				"persistence.hibernate.generateDdl", Boolean.class));
		if (isTestProfileActive()) {
			adapter.setDatabasePlatform("org.hibernate.dialect.H2Dialect");
		} else {
			adapter.setDatabasePlatform(env
					.getProperty("persistence.hibernate.databasePlatform"));
		}
		return adapter;
	}

	private boolean isTestProfileActive() {
		return Arrays.asList(env.getActiveProfiles()).contains("testing");
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getSteamId() {
		return steamId;
	}

	public void setSteamId(String steamId) {
		this.steamId = steamId;
	}

	public String getSteamIdLong() {
		return steamIdLong;
	}

	public void setSteamIdLong(String steamIdLong) {
		this.steamIdLong = steamIdLong;
	}
}
