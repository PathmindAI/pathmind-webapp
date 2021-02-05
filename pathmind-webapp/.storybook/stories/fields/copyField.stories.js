import { storiesOf } from "@storybook/polymer";
import { storyParamConfig } from "../../utils";

import "../../../frontend/src/components/molecules/copy-field.js";

storiesOf('Inputs', module)
.addDecorator(storyFn => `<div class="story-wrapper">
    ${storyFn()}
</div>`)
.add('Copyable Field', () => `
    <h1>Copyable Field</h1>
    <div class="block-wrapper">
        <p>This is a custom component from Pathmind.</p>
    </div>
    <h2>Customization</h2>
    <div class="block-wrapper">
        <div class="col-wrapper">
            <copy-field text="Text to be copied."></copy-field>
        </div>
    </div>
`, storyParamConfig);