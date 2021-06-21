import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "../atoms/loading-spinner.js";

class CopyField extends PolymerElement {
    static get is() {
        return "copy-field";
    }

    static get properties() {
        return {
            text: {
                type: String,
            },
            checkUrlReady: {
                type: Boolean,
            },
        }
    }

    static get template() {
        return html`
            <style include="shared-styles">
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
            </style>
            <div id="textToCopy" autoselect>[[text]]</div>
            <vaadin-button id="copy" theme="small" on-click="onCopy">
                <span active><iron-icon icon="vaadin:copy-o"></iron-icon></span>
                <span><iron-icon icon="vaadin:check"></iron-icon></span>
            </vaadin-button>
            <loading-spinner id="spinner"></loading-spinner>
        `;
    }

    ready() {
        super.ready();

        window.checkUrl = this.checkUrl;

        if (this.checkUrlReady) {
            this.showSpinner();
            this.checkUrl(this);
        } else {
            this.$.spinner.setAttribute("hidden", true);
        }

        this.$.textToCopy.addEventListener("click", event => {
            this.onCopy();
        });

        this.$.textToCopy.addEventListener("copy", event => {
            const selection = this.text;
            if (event.clipboardData) {
                event.clipboardData.setData("text/plain", selection);
            } else {
                window.clipboardData.setData("text", selection);
            }
            event.preventDefault();
        });
    }

    showSpinner() {
        this.$.spinner.removeAttribute("hidden");
        this.$.copy.setAttribute("hidden", true);
    }

    checkUrl(shadow) {
        fetch(shadow.text)
            .then((response, shadow) => {
                console.log("response ",response)
                if (response.status != 200) {
                    shadow.showSpinner();
                    requestAnimationFrame(function(shadow) {
                        checkUrl(shadow);
                    });
                } else {
                    shadow.$.spinner.setAttribute("hidden", true);
                    shadow.$.copy.setAttribute("hidden", false);
                }
            }).catch(error => {
                console.error("Error: ", error);
            });
    }

    onCopy() {
        const copyButton = this.$.copy;
        const copyIcon = copyButton.querySelector("span:first-child");
        const checkmarkIcon = copyButton.querySelector("span:last-child");
        const range = document.createRange();
        range.selectNode(this.$.textToCopy);
        const select = this.shadowRoot.getSelection();
        select.removeAllRanges();
        select.addRange(range);
        document.execCommand("copy");
        select.removeAllRanges();

        copyIcon.removeAttribute("active");
        checkmarkIcon.setAttribute("active", true);
        setTimeout(function() {
            copyIcon.setAttribute("active", true);
            checkmarkIcon.removeAttribute("active");
        }, 1200);
    }
}

customElements.define(CopyField.is, CopyField);