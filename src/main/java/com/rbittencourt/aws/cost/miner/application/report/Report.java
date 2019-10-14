package com.rbittencourt.aws.cost.miner.application.report;

import com.rbittencourt.aws.cost.miner.domain.miner.SearchParameters;

public interface Report {

    String templateName();

    SearchParameters buildSearchParameters();

    ReportType reportType();

}
