package step.learning.services.hash;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5OldHashService implements HashService {
    @Override
    public String hash(String input) {
        if (input == null) {
            input = "";
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            StringBuilder sb = new StringBuilder();
            for (int b : digest.digest(input.getBytes(StandardCharsets.UTF_8))) {
                sb.append(String.format("%02x", b & 0xFF));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
}
