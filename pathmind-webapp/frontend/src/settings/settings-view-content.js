import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class SettingsViewContent extends PolymerElement {
    static get template() {
        return html`
            <style>
                settings-view-content span.section-title-label + .inner-content {
                    padding: var(--lumo-space-s) 0 0;
                }
                settings-view-content .inner-content {
                    max-width: 100%;
                    overflow: hidden;
                }
                settings-view-content .grid-wrapper {
                    display: grid;
                    grid-template-columns: repeat(3, auto);
                    gap: var(--lumo-space-s);
                    padding: 0 var(--lumo-space-l);
                }
                settings-view-content h4 {
                    width: 100%;
                    background-color: var(--pm-app-bg-color);
                    padding: var(--lumo-space-xs) 0;
                    margin: var(--lumo-space-m) auto var(--lumo-space-xxs);
                }
                settings-view-content #buttonsCont {
                    margin-top: var(--lumo-space-l);
                }
                settings-view-content #buttonsCont vaadin-button {
                    margin: 0 auto;
                    border-radius: 0;
                }
            </style>
            <vaadin-horizontal-layout class="panel-wrapper">
                <div class="content">
                    <span class="section-title-label">Settings</span>
                    <vaadin-vertical-layout style="width: 100%;" class="inner-content">
                        <vaadin-vertical-layout class="grid-wrapper">
                            <vaadin-combo-box id="hiddenNodeCB" style="width: 100%;"> </vaadin-combo-box>
                            <vaadin-combo-box id="hiddenLayerCB" style="width: 100%;"> </vaadin-combo-box>
                            <vaadin-combo-box id="longerTrainingCB" style="width: 100%;"> </vaadin-combo-box>
                        </vaadin-vertical-layout>
                        <h4>Internal Users Only</h4>
                        <vaadin-vertical-layout class="grid-wrapper">
                            <vaadin-combo-box id="userLogCB" style="width: 100%;"> </vaadin-combo-box>
                            <vaadin-combo-box id="ec2InstanceTypeCB" style="width: 100%;"> </vaadin-combo-box>
                            <vaadin-combo-box id="condaVersionCB" style="width: 100%;"> </vaadin-combo-box>
                            <vaadin-combo-box id="anylogicVersionCB" style="width: 100%;"> </vaadin-combo-box>
                            <vaadin-combo-box id="nativerlVersionCB" style="width: 100%;"> </vaadin-combo-box>
                            <vaadin-combo-box id="helperVersionCB" style="width: 100%;"> </vaadin-combo-box>
                            <vaadin-combo-box id="numSampleCB" style="width: 100%;"> </vaadin-combo-box>
                            <vaadin-combo-box id="maxMemoryCB" style="width: 100%;"> </vaadin-combo-box>
                            <vaadin-combo-box id="schedulerCB" style="width: 100%;"> </vaadin-combo-box>
                            <vaadin-combo-box id="freezingCB" style="width: 100%;"> </vaadin-combo-box>
                            <vaadin-combo-box id="rayDebugCB" style="width: 100%;"> </vaadin-combo-box>
                            <vaadin-combo-box id="maxTrainingTimeCB" style="width: 100%;"> </vaadin-combo-box>
                            <vaadin-combo-box id="startCheckIterationCB" style="width: 100%;"> </vaadin-combo-box>
                        </vaadin-vertical-layout>
                        <vaadin-vertical-layout id="buttonsCont">
                            <vaadin-button id="saveBtn" theme="primary">
                                Save
                            </vaadin-button>
                        </vaadin-vertical-layout>
                    </vaadin-vertical-layout>
                </div>
            </vaadin-horizontal-layout>
        `;
    }

    static get is() {
        return "settings-view-content";
    }

    _attachDom(dom) {
        this.appendChild(dom);
    }
}

customElements.define(SettingsViewContent.is, SettingsViewContent);
