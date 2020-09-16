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
                    line-height: 1.2;
                    padding: var(--lumo-space-xxs);
                }
                :host([type~="line"]) #chartdiv path {
                    opacity: 0.4;
                }
                :host([type~="line"]) #chartdiv path[stroke~="#1a2949"] {
                    opacity: 1;
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
                "textPosition": haxistitle ? "out" : "none",
                "ticks": haxistitle ? "auto" : [],
                "format": "0",
                "baselineColor": haxistitle ? "black" : "#FFF",
                "gridlineColor": haxistitle ? "#CCC" : "#FFF"
            },
            "vAxis": {
                "title": vaxistitle,
                "titleTextStyle": {"italic": false},
                "textPosition": vaxistitle ? "out" : "none",
                "ticks": vaxistitle ? "auto" : [],
                "viewWindow": viewwindow,
                "viewWindowMode": viewwindow ? "pretty" : "maximized",
                "baselineColor": vaxistitle ? "black" : "#FFF",
                "gridlineColor": vaxistitle ? "#CCC" : "#FFF"
            },
            "legend": {"position": "none"},  // true for all usages
            "seriesType": seriestype,
            "series": series,
            "chartArea": {
                "left": !vaxistitle && !haxistitle ? 0 : "10%", 
                "top": !vaxistitle && !haxistitle ? 0 : 20, 
                "width": !vaxistitle && !haxistitle ? "100%" : "88%", 
                "height": !vaxistitle && !haxistitle ? "100%" : null
            }
        };
    }
    
    setData(cols, rows) {
        this.cols = cols;
        this.rows = rows;
        this.$.chart.redraw();
    }

    setSeries(series) {
        this.series = series;
    }

    setViewWindow(viewWindow) {
        this.viewwindow = viewWindow;
    }

    static get template() {
        return html`
            <style>
                :host {
                    width: 100%;
                }
            </style>
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