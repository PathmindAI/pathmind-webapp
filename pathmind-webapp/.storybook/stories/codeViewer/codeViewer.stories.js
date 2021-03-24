import { storiesOf } from "@storybook/polymer";
import { storyParamConfig } from "../../utils";

import "../../../frontend/src/experiment/code-viewer.js";

// const codeViewer = document.getElementById("test")
// codeViewer.codeSnippet = `reward -= after.kitchenCleanlinessLevel - before.kitchenCleanlinessLevel; // minimize kitchenCleanlinessLevel test1
// reward += after.successfulCustomers - before.successfulCustomers; // maximize successfulCustomers test 2
// reward -= after.balkedCustomers - before.balkedCustomers; // minimize balked customers test3
// reward -= after.avgServiceTime - before.avgServiceTime; // minimize average service time test4`
// codeViewer.comparisonCodeSnippet = `reward -= after.kitchenCleanlinessLevel - before.kitchenCleanlinessLevel; // minimize kitchenCleanlinessLevel test1`

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
    <h2>Customization</h2>
    <div class="block-wrapper">
        <div class="col-wrapper">
            <code-viewer id="test"></code-viewer>
        </div>
    </div>
`, storyParamConfig);