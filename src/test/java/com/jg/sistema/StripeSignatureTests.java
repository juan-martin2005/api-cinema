package com.jg.sistema;

import com.jg.sistema.payments.infrastructure.StripeGateway;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.ObjectMapper;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HexFormat;
import static org.junit.jupiter.api.Assertions.*;

class StripeSignatureTests {
    @Test void verifiesOriginalBodyAndRejectsAlteredPayload() throws Exception {
        var gateway=new StripeGateway(new ObjectMapper());
        ReflectionTestUtils.setField(gateway,"enabled",true);
        ReflectionTestUtils.setField(gateway,"webhookSecret","whsec_test_only");
        String body="{\"id\":\"evt_123\",\"object\":\"event\",\"type\":\"checkout.session.completed\",\"data\":{\"object\":{\"id\":\"cs_123\",\"object\":\"checkout.session\"}}}";
        String time=Long.toString(Instant.now().getEpochSecond());
        var mac=Mac.getInstance("HmacSHA256");mac.init(new SecretKeySpec("whsec_test_only".getBytes(StandardCharsets.UTF_8),"HmacSHA256"));
        String signature="t="+time+",v1="+HexFormat.of().formatHex(mac.doFinal((time+"."+body).getBytes(StandardCharsets.UTF_8)));
        assertEquals("cs_123",gateway.verify(body,signature).sessionId());
        assertThrows(ResponseStatusException.class,()->gateway.verify(body.replace("cs_123","cs_other"),signature));
        assertThrows(ResponseStatusException.class,()->gateway.verify(body,"invalid"));
    }
}
