import { LitElement, html, css, property } from "lit-element";

class UpgradeCtaOverlay extends LitElement {
    @property({type: Boolean, reflect: true})
    opened = false;
    @property({type: String})
    featureName = "";

    static get styles() {
        return css`
            :host {
                -webkit-user-select: none;
                -moz-user-select: none;
                -ms-user-select: none;
                user-select: none;
                display: flex;
                justify-content: center;
                align-items: center;
                position: absolute;
                width: 100%;
                height: 100%;
                top: 0;
                left: 0;
                background-color: rgba(0,0,0,0.12);
                z-index: 1;
                opacity: 0;
            }
            :host([opened]) {
                opacity: 1;
            }
            .cta-overlay {
                -webkit-user-select: none;
                -moz-user-select: none;
                -ms-user-select: none;
                user-select: none;
                display: flex;
                justify-content: center;
                align-items: center;
                position: absolute;
                width: 100%;
                height: 100%;
                top: 0;
                left: 0;
                background-color: rgba(0,0,0,0.12);
                z-index: 1;
            }
            .cta-overlay-content {
                display: flex;
                justify-content: center;
                align-items: center;
                max-width: 90%;
                background-color: white;
                line-height: 1.625;
                padding: var(--lumo-space-m);
                text-align: justify;
                border-radius: var(--lumo-border-radius-l);
                box-shadow: 0 3px 5px var(--lumo-shade-10pct);
            }
        `;
    }

    render() {
        return html`
            <vaadin-vertical-layout class="cta-overlay" ?hidden="${!this.opened}">
                <vaadin-vertical-layout class="cta-overlay-content">
                    <span>${this.featureName} are available for Professional Plan subscribers.</span>
                    <vaadin-button id="upgradeBtn" theme="primary small">Upgrade Now</vaadin-button>
                </vaadin-vertical-layout>
            </vaadin-vertical-layout>
        `;
    }

    constructor() {
        super();
        this.addEventListener("click", event => {
            this.opened = !this.opened;
        });
    }

    firstUpdated() {
        this.shadowRoot.getElementById("upgradeBtn").addEventListener("click", event => {
            event.preventDefault();
            event.stopPropagation();
        });
    }

}

customElements.define("upgrade-cta-overlay", UpgradeCtaOverlay);