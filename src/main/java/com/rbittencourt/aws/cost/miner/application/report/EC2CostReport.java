package com.rbittencourt.aws.cost.miner.application.report;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.miner.SearchParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.function.Function;

import static com.rbittencourt.aws.cost.miner.application.report.ReportType.EC2_COST_REPORT;

@Component
public class EC2CostReport implements Report {

    @Value("${groupBy:#{null}}")
    private String groupBy;

    public String templateName() {
        return "ec2CostReport.vm";
    }

    public SearchParameters buildSearchParameters() {
        SearchParameters searchParameters = new SearchParameters();

        searchParameters.addFilter(b -> "Amazon Elastic Compute Cloud".equals(b.getProductName()));

        if (groupBy != null) {
            searchParameters.addGrouper(buildGroupByClause());
        }

        return searchParameters;
    }

    @Override
    public ReportType reportType() {
        return EC2_COST_REPORT;
    }

    private Function<BillingInfo, String> buildGroupByClause() {
        return b -> {
            try {
                Method method = b.getClass().getMethod("get" + groupBy);
                return method.invoke(b).toString();
            } catch (Exception e) {
                // do nothing. This is expected.
            }

            return b.getCustomField(groupBy);
        };
    }

}
