import { LitElement, html, css, property } from "lit-element";
import "./icon-stopped.ts";
import "./loading-spinner.ts";

class StatusIcon extends LitElement {

    @property({type: String, reflect: true})
    status = "";

    @property({type: String, reflect: true})
    statusText = "";

    attributeChangedCallback(name, oldval, newval) {
        super.attributeChangedCallback(name, oldval, newval);
        if (name === "statustext") {
            this._isStatusTextChanged(newval);
        }
    }

    _isEqualTo(status, statusName) {
        return status === statusName;
    }

    _isStatusTextChanged(newValue) {
        this.setAttribute("title", newValue);
    }

    static get styles() {
        return css`
            iron-icon[icon="vaadin:pencil"] {
                --iron-icon-width: 1rem;
                --iron-icon-height: 1rem;
                --iron-icon-fill-color: var(--lumo-contrast-90pct);
                margin: 0.125rem;
            }
            iron-icon[icon="vaadin:check-circle"] {
                --iron-icon-width: var(--lumo-font-size-l);
                --iron-icon-height: var(--lumo-font-size-l);
                color: var(--pm-green-color);
            }
            iron-icon[icon="vaadin:exclamation-circle-o"] {
                --iron-icon-width: var(--lumo-font-size-l);
                --iron-icon-height: var(--lumo-font-size-l);
                color: var(--pm-danger-color);
            }`;
    }

    render() {
        return html`
            ${
                this._isEqualTo(this.status, 'pencil') ?
                    html`<iron-icon icon="vaadin:pencil"></iron-icon>` : null
            }
            ${
                this._isEqualTo(this.status, 'loading') ?
                    html`<loading-spinner></loading-spinner>` : null
            }
            ${
                this._isEqualTo(this.status, 'check') ?
                    html`<iron-icon icon="vaadin:check-circle"></iron-icon>` : null
            }
            ${
                this._isEqualTo(this.status, 'stopped') ?
                    html`<icon-stopped></icon-stopped>` : null
            }
            ${
                this._isEqualTo(this.status, 'exclamation') ?
                    html`<iron-icon icon="vaadin:exclamation-circle-o"></iron-icon>` : null
            }
        `;
    }
}

customElements.define("status-icon", StatusIcon);