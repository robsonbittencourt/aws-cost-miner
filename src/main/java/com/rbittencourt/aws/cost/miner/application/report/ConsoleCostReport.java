package com.rbittencourt.aws.cost.miner.application.report;

import com.rbittencourt.aws.cost.miner.domain.miner.AwsCostMiner;
import com.rbittencourt.aws.cost.miner.domain.miner.MinedData;
import com.rbittencourt.aws.cost.miner.infrastructure.file.TemplateWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ConsoleCostReport {

    @Autowired
    private AwsCostMiner miner;

    @Autowired
    private TemplateWriter templateWriter;

    @Autowired
    private ReportFactory reportFactory;

    @Value("${report:#{null}}")
    private ReportType reportType;

    public void writeReport() {
        Report report = reportFactory.getInstance(reportType);

        List<MinedData> minedData = miner.miningCostData(report);

        String output = templateWriter.write(report.templateName(), Map.of("minedData", minedData));

        System.out.println(output);
    }

}
