package com.rbittencourt.aws.cost.miner.application.report;

import com.rbittencourt.aws.cost.miner.domain.miner.SearchParameters;
import org.springframework.stereotype.Component;

import static com.rbittencourt.aws.cost.miner.application.report.ReportType.RESERVED_INSTANCE_USAGE_REPORT;

@Component
public class ReservedInstanceUsageReport implements Report {

    public String templateName() {
        return "reservedInstanceUsageReport.vm";
    }

    public SearchParameters buildSearchParameters() {
        SearchParameters searchParameters = new SearchParameters();

        searchParameters.addFilter(b -> "Amazon Elastic Compute Cloud".equals(b.getProductName()));

        return searchParameters;
    }

    @Override
    public ReportType reportType() {
        return RESERVED_INSTANCE_USAGE_REPORT;
    }


}
