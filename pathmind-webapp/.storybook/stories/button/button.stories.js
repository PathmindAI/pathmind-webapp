import { storiesOf } from "@storybook/polymer";
import { registerStyles, unsafeCSS, hljs, hlJava, hlConfig, copyCodeBlock, storyParamConfig } from "../../utils";

import pathmindVaadinButtonStyles from "!to-string-loader!css-loader!../../../frontend/styles/components/vaadin-button.css";
import "@vaadin/vaadin-button/vaadin-button.js";
import "@vaadin/vaadin-button/theme/lumo/vaadin-button.js";

import code from "./codeSnippets";

registerStyles("vaadin-button", unsafeCSS(pathmindVaadinButtonStyles));

hljs.registerLanguage('java', hlJava);

console.log(code)

storiesOf('Inputs', module)
.addDecorator(storyFn => `<div class="story-wrapper">
    ${storyFn()}
</div>`)
.add('Button', () => `
    <h1>Button</h1>
    <div class="block-wrapper">
        <p>The theme variants are set by Vaadin. Below are our customized styles for each theme.</p>
        <p>
            See <a href="https://vaadin.com/components/vaadin-button/html-examples/button-theme-variants-demos">Vaadin Themed Button Examples</a>
        </p>
    </div>
    <h2>Themes</h2>
    <div class="block-wrapper">
        <div class="col-wrapper">
            <vaadin-button theme="primary">Primary</vaadin-button>
            ${copyCodeBlock(code.primaryButton, hlConfig)}
        </div>
        <div class="col-wrapper">
            <vaadin-button>Secondary / Default</vaadin-button>
            ${copyCodeBlock(code.secondaryButton, hlConfig)}
        </div>
        <div class="col-wrapper">
            <vaadin-button theme="tertiary">Tertiary</vaadin-button>
            ${copyCodeBlock(code.tertiaryButton, hlConfig)}
        </div>
        <div class="col-wrapper">
            <vaadin-button theme="tertiary-inline">Tertiary Inline</vaadin-button>
            ${copyCodeBlock(code.tertiaryInlineButton, hlConfig)}
        </div>
        <div class="col-wrapper">
            <vaadin-button theme="tertiary-inline" class="action-button"><iron-icon icon="vaadin:archive"></iron-icon></vaadin-button>
            ${copyCodeBlock(code.actionButton, hlConfig)}
        </div>
        <div class="col-wrapper">
            <vaadin-button theme="icon"><iron-icon icon="vaadin:archive"></iron-icon></vaadin-button>
            ${copyCodeBlock(code.iconButton, hlConfig)}
        </div>
    </div>
    <h2>Colors</h2>
    <div class="block-wrapper">
        <h3>Success</h3>
        <div class="col-wrapper">
            <vaadin-button theme="success primary">Primary, Success</vaadin-button>
            ${copyCodeBlock(code.primarySuccessButton, hlConfig)}
        </div>
        <div class="col-wrapper">
            <vaadin-button theme="success">Secondary, Success</vaadin-button>
            ${copyCodeBlock(code.secondarySuccessButton, hlConfig)}
        </div>
        <div class="col-wrapper">
            <vaadin-button theme="success tertiary">Tertiary, Success</vaadin-button>
            ${copyCodeBlock(code.tertiarySuccessButton, hlConfig)}
        </div>
        <div class="col-wrapper">
            <vaadin-button theme="success tertiary-inline">Tertiary Inline, Success</vaadin-button>
            ${copyCodeBlock(code.tertiaryInlineSuccessButton, hlConfig)}
        </div>
        <div class="col-wrapper">
            <vaadin-button theme="success icon"><iron-icon icon="vaadin:archive"></iron-icon></vaadin-button>
            ${copyCodeBlock(code.iconSuccessButton, hlConfig)}
        </div>
        <h3>Error</h3>
        <div class="col-wrapper">
            <vaadin-button theme="error primary">Primary, Error</vaadin-button>
            ${copyCodeBlock(code.primaryErrorButton, hlConfig)}
        </div>
        <div class="col-wrapper">
            <vaadin-button theme="error">Secondary, Error</vaadin-button>
            ${copyCodeBlock(code.secondaryErrorButton, hlConfig)}
        </div>
        <div class="col-wrapper">
            <vaadin-button theme="error tertiary">Tertiary, Error</vaadin-button>
            ${copyCodeBlock(code.tertiaryErrorButton, hlConfig)}
        </div>
        <div class="col-wrapper">
            <vaadin-button theme="error tertiary-inline">Tertiary Inline, Error</vaadin-button>
            ${copyCodeBlock(code.tertiaryInlineErrorButton, hlConfig)}
        </div>
        <div class="col-wrapper">
            <vaadin-button theme="error icon"><iron-icon icon="vaadin:archive"></iron-icon></vaadin-button>
            ${copyCodeBlock(code.iconErrorButton, hlConfig)}
        </div>
    </div>
    <h2>Sizes</h2>
    <div class="block-wrapper">
        <div class="col-wrapper">
            <vaadin-button theme="large">Large</vaadin-button>
            ${copyCodeBlock(code.largeButton, hlConfig)}
        </div>
        <div class="col-wrapper">
            <vaadin-button theme="primary">Primary, Normal</vaadin-button>
            ${copyCodeBlock(code.primaryNormalButton, hlConfig)}
        </div>
        <div class="col-wrapper">
            <vaadin-button theme="primary small">Primary, Small</vaadin-button>
            ${copyCodeBlock(code.primarySmallButton, hlConfig)}
        </div>
    </div>
`, storyParamConfig);