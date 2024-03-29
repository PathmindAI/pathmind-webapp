import { LitElement, html } from "lit-element";
import "../../components/organisms/app-footer.ts";

/**
 * `change-password-view`
 *
 * ChangePasswordView element.
 *
 * @customElement
 */
class ChangePasswordViewContent extends LitElement {
    render() {
        return html`
            <style>
                change-password-view-content .notes {
                    color: var(--lumo-error-text-color);
                    margin-left: 0;
                }
            </style>
            <vaadin-horizontal-layout class="panel-wrapper">
                <div class="content">
                    <span class="section-title-label">Change Password</span>
                    <vaadin-vertical-layout style="width: 100%;" class="inner-content">
                        <div style="width: 100%;">
                            <vaadin-password-field id="currentPassword" label="Current Password"></vaadin-password-field>
                            <vaadin-vertical-layout
                                id="currentPassNotes"
                                class="notes"
                                style="width: 100%;"
                            ></vaadin-vertical-layout>
                        </div>
                        <vaadin-password-field id="newPassword" label="New Password"></vaadin-password-field>
                        <vaadin-vertical-layout
                            id="newPassNotes"
                            class="notes"
                            style="width: 100%;"
                        ></vaadin-vertical-layout>
                        <vaadin-password-field id="confirmNewPassword" label="Confirm New Password"></vaadin-password-field>
                        <vaadin-vertical-layout id="buttonsCont">
                            <vaadin-button id="updateBtn" theme="primary">Change Password</vaadin-button>
                            <vaadin-button id="cancelBtn" theme="tertiary">Cancel</vaadin-button>
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

customElements.define("change-password-view-content", ChangePasswordViewContent);
