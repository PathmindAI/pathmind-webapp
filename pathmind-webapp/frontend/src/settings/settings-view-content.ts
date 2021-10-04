import { LitElement, html, property } from "lit-element";

class SettingsViewContent extends LitElement {
    
  @property({type: Boolean})
  isPaidUser = false;
  
  @property({type: Boolean})
  isInternalUser = false;
  
  @property({type: Boolean})
  hideSaveButton = false;

  updated(changedProperties) {
    changedProperties.forEach((oldValue, name) => {
        if (name === "isPaidUser" || name === "isInternalUser") {
            if (this._isFreeUser()) {
              this._isFreeUserChanged();
            }
        }
    });
  }

  render() {
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
            settings-view-content vaadin-integer-field {
              width: auto;
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
                    <h4 ?hidden="${!this.isInternalUser}">Internal Users Only</h4>
                    <vaadin-vertical-layout class="grid-wrapper" id="internalSettings" ?hidden="${!this.isInternalUser}">
                        <vaadin-select id="userLogCB"></vaadin-select>
                        <vaadin-select id="ec2InstanceTypeCB"></vaadin-select>
                        <vaadin-select id="condaVersionCB"></vaadin-select>
                        <vaadin-select id="anylogicVersionCB"></vaadin-select>
                        <vaadin-select id="nativerlVersionCB"></vaadin-select>
                        <vaadin-select id="helperVersionCB"></vaadin-select>
                        <vaadin-select id="numSampleCB"></vaadin-select>
                        <vaadin-select id="numWorkerCB"></vaadin-select>
                        <vaadin-select id="maxMemoryCB"></vaadin-select>
                        <vaadin-select id="schedulerCB"></vaadin-select>
                        <vaadin-select id="freezingCB"></vaadin-select>
                        <vaadin-select id="rayDebugCB"></vaadin-select>
                        <vaadin-select id="maxTrainingTimeCB"></vaadin-select>
                        <vaadin-select id="gammaCB"></vaadin-select>
                        <vaadin-select id="rolloutFragmentLengthCB"></vaadin-select>
                        <vaadin-select id="batchModeCB"></vaadin-select>
                        <vaadin-select id="batchSizeCB"></vaadin-select>
                        <vaadin-integer-field id="rewardBalancePeriodField" min="1" has-controls></vaadin-integer-field>
                    </vaadin-vertical-layout>
                    <vaadin-vertical-layout id="buttonsCont" ?hidden="${this.hideSaveButton}">
                        <vaadin-button id="saveBtn" theme="primary">
                            Save
                        </vaadin-button>
                    </vaadin-vertical-layout>
                    <vaadin-vertical-layout id="ctaOverlay" class="cta-overlay" ?hidden="${!this._isFreeUser()}">
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

  createRenderRoot() {
    return this;
  }

  _isFreeUser() {
    return !(this.isPaidUser || this.isInternalUser);
  }

  _settingsPanelClickHandlerForFreeUsers(event) {
    this.querySelector("#ctaOverlay").classList.add("show");
  }

  _isFreeUserChanged() {
    this.addEventListener("click", this._settingsPanelClickHandlerForFreeUsers);
  }
}

customElements.define("settings-view-content", SettingsViewContent);
