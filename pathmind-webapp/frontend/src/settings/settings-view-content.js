import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class SettingsViewContent extends PolymerElement {
    static get template() {
        return html`
            <style>
                settings-view-content .settings-header {
                    box-sizing: border-box;
                    display: flex;
                    justify-content: space-between;
                    padding-right: var(--lumo-space-xs);
                }
                settings-view-content iron-icon {
                    display: none;
                    --iron-icon-width: var(--lumo-font-size-s);
                    --iron-icon-height: var(--lumo-font-size-s);
                }
                settings-view-content .inner-content {
                    padding: var(--lumo-space-s) 0 0;
                }
                settings-view-content .inner-content {
                    position: relative;
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
                settings-view-content .cta-overlay {
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    position: absolute;
                    width: 100%;
                    height: 100%;
                    top: 0;
                    left: 0;
                    background-color: rgba(0,0,0,0.12);
                }
                settings-view-content .cta-overlay-content {
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    background-color: white;
                    padding: var(--lumo-space-m);
                    border-radius: var(--lumo-border-radius-l);
                    box-shadow: 0 3px 5px var(--lumo-shade-10pct);
                }
            </style>
            <vaadin-horizontal-layout class="panel-wrapper">
                <div class="content">
                    <div class="settings-header">
                        <span class="section-title-label">Settings</span>
                        <a class="helper-icon" href="" title="Learn more about Settings" target="_blank"><iron-icon icon="vaadin:info-circle-o"></iron-icon></a>
                    </div>
                    <vaadin-vertical-layout class="inner-content">
                        <vaadin-vertical-layout class="grid-wrapper" id="paidSettings">
                            <vaadin-combo-box id="hiddenNodeCB"></vaadin-combo-box>
                            <vaadin-combo-box id="hiddenLayerCB"></vaadin-combo-box>
                            <vaadin-combo-box id="longerTrainingCB"></vaadin-combo-box>
                        </vaadin-vertical-layout>
                        <h4 hidden="{{!isInternalUser}}">Internal Users Only</h4>
                        <vaadin-vertical-layout class="grid-wrapper" id="internalSettings" hidden="{{!isInternalUser}}">
                            <vaadin-combo-box id="userLogCB"></vaadin-combo-box>
                            <vaadin-combo-box id="ec2InstanceTypeCB"></vaadin-combo-box>
                            <vaadin-combo-box id="condaVersionCB"></vaadin-combo-box>
                            <vaadin-combo-box id="anylogicVersionCB"></vaadin-combo-box>
                            <vaadin-combo-box id="nativerlVersionCB"></vaadin-combo-box>
                            <vaadin-combo-box id="helperVersionCB"></vaadin-combo-box>
                            <vaadin-combo-box id="numSampleCB"></vaadin-combo-box>
                            <vaadin-combo-box id="maxMemoryCB"></vaadin-combo-box>
                            <vaadin-combo-box id="schedulerCB"></vaadin-combo-box>
                            <vaadin-combo-box id="freezingCB"></vaadin-combo-box>
                            <vaadin-combo-box id="rayDebugCB"></vaadin-combo-box>
                            <vaadin-combo-box id="maxTrainingTimeCB"></vaadin-combo-box>
                            <vaadin-combo-box id="startCheckIterationCB"></vaadin-combo-box>
                        </vaadin-vertical-layout>
                        <vaadin-vertical-layout id="buttonsCont" hidden="{{hideSaveButton}}">
                            <vaadin-button id="saveBtn" theme="primary">
                                Save
                            </vaadin-button>
                        </vaadin-vertical-layout>
                        <vaadin-vertical-layout id="ctaOverlay" class="cta-overlay" hidden="{{!isFreeUser}}">
                            <vaadin-vertical-layout class="cta-overlay-content">
                                <span>This is a paid feature.</span>
                                <vaadin-button id="upgradeBtn" theme="primary small">Upgrade Now</vaadin-button>
                            </vaadin-vertical-layout>
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

    setIsFreeUser(isPaidUser, isInternalUser) {
        return !(isPaidUser || isInternalUser);
    }

    static get properties() {
        return {
            hideSaveButton: {
                type: Boolean,
                value: false,
            },
            isFreeUser: {
                type: Boolean,
                computed: 'setIsFreeUser(isPaidUser, isInternalUser)',
            }
        }
    }
}

customElements.define(SettingsViewContent.is, SettingsViewContent);
