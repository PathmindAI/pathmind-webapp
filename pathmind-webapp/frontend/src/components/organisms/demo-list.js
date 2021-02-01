import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "../atoms/loading-spinner.js";

class DemoList extends PolymerElement {
    static get is() {
        return "demo-list";
    }

    static get template() {
        return html`
            <style>
                demo-list {
                    box-sizing: border-box;
                    display: flex;
                    flex-direction: column;
                    justify-content: center;
                    align-items: center;
                    position: relative;
                    width: 100%;
                    padding: 0 var(--lumo-space-m);
                    margin-bottom: var(--lumo-space-l);
                }
                demo-list vaadin-horizontal-layout {
                    flex-wrap: wrap;
                    justify-content: center;
                    align-items: stretch;
                }
                demo-list ul {
                    display: flex;
                    list-style: none;
                    padding: 0;
                    margin: 0;
                }
                demo-list li {
                    margin: 0 var(--lumo-space-s);
                }
                demo-list h4 {
                    width: 100%;
                    font-family: var(--lumo-font-family-header);
                    text-align: center;
                    margin: 0 0 var(--lumo-space-s);
                }
                demo-list p {
                    line-height: 1.4;
                    margin-bottom: 0;
                }
                demo-list img {
                    display: block;
                    width: 100%;
                }
                demo-list .demo-item {
                    flex: 0 1 calc(33% - var(--lumo-space-l)*2);
                    max-width: 20rem;
                    font-size: var(--lumo-font-size-s);
                    padding: var(--lumo-space-m);
                    background: var(--pm-grey-color-lightest);
                    box-shadow: 0px 3px 6px rgba(0, 0, 0, 0.16);
                    border-radius: var(--lumo-border-radius);
                    margin: 0 var(--lumo-space-l) var(--lumo-space-l);
                }
                demo-list[list-view] {
                    flex-direction: column;
                    padding: 0;
                }
                demo-list[list-view] .demo-item {
                    align-items: center;
                    flex-direction: row;
                    flex: 1 0 100%;
                    width: 100%;
                    padding: var(--lumo-space-s) var(--lumo-space-m);
                    box-shadow: none;
                    border-bottom: 1px solid var(--pm-grey-color-lightest);
                }
                demo-list[list-view] .demo-item:hover {
                    background-color: var(--pm-app-bg-color);
                }
                demo-list[list-view] .demo-item h4 {
                    flex: 0 0 20%;
                    width: 20%;
                    text-align: left;
                    margin-right: var(--lumo-space-l);
                }
                demo-list[list-view] p {
                    margin: 0;
                }
                demo-list .description {
                    margin-bottom: var(--lumo-space-m);
                }
                demo-list .result {
                    margin: 0 0 var(--lumo-space-m);
                }
                demo-list .arrow-icon {
                    display: inline-block;
                    position: relative;
                    width: .7rem;
                    height: .7rem;
                    margin-left: var(--lumo-space-xs);
                }
                demo-list .arrow-icon::after {
                    content: '';
                    box-sizing: border-box;
                    display: block;
                    position: absolute;
                    width: 100%;
                    height: 100%;
                    top: -2px;
                    left: -4px;
                    border-right: 2px solid white;
                    border-bottom: 2px solid white;
                    transform: rotate(-45deg) skew(12deg, 12deg);
                }
                demo-list vaadin-button {
                    width: 100%;
                    margin-top: auto;
                }
                demo-list vaadin-button > span {
                    display: flex;
                    align-items: center;
                }
                demo-list vaadin-button[loading] > span.getStartedText,
                demo-list vaadin-button:not([loading]) > span.loadingText {
                    display: none;
                }
                demo-list loading-spinner {
                    display: inline-block;
                    margin-right: var(--lumo-space-xs);
                }
            </style>
            <vaadin-horizontal-layout>
                <vaadin-vertical-layout class="demo-item">
                    <h4>[[_getDataItem(demoDataList, '0', 'name')]]</h4>
                    <div class="demo-img-wrapper"><img src="[[_getDataItem(demoDataList, '0', 'imageUrl')]]"/></div>
                    <p class="description">[[_getDataItem(demoDataList, '0', 'description')]]</p>
                    <p class="result"><b>Result:</b> [[_getDataItem(demoDataList, '0', 'result')]]</p>
                    <vaadin-button theme="primary" data-item$="[[_getDataItem(demoDataList, '0', 'name')]]" on-click="buttonClickedHandler" disabled="[[disableButtons]]">
                        <span class="getStartedText">Get Started <span class="arrow-icon"></span></span>
                        <span class="loadingText"><loading-spinner></loading-spinner>Loading...</span>
                    </vaadin-button>
                </vaadin-vertical-layout>
                <vaadin-vertical-layout class="demo-item">
                    <h4>[[_getDataItem(demoDataList, '1', 'name')]]</h4>
                    <div class="demo-img-wrapper"><img src="[[_getDataItem(demoDataList, '1', 'imageUrl')]]"/></div>
                    <p class="description">[[_getDataItem(demoDataList, '1', 'description')]]</p>
                    <p class="result"><b>Result:</b> [[_getDataItem(demoDataList, '1', 'result')]]</p>
                    <vaadin-button theme="primary" data-item$="[[_getDataItem(demoDataList, '1', 'name')]]" on-click="buttonClickedHandler" disabled="[[disableButtons]]">
                        <span class="getStartedText">Get Started <span class="arrow-icon"></span></span>
                        <span class="loadingText"><loading-spinner></loading-spinner>Loading...</span>
                    </vaadin-button>
                </vaadin-vertical-layout>
                <vaadin-vertical-layout class="demo-item">
                    <h4>[[_getDataItem(demoDataList, '2', 'name')]]</h4>
                    <div class="demo-img-wrapper"><img src="[[_getDataItem(demoDataList, '2', 'imageUrl')]]"/></div>
                    <p class="description">[[_getDataItem(demoDataList, '2', 'description')]]</p>
                    <p class="result"><b>Result:</b> [[_getDataItem(demoDataList, '2', 'result')]]</p>
                    <vaadin-button theme="primary" name$="[[_getDataItem(demoDataList, '2', 'name')]]" on-click="buttonClickedHandler" disabled="[[disableButtons]]">
                        <span class="getStartedText">Get Started <span class="arrow-icon"></span></span>
                        <span class="loadingText"><loading-spinner></loading-spinner>Loading...</span>
                    </vaadin-button>
                </vaadin-vertical-layout>
            </vaadin-horizontal-layout>
        `;
    }

    _attachDom(dom) {
      this.appendChild(dom);
    }

    _getDataItem(demoList, index, key) {
        if (demoList.length == 0) return "";
        return demoList[index][key];
    }

    setData(demoDataList) {
        this.demoDataList = demoDataList;
    }

    buttonClickedHandler(event) {
        this.disableButtons = true;
        event.target.setAttribute("loading", true);
    }

    static get properties() {
      return {
        demoDataList: {
            value() {
                return [];
            }
        },
        hideImage: {
            type: Boolean,
            value: true,
        },
        listView: {
            type: Boolean,
            value: false,
            reflectToAttribute: true,
        },
        disableButtons: {
            type: Boolean,
            value: false,
        },
      };
    }
}

customElements.define(DemoList.is, DemoList);