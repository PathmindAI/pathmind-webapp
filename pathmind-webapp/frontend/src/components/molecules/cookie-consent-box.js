import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "cookieconsent";

class CookieConsentBox extends PolymerElement {
    static get is() {
        return "cookie-consent-box";
    }

    static get template() {
        return html`
            <style>
                :host {
                    display: none;
                }
            </style>
        `;
    }

    ready() {
        super.ready();
        window.cookieconsent.initialise({
            container: document.querySelector("vaadin-app-layout"),
            content: {
              message: "This website uses cookies to ensure you get the best experience.",
              dismiss: "Got it!",
              link: "Learn more",
              href: "https://pathmind.com/privacy"
            },
            position: "bottom-left",
        });
    }
}

customElements.define(CookieConsentBox.is, CookieConsentBox);