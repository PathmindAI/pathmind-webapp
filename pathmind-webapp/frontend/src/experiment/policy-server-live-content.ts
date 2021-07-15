import { LitElement, html, property } from "lit-element";

class PolicyServerLiveContent extends LitElement {
    @property({type: String})
    url = "";

    @property({type: String})
    userApiKey = "";

    @property({type: Boolean})
    ready = false;

    render() {
        return html`
            <h3>
                ${
                    this.ready ?
                        "The Policy is Live"
                        : "Finishing up deployment...."
                }
            </h3>
            <span>
                ${
                    this.ready ?
                        "The policy is being served at this URL:"
                        : "When the deployment is done, the policy will be served at:"
                }
            </span>
            <copy-field text="${this.url}" check-url-ready="true"></copy-field>
            ${
                this.ready && this.userApiKey ? html`
                    <span>Your access token:</span>
                    <copy-field text="${this.userApiKey}"></copy-field>
                ` : null
            }
            ${
                this.ready ? html`<p>
                    <span>Read the docs for more details:</span><br>
                    <a href="${this.url}/redoc" target="_blank">${this.url}/redoc</a>
                </p>` : null
            }
        `;
    }

    createRenderRoot() {
      return this;
    }

    firstUpdated() {
        (this.querySelector("copy-field") as any).addEventListener("url-ready", async event => {
            console.log("%cHEREEEEEE", "background-color: yellow")
            this.ready = true;
        });
    }
}

customElements.define("policy-server-live-content", PolicyServerLiveContent);