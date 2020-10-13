import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class ModelsNavbarItem extends PolymerElement {
    static get is() {
        return "models-navbar-item";
    }

    static get properties() {
        return {
            modelName: {
                type: String,
            },
            modelPackageName: {
                type: String,
            },
            modelPackageNameInBrackets: {
                type: String,
                computed: 'modelPackageNameText(modelPackageName)',
            },
            modelLink: {
                type: String,
            },
            createdDate: {
                type: String,
            },
            isCurrent: {
                type: Boolean,
                value: false,
                notify: true,
                reflectToAttribute: true,
            },
            isDraft: {
                type: Boolean,
            },
            isArchived: {
                type: Boolean,
                reflectToAttribute: true,
            },
            tagDraftText: {
                type: String,
                computed: 'tagLabelDraftText(isDraft)',
            },
        }
    }

    static get template() {
        return html`
        <style>
            :host {
                box-sizing: border-box;
                display: flex;
                align-items: center;
                flex-shrink: 0;
                width: 100%;
                padding: 0.45rem var(--lumo-space-s);
                border: 1px solid transparent;
                border-right: none;
                margin-top: -1px;
                cursor: pointer;
            }
            :host([is-current]),
            :host(:hover) {
                background-color: white;
                border-color: var(--pm-grey-color-lighter);
            }
            .model-name {
                font-size: var(--lumo-font-size-s);
                line-height: 1em;
            }
            .model-name div {
                margin-bottom: var(--lumo-space-xxxs);
            }
            a, p {
                margin: 0;
            }
            a {
                color: var(--lumo-body-text-color);
                text-decoration: none;
            }
            a:hover {
                opacity: 0.9;
            }
            .model-name p {
                font-size: var(--lumo-font-size-xs);
                font-family: var(--lumo-font-family-header); /* This font should usually be used on a header. This is an exception. */
                color: var(--pm-grey-color-dark);
            }
            vaadin-button {
                margin-left: auto;
            }
            vaadin-button[title="Archive"].action-button iron-icon,
            vaadin-button[title="Unarchive"].action-button iron-icon {
                width: var(--lumo-font-size-xs);
                height: var(--lumo-font-size-xs);
                padding: 0;
            }
            tag-label {
                font-size: var(--lumo-font-size-xs);
                line-height: 1;
                margin-left: 0;
            }
            tag-label[hidden="true"] {
                display: none;
            }
        </style>
        <div class="model-name">
            <div>
                <tag-label text="[[tagDraftText]]" size="small" outline="true"></tag-label>
            </div>
            <a router-link href=[[modelLink]]>Model #[[modelName]] [[modelPackageNameInBrackets]]</a>
            <p>Created [[createdDate]]</p>
        </div>
        <vaadin-button
            class="action-button"
            theme="tertiary-inline icon"
            title="Archive"
            on-click="onArchiveButtonClicked"
            hidden="[[isArchived]]"
        >
            <iron-icon icon="vaadin:archive" slot="prefix"></iron-icon>
        </vaadin-button>
        <vaadin-button
            class="action-button"
            theme="tertiary-inline icon"
            title="Unarchive"
            on-click="onUnarchiveButtonClicked"
            hidden="[[!isArchived]]"
        >
            <iron-icon icon="vaadin:arrow-backward" slot="prefix"></iron-icon>
        </vaadin-button>`;
    }

    ready() {
        super.ready();
        this.addEventListener("click", this.handleItemClicked);
    }

    tagLabelDraftText(isDraft) {
        return isDraft ? "Draft" : "";
    }

    modelPackageNameText(modelPackageName) {
        return modelPackageName ? `(${modelPackageName})` : "";
    }

    handleItemClicked() {
        history.pushState(window.location.href, `Model #${this.modelName}`, this.modelLink);
    }
}

customElements.define(ModelsNavbarItem.is, ModelsNavbarItem);