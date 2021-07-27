import { LitElement, html, css } from "lit-element";
import "cookieconsent";

class CookieConsentBox extends LitElement {
    static get styles() {
        return css`
            :host {
                display: none;
            }
        `
    }

    render() {
        return html``;
    }

    constructor() {
        super();
        if (document.querySelectorAll(".cc-window[aria-label~='cookieconsent']").length === 0) {
            (window as any).cookieconsent.initialise({
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
}

customElements.define("cookie-consent-box", CookieConsentBox);