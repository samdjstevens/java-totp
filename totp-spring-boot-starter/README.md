# Using Java-TOTP with Spring Boot



## Installation

To get started using the library in a Spring Boot project, add the `totp-spring-boot-starter` dependency:

#### Maven

```xml
<dependency>
  <groupId>dev.samstevens.totp</groupId>
  <artifactId>totp-spring-boot-starter</artifactId>
  <version>1.7.1</version>
</dependency>
```

#### Gradle

```
dependencies {
  compile 'dev.samstevens.totp:totp-spring-boot-starter:1.7.1'
}
```



## Usage

#### Generating QR codes

```java
@Controller
public class MfaSetupController {

    @Autowired
    private SecretGenerator secretGenerator;

    @Autowired
    private QrDataFactory qrDataFactory;

    @Autowired
    private QrGenerator qrGenerator;

    @GetMapping("/mfa/setup")
    public String setupDevice() throws QrGenerationException {
        // Generate and store the secret
        String secret = secretGenerator.generate();

        QrData data = qrDataFactory.newBuilder()
            .label("example@example.com")
            .secret(secret)
            .issuer("AppName")
            .build();

        // Generate the QR code image data as a base64 string which
        // can be used in an <img> tag:
        String qrCodeImage = getDataUriForImage(
          qrGenerator.generate(data), 
          qrGenerator.getImageMimeType()
        );
        ...
    }
}
```



#### Verifying a code

To verify a code that is submitted by a user, inject the `CodeVerifier` service and call `isValidCode`:


```java
@Controller
public class MfaVerifyController {
    @Autowired
    private CodeVerifier verifier;

    @PostMapping("/mfa/verify")
    @ResponseBody
    public String verify(@RequestParam String code) {
        // secret is fetched from some storage

        if (verifier.isValidCode(secret, code)) {
            return "CORRECT CODE";
        }

        return "INCORRECT CODE";
    }
}
```



#### Generating recovery codes

To generate recovery codes, use the `RecoveryCodeGenerator` service:

```java
Controller
public class MfaRecoveryCodesController {
    @Autowired
    private RecoveryCodeGenerator recoveryCodeGenerator;

    @GetMapping("/mfa/recovery-codes")
    public String recoveryCodes() {
        String[] codes = recoveryCodeGenerator.generateCodes(16);
        ...
    }
}
```






## Configuring

Configuring the various options that are available with the library can be achieved by setting application properties or defining beans.



##### Secret Length

Set the `totp.secret.length` property to the desired number of characters in `application.properties`:

```
totp.secret.length=128
```



##### Code Length

Set the `totp.code.length` property to the desired number of characters in `application.properties`:

```
totp.code.length=8
```



##### Time Period

Set the `totp.time.period` property to the desired number of characters in `application.properties`:

```
totp.time.period=15
```



##### Time Discrepancy

Set the `totp.time.discrepancy` property to the desired number of characters in `application.properties`:

```
totp.time.discrepancy=2
```



##### Hashing Algorithm

The default hashing algorithm is SHA1. To change it to another algorithm, define a `HashingAlgorithm` bean which returns the desired algorithm:

```java
@Configuration
public class AppConfig {
    @Bean
    public HashingAlgorithm hashingAlgorithm() {
        return HashingAlgorithm.SHA256;
    }
}
```



##### Time Provider

The default time provider uses the system time to fetch the time. To change this, define a `TimeProvider` bean that returns a `TimeProvider` instance.

```java
@Configuration
public class AppConfig {
    @Bean
    public TimeProvider timeProvider() {
        return new NtpTimeProvider("pool.ntp.org");
    }
}
```
