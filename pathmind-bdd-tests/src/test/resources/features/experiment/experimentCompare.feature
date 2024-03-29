@experiment
Feature: Experiment page compare feature

  Scenario: Check compare feature check elements of two running experiments
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Click new experiment page observation checkbox 'orderQueueSize'
    When Click new experiment page observation checkbox 'timeOfDay'
    When Click project start run button
    When Click in 'New Experiment' button
    When Click new experiment page observation checkbox 'collectQueueSize'
    When Click new experiment page observation checkbox 'kitchenCleanlinessLevel'
    When Click project start run button
    When Click side nav 'Compare' button from navbarItemMenu for 'Experiment #1'
    Then Experiment page Check 'primary' experiment-header 'Experiment #2', 'Starting Cluster', Stop Training btn shown 'true', Share with support btn shown 'true', Share with support label shown 'false', experiment shared 'true'
    Then Experiment page Check 'secondary' experiment-header 'Experiment #1', 'Starting Cluster', Stop Training btn shown 'true', Share with support btn shown 'true', Share with support label shown 'false', experiment shared 'true'

    Then Experiment page Check 'primary' middle panel
    Then Experiment page Check 'secondary' middle panel

    Then Experiment page Check 'primary' bottom panel
    Then Experiment page Check 'secondary' bottom panel

    Then Experiment page Check 'primary' simulation metrics kitchenCleanlinessLevel,successfulCustomers,balkedCustomers,avgServiceTime
    Then Experiment page Check 'secondary' simulation metrics kitchenCleanlinessLevel,successfulCustomers,balkedCustomers,avgServiceTime

    Then Experiment page Check 'primary' simulation metric 'kitchenCleanlinessLevel' is chosen 'true'
    Then Experiment page Check 'secondary' simulation metric 'successfulCustomers' is chosen 'false'

    Then Experiment page Check 'primary' observation 'orderQueueSize' is checked 'false'
    Then Experiment page Check 'primary' observation 'collectQueueSize' is checked 'false'
    Then Experiment page Check 'primary' observation 'payBillQueueSize' is checked 'true'
    Then Experiment page Check 'primary' observation 'kitchenCleanlinessLevel' is checked 'false'
    Then Experiment page Check 'primary' observation 'timeOfDay' is checked 'false'
    Then Experiment page Check 'secondary' observation 'orderQueueSize' is checked 'false'
    Then Experiment page Check 'secondary' observation 'collectQueueSize' is checked 'true'
    Then Experiment page Check 'secondary' observation 'payBillQueueSize' is checked 'true'
    Then Experiment page Check 'secondary' observation 'kitchenCleanlinessLevel' is checked 'true'
    Then Experiment page Check 'secondary' observation 'timeOfDay' is checked 'false'

    Then Experiment page Check 'primary' reward function new 'CoffeeShop/CoffeeShopRewardFunction.txt'
    Then Experiment page Check 'secondary' reward function new 'CoffeeShop/CoffeeShopRewardFunction.txt'

  Scenario: Check compare feature check elements of two stopped experiments
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Click new experiment page observation checkbox 'orderQueueSize'
    When Click new experiment page observation checkbox 'timeOfDay'
    When Click project start run button
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button
    When Click in 'New Experiment' button
    When Click new experiment page observation checkbox 'collectQueueSize'
    When Click new experiment page observation checkbox 'kitchenCleanlinessLevel'
    When Click project start run button
    When Click experiment page share with support btn
    When In confirmation dialog click in 'Share Training' button
    When Click pop-up dialog close btn
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button
    When Click side nav 'Compare' button from navbarItemMenu for 'Experiment #1'
    Then Experiment page Check 'primary' experiment-header 'Experiment #2', 'Stopping', Stop Training btn shown 'false', Share with support btn shown 'true', Share with support label shown 'true', experiment shared 'true'
    Then Experiment page Check 'secondary' experiment-header 'Experiment #1', 'Stopping', Stop Training btn shown 'false', Share with support btn shown 'true', Share with support label shown 'false', experiment shared 'true'

    Then Experiment page Check 'primary' middle panel
    Then Experiment page Check 'secondary' middle panel

    Then Experiment page Check 'primary' bottom panel
    Then Experiment page Check 'secondary' bottom panel

    Then Experiment page Check 'primary' simulation metrics kitchenCleanlinessLevel,successfulCustomers,balkedCustomers,avgServiceTime
    Then Experiment page Check 'secondary' simulation metrics kitchenCleanlinessLevel,successfulCustomers,balkedCustomers,avgServiceTime

    Then Experiment page Check 'primary' simulation metric 'kitchenCleanlinessLevel' is chosen 'true'
    Then Experiment page Check 'secondary' simulation metric 'successfulCustomers' is chosen 'false'

    Then Experiment page Check 'primary' observation 'orderQueueSize' is checked 'false'
    Then Experiment page Check 'primary' observation 'collectQueueSize' is checked 'false'
    Then Experiment page Check 'primary' observation 'payBillQueueSize' is checked 'true'
    Then Experiment page Check 'primary' observation 'kitchenCleanlinessLevel' is checked 'false'
    Then Experiment page Check 'primary' observation 'timeOfDay' is checked 'false'
    Then Experiment page Check 'secondary' observation 'orderQueueSize' is checked 'false'
    Then Experiment page Check 'secondary' observation 'collectQueueSize' is checked 'true'
    Then Experiment page Check 'secondary' observation 'payBillQueueSize' is checked 'true'
    Then Experiment page Check 'secondary' observation 'kitchenCleanlinessLevel' is checked 'true'
    Then Experiment page Check 'secondary' observation 'timeOfDay' is checked 'false'

    Then Experiment page Check 'primary' reward function new 'CoffeeShop/CoffeeShopRewardFunction.txt'
    Then Experiment page Check 'secondary' reward function new 'CoffeeShop/CoffeeShopRewardFunction.txt'

  Scenario: Check compare feature check elements of stopped and one running experiment
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Click new experiment page observation checkbox 'orderQueueSize'
    When Click new experiment page observation checkbox 'timeOfDay'
    When Click project start run button
    When Click experiment page share with support btn
    When In confirmation dialog click in 'Share Training' button
    When Click pop-up dialog close btn
    When Click in 'New Experiment' button
    When Click new experiment page observation checkbox 'collectQueueSize'
    When Click new experiment page observation checkbox 'kitchenCleanlinessLevel'
    When Click project start run button
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button
    When Click side nav 'Compare' button from navbarItemMenu for 'Experiment #1'
    Then Experiment page Check 'primary' experiment-header 'Experiment #2', 'Stopping', Stop Training btn shown 'false', Share with support btn shown 'true', Share with support label shown 'false', experiment shared 'true'
    Then Experiment page Check 'secondary' experiment-header 'Experiment #1', 'Starting Cluster', Stop Training btn shown 'true', Share with support btn shown 'false', Share with support label shown 'true', experiment shared 'true'

    Then Experiment page Check 'primary' middle panel
    Then Experiment page Check 'secondary' middle panel

    Then Experiment page Check 'primary' bottom panel
    Then Experiment page Check 'secondary' bottom panel

    Then Experiment page Check 'primary' simulation metrics kitchenCleanlinessLevel,successfulCustomers,balkedCustomers,avgServiceTime
    Then Experiment page Check 'secondary' simulation metrics kitchenCleanlinessLevel,successfulCustomers,balkedCustomers,avgServiceTime

    Then Experiment page Check 'primary' simulation metric 'kitchenCleanlinessLevel' is chosen 'true'
    Then Experiment page Check 'secondary' simulation metric 'successfulCustomers' is chosen 'false'

    Then Experiment page Check 'primary' observation 'orderQueueSize' is checked 'false'
    Then Experiment page Check 'primary' observation 'collectQueueSize' is checked 'false'
    Then Experiment page Check 'primary' observation 'payBillQueueSize' is checked 'true'
    Then Experiment page Check 'primary' observation 'kitchenCleanlinessLevel' is checked 'false'
    Then Experiment page Check 'primary' observation 'timeOfDay' is checked 'false'
    Then Experiment page Check 'secondary' observation 'orderQueueSize' is checked 'false'
    Then Experiment page Check 'secondary' observation 'collectQueueSize' is checked 'true'
    Then Experiment page Check 'secondary' observation 'payBillQueueSize' is checked 'true'
    Then Experiment page Check 'secondary' observation 'kitchenCleanlinessLevel' is checked 'true'
    Then Experiment page Check 'secondary' observation 'timeOfDay' is checked 'false'

    Then Experiment page Check 'primary' reward function new 'CoffeeShop/CoffeeShopRewardFunction.txt'
    Then Experiment page Check 'secondary' reward function new 'CoffeeShop/CoffeeShopRewardFunction.txt'

  Scenario: Check compare feature check reward variables chosen
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Click project start run button
    When Click in 'Stop Training' button
    Then Check that the 'Stop Training' confirmation dialog is shown
    When In confirmation dialog click in 'Stop Training' button
    When Click in 'New Experiment' button
    When Click project start run button
    When Click experiment page share with support btn
    When In confirmation dialog click in 'Share Training' button
    When Click pop-up dialog close btn
    When Click side nav 'Compare' button from navbarItemMenu for 'Experiment #1'
    When Experiment page 'primary' slot check reward variable 'kitchenCleanlinessLevel' is chosen 'true'
    When Experiment page 'primary' slot check reward variable 'successfulCustomers' is chosen 'false'
    When Experiment page 'primary' slot check reward variable 'balkedCustomers' is chosen 'false'
    When Experiment page 'primary' slot check reward variable 'avgServiceTime' is chosen 'false'
    When Experiment page 'secondary' slot check reward variable 'kitchenCleanlinessLevel' is chosen 'true'
    When Experiment page 'secondary' slot check reward variable 'successfulCustomers' is chosen 'false'
    When Experiment page 'secondary' slot check reward variable 'balkedCustomers' is chosen 'false'
    When Experiment page 'secondary' slot check reward variable 'avgServiceTime' is chosen 'false'
    When Experiment page 'primary' slot click reward variable 'successfulCustomers'
    When Experiment page 'primary' slot check reward variable 'kitchenCleanlinessLevel' is chosen 'false'
    When Experiment page 'primary' slot check reward variable 'successfulCustomers' is chosen 'true'
    When Experiment page 'primary' slot check reward variable 'balkedCustomers' is chosen 'false'
    When Experiment page 'primary' slot check reward variable 'avgServiceTime' is chosen 'false'
    When Experiment page 'secondary' slot check reward variable 'kitchenCleanlinessLevel' is chosen 'false'
    When Experiment page 'secondary' slot check reward variable 'successfulCustomers' is chosen 'true'
    When Experiment page 'secondary' slot check reward variable 'balkedCustomers' is chosen 'false'
    When Experiment page 'secondary' slot check reward variable 'avgServiceTime' is chosen 'false'
    When Experiment page 'primary' slot click reward variable 'balkedCustomers'
    When Experiment page 'primary' slot check reward variable 'kitchenCleanlinessLevel' is chosen 'false'
    When Experiment page 'primary' slot check reward variable 'successfulCustomers' is chosen 'false'
    When Experiment page 'primary' slot check reward variable 'balkedCustomers' is chosen 'true'
    When Experiment page 'primary' slot check reward variable 'avgServiceTime' is chosen 'false'
    When Experiment page 'secondary' slot check reward variable 'kitchenCleanlinessLevel' is chosen 'false'
    When Experiment page 'secondary' slot check reward variable 'successfulCustomers' is chosen 'false'
    When Experiment page 'secondary' slot check reward variable 'balkedCustomers' is chosen 'true'
    When Experiment page 'secondary' slot check reward variable 'avgServiceTime' is chosen 'false'
    When Experiment page 'primary' slot click reward variable 'avgServiceTime'
    When Experiment page 'primary' slot check reward variable 'kitchenCleanlinessLevel' is chosen 'false'
    When Experiment page 'primary' slot check reward variable 'successfulCustomers' is chosen 'false'
    When Experiment page 'primary' slot check reward variable 'balkedCustomers' is chosen 'false'
    When Experiment page 'primary' slot check reward variable 'avgServiceTime' is chosen 'true'
    When Experiment page 'secondary' slot check reward variable 'kitchenCleanlinessLevel' is chosen 'false'
    When Experiment page 'secondary' slot check reward variable 'successfulCustomers' is chosen 'false'
    When Experiment page 'secondary' slot check reward variable 'balkedCustomers' is chosen 'false'
    When Experiment page 'secondary' slot check reward variable 'avgServiceTime' is chosen 'true'

    When Experiment page 'primary' slot click reward variable 'kitchenCleanlinessLevel'
    When Experiment page 'secondary' slot check reward variable 'kitchenCleanlinessLevel' is chosen 'true'
    When Experiment page 'secondary' slot check reward variable 'successfulCustomers' is chosen 'false'
    When Experiment page 'secondary' slot check reward variable 'balkedCustomers' is chosen 'false'
    When Experiment page 'secondary' slot check reward variable 'avgServiceTime' is chosen 'false'
    When Experiment page 'primary' slot check reward variable 'kitchenCleanlinessLevel' is chosen 'true'
    When Experiment page 'primary' slot check reward variable 'successfulCustomers' is chosen 'false'
    When Experiment page 'primary' slot check reward variable 'balkedCustomers' is chosen 'false'
    When Experiment page 'primary' slot check reward variable 'avgServiceTime' is chosen 'false'
    When Experiment page 'secondary' slot click reward variable 'successfulCustomers'
    When Experiment page 'secondary' slot check reward variable 'kitchenCleanlinessLevel' is chosen 'false'
    When Experiment page 'secondary' slot check reward variable 'successfulCustomers' is chosen 'true'
    When Experiment page 'secondary' slot check reward variable 'balkedCustomers' is chosen 'false'
    When Experiment page 'secondary' slot check reward variable 'avgServiceTime' is chosen 'false'
    When Experiment page 'primary' slot check reward variable 'kitchenCleanlinessLevel' is chosen 'false'
    When Experiment page 'primary' slot check reward variable 'successfulCustomers' is chosen 'true'
    When Experiment page 'primary' slot check reward variable 'balkedCustomers' is chosen 'false'
    When Experiment page 'primary' slot check reward variable 'avgServiceTime' is chosen 'false'
    When Experiment page 'secondary' slot click reward variable 'balkedCustomers'
    When Experiment page 'secondary' slot check reward variable 'kitchenCleanlinessLevel' is chosen 'false'
    When Experiment page 'secondary' slot check reward variable 'successfulCustomers' is chosen 'false'
    When Experiment page 'secondary' slot check reward variable 'balkedCustomers' is chosen 'true'
    When Experiment page 'secondary' slot check reward variable 'avgServiceTime' is chosen 'false'
    When Experiment page 'primary' slot check reward variable 'kitchenCleanlinessLevel' is chosen 'false'
    When Experiment page 'primary' slot check reward variable 'successfulCustomers' is chosen 'false'
    When Experiment page 'primary' slot check reward variable 'balkedCustomers' is chosen 'true'
    When Experiment page 'primary' slot check reward variable 'avgServiceTime' is chosen 'false'
    When Experiment page 'secondary' slot click reward variable 'avgServiceTime'
    When Experiment page 'secondary' slot check reward variable 'kitchenCleanlinessLevel' is chosen 'false'
    When Experiment page 'secondary' slot check reward variable 'successfulCustomers' is chosen 'false'
    When Experiment page 'secondary' slot check reward variable 'balkedCustomers' is chosen 'false'
    When Experiment page 'secondary' slot check reward variable 'avgServiceTime' is chosen 'true'
    When Experiment page 'primary' slot check reward variable 'kitchenCleanlinessLevel' is chosen 'false'
    When Experiment page 'primary' slot check reward variable 'successfulCustomers' is chosen 'false'
    When Experiment page 'primary' slot check reward variable 'balkedCustomers' is chosen 'false'
    When Experiment page 'primary' slot check reward variable 'avgServiceTime' is chosen 'true'

  Scenario: Check comparison pin is unpinned
    Given Login to the pathmind
    When Create new CoffeeShop project with single reward function
    When Click new experiment page observation checkbox 'orderQueueSize'
    When Click new experiment page observation checkbox 'timeOfDay'
    When Click project start run button
    When Click in 'New Experiment' button
    When Click project start run button
    When Click side nav 'Compare' button from navbarItemMenu for 'Experiment #1'
    When Experiment page click comparison floating close btn

  Scenario: Check experiments comparison diff
    Given Login to the pathmind
    When Create new CoffeeShop project with draft experiment
    When Input from file reward function CoffeeShop/CoffeeShopRewardFunctionOneFunction.txt
    When Click new experiment page observation checkbox 'orderQueueSize'
    When Click new experiment page observation checkbox 'timeOfDay'
    When Click project start run button
    When Click experiment page share with support btn
    When In confirmation dialog click in 'Share Training' button
    When Click pop-up dialog close btn
    When Click in 'New Experiment' button
    When Input reward function reward += after.avgServiceTime - before.balkedCustomers; // Maximize kitchen cleanliness test1
    When Click new experiment page observation checkbox 'collectQueueSize'
    When Click new experiment page observation checkbox 'timeOfDay'
    When Click project start run button
    When Click side nav 'Compare' button from navbarItemMenu for 'Experiment #1'
    When Wait a bit 5000 ms
    Then Experiment page Check 'primary' observation 'orderQueueSize' is highlighted 'false'
    Then Experiment page Check 'primary' observation 'collectQueueSize' is highlighted 'true'
    Then Experiment page Check 'primary' observation 'payBillQueueSize' is highlighted 'false'
    Then Experiment page Check 'primary' observation 'kitchenCleanlinessLevel' is highlighted 'false'
    Then Experiment page Check 'primary' observation 'timeOfDay' is highlighted 'true'
    Then Experiment page Check 'secondary' observation 'orderQueueSize' is highlighted 'false'
    Then Experiment page Check 'secondary' observation 'collectQueueSize' is highlighted 'true'
    Then Experiment page Check 'secondary' observation 'payBillQueueSize' is highlighted 'false'
    Then Experiment page Check 'secondary' observation 'kitchenCleanlinessLevel' is highlighted 'false'
    Then Experiment page Check 'secondary' observation 'timeOfDay' is highlighted 'true'
    Then Experiment page Check 'primary' reward variable 'balkedCustomers' is highlighted 'true'
    Then Experiment page Check 'primary' reward variable 'avgServiceTime' is highlighted 'true'
    Then Experiment page Check 'secondary' reward variable 'kitchenCleanlinessLevel' is highlighted 'true'
