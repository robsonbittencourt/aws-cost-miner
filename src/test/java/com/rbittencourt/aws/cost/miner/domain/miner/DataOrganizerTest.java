package com.rbittencourt.aws.cost.miner.domain.miner;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfoRepository;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfos;
import com.rbittencourt.aws.cost.miner.fixture.BillingInfoFixture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.util.ReflectionTestUtils.setField;

public class DataOrganizerTest {

    @InjectMocks
    private DataOrganizer dataOrganizer;

    @Mock
    private BillingInfoRepository repository;

    @Before
    public void setup() {
        initMocks(this);
        setField(dataOrganizer, "considerableCost", "1");
    }

    @Test
    public void shouldOrganizeData() {
        SearchParameters searchParameters = new SearchParameters();
        searchParameters.addFilter(b -> b.getUsageType().equals("BoxUsage"));
        searchParameters.addGrouper(BillingInfo::getAvailabilityZone);

        BillingInfo billingInfo1 = BillingInfoFixture.get().ec2().withUsageType("BoxUsage").withAvailabilityZone("us-east-1").withCost(50).build();
        BillingInfo billingInfo2 = BillingInfoFixture.get().ec2().withUsageType("BoxUsage").withAvailabilityZone("us-east-2").withCost(100).build();
        BillingInfo billingInfo3 = BillingInfoFixture.get().ec2().withUsageType("DataTransferOut").withAvailabilityZone("us-east-2").withCost(30).build();
        BillingInfos billingInfos = new BillingInfos(List.of(billingInfo1, billingInfo2, billingInfo3));
        when(repository.findBillingInfos()).thenReturn(billingInfos);

        Map<String, BillingInfos> data = dataOrganizer.organizeData(searchParameters);

        assertEquals(2, data.size());
        assertEquals("us-east-2", new ArrayList<>(data.keySet()).get(0));
        assertEquals("us-east-1", new ArrayList<>(data.keySet()).get(1));
    }

}