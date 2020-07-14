// Vaadin Lumo Styles
import '@vaadin/vaadin-lumo-styles/color.js';
import '@vaadin/vaadin-lumo-styles/typography.js';
import '@vaadin/vaadin-lumo-styles/sizing.js';
import '@vaadin/vaadin-lumo-styles/spacing.js';
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/typography.js';
import '@vaadin/vaadin-lumo-styles/icons.js';
import '@vaadin/vaadin-icons/';

// Styles for Stories on Storybook
import './utils.css';

// Pathmind Styles
import '../frontend/styles/styles.css';

// For injecting Pathmind custom styles for Vaadin web components on Storybook
import { registerStyles, unsafeCSS } from "@vaadin/vaadin-themable-mixin/register-styles.js";

// For Java syntax highlighting
import hljs from "highlight.js/lib/highlight";
import hlJava from "highlight.js/lib/languages/java";

// For showing Java code
import copyCodeBlock from "@pickra/copy-code-block";

const hlConfig = {lang: "java",
  colors: {
    textColor: 'var(--lumo-body-text-color)',
    function: '#266',
    title: '#a42',
    comment: 'var(--pm-gray-color-darker)',
    built_in: '#a26',
    keyword: 'var(--pm-orange-color)',
    string: 'var(--lumo-primary-color)',
    meta: '#555',
    metaString: '#d2d'
  }
};

const storyParamConfig = {
    backgrounds: [
        { name: 'White', value: '#ffffff', default: true },
        { name: '--pm-app-bg-color', value: '#f0f1f6' },
        { name: '--pm-primary-color', value: '#4f97aa' },
    ]
};

export {
    registerStyles,
    unsafeCSS,
    hljs,
    hlJava,
    hlConfig,
    copyCodeBlock,
    storyParamConfig,
}