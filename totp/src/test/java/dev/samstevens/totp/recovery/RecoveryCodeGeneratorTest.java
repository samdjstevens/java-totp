package dev.samstevens.totp.recovery;

import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class RecoveryCodeGeneratorTest {

    @Test
    public void testCorrectAmountGenerated() {
        RecoveryCodeGenerator generator = new RecoveryCodeGenerator();
        String[] codes = generator.generateCodes(16);

        // Assert 16 non null codes generated
        assertEquals(16, codes.length);
        for (String code : codes) {
            assertNotNull(code);
        }
    }

    @Test
    public void testCorrectAmountGeneratedDifferentiated() {
        RecoveryCodeGenerator generator = new RecoveryCodeGenerator();
        CodePack[] codes = generator.generateDifferentiatedCodes(16);

        // Assert 16 non null codes generated
        assertEquals(16, codes.length);
        for (CodePack code : codes) {
            assertNotNull(code);
        }
    }

    @Test
    public void testCodesMatchFormat() {
        RecoveryCodeGenerator generator = new RecoveryCodeGenerator();
        String[] codes = generator.generateCodes(16);

        // Assert each one is the correct format
        for (String code : codes) {
            assertTrue(code.matches("[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}"), code);
        }
    }

    @Test
    public void testDifferentiatedCodesMatchReadableFormat() {
        RecoveryCodeGenerator generator = new RecoveryCodeGenerator();
        CodePack[] codes = generator.generateDifferentiatedCodes(16);

        // Assert each one is the correct format
        for (CodePack code : codes) {
            assertTrue(code.readableCode.matches("[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}"), code.readableCode);
        }
    }

    @Test
    public void testDifferentiatedCodesMatchActualFormat() {
        RecoveryCodeGenerator generator = new RecoveryCodeGenerator();
        CodePack[] codes = generator.generateDifferentiatedCodes(16);

        // Assert each one is the correct format
        for (CodePack code : codes) {
            assertTrue(code.actualCode.length()==16,code.actualCode);
        }
    }

    @Test
    public void testCodesAreUnique() {
        RecoveryCodeGenerator generator = new RecoveryCodeGenerator();
        String[] codes = generator.generateCodes(25);

        Set<String> uniqueCodes = new HashSet<>(Arrays.asList(codes));

        assertEquals(25, uniqueCodes.size());
    }

    @Test
    public void testDifferentiatedCodesAreUnique() {
        RecoveryCodeGenerator generator = new RecoveryCodeGenerator();
        CodePack[] codes = generator.generateDifferentiatedCodes(25);

        Set<String> uniqueCodes = Arrays.stream(codes).map(map->map.actualCode).collect(Collectors.toSet());

        assertEquals(25, uniqueCodes.size());
    }

    @Test
    public void testInvalidNumberThrowsException() {
        RecoveryCodeGenerator generator = new RecoveryCodeGenerator();

        assertThrows(InvalidParameterException.class, () -> {
            generator.generateCodes(-1);
        });
    }

    @Test
    public void testDifferentiatedReadableCodeGrouping(){
        RecoveryCodeGenerator generator =
                new RecoveryCodeGenerator
                .Builder()
                .setCodeLength(13)
                .setGroupNumber(3)
                .build();

        CodePack codes[] = generator.generateDifferentiatedCodes(14);
        for (CodePack code : codes) {
            assertTrue(code.readableCode.matches("[a-z0-9]{3}-[a-z0-9]{3}-[a-z0-9]{3}-[a-z0-9]{3}-[a-z0-9]{1}"), code.readableCode);
        }
    }

    @Test
    public void testInvalidGroupSize(){
        assertThrows(InvalidParameterException.class,
                ()->{
            new RecoveryCodeGenerator.Builder()
                    .setCodeLength(12)
                    .setGroupNumber(0)
                    .build();
                }
                );
    }

    @Test
    public void testInsecureCodeLength(){
        assertThrows(InsecureCodeException.class,
                ()->{
            new RecoveryCodeGenerator
                    .Builder()
                    .setGroupNumber(5)
                    .setCodeLength(6)
                    .build();
                }
        );
    }
}
