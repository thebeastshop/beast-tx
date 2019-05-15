package com.thebeastshop.tx.test.client;

import com.thebeastshop.tx.test.client.util.ServiceUtils;
import com.thebeastshop.tx.test.service.TestService;
import org.junit.Before;
import org.junit.Test;

public class ClientTest {

    private TestService testService;

    @Before
    public void prepare(){
        testService = ServiceUtils.getService(TestService.class);
    }

    @Test
    public void testClient() throws Exception{
        testService.consumerTest();
    }
}
