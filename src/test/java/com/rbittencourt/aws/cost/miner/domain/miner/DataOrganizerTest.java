package com.rbittencourt.aws.cost.miner.domain.miner;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfoRepository;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingQuery;
import com.rbittencourt.aws.cost.miner.fixture.BillingInfoFixture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DataOrganizerTest {

    @InjectMocks
    private DataOrganizer dataOrganizer;

    @Mock
    private BillingInfoRepository repository;

    @Mock
    private BillingQuery billingQuery;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldOrganizeData() {
        SearchParameters searchParameters = new SearchParameters();
        searchParameters.addFilter(b -> b.getUsageType().equals("BoxUsage"));
        searchParameters.addGrouper(BillingInfo::getAvailabilityZone);

        BillingInfo billingInfo1 = BillingInfoFixture.get().withUsageType("BoxUsage").withAvailabilityZone("us-east-1").withCost(50).build();
        BillingInfo billingInfo2 = BillingInfoFixture.get().withUsageType("BoxUsage").withAvailabilityZone("us-east-2").withCost(100).build();
        BillingInfo billingInfo3 = BillingInfoFixture.get().withUsageType("DataTransferOut").withAvailabilityZone("us-east-2").withCost(30).build();
        List<BillingInfo> billingInfos = List.of(billingInfo1, billingInfo2, billingInfo3);
        when(repository.findBillingInfos()).thenReturn(billingInfos);

        List<BillingInfo> filteredBillingInfos = List.of(billingInfo1, billingInfo2);
        when(billingQuery.filter(eq(billingInfos), anyList())).thenReturn(filteredBillingInfos);

        Map<String, List<BillingInfo>> groupedBillingInfos = Map.of(
            "us-east-1", List.of(billingInfo1),
            "us-east-2", List.of(billingInfo2)
        );
        when(billingQuery.groupBy(eq(filteredBillingInfos), any())).thenReturn(groupedBillingInfos);

        when(billingQuery.totalCost(List.of(billingInfo1))).thenReturn(new BigDecimal(50));
        when(billingQuery.totalCost(List.of(billingInfo2))).thenReturn(new BigDecimal(100));

        Map<String, List<BillingInfo>> data = dataOrganizer.organizeData(searchParameters);

        assertEquals(2, data.size());
        assertEquals("us-east-2", new ArrayList<>(data.keySet()).get(0));
        assertEquals("us-east-1", new ArrayList<>(data.keySet()).get(1));
    }

}