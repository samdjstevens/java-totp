package dev.samstevens.totp.code;

import dev.samstevens.totp.exceptions.CodeGenerationException;

interface CodeGenerator {
    String generate(String secret, long counter) throws CodeGenerationException;
}