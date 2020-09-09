import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import '@google-web-components/google-chart';
import { debounce } from 'lodash';
 
class DataChart extends PolymerElement {
    static get is() {
        return "data-chart";
    }

    static get properties() {
        return {
            type: {
                type: String,
            },
            data: {
                type: String,
            },
            options: {
                type: Object,
            },
            cols: {
                type: Array,
            },
            rows: {
                type: Array,
            },
        }
    }

    ready() {
        super.ready();
        let isInit = true;
        this.$.chart.addEventListener("google-chart-ready", event => {
            var style = document.createElement("style");
            style.innerHTML = `
                :host {
                    width: 100% !important;
                    height: 100% !important;
                }
                .google-visualization-tooltip div {
                    padding: var(--lumo-space-xxs);
                }`;
            this.$.chart.shadowRoot.appendChild(style);
            if (isInit) {
                isInit = false;
                setTimeout(() => {
                    // This is to ensure the tooltips are rendered
                    this.$.chart.redraw();
                }, 0);
            }
        });
        window.addEventListener("resize", debounce(() => {
            this.$.chart.redraw();
        }, 300));
    }

    static get template() {
        return html`
            <google-chart 
                id="chart"
                type=[[type]]
                data=[[data]]
                options=[[options]]
                cols=[[cols]]
                rows=[[rows]]
            ></google-chart>
        `;
    }
}
customElements.define(DataChart.is, DataChart);