// TODO: add demo stories
import { storiesOf } from "@storybook/polymer";
import { registerStyles, unsafeCSS, hljs, hlJava, hlConfig, copyCodeBlock, storyParamConfig } from "../utils";

import pathmindVaadinButtonStyles from "!to-string-loader!css-loader!../../frontend/styles/components/vaadin-button.css";
import "@vaadin/vaadin-button/vaadin-button.js";
import "@vaadin/vaadin-button/theme/lumo/vaadin-button.js";

registerStyles("vaadin-button", unsafeCSS(pathmindVaadinButtonStyles));

// const hljs = require("highlight.js/lib/core");
hljs.registerLanguage('java', hlJava);

const primaryButton = `
Button yourButton = new Button("Primary", evt -> doSomething());
yourButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
`;
const secondaryButton = `
Button yourButton = new Button("Secondary", evt -> doSomething());
`;
const tertiaryButton = `
Button yourButton = new Button("Tertiary", evt -> doSomething());
archiveExperimentButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
`;
const tertiaryInlineButton = `
Button yourButton = new Button("Tertiary Inline", evt -> doSomething());
archiveExperimentButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
`;
const iconButton = `
Button yourButton = new Button(VaadinIcon.ARCHIVE.create(), evt -> doSomething());
archiveExperimentButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
yourButton.addClassName("action-button");
`;

storiesOf('Inputs', module)
.addDecorator(storyFn => `<div class="story-wrapper">
    ${storyFn()}
</div>`)
.add('Button', () => `
    <h1>Button</h1>
    <div class="col-wrapper">
        <vaadin-button theme="primary">Primary</vaadin-button>
        ${copyCodeBlock(primaryButton, hlConfig)}
    </div>
    <div class="col-wrapper">
        <vaadin-button>Secondary / Default</vaadin-button>
        ${copyCodeBlock(secondaryButton, hlConfig)}
    </div>
    <div class="col-wrapper">
        <vaadin-button theme="tertiary">Tertiary</vaadin-button>
        ${copyCodeBlock(tertiaryButton, hlConfig)}
    </div>
    <div class="col-wrapper">
        <vaadin-button theme="tertiary-inline">Tertiary Inline</vaadin-button>
        ${copyCodeBlock(tertiaryInlineButton, hlConfig)}
    </div>
    <div class="col-wrapper">
        <vaadin-button theme="tertiary-inline" class="action-button"><iron-icon icon="vaadin:archive"></iron-icon></vaadin-button>
        ${copyCodeBlock(iconButton, hlConfig)}
    </div>
`, storyParamConfig);