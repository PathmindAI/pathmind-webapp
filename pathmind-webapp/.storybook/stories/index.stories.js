// TODO: add demo stories
import { storiesOf } from '@storybook/polymer';

import '@vaadin/vaadin-lumo-styles/color.js';
import '@vaadin/vaadin-lumo-styles/typography.js';
import '@vaadin/vaadin-lumo-styles/sizing.js';
import '@vaadin/vaadin-lumo-styles/spacing.js';
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/typography.js';
import '@vaadin/vaadin-lumo-styles/icons.js';
import '@vaadin/vaadin-icons/'

import '../utils.css';
import '../../frontend/styles/styles.css';
import pathmindVaadinButtonStyles from '!to-string-loader!css-loader!../../frontend/styles/components/vaadin-button.css';
import { registerStyles, unsafeCSS } from '@vaadin/vaadin-themable-mixin/register-styles.js';
import "@vaadin/vaadin-button/vaadin-button.js";
import "@vaadin/vaadin-button/theme/lumo/vaadin-button.js";

registerStyles("vaadin-button", unsafeCSS(pathmindVaadinButtonStyles));

storiesOf('Inputs', module)
.addDecorator(storyFn => `<div class="storybook-row-wrapper">
    ${storyFn()}
</div>`)
.add('Button', () => `
    <vaadin-button theme="primary">Primary</vaadin-button>
    <vaadin-button>Secondary / Default</vaadin-button>
    <vaadin-button theme="tertiary">Tertiary</vaadin-button>
    <vaadin-button theme="tertiary-inline">Tertiary Inline</vaadin-button>
    <vaadin-button theme="tertiary-inline" class="action-button"><iron-icon icon="vaadin:archive"></iron-icon></vaadin-button>
`);