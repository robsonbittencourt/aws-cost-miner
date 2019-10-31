package com.rbittencourt.aws.cost.miner;

import com.rbittencourt.aws.cost.miner.application.report.ConsoleCostReport;
import com.rbittencourt.aws.cost.miner.application.sheet.SheetWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.IOException;

@SpringBootApplication
public class AwsCostMinerApplication {

	@Autowired
	private ConsoleCostReport report;

	@Autowired
	private SheetWriter sheetWriter;

	@PostConstruct
	public void start() {
		try {
			sheetWriter.updateSheet();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(AwsCostMinerApplication.class, args);
	}

}
