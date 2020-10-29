import { storiesOf } from "@storybook/polymer";
import { storyParamConfig } from "../../utils";

import "../../../frontend/src/components/molecules/notes-field.js";

storiesOf('Inputs', module)
.addDecorator(storyFn => `<div class="story-wrapper">
    ${storyFn()}
</div>`)
.add('Notes Field', () => `
    <h1>Notes Field</h1>
    <div class="block-wrapper">
        <p>This is a customized component from Pathmind.</p>
    </div>
    <h2>Customization</h2>
    <div class="block-wrapper">
        <div class="col-wrapper">
            <notes-field title="Project Notes" warning="Max. 1000 characters"></notes-field>
        </div>
    </div>
`, storyParamConfig);