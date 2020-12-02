import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import { registerStyles, css } from "@vaadin/vaadin-themable-mixin/register-styles.js";
import { debounce } from 'lodash';
import "@vaadin/vaadin-text-field/src/vaadin-text-area.js";

registerStyles("vaadin-text-area", css`
    :host([theme~="notes-field"]) {
        box-sizing: border-box;
        width: 100%;
        line-height: 1.5em;
        background-color: white;
        padding: 0;
        border-radius: var(--lumo-border-radius);
        border: 1px solid var(--pm-grey-color);
        overflow: hidden;
    }
    :host([theme~="notes-field"][readonly]) {
        border: 1px solid var(--lumo-contrast-20pct);
    }
    :host([theme~="notes-field"][readonly]) [part="input-field"] {
        background-color: var(--lumo-contrast-5pct);
        border: none;
    }
    :host([theme~="notes-field"]) [part="input-field"],
    :host([theme~="notes-field"]) [part="input-field"] ::slotted(textarea) {
        line-height: 1.5em;
        height: 12em;
        background-color: white;
    }
    :host([theme~="notes-field"]) [part="input-field"] {
        padding: 0;
        overflow: hidden;
    }
    :host([theme~="notes-field"]:hover:not([readonly]):not([focused])) [part="input-field"] {
        background-color: rgba(0, 0, 0, 0.025);
    }
    :host([theme~="notes-field"]) [part="input-field"] [part="value"],
    :host([theme~="notes-field"]) [part="input-field"] ::slotted(textarea) {
        box-sizing: border-box;
        height: 100% !important;
        padding: 0.5rem var(--lumo-space-s);
        overflow: auto;
    }
    :host([theme~="notes-field"][compact]) [part="input-field"],
    :host([theme~="notes-field"][compact]) [part="input-field"] ::slotted(textarea) {
        height: auto;
    }
`);
class NotesField extends PolymerElement {
    static get is() {
        return "notes-field";
    }

    static get properties() {
        return {
            title: {
                type: String,
            },
            placeholder: {
                type: String,
                value: "Add Notes",
            },
            notes: {
                type: String,
                observer: "_notesChanged",
            },
            _notesChangeObserverLock: {
                type: Boolean,
                value: false,
            },
            _notesChangedObserverTriggered: {
                type: Boolean,
                value: false,
            },
            allowautosave: {
                type: Boolean,
                value: true,
                observer: "_allowAutoSaveChanged",
            },
            hidesavebutton: {
                type: Boolean,
                value: false,
                reflectToAttribute: true,
            },
            unsaved: {
                type: Boolean,
                value: false,
            },
            warning: {
                type: Boolean,
                value: false,
                computed: "_isOverMaxChar(wordcount)",
            },
            max: {
                type: Number,
                value: 1000,
            },
            wordcount: {
                type: Number,
                value: 0,
            },
            readonly: {
                type: Boolean,
                value: false,
            },
            compact: {
                type: Boolean,
                value: false,
                reflectToAttribute: true,
            },
        }
    }

    static get template() {
        return html`
            <style include="shared-styles">
                :host {
                    display: flex;
                    flex-direction: column;
                    flex: 1;
                    width: 100%;
                }
                :host([compact]) {
                    position: relative;
                    line-height: 1;
                }
                .fade-in {
                    display: block !important;
                    opacity: 1;
                }
                .header {
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    width: 100%;
                    margin-bottom: var(--lumo-space-xs);
                }
                :host([compact]) .header {
                    position: absolute;
                    width: auto;
                    right: 0;
                    bottom: 0;
                    background-color: rgba(255,255,255,0.85);
                    padding: var(--lumo-space-xs);
                    margin-bottom: 0;
                    z-index: 1;
                }
                .header span:first-child {
                    font-weight: bold;
                    color: var(--pm-text-color);
                    margin-left: 0;
                }
                .title {
                    flex: 1 1 0%;
                }
                :host([compact]) .title {
                    display: none;
                }
                iron-icon {
                    display: none;
                    width: var(--lumo-font-size-m);
                    height: var(--lumo-font-size-m);
                    color: var(--pm-friendly-color);
                    margin-right: var(--lumo-space-m);
                    opacity: 0;
                    transition: opacity 0.5s;
                }
                .hint-label {
                    align-self: center;
                    margin-left: auto;
                    margin-right: var(--lumo-space-m);
                }
                .wordcount-label {
                    align-self: center;
                    font-size: var(--lumo-font-size-s);
                    color: var(--lumo-secondary-text-color);
                }
                .wordcount-label[warning] {
                    color: var(--pm-danger-color);
                }
                .hint-label,
                .fade-out-hint-label {
                    font-size: var(--lumo-font-size-s);
                    color: var(--pm-primary-color);
                }
                .fade-out-hint-label {
                    opacity: 0;
                    transition: opacity 0.5s;
                }
                :host([hidesavebutton]) .hint-label {
                    display: none;
                }
                vaadin-button {
                    min-width: 45px;
                    height: 1.4rem;
                    line-height: calc(1.4rem - 2px);
                    font-size: var(--lumo-font-size-s);
                    font-weight: normal;
                    padding: 0 var(--lumo-space-xs);
                    margin: 0;
                    margin-left: var(--lumo-space-s);
                }
                vaadin-text-area {
                    width: 100%;
                    height: 100%;
                }
                :host([compact]) vaadin-text-area {
                    border: none;
                    border-left: 1px solid var(--pm-grey-color-lightest);
                    border-radius: 0;
                }
            </style>
            <div class="header">
                <span class="title">[[title]]</span>
                <span class="hint-label" hidden="[[!unsaved]]">Unsaved Notes!</span>
                <iron-icon icon="vaadin:check" id="saveIcon"></iron-icon>
                <span class="wordcount-label" warning$="[[warning]]">[[wordcount]]/[[max]]</span>
                <vaadin-button id="save" on-click="onSave" disabled=[[readonly]] hidden$=[[hidesavebutton]]>Save</vaadin-button>
            </div>
            <vaadin-text-area
                id="textarea"
                theme="notes-field"
                placeholder="[[placeholder]]"
                readonly$=[[readonly]]
                compact$=[[compact]]
                on-keyup="onNotesChange"></vaadin-text-area>
        `;
    }

    _allowAutoSaveChanged(newValue, oldValue) {
        this.$.textarea.removeEventListener("blur", this.autoSave.bind(this));
        if (newValue) {
            this.$.textarea.addEventListener("blur", this.autoSave.bind(this));
        }
    }

    ready() {
        super.ready();
        this.$.textarea.value = this.notes;
        this.$.textarea.addEventListener("keyup", debounce(event => {
            this.calculateWordCount(this.$.textarea.value);
            if (this.$.textarea.value !== this.notes && !this.unsaved) {
                this.unsaved = true;
            }
        }, 300));
        this._allowAutoSaveChanged(this.allowautosave);
    }

    autoSave(event) {
        this.$.save.click();
    }

    canSave(updatedNotesText) {
        return this.notes !== updatedNotesText && updatedNotesText.length <= this.max;
    }

    onSave(event) {
        if (this.canSave(this.$.textarea.value)) {
            this.notes = this.$.textarea.value;
            this.unsaved = false;
            this.$.saveIcon.classList.add('fade-in');
            setTimeout(() => {
                this.$.saveIcon.classList.remove('fade-in');
            }, 1500);
        }
    }

    calculateWordCount(notes) {
        this.wordcount = notes.length;
    }

    _notesChanged(newValue, oldValue) {
        this.$.textarea.value = newValue;
        this.calculateWordCount(newValue);
    }

    _isOverMaxChar(wordcount) {
        return wordcount > this.max;
    }
}

customElements.define(NotesField.is, NotesField);