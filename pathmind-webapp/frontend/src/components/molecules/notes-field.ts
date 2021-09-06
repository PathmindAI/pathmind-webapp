import { LitElement, html, css, property } from "lit-element";
import { registerStyles } from "@vaadin/vaadin-themable-mixin/register-styles.js";
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
class NotesField extends LitElement {

    @property({type: Number})
    wordcount = 0;

    @property({type: Number})
    max = 1000;

    @property({type: String})
    title = "";

    @property({type: String})
    placeholder = "Add Notes";

    @property({type: String})
    notes = "";

    @property({type: Boolean})
    unsaved = false;

    @property({type: Boolean})
    readonly = false;

    @property({type: Boolean, reflect: true})
    compact = false;

    @property({type: Boolean, reflect: true, attribute: "secondary-style"})
    secondaryStyle = false;

    @property({type: Boolean, reflect: true})
    hidesavebutton = false;

    @property({type: Boolean, reflect: true})
    allowautosave = true;

    @property({type: Boolean, reflect: true})
    warning = false;

    static get styles() {
        return css`
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
            :host([secondary-style]) .header {
                box-sizing: border-box;
                background: var(--pm-app-bg-color);
                padding: var(--lumo-space-xxxs) var(--lumo-space-xs);
                border-left: 1px solid var(--pm-grey-color-lightest);
                margin-bottom: 0;
            }
            .header span:first-child {
                font-weight: bold;
                color: var(--pm-text-color);
                margin-left: 0;
            }
            :host([secondary-style]) .header span:first-child {
                font-size: var(--lumo-font-size-s);
                font-weight: normal;
                color: var(--lumo-secondary-text-color);
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
            :host([secondary-style]) vaadin-text-area {
                border: 1px solid var(--pm-grey-color-lightest);
                border-right: none;
                border-bottom: none;
                border-radius: 0;
            }
        `;
    }

    render() {
        return html`
            <div class="header">
                <span class="title">${this.title}</span>
                <span class="hint-label" ?hidden="${!this.unsaved}">Unsaved Notes!</span>
                <iron-icon icon="vaadin:check" id="saveIcon"></iron-icon>
                <span class="wordcount-label" ?warning="${this.warning}">${this.wordcount}/${this.max}</span>
                <vaadin-button id="save" @click="${this.onSave}" ?hidden="${this.hidesavebutton}">Save</vaadin-button>
            </div>
            <vaadin-text-area
                id="textarea"
                theme="notes-field"
                placeholder="${this.placeholder}"
                ?readonly="${this.readonly}"
                compact="${this.compact}"
                @keyup="${event => (this as any).$server.onNotesChange(event.target.value)}"></vaadin-text-area>
        `;
    }

    _allowAutoSaveChanged(newValue) {
        const textarea = this.shadowRoot.getElementById("textarea");
        textarea.removeEventListener("blur", this.autoSave.bind(this));
        if (newValue) {
            textarea.addEventListener("blur", this.autoSave.bind(this));
        }
    }

    firstUpdated() {
        const textarea: HTMLTextAreaElement = this.shadowRoot.getElementById("textarea") as HTMLTextAreaElement;
        textarea.addEventListener("keyup", debounce(event => {
            this.calculateWordCount(textarea.value);
            if (textarea.value !== this.notes && !this.unsaved) {
                this.unsaved = true;
            }
        }, 300));
        this._allowAutoSaveChanged(this.allowautosave);
        this._notesChanged(this.notes);
        textarea.value = this.notes;
    }

    updated(changedProperties) {
        changedProperties.forEach((oldValue, name) => {
            if (name === "allowautosave") {
                this._allowAutoSaveChanged(this.allowautosave);
            }
            if (name === "wordcount") {
                this._isOverMaxChar(this.wordcount);
            }
        })
    }

    autoSave(event) {
        this.shadowRoot.getElementById("save").click();
    }

    canSave(updatedNotesText) {
        return this.notes !== updatedNotesText && updatedNotesText.length <= this.max;
    }

    onSave(event) {
        const textarea: HTMLTextAreaElement = this.shadowRoot.getElementById("textarea") as HTMLTextAreaElement;
        const saveIcon = this.shadowRoot.getElementById("saveIcon");
        if (this.canSave(textarea.value)) {
            this.notes = textarea.value ? textarea.value.slice(0) : "";
            this.unsaved = false;
            saveIcon.classList.add('fade-in');
            setTimeout(() => {
                saveIcon.classList.remove('fade-in');
            }, 1500);
            (this as any).$server.onSave(this.notes);
        }
    }

    calculateWordCount(notes) {
        this.wordcount = notes ? notes.length : 0;
    }

    _notesChanged(newValue) {
        this.notes = newValue;
        if (this.shadowRoot.getElementById("textarea")) {
            (this.shadowRoot.getElementById("textarea") as HTMLTextAreaElement).value = newValue ? newValue.slice(0) : "";
        }
        this.calculateWordCount(newValue);
    }

    _isOverMaxChar(wordcount) {
        this.warning = wordcount > this.max;
    }
}

customElements.define("notes-field", NotesField);