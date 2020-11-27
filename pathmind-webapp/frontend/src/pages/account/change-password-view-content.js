import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "../../components/organisms/app-footer.js";

/**
 * `change-password-view`
 *
 * ChangePasswordView element.
 *
 * @customElement
 * @polymer
 */
class ChangePasswordViewContent extends PolymerElement {
    static get template() {
        return html`
            <style include="shared-styles pathmind-dialog-view">
                :host {
                    justify-content: space-between;
                }
                .panel-wrapper {
                    overflow: visible;
                    min-height: auto;
                }
                .inner-content {
                    text-align: left;
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
            <app-footer 
                contactlink="{{contactLink}}"
            ></app-footer>`;
    }

    static get is() {
        return "change-password-view-content";
    }

    static get properties() {
        return {
            // Declare your properties here.
        };
    }
}

customElements.define(ChangePasswordViewContent.is, ChangePasswordViewContent);
