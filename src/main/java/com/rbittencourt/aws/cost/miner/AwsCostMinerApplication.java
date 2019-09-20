package com.rbittencourt.aws.cost.miner;

import com.rbittencourt.aws.cost.miner.application.report.ConsoleCostReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class AwsCostMinerApplication {

	@Autowired
	private ConsoleCostReport report;

	@PostConstruct
	public void start() {
		report.writeReport();
	}

	public static void main(String[] args) {
		SpringApplication.run(AwsCostMinerApplication.class, args);
	}

}
