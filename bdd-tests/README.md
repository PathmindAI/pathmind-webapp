#### BDD Tests for Pathmind WebApp

###### Serenity+Cucumber+Selenium+Maven

**Run tests**
1. Clone project
2. Run tests on localhost `mvn clean verify`
    
    Run tests on https://pathmind-dev.azurewebsites.net
    
    `mvn clean verify -Denvironment=pathmind-dev`
    
    Run tests on https://app-beta.pathmind.com
    
    `mvn clean verify -Denvironment=pathmind-app-beta`
    
    Run tests with headless browser (for now available for chrome only)
    
    ` mvn clean verify -Dheadless=true`

After tests run, check generated report here `target/site/serenity/index.html`
