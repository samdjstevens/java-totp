package dev.samstevens.totp.recovery;

import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

public class RecoveryCodeGenerator {

    // Recovery code must reach a minimum entropy to be secured
    //   code entropy = log( {characters-count} ^ {code-length} ) / log(2)
    // the default settings used below allows the code to reach an entropy of 82 bits :
    //  log(36^16) / log(2) == 82.7...

    // the minimum allowed settings used below allows the code to reach a minimum entropy of 36 bits :
    // log(36^7) / log(2) == 36.18.. which is considered reasonable; fairly secure passwords

    // Recovery code must be simple to read and enter by end user when needed : 
    //  - generate a code composed of numbers and lower case characters from latin alphabet (36 possible characters)
    //  - split code in groups separated with dash for better readability, for example 4ckn-xspn-et8t-xgr0
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
    private static int CODE_LENGTH;
    private static int GROUPS_NBR;
  
    private Random random = new SecureRandom();

    public RecoveryCodeGenerator(){
        CODE_LENGTH = 16;
        GROUPS_NBR = 4;
    }

    public RecoveryCodeGenerator(int codeLength,int groupNumber){
        if (groupNumber < 1) {
            throw new InvalidParameterException("Group Number must be at least 1.");
        }
        if(codeLength < 7){
            throw new InsecureCodeException("Code Length must be at least 7 to be secure");
        }

        CODE_LENGTH=codeLength;
        GROUPS_NBR=groupNumber;
    }

    public static class Builder{
        private int codeLength=16;
        private int groupNumber=4;

        public Builder setCodeLength(int codeLength) {
            this.codeLength = codeLength;
            return this;
        }

        public Builder setGroupNumber(int groupNumber) {
            this.groupNumber = groupNumber;
            return this;
        }

        public RecoveryCodeGenerator build(){
            return new RecoveryCodeGenerator(codeLength,groupNumber);
        }
    }

    public CodePack[] generateCodes(int amount) {
        // Must generate at least one code
        if (amount < 1) {
            throw new InvalidParameterException("Amount must be at least 1.");
        }

        // Create an array and fill with generated codes
        CodePack[] codes = new CodePack[amount];
        Arrays.setAll(codes, i -> generateCode());

        return codes;
    }

    private CodePack generateCode() {
        final StringBuilder code_readable = new StringBuilder(CODE_LENGTH + (CODE_LENGTH/GROUPS_NBR) - 1);
        final StringBuilder code_actual = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            // Append random character from authorized ones
            char c =CHARACTERS[random.nextInt(CHARACTERS.length)];
            code_actual.append(c);
            code_readable.append(c);
            
            // Split code into groups for increased readability
            if ((i+1) % GROUPS_NBR == 0 && (i+1) != CODE_LENGTH) {
                code_readable.append("-");
            }
        }
        
        return new CodePack(code_readable.toString(),code_actual.toString());
    }

}