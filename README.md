# Time-based One Time Password (MFA) Library for Java

[![CircleCI](https://circleci.com/gh/samdjstevens/java-totp/tree/master.svg?style=svg&circle-token=10b865d8ba6091caba7a73a5a2295bd642ab79d5)](https://circleci.com/gh/samdjstevens/java-totp/tree/master)

A java library to help generate and verify time-based one time passwords for Multi-Factor Authentication.

Generates QR codes that are recognisable by applications like Google Authenticator, and verify the one time passwords they produce.

Inspired by [PHP library for Two Factor Authentication](https://github.com/RobThree/TwoFactorAuth), a similar library for PHP.

## Requirements

- Java 8+



## Installation

#### Maven

To add this library to your java project using Maven, add the following dependency:

```xml
<dependency>
  <groupId>dev.samstevens.totp</groupId>
  <artifactId>totp</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```



#### Gradle

To add the dependency using Gradle, add the following to the build script:

```
dependencies {
  compile 'dev.samstevens.totp:totp:1.0-SNAPSHOT'
}
```



## Usage

#### Generating a shared secret

@TODO



#### Displaying a QR code

@TODO



#### Verifying one time passwords

@TODO



## Running Tests

To run the tests for the library with Maven, run `mvn test`.




## License

This project is licensed under the [MIT license](https://opensource.org/licenses/MIT).