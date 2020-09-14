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
                value: "combo",
            },
            showtooltip: {
                type: Boolean,
            },
            haxistitle: {
                type: String,
            },
            vaxistitle: {
                type: String,
            },
            curvelines: {
                type: Boolean,
            },
            seriestype: {
                type: String,
            },
            stacked: {
                type: Boolean,
            },
            cols: {
                type: Array,
            },
            rows: {
                type: Array,
            },
            series: {
                type: Object,
            },
            viewwindow: {
                type: Object,
            },
            options: {
                type: Object,
                computed: `_computeOptions(
                                showtooltip, 
                                haxistitle, 
                                vaxistitle, 
                                curvelines, 
                                seriestype, 
                                series,
                                stacked,
                                viewwindow)`,
            }
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

    _computeOptions(showtooltip, haxistitle, vaxistitle, curvelines, seriestype, series, stacked, viewwindow) {
        return {
            "tooltip": showtooltip ? { "isHtml": true } : { "trigger": "none" },
            "curveType": curvelines ? "function" : null,
            "isStacked": stacked,
            "hAxis": {
                "title": haxistitle,
                "titleTextStyle": {"italic": false},
                "textPosition": haxistitle ? "none" : "out",
                "ticks": haxistitle ? "auto" : [],
                "format": "0",
                "baselineColor": haxistitle ? "black" : "#FFF",
                "gridlineColor": haxistitle ? "#CCC" : "#FFF"
            },
            "vAxis": {
                "title": vaxistitle,
                "titleTextStyle": {"italic": false},
                "textPosition": vaxistitle ? "none" : "out",
                "ticks": vaxistitle ? "auto" : [],
                "viewWindow": viewwindow,
                "viewWindowMode": viewwindow ? "pretty" : "maximized",
                "baselineColor": vaxistitle ? "black" : "#FFF",
                "gridlineColor": vaxistitle ? "#CCC" : "#FFF"
            },
            "legend": {"position": "none"},  // true for all usages
            "seriesType": seriestype,
            "series": series,
            "chartArea": !vaxistitle && !haxistitle ? {"left": 0, "top": 0, "width": "100%", "height": "75%"} : null
        };
    }
    
    setData(cols, rows) {
        this.cols = cols;
        this.rows = rows;
    }

    setSeries(series) {
        this.series = series;
    }

    setViewWindow(viewWindow) {
        this.viewwindow = viewWindow;
    }

    static get template() {
        return html`
            <google-chart 
                id="chart"
                type=[[type]]
                cols=[[cols]]
                rows=[[rows]]
                options=[[options]]
            ></google-chart>
        `;
    }
}
customElements.define(DataChart.is, DataChart);