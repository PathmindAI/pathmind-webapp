import { LitElement, html } from "lit-element";
import "../../components/organisms/app-footer.ts";

class AccountEditViewContent extends LitElement {
    render() {
        return html`
            <vaadin-horizontal-layout class="panel-wrapper">
                <div class="content">
                    <span class="section-title-label">Edit Account</span>
                    <vaadin-vertical-layout style="width: 100%;" class="inner-content">
                        <vaadin-vertical-layout style="width: 100%; height: 100%;">
                            <vaadin-text-field id="email" label="Email"></vaadin-text-field>
                            <vaadin-text-field id="firstName" label="First Name"></vaadin-text-field>
                            <vaadin-text-field id="lastName" label="Last Name"></vaadin-text-field>
                            <vaadin-vertical-layout id="buttonsCont">
                                <vaadin-button id="updateBtn" theme="primary">
                                    Update
                                </vaadin-button>
                                <vaadin-button id="cancelBtn" theme="tertiary">Cancel</vaadin-button>
                            </vaadin-vertical-layout>
                        </vaadin-vertical-layout>
                    </vaadin-vertical-layout>
                </div>
            </vaadin-horizontal-layout>
            <app-footer></app-footer>`;
    }

    createRenderRoot() {
      return this;
    }
}

customElements.define("account-edit-view-content", AccountEditViewContent);
