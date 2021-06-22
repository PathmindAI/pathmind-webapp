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
            metric1axistitle: {
                type: String,
            },
            metric2axistitle: {
                type: String,
            },
            metric1color: {
                type: String,
            },
            metric2color: {
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
            chartready: {
                type: Boolean,
                reflectToAttribute: true,
            },
            dimlines: {
                type: Boolean,
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
                                metric1axistitle,
                                metric2axistitle,
                                metric1color,
                                metric2color,
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
                :host(#chart) {
                    width: 100%;
                    height: 100%;
                }
                div[dir="ltr"] {
                    width: 100% !important;
                    height: 0 !important;
                    padding-bottom: 32%;
                }
                .google-visualization-tooltip div {
                    line-height: 1.2;
                    padding: var(--lumo-space-xxs);
                }
                :host([dimlines]) #chartdiv path {
                    opacity: 0.4;
                }
                :host([dimlines]) #chartdiv path[stroke~="#1a2949"] {
                    opacity: 1;
                }`;
            this.$.chart.shadowRoot.appendChild(style);
            if (isInit) {
                isInit = false;
                if (this.dimlines) {
                    this.$.chart.setAttribute("dimlines", true);
                }
                setTimeout(() => {
                    const is_safari = navigator.userAgent.indexOf("Safari") > -1;
                    const waitingTime = is_safari ? 1000 : 200;
                    // This is to ensure the tooltips are rendered
                    this.chartready = true;
                    
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

    _computeOptions(showtooltip, haxistitle, vaxistitle, curvelines, seriestype, series, metric1axistitle, metric2axistitle, metric1color, metric2color, stacked, viewwindow) {
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
            "vAxes": [
                {
                    "title": metric1axistitle ? metric1axistitle : vaxistitle,
                    "titleTextStyle": {"italic": false, "color": metric1axistitle ? metric1color : "black"},
                    "textStyle": {"color": metric1axistitle ? metric1color : "black"},
                    "textPosition": vaxistitle ? "out" : "none",
                    "ticks": vaxistitle ? "auto" : [],
                    "viewWindow": viewwindow,
                    "viewWindowMode": viewwindow ? "pretty" : "maximized",
                    "baselineColor": vaxistitle ? "black" : "#FFF",
                    "gridlineColor": vaxistitle ? "#CCC" : "#FFF"
                },
                {
                    "title": metric2axistitle,
                    "titleTextStyle": {"italic": false, "color": metric2axistitle ? (metric2color == metric1color ? "black" : metric2color) : "black"},
                    "textStyle": {"color": metric2axistitle ? (metric2color == metric1color ? "black" : metric2color) : "black"},
                    "textPosition": vaxistitle ? "out" : "none",
                    "ticks": vaxistitle ? "auto" : [],
                    "viewWindow": viewwindow,
                    "viewWindowMode": viewwindow ? "pretty" : "maximized",
                    "baselineColor": "transparent",
                    "gridlineColor": "transparent",
                    "slantedText":true,
                    "slantedTextAngle":90 
                }
            ],
            "interpolateNulls": true,
            "legend": {"position": "none"},  // true for all usages
            "seriesType": seriestype,
            "series": series,
            "chartArea": {
                "left": !vaxistitle && !haxistitle ? 0 : "10%", 
                "top": !vaxistitle && !haxistitle ? 0 : "5%", 
                "width": !vaxistitle && !haxistitle ? "100%" : metric2axistitle ? "80%" : "88%", 
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
        }
        this.rows = [];
        this.redraw();
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
                type=[[type]]
                cols=[[cols]]
                rows=[[rows]]
                options=[[options]]
            ></google-chart>
        `;
    }
}
customElements.define(DataChart.is, DataChart);