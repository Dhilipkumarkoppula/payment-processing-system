package com.pripe.paymentsSystem.webhook;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

@Component
public class webhookSignatureVerifier {
    @Value("${webhook.secret}")
    private String Secret;
    public boolean isValid(String RawBody, String SignatureHeader) {
        try {
            Mac Hmac = Mac.getInstance("HmacSHA256");
            Hmac.init(new SecretKeySpec(Secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] ComputedBytes = Hmac.doFinal(RawBody.getBytes(StandardCharsets.UTF_8));
            String ComputedHex = HexFormat.of().formatHex(ComputedBytes);
            return ComputedHex.equalsIgnoreCase(SignatureHeader);
        } catch (Exception e) {
            return false;
        }
    }
}
