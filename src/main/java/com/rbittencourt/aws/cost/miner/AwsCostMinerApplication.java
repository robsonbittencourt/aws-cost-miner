package com.rbittencourt.aws.cost.miner;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingLineInfo;
import com.rbittencourt.aws.cost.miner.infrastructure.file.CsvReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootApplication
public class AwsCostMinerApplication {

	@Autowired
	private CsvReaderService csvReaderService;

	@PostConstruct
	public void start() {
		String userDir = System.getProperty("user.dir");

		List<BillingLineInfo> list = csvReaderService.csvToObjects(userDir + "/data.csv", BillingLineInfo.class);

		int count = 0;
		for (BillingLineInfo billingLineInfo : list) {
			System.out.println(count + ": " + billingLineInfo);
			count++;
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(AwsCostMinerApplication.class, args);
	}

}
