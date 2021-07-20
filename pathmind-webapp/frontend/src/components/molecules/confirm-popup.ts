import { LitElement, html, css, property } from "lit-element";

class ConfirmPopup extends LitElement {
    @property({type: Boolean, reflect: true})
    opened = false;

    @property({type: String})
    headerText = "";

    @property({type: String})
    message = "";

    @property({type: String})
    cancelText = "";

    @property({type: String})
    confirmText = "";

    @property({type: String})
    confirmButtonThemes = "primary";

    @property({type: Function})
    documentKeypressListener;

    static get styles() {
        return css`
            :host {
                display: none;
            }
            :host([opened]) {
                display: flex;
                justify-content: center;
                align-items: center;
                position: fixed;
                width: 100%;
                height: 100%;
                top: 0;
                left: 0;
                z-index: 10;
            }
            #overlay {
                position: absolute;
                width: 100%;
                height: 100%;
                top: 0;
                left: 0;
                background-color: rgba(0,0,0,0.08);
            }
            popup-content {
                box-sizing: border-box;
                position: relative;
                max-width: 30rem;
                min-width: 25rem;
                line-height: 1.6em;
                background-color: white;
                padding: var(--lumo-space-l);
                border-radius: var(--lumo-border-radius-l);
                box-shadow: 0 3px 5px var(--lumo-shade-10pct);
            }
            h3 {
                margin: 0;
            }
            .message {
                margin: var(--lumo-space-l) 0;
            }
            .buttons-wrapper {
                display: flex;
                justify-content: space-between;
                align-items: center;
            }
            vaadin-button {
                min-width: 46%;
                margin: 0;
            }
            vaadin-button[hidden] ~ vaadin-button {
                width: 100%;
            }
        `;
    }

    render() {
        return html`
            <div id="overlay" @click="${this.closePopup}"></div>
            <popup-content>
                <h3>${this.headerText}</h3>
                <div class="message">
                    <slot></slot>
                    ${this.message}
                </div>
                <div class="buttons-wrapper">
                    <vaadin-button id="cancel" tabindex="0" role="button" @click="${this.onCancel}" ?hidden="${!this.cancelText}">${this.cancelText}</vaadin-button>
                    <vaadin-button id="confirm" theme="${this.confirmButtonThemes}" tabindex="0" role="button" @click="${this.onConfirm}">${this.confirmText}</vaadin-button>
                </div>
            </popup-content>
        `;
    }

    firstUpdated() {
        const confirmButton : HTMLElement = this.shadowRoot.querySelector("#confirm");
        confirmButton.focus();
        this.documentKeypressListener = event => {
            if (event.key.toLowerCase() === "enter") {
                event.preventDefault();
                confirmButton.click();
            }
        };
        
        document.addEventListener("keypress", this.documentKeypressListener);
    }

    closePopup() {
        this.removeKeypressListener();
        (this as any).$server.closePopup();
    }

    onConfirm() {
        this.removeKeypressListener();
        (this as any).$server.onConfirm();
    }

    onCancel() {
        this.removeKeypressListener();
        (this as any).$server.onCancel();
    }

    removeKeypressListener() {
        document.removeEventListener("keypress", this.documentKeypressListener);
    }
}

customElements.define("confirm-popup", ConfirmPopup);