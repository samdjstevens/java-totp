package dev.samstevens.totp.secret;

public interface SecretGenerator {
    /**
     * @return A random base32 encoded string to use as the shared secret/key between the server and the client.
     */
    String generate();
}
