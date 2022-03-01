package com.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.spacestore.common.entity"})
public class SpacestoreRestAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpacestoreRestAdminApplication.class, args);
	}

}
