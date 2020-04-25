package dev.samstevens.totp.code;

import dev.samstevens.totp.exceptions.CodeGenerationException;
import java.time.Instant;
import java.util.List;

public interface CodeGenerator {

    /**
     * Returns a GeneratedCode object representing the code for a given secret and time.
     *
     * @param secret The shared secret/key to generate the code with.
     * @param atTime An Instant object representing the time to generate the code for.
     * @return The GeneratedCode object containing the n-digit code and validity period.
     * @throws CodeGenerationException Thrown if the code generation fails for any reason.
     */
    GeneratedCode generate(String secret, Instant atTime) throws CodeGenerationException;

    /**
     * Returns a list of GeneratedCode objects representing the code for a given secret and time,
     * and the N previous and next codes.
     *
     * @param secret The shared secret/key to generate the code with.
     * @param atTime An Instant object representing the time to generate the code for.
     * @return The list of GeneratedCode objects.
     * @throws CodeGenerationException Thrown if the code generation fails for any reason.
     */
    List<GeneratedCode> generate(String secret, Instant atTime, int beforeAndAfter) throws CodeGenerationException;

    /**
     * Returns a list of GeneratedCode objects that are valid for a given secret between a start and end time.
     *
     * @param secret The shared secret/key to generate the code with.
     * @param startTime An Instant object representing the time to start generating codes.
     * @param endTime An Instant object representing the time to stop generating codes.
     * @return The list of GeneratedCode objects.
     * @throws CodeGenerationException Thrown if the code generation fails for any reason.
     */
    List<GeneratedCode> generateAllBetween(String secret, Instant startTime, Instant endTime) throws CodeGenerationException;
}
