import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import { sparkline } from "@fnando/sparkline";

class SparkLine extends PolymerElement {
    static get is() {
        return "spark-line";
    }

    static get template() {
        return html`
            <style>
                :host {
                    display: flex;
                    align-items: center;
                    height: 2rem;
                    font-size: var(--lumo-font-size-xs);
                    flex: 1 0 2rem;
                    margin-left: var(--lumo-space-s);
                }
                svg {
                    --sparkline-color: 255, 255, 255;
                    stroke: rgb(var(--sparkline-color));
                    fill: rgba(var(--sparkline-color), 0.3);
                }
                .sparkline-0 {
                    --sparkline-color: 103, 234, 147;
                }
                .sparkline-1 {
                    --sparkline-color: 33, 78, 150;
                }
                .sparkline-2 {
                    --sparkline-color: 155, 247, 236;
                }
                .sparkline-3 {
                    --sparkline-color: 117, 80, 229;
                }
                .sparkline-4 {
                    --sparkline-color: 176, 247, 140;
                }
                .sparkline-5 {
                    --sparkline-color: 239, 153, 164;
                }
                .sparkline-6 {
                    --sparkline-color: 155, 192, 247;
                }
                .sparkline-7 {
                    --sparkline-color: 147, 25, 1;
                }
                .sparkline-8 {
                    --sparkline-color: 244, 19, 188;
                }
                .sparkline-9 {
                    --sparkline-color: 209, 177, 18;
                }
                .tooltip {
                    position: fixed;
                    line-height: 1.2;
                    background-color: rgba(247, 247, 247, .85);
                    padding: var(--lumo-space-xxs);
                    border: 1px solid var(--pm-text-color);
                    z-index: 1;
                }
                .label {
                    font-weight: bold;
                }
            </style>
            <svg width="100" height="24" stroke-width="2"></svg>
            <div class="tooltip" hidden="true">
                <!-- <div class="iteration">
                    <span class="label">Iteration</span> <span class="data">[[iteration]]</span>
                </div> -->
                <div class="value">
                    <span class="label">Mean Value</span> <span class="data">[[value]]</span>
                </div>
                <!-- <div class="episodes-count">
                    <span class="label">Episode Count</span> <span class="data">[[episodeCount]]</span>
                </div> -->
            </div>
        `;
    }

    constructor() {
        super();
    }

    static get properties() {
        return {
            smallestNum: {
                type: Number,
                value: 0,
            },
            iteration: {
                type: Number,
                value: 0,
            },
            value: {
                type: Number,
                value: 0,
            },
            episodeCount: {
                type: Number,
                value: 0,
            }
        }
    }

    ready() {
        super.ready();
    }

    setSparkLine(dataPoints, variableIndex) {
        const options = {
            onmousemove: (event, datapoint) => {
              const tooltip = this.shadowRoot.querySelector(".tooltip");
              this.value = (this.smallestNum + datapoint.value).toFixed(2);
              tooltip.hidden = false;
              tooltip.style.top = `${event.clientY}px`;
              tooltip.style.left = `${event.clientX + 20}px`;
            },
            onmouseout: () => {
              const tooltip = this.shadowRoot.querySelector(".tooltip");
              tooltip.hidden = true;
            }
        };
        const svgElement = this.shadowRoot.querySelector("svg");
        svgElement.classList.add(`sparkline-${variableIndex % 10}`);
        sparkline(svgElement, this.calibrateScale(dataPoints), options);
    }

    calibrateScale(originalDataPoints) {
        this.smallestNum = originalDataPoints.reduce((a, b) => Math.min(a, b));
        return originalDataPoints.map(dataPoint => dataPoint - this.smallestNum);
    }
}

customElements.define(SparkLine.is, SparkLine);
