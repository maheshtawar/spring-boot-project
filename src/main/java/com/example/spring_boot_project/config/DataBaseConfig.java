/**
 * 
 */
package com.example.spring_boot_project.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author MaheshT
 *
 */
@Configuration
public class DataBaseConfig {

	@Bean(name = "reader-db")
	@ConfigurationProperties(prefix = "spring.readerdatasource")
	public DataSource readerDbDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "writer-db")
	@ConfigurationProperties(prefix = "spring.writerdatasource")
	public DataSource writerDbDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "reader")
	public JdbcTemplate reader(@Qualifier("reader-db") DataSource readerDbDataSource) {
		return new JdbcTemplate(readerDbDataSource);
	}

	@Bean(name = "writer")
	public JdbcTemplate writer(@Qualifier("writer-db") DataSource writerDbDataSource) {
		return new JdbcTemplate(writerDbDataSource);
	}
}
