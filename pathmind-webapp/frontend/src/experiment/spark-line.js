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
                    height: 2rem;
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
            </style>
            <svg width="100" height="24" stroke-width="2"></svg>
        `;
    }

    constructor() {
        super();
    }

    ready() {
        super.ready();
    }

    setSparkLine(dataPoints, variableIndex) {
        const svgElement = this.shadowRoot.querySelector("svg");
        svgElement.classList.add(`sparkline-${variableIndex % 10}`);
        sparkline(svgElement, calibrateScale(dataPoints));

        function calibrateScale(originalDataPoints) {
            const smallestNum = originalDataPoints.reduce((a, b) => Math.min(a, b));
            return originalDataPoints.map(dataPoint => dataPoint - smallestNum);
        }
    }
}

customElements.define(SparkLine.is, SparkLine);
