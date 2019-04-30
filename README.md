# mentoring-home-project

### Prerequisites

Preferred IDE: IntelliJ IDEA

* Java 8
* Gradle 4.5.1
* Lombok plugin
* .ignore plugin
* CheckStyle-IDEA plugin (for development only)

Annotation processing must be enabled for local test execution.

### Setup TestNG for local test-execution

* Click 'Add Configuration' in the toolbar (IntelliJ)
* Expand 'Templates'
* Select 'TestNG'
* Add environment variables as listed below ('Environment variables' section) with one corresponding value
* Save environment / TestNG setting by clicking 'Ok'

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