package com.rbittencourt.aws.cost.miner;

import com.rbittencourt.aws.cost.miner.domain.report.ec2.Ec2Report;
import com.rbittencourt.aws.cost.miner.domain.report.ReportWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class AwsCostMinerApplication {

	@Autowired
	private Ec2Report ec2Report;

	@Autowired
	private ReportWriter reportWriter;

	@PostConstruct
	public void start() {
		reportWriter.write(ec2Report);
	}

	public static void main(String[] args) {
		SpringApplication.run(AwsCostMinerApplication.class, args);
	}

}
