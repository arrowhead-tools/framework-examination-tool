# Arrowhead Framework Examination Tool
Tool for use case and load testing Arrowhed Framewrok 4.1.3

### Requirements

The project has the following dependencies:
* **JRE/JDK 11** [Download from here](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html)
* **Maven 3.5+** [Download from here](http://maven.apache.org/download.cgi) | [Install guide](https://www.baeldung.com/install-maven-on-windows-linux-mac)

### Currently available test scenarios

* **Register provider with service then delete system and service definition** (SystemOperatorUseCase)
* **Orchestration load test** (ApplicationSystemUseCase)

### How to use?

* Clone or fork the repository.
* Set your Sevrice Registry address and port in `application.properties` located in src/main/resources folder.
* Compile the source code with `mvn clean install`.
* Start your Arrowhead Framewrok.
* Start examination tool with `java -jar framework-examination-tool-1.0.0.jar` located in target folder.
* Open the `report.hmtl` located in report folder and upload the report csv files from the same folder.

### How to implement new test scenarios?

A new scenario is possible to easily implement by creating a new java class within one of the following packages and annotated with `@Component`.
* eu.arrowhead.tool.examination.use_case.system_operator
* eu.arrowhead.tool.examination.use_case.application_system

Depending on the scenario the `SystemOperatorUseCase` or the `ApplicationSystemUseCase` have to be extened and the `public void runUseCase()` method have to be overridden with your scenario. Use the provided `request(...)` methods to raise an HTTP(S) request and the various `assert...(...)` methods to verify the responses. Upon application start-up it will be automatically detected, ran and reported.
