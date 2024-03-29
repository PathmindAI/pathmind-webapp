import { LitElement, html, property } from "lit-element";
import '@google-web-components/google-chart';
import { debounce } from 'lodash';
 
class DataChart extends LitElement {
  @property({type: String})
  type = "combo";
  @property({type: Boolean})
  showtooltip = false;
  @property({type: String})
  haxistitle = "";
  @property({type: String})
  vaxistitle = "";
  @property({type: String})
  metric1axistitle = "";
  @property({type: String})
  metric1color;
  @property({type: Boolean})
  curvelines;
  @property({type: String})
  seriestype;
  @property({type: Boolean})
  stacked;
  @property({type: Array})
  cols;
  @property({type: Array})
  rows;
  @property({type: Object})
  series;
  @property({type: Object})
  viewwindow = {};
  @property({type: Boolean, reflect: true})
  chartready = false;
  @property({type: Boolean})
  dimlines = false;
  @property({type: Object})
  options;

  firstUpdated() {
    let isInit = true;
    const chart : any = this.shadowRoot.getElementById("chart");
    chart.addEventListener("google-chart-ready", event => {
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
      chart.shadowRoot.appendChild(style);
      if (isInit) {
        isInit = false;
        if (this.dimlines) {
          chart.setAttribute("dimlines", "true");
        }
        setTimeout(() => {
          const is_safari = navigator.userAgent.indexOf("Safari") > -1;
          const waitingTime = is_safari ? 1000 : 200;
          // This is to ensure the tooltips are rendered
          this.chartready = true;
          
          setTimeout(() => {
              this.style.opacity = "1";
              this.redraw();
          }, waitingTime);
        }, 0);
      }
    });
    window.addEventListener("resize", debounce(() => {
      this.redraw();
    }, 300));
  }

  updated(changedProperties) {
    const chart : any = this.shadowRoot.getElementById("chart");
    changedProperties.forEach((oldValue, name) => {
      if (name === "showtooltip" || 
          name === "haxistitle" ||
          name === "vaxistitle" ||
          name === "curvelines" ||
          name === "seriestype" ||
          name === "series" ||
          name === "metric1axistitle" ||
          name === "metric1color" ||
          name === "stacked" ||
          name === "viewwindow") {
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
      "tooltip": this.showtooltip ? { "isHtml": true } : { "trigger": "none" },
      "curveType": this.curvelines ? "function" : null,
      "isStacked": this.stacked === false ? null : true,
      "hAxis": {
        "title": this.haxistitle,
        "titleTextStyle": {"italic": false},
        "textPosition": this.haxistitle ? "out" : "none",
        "ticks": this.haxistitle ? "auto" : [],
        "format": "0",
        "baselineColor": this.haxistitle ? "black" : "#FFF",
        "gridlineColor": this.haxistitle ? "#CCC" : "#FFF"
      },
      "vAxes": [
        {
          "title": this.metric1axistitle ? this.metric1axistitle : this.vaxistitle,
          "titleTextStyle": {"italic": false, "color": this.metric1axistitle ? this.metric1color : "black"},
          "textStyle": {"color": this.metric1axistitle ? this.metric1color : "black"},
          "textPosition": this.vaxistitle ? "out" : "none",
          "ticks": this.vaxistitle ? "auto" : [],
          "viewWindow": this.viewwindow,
          "viewWindowMode": this.viewwindow ? "pretty" : "maximized",
          "baselineColor": this.vaxistitle ? "black" : "#FFF",
          "gridlineColor": this.vaxistitle ? "#CCC" : "#FFF"
        },
      ],
      "interpolateNulls": true,
      "legend": {"position": "none"},  // true for all usages
      "seriesType": this.seriestype === "" ? null : this.seriestype,
      "series": this.series,
      "chartArea": {
        "left": !this.vaxistitle && !this.haxistitle ? 0 : "10%", 
        "top": !this.vaxistitle && !this.haxistitle ? 0 : "5%", 
        "width": !this.vaxistitle && !this.haxistitle ? "100%" : "88%", 
        "height": !this.vaxistitle && !this.haxistitle ? "100%" : "80%"
      }
    };
  }

  redraw() {
    const chart : any = this.shadowRoot.getElementById("chart");
    if (chart != null) {
      chart.redraw();
    }
  }
    
  setChartEmpty() {
    this.cols = [
        {"label":"Iteration", "type":"number"},
        {"label":"line", "type":"number"},
    ];
    this.metric1axistitle = "";

    this.rows = [];
  }

  setSeries(series) {
    this.series = series;
  }

  setViewWindow(viewWindow) {
    this.viewwindow = viewWindow;
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
          type="${this.type}"
      ></google-chart>
    `;
  }
}
customElements.define("data-chart", DataChart);