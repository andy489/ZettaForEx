# Prerequisites
- Java JDK 17 or later
- Gradle 8.x or later
- Spring Boot 3.5.0 or latest stable
- Docker 27.x or later

# Setting Up a Database with Docker
```bash
# specific to macOS (needs to open the Docker app first)
open -a docker

# Run services described in the compose.yml in background
docker compose up -d 
```

# Running the Application
```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun

```

# Project Structure

```
├── gradlew.bat
├── settings.gradle
├── compose.yml
├── auto-generated-changelog.mysql.xml
src
├── test
│   ├── resources
│   │   └── application-test.yml
│   └── java
│       └── com
│           └── zetta
│               └── forex
│                   ├── util
│                   │   ├── TestConstants.java
│                   │   └── CurrencyMapCreator.java
│                   ├── ZettaForExApplicationTests.java
│                   └── service
│                       ├── ForexControllerIntegrationTest.java
│                       └── ForexApiServiceUnitTest.java
└── main
    ├── resources
    │   ├── log4j2.xml
    │   ├── db
    │   │   └── changelog
    │   │       ├── db.changelog-master.xml
    │   │       ├── changelog-v2.0.xml
    │   │       ├── data
    │   │       │   └── conversions_data.csv
    │   │       └── changelog-v1.0.xml
    │   └── application.yml
    └── java
        └── com
            └── zetta 
                └── forex
                    ├── ZettaForExApplication.java
                    ├── config
                    │   ├── LocalDateTimeProvider.java
                    │   ├── MapStructMapper.java
                    │   ├── RestConsumerConfig.java
                    │   ├── GlobalExceptionHandler.java
                    │   └── DataBaseInitializer.java
                    ├── aop
                    │   ├── ConversionHistoryMarker.java
                    │   └── ConversionHistoryAspect.java
                    ├── specification
                    │   └── ConversionHistorySpecification.java
                    ├── model
                    │   ├── dto
                    │   │   ├── ConversionHistoryResponse.java
                    │   │   ├── ConversionResponseDto.java
                    │   │   ├── AllRatesResponseDto.java
                    │   │   ├── ExchangeRateResponseDto.java
                    │   │   └── ErrorResponse.java
                    │   ├── entity
                    │   │   ├── ExchangeRatesEntity.java
                    │   │   ├── BaseEntity.java
                    │   │   └── ConversionHistoryEntity.java
                    │   ├── criteria
                    │   │   └── ConversionHistoryCriteria.java
                    │   └── validation
                    │       ├── ValidCurrencyAmount.java
                    │       ├── ValidCurrencyCode.java
                    │       ├── CurrencyCodeValidator.java
                    │       └── CurrencyAmountValidator.java
                    ├── util
                    │   └── Util.java
                    ├── service
                    │   ├── ConversionHistoryService.java
                    │   └── ForexApiService.java
                    ├── repo
                    │   ├── ConversionHistoryRepository.java
                    │   └── ExchangeRateRepository.java
                    └── rest
                        ├── ForexController.java
                        └── ConversionHistoryController.java
```