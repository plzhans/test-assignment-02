package com.plzhans.assignment.api.service.spread;

import com.plzhans.assignment.api.auth.AuthRoomRequester;
import com.plzhans.assignment.api.repository.SpreadRepository;
import com.plzhans.assignment.api.service.spread.datatype.DistributeReceiveResult;
import com.plzhans.assignment.api.service.spread.datatype.DistributeRequest;
import com.plzhans.assignment.api.service.spread.datatype.DistributeResult;
import com.plzhans.assignment.api.service.spread.datatype.DistributeStatusResult;
import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaRepositories({"com.plzhans.assignment.common.repository", "com.plzhans.assignment.api.repository"})
@EnableJpaAuditing
@EntityScan({"com.plzhans.assignment.common.entity", "com.plzhans.assignment.api.entity"})
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("debug")
public class SpreadServiceImplTest {

    //@MockBean
    @Autowired
    SpreadRepository spreadRepository;

    SpreadService spreadService;

    AuthRoomRequester testRequester;

    /**
     * Init.
     */
    @Before
    public void init() {
        this.spreadService = new SpreadServiceImpl(spreadRepository);
        this.testRequester = new AuthRoomRequester(3019, 1234);
    }

    @Test
    public void distribute_ok() throws Exception {

        // TEST : distribute
        // GIVEN
        val request = new DistributeRequest();
        request.setAmount(1000);
        request.setReceiverCount(5);

        // WHEN
        DistributeResult result = this.spreadService.distribute(testRequester, request);

        // THEN
        assertNotNull(result.getToken());
        assertNotNull(result.getCreatedDate());
        assertNotNull(result.getExpiredDate());

        // TEST : getDistributeStatusByToken
        // WHEN
        int totalAmount = 0;
        for (int i = 1; i <= request.getReceiverCount(); i++) {
            val receiver = new AuthRoomRequester(1000 + i, testRequester.getRoomId());
            DistributeReceiveResult distributeReceiveResult = this.spreadService.receiveByToken(receiver, result.getToken());
            val receiveResult = distributeReceiveResult;
            totalAmount += receiveResult.getAmount();
        }

        // THEN
        assertEquals(totalAmount, request.getAmount());

        // TEST : getDistributeStatusByToken
        // WHEN
        DistributeStatusResult result2 = this.spreadService.getDistributeStatusByToken(testRequester, result.getToken());

        // THEN
        assertNotNull(result2.getData());
        assertEquals(result2.getData().getCreatedDate(), result.getCreatedDate());
        assertEquals(result2.getData().getTotalAmount(), request.getAmount());
    }


}