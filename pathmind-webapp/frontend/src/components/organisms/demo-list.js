import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class DemoList extends PolymerElement {
    static get is() {
        return "demo-list";
    }

    static get template() {
        return html`
            <style>
                :host {
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
                vaadin-horizontal-layout {
                    flex-wrap: wrap;
                    justify-content: center;
                    align-items: stretch;
                }
                ul {
                    display: flex;
                    list-style: none;
                    padding: 0;
                    margin: 0;
                }
                li {
                    margin: 0 var(--lumo-space-s);
                }
                h4 {
                    width: 100%;
                    font-family: var(--lumo-font-family-header);
                    text-align: center;
                    margin: 0;
                }
                p {
                    line-height: 1.4;
                    margin-bottom: 0;
                }
                img {
                    display: block;
                    width: 100%;
                }
                .demo-item {
                    flex: 0 1 calc(33% - var(--lumo-space-l)*2);
                    max-width: 20rem;
                    font-size: var(--lumo-font-size-s);
                    padding: var(--lumo-space-m);
                    background: var(--pm-grey-color-lightest);
                    box-shadow: 0px 3px 6px rgba(0, 0, 0, 0.16);
                    border-radius: var(--lumo-border-radius);
                    margin: 0 var(--lumo-space-l) var(--lumo-space-l);
                    cursor: pointer;
                }
                .demo-item:hover {
                    box-shadow: 0px 6px 12px rgba(0, 0, 0, 0.18);
                }
                details {
                    width: 100%;
                    cursor: pointer;
                }
                summary:hover {
                    color: var(--lumo-primary-color);
                }
                :host([list-view]) {
                    flex-direction: column;
                    padding: 0;
                }
                :host([list-view]) .demo-item {
                    align-items: center;
                    flex-direction: row;
                    flex: 1 0 100%;
                    width: 100%;
                    padding: var(--lumo-space-s) var(--lumo-space-m);
                    box-shadow: none;
                    border-bottom: 1px solid var(--pm-grey-color-lightest);
                }
                :host([list-view]) .demo-item:hover {
                    background-color: var(--pm-app-bg-color);
                }
                :host([list-view]) .demo-item h4 {
                    flex: 0 0 20%;
                    width: 20%;
                    text-align: left;
                    margin-right: var(--lumo-space-l);
                }
                :host([list-view]) p {
                    margin: 0;
                }
                .description {
                    margin-bottom: var(--lumo-space-m);
                }
                .result {
                    margin: auto 0 0;
                }
            </style>
            <vaadin-horizontal-layout>
                <dom-repeat items="{{demoDataList}}">
                    <template>
                        <vaadin-vertical-layout class="demo-item" data-item$="{{item.name}}" on-click="chooseDemoHandler">
                            <h4>{{item.name}}</h4>
                            <div class="demo-img-wrapper" hidden="{{hideImage}}"><img src="{{item.imageUrl}}"/></div>
                            <p class="description">{{item.description}}</p>
                            <p class="result"><b>Result:</b> {{item.result}}</p>
                        </vaadin-vertical-layout>
                    </template>
                </dom-repeat>
            </vaadin-horizontal-layout>
        `;
    }

    setData(demoDataList) {
        this.demoDataList = demoDataList;
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
      };
    }
}

customElements.define(DemoList.is, DemoList);