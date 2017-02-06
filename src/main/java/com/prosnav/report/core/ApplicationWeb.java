package com.prosnav.report.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.XmlViewResolver;

@Configuration
public class ApplicationWeb extends WebMvcConfigurerAdapter {
	
	@Autowired
	private Environment env;
	
	@Bean
	public FilterRegistrationBean characterEncodingFilterRegistrationBean () {
	    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
	    CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
	    characterEncodingFilter.setEncoding("UTF-8");
	    characterEncodingFilter.setForceEncoding(true);
	    registrationBean.setFilter(characterEncodingFilter);
	    registrationBean.setOrder(0);
	    return registrationBean;
	}
	
	@Bean
	public FilterRegistrationBean sessionFilterRegistrationBean () {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		SessionFilter filter = new SessionFilter();
		registrationBean.setFilter(filter);
		registrationBean.addInitParameter("exclude", env.getProperty("session.exclude"));
		registrationBean.addInitParameter("check", env.getProperty("session.check"));
		registrationBean.setOrder(1);
	    return registrationBean;
	}
	
	public @Bean XmlViewResolver getXmlViewResolver() {
		XmlViewResolver resolver = new XmlViewResolver();
		resolver.setLocation(new ClassPathResource("jasper/jasper-views.xml"));
		resolver.setOrder(0);
		return resolver;
	}
}
