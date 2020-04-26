package dev.samstevens.totp.code;

import dev.samstevens.totp.exceptions.CodeGenerationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.security.InvalidParameterException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class DefaultCodeGeneratorTest {

    @Test
    public void testInvalidHashingAlgorithmThrowsException() {
        assertThrows(InvalidParameterException.class, () -> {
            new DefaultCodeGenerator(null);
        });
    }

    @Test
    public void testInvalidDigitLengthThrowsException() {
        assertThrows(InvalidParameterException.class, () -> {
            new DefaultCodeGenerator(HashingAlgorithm.SHA1, 0);
        });
    }

    @Test
    public void testInvalidValidityDurationThrowsException() {
        assertThrows(InvalidParameterException.class, () -> {
            new DefaultCodeGenerator(HashingAlgorithm.SHA1, 6, Duration.ofSeconds(0));
        });
    }

    @Test
    public void testGeneratingWithNegativeBeforeAndAfterThrowsException()
    {
        InvalidParameterException e = assertThrows(InvalidParameterException.class, () -> {
            CodeGenerator generator = new DefaultCodeGenerator(HashingAlgorithm.SHA1, 6);
            generator.generate("W3C5B3WKR4AUKFVWYU2WNMYB756OAKWY", Instant.now(), - 1);
        });

        assertEquals("Number of codes before and after to generate must be greater or equal to zero.", e.getMessage());
    }


    @Test
    public void testGeneratingBetweenWithBadTimesThrowsException()
    {
        InvalidParameterException e = assertThrows(InvalidParameterException.class, () -> {
            CodeGenerator generator = new DefaultCodeGenerator(HashingAlgorithm.SHA1, 6);
            generator.generateBetween("W3C5B3WKR4AUKFVWYU2WNMYB756OAKWY", Instant.now(), Instant.now().minusSeconds(60));
        });

        assertEquals("End time must be after start time.", e.getMessage());
    }

    @ParameterizedTest
    @MethodSource("testGeneratingCodesProvider")
    public void testGeneratingCodes(HashingAlgorithm algo, String secret, Instant timeToRunAt, int disc, List<GeneratedCode> expectedCodes) throws CodeGenerationException {
        CodeGenerator generator = new DefaultCodeGenerator(algo);
        List<GeneratedCode> generatedCodes = generator.generate(secret, timeToRunAt, disc);
        assertEquals(generatedCodes, expectedCodes);
    }

    static Stream<Arguments> testGeneratingCodesProvider() {
        return Stream.of(
            arguments(
                // Hashing algorithm,
                HashingAlgorithm.SHA1,
                // Secret
                "W3C5B3WKR4AUKFVWYU2WNMYB756OAKWY",
                // Time to run for
                Instant.ofEpochSecond(1587772860),
                // Number of codes to generate before and after
                2,
                // Expected codes
                Arrays.asList(
                    createGeneratedCode("897990", 1587772800, 1587772829),
                    createGeneratedCode("987260", 1587772830, 1587772859),
                    createGeneratedCode("594169", 1587772860, 1587772889),
                    createGeneratedCode("896541", 1587772890, 1587772919),
                    createGeneratedCode("733574", 1587772920, 1587772949)
                )
            ),

            arguments(
                // Hashing algorithm,
                HashingAlgorithm.SHA256,
                // Secret
                "makrzl2hict4ojeji2iah4kndmq6sgka",
                // Time to run for
                Instant.ofEpochSecond(1593607513),
                    // Number of codes to generate before and after
                1,
                // Expected codes
                Arrays.asList(
                    createGeneratedCode("773412", 1593607470, 1593607499),
                    createGeneratedCode("250378", 1593607500, 1593607529),
                    createGeneratedCode("967103", 1593607530, 1593607559)
                )
            ),

            arguments(
                // Hashing algorithm,
                HashingAlgorithm.SHA512,
                // Secret
                "EX47GINFPBK5GNLYLILGD2H6ZLGJNNWB",
                // Time to run for
                Instant.ofEpochSecond(1608940785),
                // Number of codes to generate before and after
                3,
                // Expected codes
                Arrays.asList(
                    createGeneratedCode("381260", 1608940680, 1608940709),
                    createGeneratedCode("546994", 1608940710, 1608940739),
                    createGeneratedCode("492946", 1608940740, 1608940769),

                    createGeneratedCode("977130", 1608940770, 1608940799),

                    createGeneratedCode("416247", 1608940800, 1608940829),
                    createGeneratedCode("610646", 1608940830, 1608940859),
                    createGeneratedCode("418802", 1608940860, 1608940889)
                )
            )
        );
    }




    static Stream<Arguments> expectedCodesProvider() {
        return Stream.of(
            arguments("W3C5B3WKR4AUKFVWYU2WNMYB756OAKWY", 1567631536, HashingAlgorithm.SHA1, "082371"),
            arguments("W3C5B3WKR4AUKFVWYU2WNMYB756OAKWY", 1567631536, HashingAlgorithm.SHA256, "272978"),
            arguments("W3C5B3WKR4AUKFVWYU2WNMYB756OAKWY", 1567631536, HashingAlgorithm.SHA512, "325200"),

            arguments("makrzl2hict4ojeji2iah4kndmq6sgka", 1582750403, HashingAlgorithm.SHA1, "848586"),
            arguments("makrzl2hict4ojeji2iah4kndmq6sgka", 1582750403, HashingAlgorithm.SHA256, "965726"),
            arguments("makrzl2hict4ojeji2iah4kndmq6sgka", 1582750403, HashingAlgorithm.SHA512, "741306")
        );
    }


    @Test
    public void testGeneratingMultipleCodesBetweenStartAndEndTimes() throws CodeGenerationException {

        String secret = "W3C5B3WKR4AUKFVWYU2WNMYB756OAKWY";
        // 2020-04-25 00:00:00 UTC
        Instant startTime = Instant.ofEpochSecond(1587772800);
        // 2020-04-25 00:01:30 UTC
        Instant endTime = Instant.ofEpochSecond(1587772890);

        DefaultCodeGenerator generator = new DefaultCodeGenerator(HashingAlgorithm.SHA1);

        List<String> expectedCodes = Arrays.asList("897990", "987260", "594169", "896541");
        List<Instant> expectedStartTimes = Arrays.asList(
            Instant.ofEpochSecond(1587772800),
            Instant.ofEpochSecond(1587772830),
            Instant.ofEpochSecond(1587772860),
            Instant.ofEpochSecond(1587772890)
        );
        List<Instant> expectedEndTimes = Arrays.asList(
                Instant.ofEpochSecond(1587772829),
                Instant.ofEpochSecond(1587772859),
                Instant.ofEpochSecond(1587772889),
                Instant.ofEpochSecond(1587772919)
        );

        List<GeneratedCode> codes = generator.generateBetween(secret, startTime, endTime);

        for (int i = 0; i < codes.size(); i++) {
            assertEquals(expectedCodes.get(i), codes.get(i).getDigits());
            assertEquals(expectedStartTimes.get(i), codes.get(i).getValidityPeriod().getStart());
            assertEquals(expectedEndTimes.get(i), codes.get(i).getValidityPeriod().getEnd());
        }
    }

    @Test
    public void testDigitLength() throws CodeGenerationException {
//        DefaultCodeGenerator g = new DefaultCodeGenerator(HashingAlgorithm.SHA1);
//        GeneratedCode code = g.generate("W3C5B3WKR4AUKFVWYU2WNMYB756OAKWY", Instant.ofEpochSecond(1567631536));
//        assertEquals(6, code.getDigits().length());
//
//        g = new DefaultCodeGenerator(HashingAlgorithm.SHA1, 8);
//        code = g.generate("W3C5B3WKR4AUKFVWYU2WNMYB756OAKWY", Instant.ofEpochSecond(1567631536));
//        assertEquals(8, code.getDigits().length());
//
//        g = new DefaultCodeGenerator(HashingAlgorithm.SHA1, 4);
//        code = g.generate("W3C5B3WKR4AUKFVWYU2WNMYB756OAKWY", Instant.ofEpochSecond(1567631536));
//        assertEquals(4, code.getDigits().length());
    }



    @Test
    public void testInvalidKeyThrowsCodeGenerationException() {
        CodeGenerationException e = assertThrows(CodeGenerationException.class, () -> {
            DefaultCodeGenerator g = new DefaultCodeGenerator(HashingAlgorithm.SHA1, 4);
            g.generate("1234", Instant.ofEpochSecond(1567631536), 0);
        });
        assertNotNull(e.getCause());
    }

    /**
     * Helper method to construct a GeneratedCode object.
     */
    private static GeneratedCode createGeneratedCode(String digits, long startTime, long endTime) {
        return new GeneratedCode(digits, new ValidityPeriod(Instant.ofEpochSecond(startTime), Instant.ofEpochSecond(endTime)));
    }
}
