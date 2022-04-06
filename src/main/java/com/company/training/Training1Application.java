package com.company.training;

import com.google.common.base.Strings;
import freemarker.template.Configuration;
import io.jmix.core.CoreProperties;
import io.jmix.core.Resources;
import io.jmix.localfs.LocalFileStorage;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.ui.freemarker.SpringTemplateLoader;

import javax.sql.DataSource;

@SpringBootApplication
public class Training1Application {

    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(Training1Application.class, args);
    }

    @Bean
    @Primary
    @ConfigurationProperties("main.datasource")
    DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("main.datasource.hikari")
    DataSource dataSource(DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @EventListener
    public void printApplicationUrl(ApplicationStartedEvent event) {
        LoggerFactory.getLogger(Training1Application.class).info("Application started at "
                + "http://localhost:"
                + environment.getProperty("local.server.port")
                + Strings.nullToEmpty(environment.getProperty("server.servlet.context-path")));
    }

    @Bean
    public LocalFileStorage secondFileStorage(CoreProperties coreProperties) {
        return new LocalFileStorage("fs2", coreProperties.getWorkDir() + "/fs2");
    }

    @Bean
    public Configuration freemarkerConfiguration(Resources resources) {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        configuration.setTemplateLoader(new SpringTemplateLoader(resources, "templates"));
        return configuration;
    }
}
