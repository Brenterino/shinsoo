package dev.zygon.shinsoo.core.security;

import dev.zygon.shinsoo.security.NonceGenerator;

import javax.enterprise.context.RequestScoped;
import java.math.BigInteger;

@RequestScoped
public class SessionNonceGenerator implements NonceGenerator {

    @Override
    public String nonce(String metadata) {
        return numericNonce(metadata).toString();
    }

    private BigInteger numericNonce(String metadata) {
        return mashed(System.currentTimeMillis(),
            metadata.hashCode());
    }

    private BigInteger mashed(long left, long right) {
        BigInteger bigLeft = BigInteger.valueOf(left);
        BigInteger bigRight = BigInteger.valueOf(right);

        return bigLeft
                .shiftLeft(Long.SIZE)
                .or(bigRight);
    }
}
