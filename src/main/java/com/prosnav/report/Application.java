package com.prosnav.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.mongodb.MongoClient;

/**
 * 
 * @author tanyuan
 * @SpringBootApplication same as @Configuration @EnableAutoConfiguration @CompotentScan
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableScheduling
public class Application {

	@Autowired
	private Environment env;
	
	public @Bean MongoDbFactory mongoDbFactory() throws Exception {
	    UserCredentials userCredentials = new UserCredentials(env.getProperty("mongo.crm.username"), env.getProperty("mongo.crm.password"));
	    return new SimpleMongoDbFactory(new MongoClient(env.getProperty("mongo.crm.host")), env.getProperty("mongo.crm.name"), userCredentials);
    }

	public @Bean MongoTemplate mongoTemplate() throws Exception {
	    return new MongoTemplate(mongoDbFactory());
	}
	
	public @Bean MongoDbFactory mongoDbFactoryUpm() throws Exception {
	    UserCredentials userCredentials = new UserCredentials(env.getProperty("mongo.upm.username"), env.getProperty("mongo.upm.password"));
	    return new SimpleMongoDbFactory(new MongoClient(env.getProperty("mongo.upm.host")), env.getProperty("mongo.upm.name"), userCredentials);
    }
	
	public @Bean MongoTemplate mongoTemplateUpm() throws Exception {
	    return new MongoTemplate(mongoDbFactoryUpm());
	}
	
	public @Bean MongoDbFactory mongoDbFactoryReport() throws Exception {
	    UserCredentials userCredentials = new UserCredentials(env.getProperty("mongo.report.username"), env.getProperty("mongo.report.password"));
	    return new SimpleMongoDbFactory(new MongoClient(env.getProperty("mongo.report.host")), env.getProperty("mongo.report.name"), userCredentials);
    }
	
	public @Bean MongoTemplate mongoTemplateReport() throws Exception {
	    return new MongoTemplate(mongoDbFactoryReport());
	}

    public static void main(String[] args) {
        SpringApplication.run(new Object[] {Application.class}, args);
    }
}
