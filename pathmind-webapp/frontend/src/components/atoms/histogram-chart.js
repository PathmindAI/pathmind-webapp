import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import '@google-web-components/google-chart';
import { debounce } from 'lodash';
 
class HistogramChart extends PolymerElement {
    static get is() {
        return "histogram-chart";
    }

    static get properties() {
        return {
            title: {
                type: String,
            },
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
                                title,
                                haxistitle, 
                                vaxistitle, 
                                colors)`,
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
                div[dir="ltr"] {
                    width: 100% !important;
                    height: 0 !important;
                    padding-bottom: 32%;
                }
                .google-visualization-tooltip div {
                    line-height: 1.2;
                    padding: var(--lumo-space-xxs);
                }`;
            this.$.chart.shadowRoot.appendChild(style);
            if (isInit) {
                isInit = false;
                setTimeout(() => {
                    const is_safari = navigator.userAgent.indexOf("Safari") > -1;
                    const waitingTime = is_safari ? 1000 : 200;
                    // This is to ensure the tooltips are rendered
                    this.chartready = true;
                    this.$.chart.redraw();
                    
                    setTimeout(() => {
                        this.style.opacity = 1;
                        this.$.chart.redraw();
                    }, waitingTime);
                }, 0);
            }
        });
        window.addEventListener("resize", debounce(() => {
            this.$.chart.redraw();
        }, 300));
    }

    _computeOptions(title, haxistitle, vaxistitle, colors) {
        return {
            "title": title || null,
            "legend": {
                "position": "none"
            },
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
            "histogram": {
                "hideBucketItems": true
            },
            "colors": colors,
            "chartArea": {
                "left": !vaxistitle && !haxistitle ? 0 : "10%", 
                "top": "10%", 
                "width": !vaxistitle && !haxistitle ? "100%" : "85%",
                "height": !vaxistitle && !haxistitle ? "100%" : "80%"
            },
            "enableInteractivity": !(!vaxistitle && !haxistitle)
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