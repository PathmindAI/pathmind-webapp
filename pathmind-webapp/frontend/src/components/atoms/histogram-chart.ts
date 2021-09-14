import { LitElement, html, property } from "lit-element";
import '@google-web-components/google-chart';
import { debounce } from 'lodash';
 
class HistogramChart extends LitElement {
    @property({type: String})
    title = "";
    @property({type: String})
    haxistitle = "";
    @property({type: String})
    vaxistitle = "";
    @property({type: Array})
    cols = [];
    @property({type: Array})
    rows = [];
    @property({type: Array})
    colors = [];
    @property({type: Number})
    bucketsize = 0;
    @property({type: Boolean, reflect: true})
    chartready = false;
    @property({type: Object})
    options = {};
    @property({type: Boolean})
    isInit = true;

    firstUpdated() {
        const chart : any = this.shadowRoot.getElementById("chart");
        chart.addEventListener("google-chart-ready", event => {
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
                chart.shadowRoot.appendChild(style);
            if (this.isInit) {
                this.isInit = false;
                setTimeout(() => {
                    const is_safari = navigator.userAgent.indexOf("Safari") > -1;
                    const waitingTime = is_safari ? 1000 : 200;
                    // This is to ensure the tooltips are rendered
                    this.chartready = true;
                    chart.redraw();
                    
                    setTimeout(() => {
                        (this as any).style.opacity = 1;
                        chart.redraw();
                    }, waitingTime);
                }, 0);
            }
        });
        window.addEventListener("resize", debounce(() => {
            chart.redraw();
        }, 300));
    }

    updated(changedProperties) {
        const chart : any = this.shadowRoot.getElementById("chart");
        changedProperties.forEach((oldValue, name) => {
            if (name === "title" || name === "haxistitle" ||
                name === "vaxistitle" || name === "colors") {
                this._computeOptions();
            }
            if (name === "cols" || name === "rows" || name === "options") {
                chart.cols = this.cols;
                chart.rows = this.rows;
                chart.options = this.options;
                this.redraw();
            }
        })
    }

    setData(cols, rows) {
        this.cols = cols;
        this.rows = rows;
    }

    _computeOptions() {
        this.options = {
            "title": this.title || null,
            "legend": {
                "position": "none"
            },
            "hAxis": {
                "title": this.haxistitle,
                "titleTextStyle": {"italic": false},
                "textPosition": this.haxistitle ? "in" : "none",
                "ticks": this.haxistitle ? "auto" : [],
                "baselineColor": this.haxistitle ? "black" : "#FFF",
                "gridlineColor": this.haxistitle ? "#CCC" : "#FFF"
            },
            "vAxes": [
                {
                    "title": this.vaxistitle,
                    "titleTextStyle": {"italic": false},
                    "textPosition": this.vaxistitle ? "out" : "none",
                    "ticks": this.vaxistitle ? "auto" : [],
                    "baselineColor": this.vaxistitle ? "black" : "#FFF",
                    "gridlineColor": this.vaxistitle ? "#CCC" : "#FFF",
                }
            ],
            "histogram": {
                "hideBucketItems": true
            },
            "colors": this.colors,
            "chartArea": {
                "left": !this.vaxistitle && !this.haxistitle ? 0 : "10%", 
                "top": "10%", 
                "width": !this.vaxistitle && !this.haxistitle ? "100%" : "85%",
                "height": !this.vaxistitle && !this.haxistitle ? "100%" : "80%"
            },
            "enableInteractivity": !(!this.vaxistitle && !this.haxistitle)
        };
    }

    redraw() {
        const chart : any = this.shadowRoot.getElementById("chart");
        if (chart != null) {
            chart.redraw();
        }
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
    }

    render() {
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
            ></google-chart>
        `;
    }
}
customElements.define("histogram-chart", HistogramChart);