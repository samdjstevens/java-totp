package dev.samstevens.totp.recovery;

public class CodePack {
    public String readableCode=null;
    public String actualCode=null;

    public CodePack(String readableCode, String actualCode) {
        this.readableCode = readableCode;
        this.actualCode = actualCode;
    }
}
