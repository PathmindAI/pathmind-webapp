#### BDD Tests for Pathmind WebApp

###### Serenity+Cucumber+Selenium+Maven

**Run tests**
1. Clone project
2. Define your credentials for pathmind in system variables  
(e.g., export TESTS_PASSWORD=mypass)  
`${TESTS_USERNAME}`  
`${TESTS_PASSWORD}`
3. Run tests on localhost `mvn clean verify -Pbdd-tests`
    
    Run tests on https://dev.devpathmind.com/
    
    `mvn clean verify -Denvironment=pathmind-dev -Pbdd-tests`
    
    Run tests on https://test.devpathmind.com/
        
    `mvn clean verify -Denvironment=pathmind-test -Pbdd-tests`
    
    Run tests with headless browser (for now available for chrome only)
    
    `mvn clean verify -Dheadless=true -Pbdd-tests`
    
    Run tests marked with tag
    
    `mvn clean verify "-Dcucumber.options=--tags @tag" -Pbdd-tests`
    
**E2E tests**

There are two E2E tests with full training `features/e2e.feature`. Tests separated from the full run. E2e tests could be started with `@e2e` tag, here is the example of the command `mvn clean verify -Dheadless=true -Pbdd-tests -Dtags=e2e`

1. CheeseChasing_6Observations_4Actions.zip Reward function: `reward = after[0] - before[0];` Expected reward score: > 0.9
2. CoffeeShopPathmindDemo.zip Expected reward score: > 120

    ````java
    reward += after[0] - before[0]; // Maximize kitchen cleanliness
    reward += after[1] - before[1]; // Maximize successful exits
    reward -= after[2] - before[2]; // Minimize balked customers
    reward -= after[3] - before[3]; // Minimize average service time
    ````

##### Resources

[Serenity Reference Manual](http://thucydides.info/docs/serenity-staging/)

You will need to use direct XPath or CSS selectors to select the elements.
[XPath cheatsheet](https://devhints.io/xpath)

After tests run, check generated report here `target/site/serenity/index.html`


