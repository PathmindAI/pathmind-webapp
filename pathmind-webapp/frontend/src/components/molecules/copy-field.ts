import { LitElement, html, css, property } from "lit-element";
import "../atoms/loading-spinner.ts";

class CopyField extends LitElement {
    @property({type: String})
    text = "";
    @property({type: Boolean, attribute: "check-url-ready"})
    checkUrlReady = false;

    static get styles() {
        return css`
            :host {
                box-sizing: border-box;
                display: flex;
                align-items: center;
                max-width: 400px;
                width: 100%;
                border: 1px solid var(--pm-grey-color);
            }
            #textToCopy {
                -webkit-user-select: all;
                user-select: all;
                flex: 1 0 auto;
                font-size: var(--lumo-font-size-s);
                padding: var(--lumo-space-xxxs) var(--lumo-space-xxs);
            }
            vaadin-button {
                padding: var(--lumo-space-xxxs) var(--lumo-space-xxs);
                margin: 0;
                border-radius: 0;
                border: none;
            }
            #copy {
                position: relative;
                width: calc(1rem + var(--lumo-space-xxs) * 2);
            }
            #copy span {
                display: flex;
                justify-content: center;
                align-items: center;
                position: absolute;
                width: 100%;
                top: 50%;
                left: 0;
                opacity: 0;
                transition: opacity 0.3s;
                transform: translateY(-50%);
            }
            #copy span iron-icon {
                width: 1rem;
                height: 1rem;
            }
            #copy span[active] {
                opacity: 1;
            }
            loading-spinner {
                margin: var(--lumo-space-xxs);
            }
        `;
    }

    render() {
        return html`
            <style include="shared-styles"></style>
            <div id="textToCopy" autoselect>${this.text}</div>
            <vaadin-button id="copy" theme="small" @click="${this.onCopy}">
                <span active><iron-icon icon="vaadin:copy-o"></iron-icon></span>
                <span><iron-icon icon="vaadin:check"></iron-icon></span>
            </vaadin-button>
            <loading-spinner id="spinner"></loading-spinner>
        `;
    }

    firstUpdated() {
        const textToCopy = this.shadowRoot.getElementById("textToCopy");

        this.checkUrl = this.checkUrl.bind(this);

        if (this.checkUrlReady) {
            this.showSpinner();
            this.checkUrl();
        } else {
            this.shadowRoot.getElementById("spinner").setAttribute("hidden", "true");
        }

        textToCopy.addEventListener("click", event => {
            this.onCopy();
        });

        textToCopy.addEventListener("copy", event => {
            const selection = this.text;
            if (event.clipboardData) {
                event.clipboardData.setData("text/plain", selection);
            } else {
                (window as any).clipboardData.setData("text", selection);
            }
            event.preventDefault();
        });
    }

    showSpinner() {
        this.shadowRoot.getElementById("spinner").removeAttribute("hidden");
        this.shadowRoot.getElementById("copy").setAttribute("hidden", "true");
    }

    checkUrl() {
        fetch(this.text)
            .then(response => {
                if (response.status != 200) {
                    this.showSpinner();
                    requestAnimationFrame(this.checkUrl);
                } else {
                    this.dispatchEvent(new CustomEvent("url-ready", { composed: true }));
                    this.shadowRoot.getElementById("spinner").setAttribute("hidden", "true");
                    this.shadowRoot.getElementById("copy").removeAttribute("hidden");
                }
            }).catch(error => {
                console.error("Error: ", error);
            });
    }

    onCopy() {
        const copyButton = this.shadowRoot.getElementById("copy");
        const copyIcon = copyButton.querySelector("span:first-child");
        const checkmarkIcon = copyButton.querySelector("span:last-child");
        const range = document.createRange();
        range.selectNode(this.shadowRoot.getElementById("textToCopy"));
        const select = navigator.userAgent.toLowerCase().indexOf('firefox') > -1 
                ? window.getSelection() : (this.shadowRoot as any).getSelection();
        select.removeAllRanges();
        select.addRange(range);
        document.execCommand("copy");
        select.removeAllRanges();

        copyIcon.removeAttribute("active");
        checkmarkIcon.setAttribute("active", "true");
        setTimeout(function() {
            copyIcon.setAttribute("active", "true");
            checkmarkIcon.removeAttribute("active");
        }, 1200);
    }
}

customElements.define("copy-field", CopyField);