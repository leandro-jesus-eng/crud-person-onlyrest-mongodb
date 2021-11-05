package br.com.leandro.crud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication (exclude = {DataSourceAutoConfiguration.class })
@EnableMongoRepositories
public class CrudPersonOnlyrestApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudPersonOnlyrestApplication.class, args);
	}
}
