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
                }
                demo-list[is-vertical] {
                    padding: 0;
                }
                demo-list vaadin-horizontal-layout {
                    flex-wrap: wrap;
                    justify-content: center;
                    align-items: stretch;
                }
                demo-list[is-vertical] vaadin-horizontal-layout {
                    flex-direction: column;
                }
                demo-list[is-vertical] .demo-item {
                    margin: 0 0 var(--lumo-space-l);
                }
                demo-list .demo-item {
                    flex: 0 1 calc(33% - var(--lumo-space-l)*2);
                    max-width: 20rem;
                    font-size: var(--lumo-font-size-s);
                    padding: var(--lumo-space-s) var(--lumo-space-s) var(--lumo-space-xs);
                    background: var(--pm-grey-color-lightest);
                    box-shadow: 0px 3px 6px rgba(0, 0, 0, 0.16);
                    border-radius: var(--lumo-border-radius);
                    margin: 0 var(--lumo-space-l) var(--lumo-space-l);
                }
                demo-list span {
                    width: 100%;
                    font-weight: 500;
                    font-family: var(--lumo-font-family-header);
                    line-height: 1.3em;
                    text-align: center;
                    margin: 0 0 var(--lumo-space-s);
                }
                demo-list img {
                    display: block;
                    width: 14rem;
                    height: 8rem;
                    object-fit: cover;
                    transition: filter 0.2s;
                    cursor: pointer;
                }
                demo-list img:hover {
                    filter: contrast(105%) brightness(103%);
                }
                demo-list[is-vertical] img {
                    height: 13.5vh;
                }
                demo-list .demo-item[loading] > img,
                demo-list .demo-item:not([loading]) > .loading-wrapper {
                    display: none;
                }
                demo-list .loading-wrapper {
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    width: 14rem;
                    height: 8rem;
                }
                demo-list loading-spinner {
                    --icon-size: 4rem;
                    --border-size: 4px;
                    display: inline-block;
                    margin-right: var(--lumo-space-xs);
                }
                demo-list a {
                    align-self: center;
                    margin-top: var(--lumo-space-xs);
                }
            </style>
            <vaadin-horizontal-layout name="{{name}}">
                <vaadin-vertical-layout
                    class="demo-item"
                    name$="[[_getDataItem(demoDataList, '0', 'name')]]"
                    disabled="[[disableButtons]]"
                >
                    <span>[[_getDataItem(demoDataList, '0', 'name')]]</span>
                    <img
                        src="[[_getDataItem(demoDataList, '0', 'imageUrl')]]"
                        on-click="buttonClickedHandler"
                    />
                    <div class="loading-wrapper">
                        <loading-spinner></loading-spinner>
                    </div>
                    <a href="[[_getDataItem(demoDataList, '0', 'tutorialUrl')]]" target="_blank">
                        Read Tutorial Article
                    </a>
                </vaadin-vertical-layout>
                <vaadin-vertical-layout
                    class="demo-item"
                    name$="[[_getDataItem(demoDataList, '1', 'name')]]"
                    disabled="[[disableButtons]]"
                >
                    <span>[[_getDataItem(demoDataList, '1', 'name')]]</span>
                    <img
                        src="[[_getDataItem(demoDataList, '1', 'imageUrl')]]"
                        on-click="buttonClickedHandler"
                    />
                    <div class="loading-wrapper">
                        <loading-spinner></loading-spinner>
                    </div>
                    <a href="[[_getDataItem(demoDataList, '1', 'tutorialUrl')]]" target="_blank">
                        Read Tutorial Article
                    </a>
                </vaadin-vertical-layout>
                <vaadin-vertical-layout
                    class="demo-item"
                    name$="[[_getDataItem(demoDataList, '2', 'name')]]"
                    disabled="[[disableButtons]]"
                >
                    <span>[[_getDataItem(demoDataList, '2', 'name')]]</span>
                    <img
                        src="[[_getDataItem(demoDataList, '2', 'imageUrl')]]"
                        on-click="buttonClickedHandler"
                    />
                    <div class="loading-wrapper">
                        <loading-spinner></loading-spinner>
                    </div>
                    <a href="[[_getDataItem(demoDataList, '2', 'tutorialUrl')]]" target="_blank">
                        Read Tutorial Article
                    </a>
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

    _findParent(el, tag) {
      while (el.parentNode) {
        el = el.parentNode;
        return el.tagName.toLowerCase() === tag.toLowerCase() ? el : null;
      }
    }

    setData(demoDataList) {
        this.demoDataList = demoDataList;
    }

    buttonClickedHandler(event) {
        this.disableButtons = true;
        event.target.parentNode.setAttribute("loading", true);
        this.set("name", event.target.parentNode.getAttribute("name"));
    }

    static get properties() {
      return {
        demoDataList: {
            value() {
                return [];
            }
        },
        disableButtons: {
            type: Boolean,
            value: false,
        },
        name: {
            type: String,
        },
        isVertical: {
            type: Boolean,
            value: false,
            reflectToAttribute: true,
        }
      };
    }
}

customElements.define(DemoList.is, DemoList);