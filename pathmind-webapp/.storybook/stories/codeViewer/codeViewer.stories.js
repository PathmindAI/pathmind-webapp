import { storiesOf } from "@storybook/polymer";
import { storyParamConfig } from "../../utils";

import "../../../frontend/src/experiment/code-viewer.js";

const single = `reward -= after.kitchenCleanlinessLevel - before.kitchenCleanlinessLevel;
// processhead
reward += after.successfulCustomers - before.successfulCustomers;
// reward += after.successfulCustomers - before.successfulCustomers;
/* reward += after.successfulCustomers - before.successfulCustomers; */
/* reward += after.successfulCustomers - before.succ
afdafasfasfsdf
essfulCustomers; */
// gantry
reward -= after.balkedCustomers - before.balkedCustomers / 2;
// reward -= after.balkedCustomers - before.balkedCustomers / 2;
reward -= after.avgServiceTime - before.avgServiceTime; // minimize average service time test4`

const compareCodeSnippet = `reward -= after.kitchenCleanlinessLevel - before.kitchenCleanlinessLevel; // minimize kitchenCleanlinessLevel test1
reward += after.successfulCustomers - before.successfulCustomers; // maximize successfulCustomers test 2
reward -= after.balkedCustomers - before.balkedCustomers; // minimize balked customers test3
reward -= after.avgServiceTime - before.avgServiceTime; // minimize average service time test4`

const compareComparisonSnippet = `reward -= after.kitchenCleanlinessLevel - before.kitchenCleanlinessLevel; // minimize kitchenCleanlinessLevel test1`

storiesOf('Code Viewer', module)
.addDecorator(storyFn => `
    <div class="story-wrapper">
        ${storyFn()}
    </div>`)
.add('Code Viewer', () => `
    <h1>Code Viewer</h1>
    <div class="block-wrapper">
        <p>This is a custom component from Pathmind.</p>
    </div>
    <h3>Experiment View (Single)</h3>
    <div class="block-wrapper">
        <div class="col-wrapper">
            <code-viewer id="test" code-snippet="${single}"></code-viewer>
        </div>
    </div>
    <h3>Experiment View (Comparison)</h3>
    <div class="block-wrapper">
        <div class="col-wrapper">
            <code-viewer id="test" code-snippet="${compareCodeSnippet}" comparison-code-snippet="${compareComparisonSnippet}"></code-viewer>
        </div>
    </div>
`, storyParamConfig);