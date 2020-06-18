import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-tabs/src/vaadin-tabs.js";
import "@vaadin/vaadin-tabs/src/vaadin-tab.js";
import "@vaadin/vaadin-ordered-layout/src/vaadin-horizontal-layout.js";
import "@vaadin/vaadin-combo-box/src/vaadin-combo-box.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";
import "@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js";

class SettingsViewContent extends PolymerElement {
    static get template() {
        return html`
            <style include="shared-styles pathmind-dialog-view">
                .small {
                    font-size: small;
                }
                .small:empty {
                    display: none;
                }
            </style>
            <div class="content">
                <vaadin-vertical-layout style="width: 100%;" class="inner-content">
                        <vaadin-vertical-layout style="width: 100%; height: 100%;">
                            <vaadin-combo-box id="ec2InstanceTypeCB" style="width: 100%;"> </vaadin-combo-box>
                            <vaadin-combo-box id="nativerlVersionCB" style="width: 100%;"> </vaadin-combo-box>
                            <vaadin-vertical-layout id="buttonsCont">
                            	<vaadin-button id="saveBtn" theme="primary">
                                    Save
                            	</vaadin-button>
                            </vaadin-vertical-layout>
                        </vaadin-vertical-layout>
                </vaadin-vertical-layout>
            </div>
        `;
    }

    static get is() {
        return "settings-view-content";
    }

    static get properties() {
        return {
            // Declare your properties here.
        };
    }
}

customElements.define(SettingsViewContent.is, SettingsViewContent);
