import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import '@google-web-components/google-chart';
import { debounce } from 'lodash';
 
class HistogramChart extends PolymerElement {
    static get is() {
        return "histogram-chart";
    }

    static get properties() {
        return {
            haxistitle: {
                type: String,
            },
            vaxistitle: {
                type: String,
            },
            cols: {
                type: Array,
            },
            rows: {
                type: Array,
            },
            colors: {
                type: Array,
            },
            bucketsize: {
                type: Number,
            },
            chartready: {
                type: Boolean,
                reflectToAttribute: true,
            },
            options: {
                type: Object,
                computed: `_computeOptions(
                                haxistitle, 
                                vaxistitle, 
                                colors,
                                bucketsize)`,
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
                    line-height: 1.2;
                    padding: var(--lumo-space-xxs);
                }`;
            this.$.chart.shadowRoot.appendChild(style);
            if (isInit) {
                isInit = false;
                setTimeout(() => {
                    // This is to ensure the tooltips are rendered
                    this.chartready = true;
                    this.$.chart.redraw();

                    setTimeout(() => {
                        this.style.opacity = 1;
                    }, 200);
                }, 0);
            }
        });
        window.addEventListener("resize", debounce(() => {
            this.$.chart.redraw();
        }, 300));
    }

    _computeOptions(haxistitle, vaxistitle, colors, bucketsize) {
        return {
            "hAxis": {
                "title": haxistitle,
                "titleTextStyle": {"italic": false},
                "textPosition": haxistitle ? "out" : "none",
                "ticks": haxistitle ? "auto" : [],
                "baselineColor": haxistitle ? "black" : "#FFF",
                "gridlineColor": haxistitle ? "#CCC" : "#FFF"
            },
            "vAxes": [
                {
                    "title": vaxistitle,
                    "titleTextStyle": {"italic": false},
                    "textPosition": vaxistitle ? "out" : "none",
                    "ticks": vaxistitle ? "auto" : [],
                    "baselineColor": vaxistitle ? "black" : "#FFF",
                    "gridlineColor": vaxistitle ? "#CCC" : "#FFF",
                }
            ],
            "bar": { "gap": 0 },
            "histogram": {
                "bucketSize": bucketsize || "auto",
            },
            "interpolateNulls": false,
            "colors": colors,
            "chartArea": {
                "left": !vaxistitle && !haxistitle ? 0 : "10%", 
                "top": !vaxistitle && !haxistitle ? 0 : "5%", 
                "height": !vaxistitle && !haxistitle ? "100%" : "80%"
            }
        };
    }

    redraw() {
        this.$.chart.redraw();
    }
    
    setData(cols, rows) {
        this.cols = cols;
        this.rows = rows;
        this.redraw();
    }
    
    setChartEmpty() {
        if (this.cols == undefined) {
            this.cols = [
                {"label":"Iteration", "type":"number"},
                {"label":"line", "type":"number"},
            ]
        } else {
            this.cols = [{"label":"Metrics", "type":"number"}];
        }

        this.rows = [];
        this.redraw();
    }

    static get template() {
        return html`
            <style>
                :host {
                    width: 1px;
                    height: 1px;
                    opacity: 0;
                    overflow: hidden;
                }
                :host([chartready]) {
                    width: 100%;
                    height: 100%;
                }
            </style>
            <google-chart 
                id="chart"
                type="histogram"
                cols=[[cols]]
                rows=[[rows]]
                options=[[options]]
            ></google-chart>
        `;
    }
}
customElements.define(HistogramChart.is, HistogramChart);