import { LitElement, html, css, property } from "lit-element";

class ToggleButton extends LitElement {
    @property({type: Boolean, reflect: true})
    state = false;
    @property({type: String})
    trueText = "";
    @property({type: String})
    falseText = "";

    static get styles() {
        return css`
            :host {
                -webkit-user-select: none;
                -moz-user-select: none;
                -ms-user-select: none;
                user-select: none;
                display: flex;
                align-items: center;
                align-self: center;
                position: relative;
                width: 5rem;
                font-size: var(--lumo-font-size-xs);
                color: var(--pm-grey-color-darker);
                background-color: var(--pm-grey-color-lighter);
                border-radius: var(--lumo-border-radius);
                overflow: hidden;
            }
            vaadin-horizontal-layout {
              line-height: 1;
            }
            vaadin-horizontal-layout::before {
              content: "";
              position: absolute;
              width: 50%;
              height: 100%;
              top: 0;
              left: 50%;
              background-color: var(--lumo-primary-color-75pct);
              transition: 0.3s left;
            }
            :host([state]) vaadin-horizontal-layout::before {
              left: 0;
            }
            span {
              box-sizing: border-box;
              flex: 0 0 50%;
              position: relative;
              text-align: center;
              padding: var(--lumo-space-xxs) var(--lumo-space-xs);
              transition: 0.3s color;
            }
            :host([state]) span:first-child,
            :host(:not([state])) span:last-child {
              color: white;
            }
            :host([state]) span:last-child,
            :host(:not([state])) span:first-child {
              color: var(--pm-grey-color-darker);
            }
            input {
              position: absolute;
              top: 0;
              left: 0;
              width: 100%;
              height: 100%;
              padding: 0;
              margin: 0;
              opacity: 0;
              cursor: pointer;
            }
        `;
    }

    render() {
        return html`
          <vaadin-horizontal-layout>
            <span>${this.trueText}</span>
            <span>${this.falseText}</span>
          </vaadin-horizontal-layout>
          <input type="checkbox" @change="${event => (this as any).$server.toggleHandler()}">
        `;
    }
}

customElements.define("toggle-button", ToggleButton);
