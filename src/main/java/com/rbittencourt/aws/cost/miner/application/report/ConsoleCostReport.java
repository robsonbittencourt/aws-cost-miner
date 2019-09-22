package com.rbittencourt.aws.cost.miner.application.report;

import com.rbittencourt.aws.cost.miner.domain.awsproduct.AwsProduct;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.miner.AwsCostMiner;
import com.rbittencourt.aws.cost.miner.domain.miner.MinedData;
import com.rbittencourt.aws.cost.miner.domain.miner.SearchParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;

@Component
public class ConsoleCostReport {

    @Autowired
    private AwsCostMiner miner;

    @Autowired
    private ConsoleCostReportPrinter printer;

    @Value("${product:#{null}}")
    private String product;

    @Value("${groupBy:#{null}}")
    private String groupBy;

    public void writeReport() {
        AwsProduct awsProduct = AwsProduct.fromName(product);
        SearchParameters searchParameters = buildSearchParameters(awsProduct);
        List<MinedData> minedData = miner.miningCostData(awsProduct, searchParameters);

        printer.printReport(minedData);
    }

    private SearchParameters buildSearchParameters(AwsProduct awsProduct) {
        SearchParameters searchParameters = new SearchParameters();

        if (awsProduct != null) {
            searchParameters.addFilter(b -> awsProduct.getDescription().equals(b.getProductName()));
        }

        if (groupBy != null) {
            searchParameters.addGrouper(buildGroupByClause());
        }
        return searchParameters;
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
