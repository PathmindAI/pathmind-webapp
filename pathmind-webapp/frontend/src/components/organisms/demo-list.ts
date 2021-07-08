import { LitElement, html, css, property } from "lit-element";
import "../atoms/loading-spinner.ts";

class DemoList extends LitElement {

    @property({type: Array})
    demoDataList = [];

    @property({type: Boolean})
    disableButtons = false;

    @property({type: String, reflect: true})
    name = "";

    @property({type: Boolean, reflect: true})
    isVertical = false;

    static get styles() {
        return css`
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
            demo-list[isvertical] {
                padding: 0;
            }
            demo-list vaadin-horizontal-layout {
                flex-wrap: wrap;
                justify-content: center;
                align-items: stretch;
            }
            demo-list[isvertical] vaadin-horizontal-layout {
                flex-direction: column;
            }
            demo-list[isvertical] .demo-item {
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
            demo-list[isvertical] img {
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
            }`;
    }

    render() {
        return html`
            <vaadin-horizontal-layout name="${this.name}">
                ${
                    this.demoDataList.map(item => html`
                        <vaadin-vertical-layout
                            class="demo-item"
                            name="${item.name}"
                            disabled?="${this.disableButtons}"
                        >
                            <span>${item.name}</span>
                            <img
                                src="${item.imageUrl}"
                                @click="${this.buttonClickedHandler}"
                            />
                            <div class="loading-wrapper">
                                <loading-spinner></loading-spinner>
                            </div>
                            <a href="${item.tutorialUrl}" target="_blank">
                                Read Tutorial Article
                            </a>
                        </vaadin-vertical-layout>`)
                }
            </vaadin-horizontal-layout>
        `;
    }

    _getDataItem(index, key) {
        if (this.demoDataList.length == 0) return "";
        return this.demoDataList[index][key];
    }

    setData(demoDataList) {
        console.log(demoDataList)
        this.demoDataList = demoDataList;
    }

    buttonClickedHandler(event) {
        this.disableButtons = true;
        event.target.parentNode.setAttribute("loading", true);
        this.setAttribute("name", event.target.parentNode.getAttribute("name"));
    }

    createRenderRoot() {
      return this;
    }
}

customElements.define("demo-list", DemoList);