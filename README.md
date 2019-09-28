# mentoring-home-project

### Description
This is a UI test automation framework for web application called 'SkyXplore'.
More on SUT setup: https://docs.google.com/document/d/1jCLJbRgxdRNUktoMjbmOjZXWzvuCbZDO2fOiSjAPfsM

### Prerequisites

* Java 8
* Gradle 4.5.1
* Latest Google Chrome
* IntelliJ IDEA with plugins added:
    * Lombok 
    * .ignore
    * CheckStyle-IDEA

### Environment variables

Environment variable name | Possible values | Comment
--- | --- | ---
BROWSER | chrome, remote_chrome, firefox | For now only chrome is supported. Fallback browser is 'chrome'.
ENVIRONMENT | skyxplore-localhost, skyxplore-prod | -

### Generating Allure test-report

1. Run test task from Gradle
    ```
    gradle clean build {taskName}
    ```
2. Generate Allure test report through Gradle
    ```
    gradle build allureReport
    ```
3. Open 'index.html' located at: /build/reports/allure-report