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
                    text-align: left;
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
                    display: none;
                    justify-content: center;
                    align-items: center;
                    position: absolute;
                    width: 100%;
                    height: 100%;
                    top: 0;
                    left: 0;
                    background-color: rgba(0,0,0,0.12);
                }
                settings-view-content .cta-overlay.show {
                    display: flex;
                }
                settings-view-content .cta-overlay-content {
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    max-width: 90%;
                    background-color: white;
                    padding: var(--lumo-space-m);
                    text-align: justify;
                    border-radius: var(--lumo-border-radius-l);
                    box-shadow: 0 3px 5px var(--lumo-shade-10pct);
                }
            </style>
            <vaadin-horizontal-layout class="panel-wrapper">
                <div class="content">
                    <div class="settings-header">
                        <span class="section-title-label">Advanced Settings</span>
                        <a class="helper-icon" href="http://help.pathmind.com/en/articles/5464077-pathmind-experiment-settings" title="Learn more about Settings" target="_blank"><iron-icon icon="vaadin:info-circle-o"></iron-icon></a>
                    </div>
                    <vaadin-vertical-layout class="inner-content">
                        <vaadin-vertical-layout class="grid-wrapper" id="paidSettings">
                            <vaadin-select id="longerTrainingCB"></vaadin-select>
                            <vaadin-select id="startCheckIterationCB"></vaadin-select>
                            <vaadin-select id="hiddenNodeCB"></vaadin-select>
                            <vaadin-select id="hiddenLayerCB"></vaadin-select>
                        </vaadin-vertical-layout>
                        <h4 hidden="{{!isInternalUser}}">Internal Users Only</h4>
                        <vaadin-vertical-layout class="grid-wrapper" id="internalSettings" hidden="{{!isInternalUser}}">
                            <vaadin-select id="userLogCB"></vaadin-select>
                            <vaadin-select id="ec2InstanceTypeCB"></vaadin-select>
                            <vaadin-select id="condaVersionCB"></vaadin-select>
                            <vaadin-select id="anylogicVersionCB"></vaadin-select>
                            <vaadin-select id="nativerlVersionCB"></vaadin-select>
                            <vaadin-select id="helperVersionCB"></vaadin-select>
                            <vaadin-select id="numSampleCB"></vaadin-select>
                            <vaadin-select id="maxMemoryCB"></vaadin-select>
                            <vaadin-select id="schedulerCB"></vaadin-select>
                            <vaadin-select id="freezingCB"></vaadin-select>
                            <vaadin-select id="rayDebugCB"></vaadin-select>
                            <vaadin-select id="maxTrainingTimeCB"></vaadin-select>
                        </vaadin-vertical-layout>
                        <vaadin-vertical-layout id="buttonsCont" hidden="{{hideSaveButton}}">
                            <vaadin-button id="saveBtn" theme="primary">
                                Save
                            </vaadin-button>
                        </vaadin-vertical-layout>
                        <vaadin-vertical-layout id="ctaOverlay" class="cta-overlay" hidden="{{!isFreeUser}}">
                            <vaadin-vertical-layout class="cta-overlay-content">
                                <span>Advanced settings are available for Professional Plan subscribers.</span>
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

    _isFreeUserChanged(newValue) {
        if (newValue) {
            this.addEventListener("click", event => {
                this.querySelector("#ctaOverlay").classList.add("show");
            });
        }
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
                observer: '_isFreeUserChanged',
            }
        }
    }
}

customElements.define(SettingsViewContent.is, SettingsViewContent);
