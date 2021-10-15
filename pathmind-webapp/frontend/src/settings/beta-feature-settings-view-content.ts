import { LitElement, html, property } from "lit-element";

class BetaFeatureSettingsViewContent extends LitElement {
    
    @property({type: Boolean})
    withRewardTerms = false;

    updated(changedProperties) {
      changedProperties.forEach((oldValue, name) => {
          if (name === "isPaidUser") {
          }
      });
    }

    render() {
        return html`
            <style>
                beta-feature-settings-view-content .inner-content {
                  position: relative;
                  max-width: 500px;
                  width: 100%;
                  text-align: left;
                  padding: var(--lumo-space-s) var(--lumo-space-m);
                  overflow: hidden;
                }
                beta-feature-settings-view-content h4 {
                    width: 100%;
                    margin: 0 auto var(--lumo-space-xxs);
                }
            </style>
            <vaadin-horizontal-layout class="panel-wrapper">
                <div class="content">
                      <span class="section-title-label">Beta Feature Settings</span>
                    <vaadin-vertical-layout class="inner-content">
                        <vaadin-vertical-layout>
                          <h4>Reward Terms</h4>
                          <p>Replace the reward function editor with a low-code reward terms generator. You can continue to use the code editor with this new interface, or use a mix of helper-generated reward terms and your own code snippets.</p>
                          <vaadin-button theme="small" @click="${this.toggleRewardTerms}">${this.withRewardTerms ? "Disable" : "Enable"}</vaadin-button>
                        </vaadin-vertical-layout>
                    </vaadin-vertical-layout>
                </div>
            </vaadin-horizontal-layout>
        `;
    }

    createRenderRoot() {
        return this;
    }

    toggleRewardTerms(event) {
      (this as any).$server.toggleRewardTerms();
    }

}

customElements.define("beta-feature-settings-view-content", BetaFeatureSettingsViewContent);
