import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class ChartPlaceholder extends PolymerElement {
    constructor() {
        super();
    }

    static get is() {
        return "chart-placeholder";
    }

    static get template() {
        return html`
            <style>
                :host {
                    display: block;
                    width: 100%;
                    overflow: hidden;
                }

                :host([hidden]) {
                    display: none !important;
                }

                .highcharts-container {
                    position: relative;
                    overflow: hidden;
                    width: 100%;
                    height: 100%;
                    text-align: left;
                    line-height: normal;
                    z-index: 0;

                    -webkit-tap-highlight-color: transparent;
                    font-family: "Lucida Grande", "Lucida Sans Unicode", Arial, Helvetica, sans-serif;
                    font-size: 12px;
                }

                .highcharts-root {
                    display: block;
                    width: 100%;
                    height: 100%;
                }

                .highcharts-root text {
                    stroke-width: 0;
                }

                .highcharts-strong {
                    font-weight: bold;
                }

                .highcharts-emphasized {
                    font-style: italic;
                }

                .highcharts-anchor {
                    cursor: pointer;
                }

                .highcharts-background {
                    fill: none;
                }

                .highcharts-plot-border,
                .highcharts-plot-background {
                    fill: none;
                }

                .highcharts-label-box {
                    fill: none;
                }

                .highcharts-button-box {
                    fill: inherit;
                }

                .highcharts-tracker-line {
                    stroke-linejoin: round;
                    stroke: rgba(192, 192, 192, 0.0001);
                    stroke-width: 22;
                    fill: none;
                }

                .highcharts-tracker-area {
                    fill: rgba(192, 192, 192, 0.0001);
                    stroke-width: 0;
                }

                .highcharts-title {
                    fill: #333333;
                    font-size: 1.5em;
                }

                .highcharts-subtitle {
                    fill: #666666;
                }

                .highcharts-axis-line {
                    fill: none;
                    stroke: #ccd6eb;
                }

                .highcharts-yaxis .highcharts-axis-line {
                    stroke-width: 0;
                }

                .highcharts-axis-title {
                    fill: #666666;
                }

                .highcharts-axis-labels {
                    fill: #666666;
                    cursor: default;
                    font-size: 0.9em;
                }

                .highcharts-grid-line {
                    fill: none;
                    stroke: #e6e6e6;
                }

                .highcharts-xaxis-grid .highcharts-grid-line {
                    stroke-width: 0px;
                }

                .highcharts-tick {
                    stroke: #ccd6eb;
                }

                .highcharts-yaxis .highcharts-tick {
                    stroke-width: 0;
                }

                .highcharts-minor-grid-line {
                    stroke: #f2f2f2;
                }

                .highcharts-crosshair-thin {
                    stroke-width: 1px;
                    stroke: #cccccc;
                }

                .highcharts-crosshair-category {
                    stroke: #ccd6eb;
                    stroke-opacity: 0.25;
                }

                .highcharts-credits {
                    cursor: pointer;
                    fill: #999999;
                    font-size: 0.7em;
                    transition: fill 250ms, font-size 250ms;
                }

                .highcharts-credits:hover {
                    fill: black;
                    font-size: 1em;
                }

                .highcharts-tooltip {
                    cursor: default;
                    pointer-events: none;
                    white-space: nowrap;
                    transition: stroke 150ms;
                }

                .highcharts-tooltip text {
                    fill: #333333;
                }

                .highcharts-tooltip .highcharts-header {
                    font-size: 0.85em;
                }

                .highcharts-tooltip-box {
                    stroke-width: 1px;
                    fill: #f7f7f7;
                    fill-opacity: 0.85;
                }

                .highcharts-tooltip-box .highcharts-label-box {
                    fill: #f7f7f7;
                    fill-opacity: 0.85;
                }

                .highcharts-selection-marker {
                    fill: #335cad;
                    fill-opacity: 0.25;
                }

                .highcharts-graph {
                    fill: none;
                    stroke-width: 2px;
                    stroke-linecap: round;
                    stroke-linejoin: round;
                }

                .highcharts-state-hover .highcharts-graph {
                    stroke-width: 3;
                }

                .highcharts-state-hover path {
                    transition: stroke-width 50;
                }

                .highcharts-state-normal path {
                    transition: stroke-width 250ms;
                }

                g.highcharts-series,
                .highcharts-point,
                .highcharts-markers,
                .highcharts-data-labels {
                    transition: opacity 250ms;
                }

                .highcharts-legend-series-active g.highcharts-series:not(.highcharts-series-hover),
                .highcharts-legend-point-active .highcharts-point:not(.highcharts-point-hover),
                .highcharts-legend-series-active .highcharts-markers:not(.highcharts-series-hover),
                .highcharts-legend-series-active .highcharts-data-labels:not(.highcharts-series-hover) {
                    opacity: 0.2;
                }

                .highcharts-color-0 {
                    fill: var(--vaadin-charts-color-0, #7cb5ec);
                    stroke: var(--vaadin-charts-color-0, #7cb5ec);
                }

                .highcharts-color-1 {
                    fill: var(--vaadin-charts-color-1, #434348);
                    stroke: var(--vaadin-charts-color-1, #434348);
                }

                .highcharts-color-2 {
                    fill: var(--vaadin-charts-color-2, #90ed7d);
                    stroke: var(--vaadin-charts-color-2, #90ed7d);
                }

                .highcharts-color-3 {
                    fill: var(--vaadin-charts-color-3, #f7a35c);
                    stroke: var(--vaadin-charts-color-3, #f7a35c);
                }

                .highcharts-color-4 {
                    fill: var(--vaadin-charts-color-4, #8085e9);
                    stroke: var(--vaadin-charts-color-4, #8085e9);
                }

                .highcharts-color-5 {
                    fill: var(--vaadin-charts-color-5, #f15c80);
                    stroke: var(--vaadin-charts-color-5, #f15c80);
                }

                .highcharts-color-6 {
                    fill: var(--vaadin-charts-color-6, #e4d354);
                    stroke: var(--vaadin-charts-color-6, #e4d354);
                }

                .highcharts-color-7 {
                    fill: var(--vaadin-charts-color-7, #2b908f);
                    stroke: var(--vaadin-charts-color-7, #2b908f);
                }

                .highcharts-color-8 {
                    fill: var(--vaadin-charts-color-8, #f45b5b);
                    stroke: var(--vaadin-charts-color-8, #f45b5b);
                }

                .highcharts-color-9 {
                    fill: var(--vaadin-charts-color-9, #91e8e1);
                    stroke: var(--vaadin-charts-color-9, #91e8e1);
                }

                .highcharts-area {
                    fill-opacity: 0.75;
                    stroke-width: 0;
                }

                .highcharts-markers {
                    stroke-width: 1px;
                    stroke: #ffffff;
                }

                .highcharts-point {
                    stroke-width: 1px;
                }

                .highcharts-dense-data .highcharts-point {
                    stroke-width: 0;
                }

                .highcharts-data-label {
                    font-size: 0.9em;
                    font-weight: bold;
                }

                .highcharts-data-label-box {
                    fill: none;
                    stroke-width: 0;
                }

                .highcharts-data-label text,
                text.highcharts-data-label {
                    fill: #333333;
                }

                .highcharts-data-label-connector {
                    fill: none;
                }

                .highcharts-halo {
                    fill-opacity: 0.25;
                    stroke-width: 0;
                }

                .highcharts-series:not(.highcharts-pie-series) .highcharts-point-select {
                    fill: #cccccc;
                    stroke: #000000;
                }

                .highcharts-column-series rect.highcharts-point {
                    stroke: #ffffff;
                }

                .highcharts-column-series .highcharts-point {
                    transition: fill-opacity 250ms;
                }

                .highcharts-column-series .highcharts-point-hover {
                    fill-opacity: 0.75;
                    transition: fill-opacity 50ms;
                }

                .highcharts-pie-series .highcharts-point {
                    stroke-linejoin: round;
                    stroke: #ffffff;
                }

                .highcharts-pie-series .highcharts-point-hover {
                    fill-opacity: 0.75;
                    transition: fill-opacity 50ms;
                }

                .highcharts-funnel-series .highcharts-point {
                    stroke-linejoin: round;
                    stroke: #ffffff;
                }

                .highcharts-funnel-series .highcharts-point-hover {
                    fill-opacity: 0.75;
                    transition: fill-opacity 50ms;
                }

                .highcharts-funnel-series .highcharts-point-select {
                    fill: inherit;
                    stroke: inherit;
                }

                .highcharts-pyramid-series .highcharts-point {
                    stroke-linejoin: round;
                    stroke: #ffffff;
                }

                .highcharts-pyramid-series .highcharts-point-hover {
                    fill-opacity: 0.75;
                    transition: fill-opacity 50ms;
                }

                .highcharts-pyramid-series .highcharts-point-select {
                    fill: inherit;
                    stroke: inherit;
                }

                .highcharts-solidgauge-series .highcharts-point {
                    stroke-width: 0;
                }

                .highcharts-treemap-series .highcharts-point {
                    stroke-width: 1px;
                    stroke: #e6e6e6;
                    transition: stroke 250ms, fill 250ms, fill-opacity 250ms;
                }

                .highcharts-treemap-series .highcharts-point-hover {
                    stroke: #999999;
                    transition: stroke 25ms, fill 25ms, fill-opacity 25ms;
                }

                .highcharts-treemap-series .highcharts-above-level {
                    display: none;
                }

                .highcharts-treemap-series .highcharts-internal-node {
                    fill: none;
                }

                .highcharts-treemap-series .highcharts-internal-node-interactive {
                    fill-opacity: 0.15;
                    cursor: pointer;
                }

                .highcharts-treemap-series .highcharts-internal-node-interactive:hover {
                    fill-opacity: 0.75;
                }

                .highcharts-legend-box {
                    fill: none;
                    stroke-width: 0;
                }

                .highcharts-legend-item text {
                    fill: #333333;
                    font-weight: bold;
                    font-size: 1em;
                    cursor: pointer;
                    stroke-width: 0;
                }

                .highcharts-legend-item:hover text {
                    fill: #000000;
                }

                .highcharts-legend-item-hidden * {
                    fill: #cccccc !important;
                    stroke: #cccccc !important;
                    transition: fill 250ms;
                }

                .highcharts-legend-nav-active {
                    fill: #003399;
                    cursor: pointer;
                }

                .highcharts-legend-nav-inactive {
                    fill: #cccccc;
                }

                .highcharts-legend-title-box {
                    fill: none;
                    stroke-width: 0;
                }

                .highcharts-loading {
                    position: absolute;
                    background-color: #ffffff;
                    opacity: 0.5;
                    text-align: center;
                    z-index: 10;
                    transition: opacity 250ms;
                }

                .highcharts-loading-hidden {
                    height: 0 !important;
                    opacity: 0;
                    overflow: hidden;
                    transition: opacity 250ms, height 250ms step-end;
                }

                .highcharts-loading-inner {
                    font-weight: bold;
                    position: relative;
                    top: 45%;
                }

                .highcharts-plot-band,
                .highcharts-pane {
                    fill: #000000;
                    fill-opacity: 0.05;
                }

                .highcharts-plot-line {
                    fill: none;
                    stroke: #999999;
                    stroke-width: 1px;
                }

                .highcharts-boxplot-box {
                    fill: #ffffff;
                }

                .highcharts-boxplot-median {
                    stroke-width: 2px;
                }

                .highcharts-bubble-series .highcharts-point {
                    fill-opacity: 0.5;
                }

                .highcharts-errorbar-series .highcharts-point {
                    stroke: #000000;
                }

                .highcharts-gauge-series .highcharts-data-label-box {
                    stroke: #cccccc;
                    stroke-width: 1px;
                }

                .highcharts-gauge-series .highcharts-dial {
                    fill: #000000;
                    stroke-width: 0;
                }

                .highcharts-polygon-series .highcharts-graph {
                    fill: inherit;
                    stroke-width: 0;
                }

                .highcharts-waterfall-series .highcharts-graph {
                    stroke: #333333;
                    stroke-dasharray: 1, 3;
                }

                .highcharts-sankey-series .highcharts-point {
                    stroke-width: 0;
                }

                .highcharts-sankey-series .highcharts-link {
                    transition: fill 250ms, fill-opacity 250ms;
                    fill-opacity: 0.5;
                }

                .highcharts-sankey-series .highcharts-point-hover.highcharts-link {
                    transition: fill 50ms, fill-opacity 50ms;
                    fill-opacity: 1;
                }

                .highcharts-navigator-mask-outside {
                    fill-opacity: 0;
                }

                .highcharts-navigator-mask-inside {
                    fill: #6685c2;

                    fill-opacity: 0.25;
                    cursor: ew-resize;
                }

                .highcharts-navigator-outline {
                    stroke: #cccccc;
                    fill: none;
                }

                .highcharts-navigator-handle {
                    stroke: #cccccc;
                    fill: #f2f2f2;
                    cursor: ew-resize;
                }

                .highcharts-navigator-series {
                    fill: #335cad;
                    stroke: #335cad;
                }

                .highcharts-navigator-series .highcharts-graph {
                    stroke-width: 1px;
                }

                .highcharts-navigator-series .highcharts-area {
                    fill-opacity: 0.05;
                }

                .highcharts-navigator-xaxis .highcharts-axis-line {
                    stroke-width: 0;
                }

                .highcharts-navigator-xaxis .highcharts-grid-line {
                    stroke-width: 1px;
                    stroke: #e6e6e6;
                }

                .highcharts-navigator-xaxis.highcharts-axis-labels {
                    fill: #999999;
                }

                .highcharts-navigator-yaxis .highcharts-grid-line {
                    stroke-width: 0;
                }

                .highcharts-scrollbar-thumb {
                    fill: #cccccc;
                    stroke: #cccccc;
                    stroke-width: 1px;
                }

                .highcharts-scrollbar-button {
                    fill: #e6e6e6;
                    stroke: #cccccc;
                    stroke-width: 1px;
                }

                .highcharts-scrollbar-arrow {
                    fill: #666666;
                }

                .highcharts-scrollbar-rifles {
                    stroke: #666666;
                    stroke-width: 1px;
                }

                .highcharts-scrollbar-track {
                    fill: #f2f2f2;
                    stroke: #f2f2f2;
                    stroke-width: 1px;
                }

                .highcharts-button {
                    fill: #f7f7f7;
                    stroke: #cccccc;
                    cursor: default;
                    stroke-width: 1px;
                    transition: fill 250ms;
                }

                .highcharts-button text {
                    fill: #333333;
                }

                .highcharts-button-hover {
                    transition: fill 0ms;
                    fill: #e6e6e6;
                    stroke: #cccccc;
                }

                .highcharts-button-hover text {
                    fill: #333333;
                }

                .highcharts-button-pressed {
                    font-weight: bold;
                    fill: #e6ebf5;
                    stroke: #cccccc;
                }

                .highcharts-button-pressed text {
                    fill: #333333;
                    font-weight: bold;
                }

                .highcharts-button-disabled text {
                    fill: #333333;
                }

                .highcharts-range-selector-buttons .highcharts-button {
                    stroke-width: 0px;
                }

                .highcharts-range-label rect {
                    fill: none;
                }

                .highcharts-range-label text {
                    fill: #666666;
                }

                .highcharts-range-input rect {
                    fill: none;
                }

                .highcharts-range-input text {
                    fill: #333333;
                }

                .highcharts-range-input {
                    stroke-width: 1px;
                    stroke: #cccccc;
                }

                input.highcharts-range-selector {
                    position: absolute;
                    border: 0;
                    width: 1px;

                    height: 1px;
                    padding: 0;
                    text-align: center;
                    left: -9em;
                }

                .highcharts-crosshair-label text {
                    fill: #ffffff;
                    font-size: 1.1em;
                }

                .highcharts-crosshair-label .highcharts-label-box {
                    fill: inherit;
                }

                .highcharts-candlestick-series .highcharts-point {
                    stroke: #000000;
                    stroke-width: 1px;
                }

                .highcharts-candlestick-series .highcharts-point-up {
                    fill: #ffffff;
                }

                .highcharts-ohlc-series .highcharts-point-hover {
                    stroke-width: 3px;
                }

                .highcharts-flags-series .highcharts-point .highcharts-label-box {
                    stroke: #999999;
                    fill: #ffffff;
                    transition: fill 250ms;
                }

                .highcharts-flags-series .highcharts-point-hover .highcharts-label-box {
                    stroke: #000000;
                    fill: #ccd6eb;
                }

                .highcharts-flags-series .highcharts-point text {
                    fill: #000000;
                    font-size: 0.9em;
                    font-weight: bold;
                }

                .highcharts-map-series .highcharts-point {
                    transition: fill 500ms, fill-opacity 500ms, stroke-width 250ms;
                    stroke: #cccccc;
                }

                .highcharts-map-series .highcharts-point-hover {
                    transition: fill 0ms, fill-opacity 0ms;
                    fill-opacity: 0.5;
                    stroke-width: 2px;
                }

                .highcharts-mapline-series .highcharts-point {
                    fill: none;
                }

                .highcharts-heatmap-series .highcharts-point {
                    stroke-width: 0;
                }

                .highcharts-map-navigation {
                    font-size: 1.3em;
                    font-weight: bold;
                    text-align: center;
                }

                .highcharts-coloraxis {
                    stroke-width: 0;
                }

                .highcharts-coloraxis-marker {
                    fill: #999999;
                }

                .highcharts-null-point {
                    fill: #f7f7f7;
                }

                .highcharts-3d-frame {
                    fill: transparent;
                }

                .highcharts-contextbutton {
                    fill: #ffffff;

                    stroke: none;
                    stroke-linecap: round;
                }

                .highcharts-contextbutton:hover {
                    fill: #e6e6e6;
                    stroke: #e6e6e6;
                }

                .highcharts-button-symbol {
                    stroke: #666666;
                    stroke-width: 3px;
                }

                .highcharts-menu {
                    border: 1px solid #999999;
                    background: #ffffff;
                    padding: 5px 0;
                    box-shadow: 3px 3px 10px #888;
                }

                .highcharts-menu-item {
                    padding: 0.5em 1em;
                    background: none;
                    color: #333333;
                    cursor: pointer;
                    transition: background 250ms, color 250ms;
                }

                .highcharts-menu-item:hover {
                    background: #335cad;
                    color: #ffffff;
                }

                .highcharts-drilldown-point {
                    cursor: pointer;
                }

                .highcharts-drilldown-data-label text,
                text.highcharts-drilldown-data-label,
                .highcharts-drilldown-axis-label {
                    cursor: pointer;
                    fill: #003399;
                    font-weight: bold;
                    text-decoration: underline;
                }

                .highcharts-no-data text {
                    font-weight: bold;
                    font-size: 12px;
                    fill: #666666;
                }

                .highcharts-axis-resizer {
                    cursor: ns-resize;
                    stroke: black;
                    stroke-width: 2px;
                }

                .highcharts-bullet-target {
                    stroke-width: 0;
                }

                .highcharts-lineargauge-target {
                    stroke-width: 1px;
                    stroke: #333333;
                }

                .highcharts-lineargauge-target-line {
                    stroke-width: 1px;
                    stroke: #333333;
                }

                .highcharts-annotation-label-box {
                    stroke-width: 1px;
                    stroke: #000000;
                    fill: #000000;
                    fill-opacity: 0.75;
                }

                .highcharts-annotation-label text {
                    fill: #e6e6e6;
                }

                .highcharts-grid-line {
                    stroke-dasharray: 4 4;
                }

                .pm-active-series,
                .pm-passive-series {
                    opacity: 0.2;
                }

                .highcharts-tooltip-box {
                    stroke: var(--pm-text-color);
                }
            </style>
            <div id="highcharts-i2gfoa5-119" dir="ltr" class="highcharts-container">
                <svg
                    version="1.1"
                    class="highcharts-root"
                    xmlns="http://www.w3.org/2000/svg"
                    width="753"
                    height="470"
                    viewBox="0 0 753 470"
                >
                    <defs>
                        <filter id="highcharts-darker">
                            <feComponentTransfer>
                                <feFuncR type="linear" slope="0.6"></feFuncR>
                                <feFuncG type="linear" slope="0.6"></feFuncG>
                                <feFuncB type="linear" slope="0.6"></feFuncB>
                            </feComponentTransfer>
                        </filter>
                        <filter id="highcharts-brighter">
                            <feComponentTransfer>
                                <feFuncR type="linear" slope="1.4"></feFuncR>
                                <feFuncG type="linear" slope="1.4"></feFuncG>
                                <feFuncB type="linear" slope="1.4"></feFuncB>
                            </feComponentTransfer>
                        </filter>
                        <style>
                            .highcharts-3d-top {
                                filter: url(#highcharts-brighter);
                            }
                            .highcharts-3d-side {
                                filter: url(#highcharts-darker);
                            }
                        </style>
                        <clipPath id="highcharts-i2gfoa5-120">
                            <rect x="0" y="0" width="659" height="397"></rect>
                        </clipPath>
                        <clipPath id="highcharts-i2gfoa5-122">
                            <rect x="0" y="0" width="569" height="421"></rect>
                        </clipPath>
                        <clipPath id="highcharts-i2gfoa5-123">
                            <rect x="0" y="0" width="860" height="466"></rect>
                        </clipPath>
                        <clipPath id="highcharts-i2gfoa5-125">
                            <rect x="0" y="0" width="582" height="421"></rect>
                        </clipPath>
                        <filter id="drop-shadow-29" opacity="0.5">
                            <feGaussianBlur in="SourceAlpha" stdDeviation="1"></feGaussianBlur>
                            <feOffset dx="1" dy="1"></feOffset>
                            <feComponentTransfer>
                                <feFuncA type="linear" slope="0.3"></feFuncA>
                            </feComponentTransfer>
                            <feMerge>
                                <feMergeNode></feMergeNode>
                                <feMergeNode in="SourceGraphic"></feMergeNode>
                            </feMerge>
                        </filter>
                        <style>
                            .highcharts-tooltip-29 {
                                filter: url(#drop-shadow-29);
                            }
                        </style>
                        <clipPath id="highcharts-i2gfoa5-127">
                            <rect x="0" y="0" width="659" height="397"></rect>
                        </clipPath>
                        <clipPath id="highcharts-i2gfoa5-128">
                            <rect x="0" y="0" width="659" height="397"></rect>
                        </clipPath>
                        <filter id="drop-shadow-29" opacity="0.5">
                            <feGaussianBlur in="SourceAlpha" stdDeviation="1"></feGaussianBlur>
                            <feOffset dx="1" dy="1"></feOffset>
                            <feComponentTransfer>
                                <feFuncA type="linear" slope="0.3"></feFuncA>
                            </feComponentTransfer>
                            <feMerge>
                                <feMergeNode></feMergeNode>
                                <feMergeNode in="SourceGraphic"></feMergeNode>
                            </feMerge>
                        </filter>
                        <style>
                            .highcharts-tooltip-29 {
                                filter: url(#drop-shadow-29);
                            }
                        </style>
                        <clipPath id="highcharts-i2gfoa5-129">
                            <rect x="0" y="0" width="659" height="397"></rect>
                        </clipPath>
                    </defs>
                    <rect class="highcharts-background" x="0.5" y="0.5" width="751" height="468" rx="0" ry="0"></rect>
                    <rect class="highcharts-plot-background" x="84" y="10" width="659" height="397"></rect>
                    <g class="highcharts-pane-group" data-z-index="0"></g>
                    <rect
                        class="highcharts-plot-border"
                        data-z-index="1"
                        x="83.5"
                        y="9.5"
                        width="660"
                        height="398"
                    ></rect>
                    <g class="highcharts-grid highcharts-xaxis-grid" data-z-index="1">
                        <path
                            data-z-index="1"
                            class="highcharts-grid-line"
                            d="M 165.5 10 L 165.5 407"
                            opacity="1"
                        ></path>
                        <path
                            data-z-index="1"
                            class="highcharts-grid-line"
                            d="M 292.5 10 L 292.5 407"
                            opacity="1"
                        ></path>
                        <path
                            data-z-index="1"
                            class="highcharts-grid-line"
                            d="M 419.5 10 L 419.5 407"
                            opacity="1"
                        ></path>
                        <path
                            data-z-index="1"
                            class="highcharts-grid-line"
                            d="M 546.5 10 L 546.5 407"
                            opacity="1"
                        ></path>
                        <path
                            data-z-index="1"
                            class="highcharts-grid-line"
                            d="M 672.5 10 L 672.5 407"
                            opacity="1"
                        ></path>
                    </g>
                    <g class="highcharts-grid highcharts-yaxis-grid" data-z-index="1">
                        <path
                            data-z-index="1"
                            class="highcharts-grid-line"
                            d="M 84 407.5 L 743 407.5"
                            opacity="1"
                        ></path>
                        <path
                            data-z-index="1"
                            class="highcharts-grid-line"
                            d="M 84 350.5 L 743 350.5"
                            opacity="1"
                        ></path>
                        <path
                            data-z-index="1"
                            class="highcharts-grid-line"
                            d="M 84 294.5 L 743 294.5"
                            opacity="1"
                        ></path>
                        <path
                            data-z-index="1"
                            class="highcharts-grid-line"
                            d="M 84 237.5 L 743 237.5"
                            opacity="1"
                        ></path>
                        <path
                            data-z-index="1"
                            class="highcharts-grid-line"
                            d="M 84 180.5 L 743 180.5"
                            opacity="1"
                        ></path>
                        <path
                            data-z-index="1"
                            class="highcharts-grid-line"
                            d="M 84 123.5 L 743 123.5"
                            opacity="1"
                        ></path>
                        <path data-z-index="1" class="highcharts-grid-line" d="M 84 67.5 L 743 67.5" opacity="1"></path>
                        <path data-z-index="1" class="highcharts-grid-line" d="M 84 9.5 L 743 9.5" opacity="1"></path>
                    </g>
                    <g class="highcharts-axis highcharts-xaxis" data-z-index="2">
                        <path class="highcharts-axis-line" d="M 84 407.5 L 743 407.5"></path>
                        <path class="highcharts-tick" d="M 165.5 407 L 165.5 417" opacity="1"></path>
                        <path class="highcharts-tick" d="M 292.5 407 L 292.5 417" opacity="1"></path>
                        <path class="highcharts-tick" d="M 419.5 407 L 419.5 417" opacity="1"></path>
                        <path class="highcharts-tick" d="M 546.5 407 L 546.5 417" opacity="1"></path>
                        <path class="highcharts-tick" d="M 672.5 407 L 672.5 417" opacity="1"></path>
                        <text
                            x="413.5"
                            data-z-index="7"
                            text-anchor="middle"
                            transform="translate(0,0)"
                            class="highcharts-axis-title"
                            y="448.8125"
                        >
                            <tspan>Iterations</tspan>
                        </text>
                    </g>
                    <g class="highcharts-axis highcharts-yaxis" data-z-index="2">
                        <path class="highcharts-axis-line" d="M 84 10 L 84 407"></path>
                        <text
                            x="26.046875"
                            data-z-index="7"
                            text-anchor="middle"
                            transform="translate(0,0) rotate(270 26.046875 208.5)"
                            class="highcharts-axis-title"
                            y="208.5"
                        >
                            <tspan>Mean Reward Score over All Episodes</tspan>
                        </text>
                    </g>
                    <g class="highcharts-series-group" data-z-index="3">
                        <g
                            data-z-index="0.1"
                            class="highcharts-series highcharts-series-0 highcharts-spline-series highcharts-color-0 pm-passive-series"
                            transform="translate(84,10) scale(1 1)"
                            clip-path="url(http://localhost:8080/experiment/32#highcharts-i2gfoa5-129)"
                        >
                            <path
                                d="M 31.797193387159 342.8739839541915 C 31.797193387159 342.8739839541915 39.3981161091886 312.6742152390995 44.465397923875 312.6742152390995 C 49.532679738561804 312.6742152390995 52.06632064590521 312.6742152390995 57.133602460592 312.6742152390995 C 62.2008842752788 312.6742152390995 64.7345251826222 312.6742152390995 69.801806997309 312.6742152390995 C 74.8690888119954 312.6742152390995 77.40272971933861 312.6742152390995 82.470011534025 312.6742152390995 C 87.53729334871181 312.6742152390995 90.0709342560552 312.6742152390995 95.138216070742 312.6742152390995 C 100.2054978854292 312.6742152390995 102.73913879277279 300.31594233056546 107.80642060746 295.906844663682 C 112.873702422148 291.4977469967979 115.40734332949201 290.6287269046807 120.47462514418 290.6287269046807 C 125.541906958864 290.6287269046807 128.075547866206 290.6287269046807 133.14282968089 290.6287269046807 C 138.210111495578 290.6287269046807 140.743752402922 290.6287269046807 145.81103421761 290.6287269046807 C 150.87831603229802 290.6287269046807 153.411956939642 290.6287269046807 158.47923875433 290.6287269046807 C 163.546520569014 290.6287269046807 166.08016147635598 290.6287269046807 171.14744329104 290.6287269046807 C 176.21472510572798 290.6287269046807 178.74836601307197 276.2124465966409 183.81564782776 270.8519672281549 C 188.88292964244798 265.4914878596689 191.416570549792 263.82633006225075 196.48385236448 263.82633006225075 C 201.551134179164 263.82633006225075 204.084775086506 263.82633006225075 209.15205690119 263.82633006225075 C 214.219338715878 263.82633006225075 216.75297962322202 263.82633006225075 221.82026143791 263.82633006225075 C 226.88754325259802 263.82633006225075 229.42118415994202 263.82633006225075 234.48846597463 263.82633006225075 C 239.555747789314 263.82633006225075 242.089388696656 263.82633006225075 247.15667051134 263.5311023075351 C 252.22395232602798 263.23587455281944 254.75759323337198 256.8259380897871 259.82487504806 256.8259380897871 C 264.89215686274804 256.8259380897871 267.425797770092 258.6875673315561 272.49307958478 258.6875673315561 C 277.560361399464 258.6875673315561 280.094002306806 258.6875673315561 285.16128412149 258.6875673315561 C 290.228565936178 258.6875673315561 292.762206843522 258.6875673315561 297.82948865821 258.6875673315561 C 302.89677047289797 258.6875673315561 305.430411380242 258.6875673315561 310.49769319493 258.6875673315561 C 315.56497500961393 258.6875673315561 318.09861591695596 249.48660574139356 323.16589773164 249.48660574139356 C 328.233179546328 249.48660574139356 330.766820453672 254.60888185004075 335.83410226836 254.60888185004075 C 340.90138408304404 254.60888185004075 343.43502499038607 248.49407155406985 348.50230680507 248.49407155406985 C 353.569588619758 248.49407155406985 356.10322952710203 248.49407155406985 361.17051134179 248.49407155406985 C 366.237793156478 248.49407155406985 368.771434063822 248.49407155406985 373.83871587851 248.49407155406985 C 378.9059976931941 248.49407155406985 381.439638600536 248.49407155406985 386.50692041522 248.49407155406985 C 391.574202229908 248.49407155406985 394.10784313725196 237.15839543626007 399.17512495194 237.15839543626007 C 404.24240676662805 237.15839543626007 406.776047673972 241.12643674705356 411.84332948866 241.12643674705356 C 416.910611303344 241.12643674705356 419.44425221068593 240.61894012695308 424.51153402537 240.61894012695308 C 429.578815840058 240.61894012695308 432.112456747402 240.61894012695308 437.17973856209 240.61894012695308 C 442.247020376778 240.61894012695308 444.780661284122 240.61894012695308 449.84794309881 240.61894012695308 C 454.91522491349394 240.61894012695308 457.448865820836 240.61894012695308 462.51614763552 240.61894012695308 C 467.583429450208 240.61894012695308 475.18435217224 240.1289892321848 475.18435217224 240.1289892321848"
                                class="highcharts-graph"
                                data-z-index="1"
                            ></path>
                            <path
                                d="M 21.797193387159 342.8739839541915 L 31.797193387159 342.8739839541915 C 31.797193387159 342.8739839541915 39.3981161091886 312.6742152390995 44.465397923875 312.6742152390995 C 49.532679738561804 312.6742152390995 52.06632064590521 312.6742152390995 57.133602460592 312.6742152390995 C 62.2008842752788 312.6742152390995 64.7345251826222 312.6742152390995 69.801806997309 312.6742152390995 C 74.8690888119954 312.6742152390995 77.40272971933861 312.6742152390995 82.470011534025 312.6742152390995 C 87.53729334871181 312.6742152390995 90.0709342560552 312.6742152390995 95.138216070742 312.6742152390995 C 100.2054978854292 312.6742152390995 102.73913879277279 300.31594233056546 107.80642060746 295.906844663682 C 112.873702422148 291.4977469967979 115.40734332949201 290.6287269046807 120.47462514418 290.6287269046807 C 125.541906958864 290.6287269046807 128.075547866206 290.6287269046807 133.14282968089 290.6287269046807 C 138.210111495578 290.6287269046807 140.743752402922 290.6287269046807 145.81103421761 290.6287269046807 C 150.87831603229802 290.6287269046807 153.411956939642 290.6287269046807 158.47923875433 290.6287269046807 C 163.546520569014 290.6287269046807 166.08016147635598 290.6287269046807 171.14744329104 290.6287269046807 C 176.21472510572798 290.6287269046807 178.74836601307197 276.2124465966409 183.81564782776 270.8519672281549 C 188.88292964244798 265.4914878596689 191.416570549792 263.82633006225075 196.48385236448 263.82633006225075 C 201.551134179164 263.82633006225075 204.084775086506 263.82633006225075 209.15205690119 263.82633006225075 C 214.219338715878 263.82633006225075 216.75297962322202 263.82633006225075 221.82026143791 263.82633006225075 C 226.88754325259802 263.82633006225075 229.42118415994202 263.82633006225075 234.48846597463 263.82633006225075 C 239.555747789314 263.82633006225075 242.089388696656 263.82633006225075 247.15667051134 263.5311023075351 C 252.22395232602798 263.23587455281944 254.75759323337198 256.8259380897871 259.82487504806 256.8259380897871 C 264.89215686274804 256.8259380897871 267.425797770092 258.6875673315561 272.49307958478 258.6875673315561 C 277.560361399464 258.6875673315561 280.094002306806 258.6875673315561 285.16128412149 258.6875673315561 C 290.228565936178 258.6875673315561 292.762206843522 258.6875673315561 297.82948865821 258.6875673315561 C 302.89677047289797 258.6875673315561 305.430411380242 258.6875673315561 310.49769319493 258.6875673315561 C 315.56497500961393 258.6875673315561 318.09861591695596 249.48660574139356 323.16589773164 249.48660574139356 C 328.233179546328 249.48660574139356 330.766820453672 254.60888185004075 335.83410226836 254.60888185004075 C 340.90138408304404 254.60888185004075 343.43502499038607 248.49407155406985 348.50230680507 248.49407155406985 C 353.569588619758 248.49407155406985 356.10322952710203 248.49407155406985 361.17051134179 248.49407155406985 C 366.237793156478 248.49407155406985 368.771434063822 248.49407155406985 373.83871587851 248.49407155406985 C 378.9059976931941 248.49407155406985 381.439638600536 248.49407155406985 386.50692041522 248.49407155406985 C 391.574202229908 248.49407155406985 394.10784313725196 237.15839543626007 399.17512495194 237.15839543626007 C 404.24240676662805 237.15839543626007 406.776047673972 241.12643674705356 411.84332948866 241.12643674705356 C 416.910611303344 241.12643674705356 419.44425221068593 240.61894012695308 424.51153402537 240.61894012695308 C 429.578815840058 240.61894012695308 432.112456747402 240.61894012695308 437.17973856209 240.61894012695308 C 442.247020376778 240.61894012695308 444.780661284122 240.61894012695308 449.84794309881 240.61894012695308 C 454.91522491349394 240.61894012695308 457.448865820836 240.61894012695308 462.51614763552 240.61894012695308 C 467.583429450208 240.61894012695308 475.18435217224 240.1289892321848 475.18435217224 240.1289892321848 L 485.18435217224 240.1289892321848"
                                visibility="visible"
                                data-z-index="2"
                                class="highcharts-tracker-line"
                            ></path>
                        </g>
                        <g
                            data-z-index="0.1"
                            class="highcharts-markers highcharts-series-0 highcharts-spline-series highcharts-color-0 pm-passive-series highcharts-tracker"
                            transform="translate(84,10) scale(1 1)"
                            clip-path="none"
                        ></g>
                        <g
                            data-z-index="0.1"
                            class="highcharts-series highcharts-series-1 highcharts-spline-series highcharts-color-1 pm-passive-series"
                            transform="translate(84,10) scale(1 1)"
                            clip-path="url(http://localhost:8080/experiment/32#highcharts-i2gfoa5-129)"
                        >
                            <path
                                d="M 31.797193387159 172.33443712372144 C 31.797193387159 172.33443712372144 39.3981161091886 187.1476112099454 44.465397923875 187.1476112099454 C 49.532679738561804 187.1476112099454 52.06632064590521 187.1476112099454 57.133602460592 187.1476112099454 C 62.2008842752788 187.1476112099454 64.7345251826222 187.1476112099454 69.801806997309 187.1476112099454 C 74.8690888119954 187.1476112099454 77.40272971933861 187.1476112099454 82.470011534025 187.1476112099454 C 87.53729334871181 187.1476112099454 90.0709342560552 187.1476112099454 95.138216070742 187.1476112099454 C 100.2054978854292 187.1476112099454 102.73913879277279 159.03618516060837 107.80642060746 159.03618516060837 C 112.873702422148 159.03618516060837 115.40734332949201 175.50112580205547 120.47462514418 175.50112580205547 C 125.541906958864 175.50112580205547 128.075547866206 175.50112580205547 133.14282968089 175.50112580205547 C 138.210111495578 175.50112580205547 140.743752402922 175.50112580205547 145.81103421761 175.50112580205547 C 150.87831603229802 175.50112580205547 153.411956939642 175.50112580205547 158.47923875433 175.50112580205547 C 163.546520569014 175.50112580205547 166.08016147635598 187.60804470740777 171.14744329104 187.60804470740777 C 176.21472510572798 187.60804470740777 178.74836601307197 184.00126461591023 183.81564782776 184.00126461591023 C 188.88292964244798 184.00126461591023 191.416570549792 189.80654765410972 196.48385236448 189.80654765410972 C 201.551134179164 189.80654765410972 204.084775086506 189.80654765410972 209.15205690119 189.80654765410972 C 214.219338715878 189.80654765410972 216.75297962322202 189.80654765410972 221.82026143791 189.80654765410972 C 226.88754325259802 189.80654765410972 229.42118415994202 189.80654765410972 234.48846597463 189.80654765410972 C 239.555747789314 189.80654765410972 242.089388696656 196.63253325533938 247.15667051134 196.63253325533938 C 252.22395232602798 196.63253325533938 254.75759323337198 196.63253325533938 259.82487504806 196.63253325533938 C 264.89215686274804 196.63253325533938 267.425797770092 196.63253325533938 272.49307958478 196.63253325533938 C 277.560361399464 196.63253325533938 280.094002306806 202.441438308411 285.16128412149 202.441438308411 C 290.228565936178 202.441438308411 292.762206843522 202.441438308411 297.82948865821 202.441438308411 C 302.89677047289797 202.441438308411 305.430411380242 202.441438308411 310.49769319493 202.441438308411 C 315.56497500961393 202.441438308411 318.09861591695596 196.11323124965986 323.16589773164 196.11323124965986 C 328.233179546328 196.11323124965986 330.766820453672 196.11323124965986 335.83410226836 196.11323124965986 C 340.90138408304404 196.11323124965986 348.50230680507 196.11323124965986 348.50230680507 196.11323124965986"
                                class="highcharts-graph"
                                data-z-index="1"
                            ></path>
                            <path
                                d="M 21.797193387159 172.33443712372144 L 31.797193387159 172.33443712372144 C 31.797193387159 172.33443712372144 39.3981161091886 187.1476112099454 44.465397923875 187.1476112099454 C 49.532679738561804 187.1476112099454 52.06632064590521 187.1476112099454 57.133602460592 187.1476112099454 C 62.2008842752788 187.1476112099454 64.7345251826222 187.1476112099454 69.801806997309 187.1476112099454 C 74.8690888119954 187.1476112099454 77.40272971933861 187.1476112099454 82.470011534025 187.1476112099454 C 87.53729334871181 187.1476112099454 90.0709342560552 187.1476112099454 95.138216070742 187.1476112099454 C 100.2054978854292 187.1476112099454 102.73913879277279 159.03618516060837 107.80642060746 159.03618516060837 C 112.873702422148 159.03618516060837 115.40734332949201 175.50112580205547 120.47462514418 175.50112580205547 C 125.541906958864 175.50112580205547 128.075547866206 175.50112580205547 133.14282968089 175.50112580205547 C 138.210111495578 175.50112580205547 140.743752402922 175.50112580205547 145.81103421761 175.50112580205547 C 150.87831603229802 175.50112580205547 153.411956939642 175.50112580205547 158.47923875433 175.50112580205547 C 163.546520569014 175.50112580205547 166.08016147635598 187.60804470740777 171.14744329104 187.60804470740777 C 176.21472510572798 187.60804470740777 178.74836601307197 184.00126461591023 183.81564782776 184.00126461591023 C 188.88292964244798 184.00126461591023 191.416570549792 189.80654765410972 196.48385236448 189.80654765410972 C 201.551134179164 189.80654765410972 204.084775086506 189.80654765410972 209.15205690119 189.80654765410972 C 214.219338715878 189.80654765410972 216.75297962322202 189.80654765410972 221.82026143791 189.80654765410972 C 226.88754325259802 189.80654765410972 229.42118415994202 189.80654765410972 234.48846597463 189.80654765410972 C 239.555747789314 189.80654765410972 242.089388696656 196.63253325533938 247.15667051134 196.63253325533938 C 252.22395232602798 196.63253325533938 254.75759323337198 196.63253325533938 259.82487504806 196.63253325533938 C 264.89215686274804 196.63253325533938 267.425797770092 196.63253325533938 272.49307958478 196.63253325533938 C 277.560361399464 196.63253325533938 280.094002306806 202.441438308411 285.16128412149 202.441438308411 C 290.228565936178 202.441438308411 292.762206843522 202.441438308411 297.82948865821 202.441438308411 C 302.89677047289797 202.441438308411 305.430411380242 202.441438308411 310.49769319493 202.441438308411 C 315.56497500961393 202.441438308411 318.09861591695596 196.11323124965986 323.16589773164 196.11323124965986 C 328.233179546328 196.11323124965986 330.766820453672 196.11323124965986 335.83410226836 196.11323124965986 C 340.90138408304404 196.11323124965986 348.50230680507 196.11323124965986 348.50230680507 196.11323124965986 L 358.50230680507 196.11323124965986"
                                visibility="visible"
                                data-z-index="2"
                                class="highcharts-tracker-line"
                            ></path>
                        </g>
                        <g
                            data-z-index="0.1"
                            class="highcharts-markers highcharts-series-1 highcharts-spline-series highcharts-color-1 pm-passive-series highcharts-tracker"
                            transform="translate(84,10) scale(1 1)"
                            clip-path="none"
                        ></g>
                        <g
                            data-z-index="0.1"
                            class="highcharts-series highcharts-series-2 highcharts-spline-series highcharts-color-2 pm-passive-series"
                            transform="translate(84,10) scale(1 1)"
                            clip-path="url(http://localhost:8080/experiment/32#highcharts-i2gfoa5-129)"
                        >
                            <path
                                d="M 6.4607843137255 279.4405871683624 C 6.4607843137255 279.4405871683624 14.061707035755399 279.4405871683624 19.128988850442 279.4405871683624 C 24.1962706651288 279.4405871683624 26.7299115724722 279.4405871683624 31.797193387159 279.4405871683624 C 36.8644752018454 279.4405871683624 39.3981161091886 279.4405871683624 44.465397923875 279.4405871683624 C 49.532679738561804 279.4405871683624 52.06632064590521 213.8894223406834 57.133602460592 213.8894223406834 C 62.2008842752788 213.8894223406834 64.7345251826222 223.1111038621392 69.801806997309 223.1111038621392 C 74.8690888119954 223.1111038621392 77.40272971933861 223.1111038621392 82.470011534025 223.1111038621392 C 87.53729334871181 223.1111038621392 90.0709342560552 223.1111038621392 95.138216070742 223.1111038621392 C 100.2054978854292 223.1111038621392 102.73913879277279 246.86162290230095 107.80642060746 246.86162290230095 C 112.873702422148 246.86162290230095 115.40734332949201 246.86162290230095 120.47462514418 246.86162290230095 C 125.541906958864 246.86162290230095 128.075547866206 246.86162290230095 133.14282968089 246.86162290230095 C 138.210111495578 246.86162290230095 140.743752402922 262.5992430427028 145.81103421761 262.5992430427028 C 150.87831603229802 262.5992430427028 153.411956939642 262.5992430427028 158.47923875433 262.5992430427028 C 163.546520569014 262.5992430427028 166.08016147635598 276.53503416432284 171.14744329104 276.53503416432284 C 176.21472510572798 276.53503416432284 178.74836601307197 259.69358553144014 183.81564782776 251.27112876215983 C 188.88292964244798 242.84867199287962 191.416570549792 234.42275031792158 196.48385236448 234.42275031792158 C 201.551134179164 234.42275031792158 204.084775086506 234.42275031792158 209.15205690119 234.42275031792158 C 214.219338715878 234.42275031792158 216.75297962322202 234.42275031792158 221.82026143791 234.42275031792158 C 226.88754325259802 234.42275031792158 229.42118415994202 225.10023384879875 234.48846597463 225.10023384879875 C 239.555747789314 225.10023384879875 242.089388696656 225.10023384879875 247.15667051134 225.10023384879875 C 252.22395232602798 225.10023384879875 254.75759323337198 225.10023384879875 259.82487504806 225.10023384879875 C 264.89215686274804 225.10023384879875 267.425797770092 225.10023384879875 272.49307958478 225.10023384879875 C 277.560361399464 225.10023384879875 280.094002306806 187.08285428201998 285.16128412149 187.08285428201998 C 290.228565936178 187.08285428201998 292.762206843522 187.08285428201998 297.82948865821 187.08285428201998 C 302.89677047289797 187.08285428201998 305.430411380242 187.08285428201998 310.49769319493 187.08285428201998 C 315.56497500961393 187.08285428201998 318.09861591695596 180.61375250998043 323.16589773164 180.61375250998043 C 328.233179546328 180.61375250998043 330.766820453672 180.61375250998043 335.83410226836 180.61375250998043 C 340.90138408304404 180.61375250998043 343.43502499038607 180.61375250998043 348.50230680507 180.61375250998043 C 353.569588619758 180.61375250998043 356.10322952710203 180.61375250998043 361.17051134179 180.61375250998043 C 366.237793156478 180.61375250998043 368.771434063822 178.17958159281127 373.83871587851 175.83905473852982 C 378.9059976931941 173.49852788425017 381.439638600536 170.9172620104732 386.50692041522 168.91111823857767 C 391.574202229908 166.90497446668059 394.10784313725196 167.01607225682042 399.17512495194 165.80833587904831 C 404.24240676662805 164.60059950127618 406.776047673972 162.8724363497171 411.84332948866 162.8724363497171 C 416.910611303344 162.8724363497171 419.44425221068593 162.8724363497171 424.51153402537 162.8724363497171 C 429.578815840058 162.8724363497171 432.112456747402 162.8724363497171 437.17973856209 162.8724363497171 C 442.247020376778 162.8724363497171 444.780661284122 162.8724363497171 449.84794309881 162.8724363497171 C 454.91522491349394 162.8724363497171 457.448865820836 155.5643015928205 462.51614763552 155.5643015928205 C 467.583429450208 155.5643015928205 470.117070357552 155.5643015928205 475.18435217224 155.5643015928205 C 480.251633986928 155.5643015928205 482.785274894272 155.5643015928205 487.85255670896 155.5643015928205 C 492.91983852364393 155.5643015928205 495.45347943098596 152.3453357021468 500.52076124567 147.16583484111044 C 505.588043060358 141.98633398006996 508.12168396770204 129.66679728762836 513.18896578239 129.66679728762836 C 518.2562475970781 129.66679728762836 520.789888504422 129.66679728762836 525.85717031911 129.66679728762836 C 530.9244521337939 129.66679728762836 533.458093041136 129.66679728762836 538.52537485582 129.66679728762836 C 543.5926566705081 129.66679728762836 546.126297577852 120.43270814556138 551.19357939254 120.43270814556138 C 556.2608612072279 120.43270814556138 558.794502114572 120.43270814556138 563.86178392926 120.43270814556138 C 568.929065743944 120.43270814556138 571.462706651286 120.43270814556138 576.52998846597 120.43270814556138 C 581.597270280658 120.43270814556138 584.1309111880021 120.43270814556138 589.19819300269 120.43270814556138 C 594.2654748173779 120.43270814556138 596.7991157247219 117.39250245542547 601.86639753941 115.59651658290142 C 606.933679354094 113.80053071037877 609.467320261436 111.45277878294468 614.53460207612 111.45277878294468 C 619.601883890808 111.45277878294468 622.1355247981521 111.45277878294468 627.20280661284 111.45277878294468 C 632.270088427528 111.45277878294468 639.87101114956 103.90622504315911 639.87101114956 103.90622504315911"
                                class="highcharts-graph"
                                data-z-index="1"
                            ></path>
                            <path
                                d="M -3.5392156862745 279.4405871683624 L 6.4607843137255 279.4405871683624 C 6.4607843137255 279.4405871683624 14.061707035755399 279.4405871683624 19.128988850442 279.4405871683624 C 24.1962706651288 279.4405871683624 26.7299115724722 279.4405871683624 31.797193387159 279.4405871683624 C 36.8644752018454 279.4405871683624 39.3981161091886 279.4405871683624 44.465397923875 279.4405871683624 C 49.532679738561804 279.4405871683624 52.06632064590521 213.8894223406834 57.133602460592 213.8894223406834 C 62.2008842752788 213.8894223406834 64.7345251826222 223.1111038621392 69.801806997309 223.1111038621392 C 74.8690888119954 223.1111038621392 77.40272971933861 223.1111038621392 82.470011534025 223.1111038621392 C 87.53729334871181 223.1111038621392 90.0709342560552 223.1111038621392 95.138216070742 223.1111038621392 C 100.2054978854292 223.1111038621392 102.73913879277279 246.86162290230095 107.80642060746 246.86162290230095 C 112.873702422148 246.86162290230095 115.40734332949201 246.86162290230095 120.47462514418 246.86162290230095 C 125.541906958864 246.86162290230095 128.075547866206 246.86162290230095 133.14282968089 246.86162290230095 C 138.210111495578 246.86162290230095 140.743752402922 262.5992430427028 145.81103421761 262.5992430427028 C 150.87831603229802 262.5992430427028 153.411956939642 262.5992430427028 158.47923875433 262.5992430427028 C 163.546520569014 262.5992430427028 166.08016147635598 276.53503416432284 171.14744329104 276.53503416432284 C 176.21472510572798 276.53503416432284 178.74836601307197 259.69358553144014 183.81564782776 251.27112876215983 C 188.88292964244798 242.84867199287962 191.416570549792 234.42275031792158 196.48385236448 234.42275031792158 C 201.551134179164 234.42275031792158 204.084775086506 234.42275031792158 209.15205690119 234.42275031792158 C 214.219338715878 234.42275031792158 216.75297962322202 234.42275031792158 221.82026143791 234.42275031792158 C 226.88754325259802 234.42275031792158 229.42118415994202 225.10023384879875 234.48846597463 225.10023384879875 C 239.555747789314 225.10023384879875 242.089388696656 225.10023384879875 247.15667051134 225.10023384879875 C 252.22395232602798 225.10023384879875 254.75759323337198 225.10023384879875 259.82487504806 225.10023384879875 C 264.89215686274804 225.10023384879875 267.425797770092 225.10023384879875 272.49307958478 225.10023384879875 C 277.560361399464 225.10023384879875 280.094002306806 187.08285428201998 285.16128412149 187.08285428201998 C 290.228565936178 187.08285428201998 292.762206843522 187.08285428201998 297.82948865821 187.08285428201998 C 302.89677047289797 187.08285428201998 305.430411380242 187.08285428201998 310.49769319493 187.08285428201998 C 315.56497500961393 187.08285428201998 318.09861591695596 180.61375250998043 323.16589773164 180.61375250998043 C 328.233179546328 180.61375250998043 330.766820453672 180.61375250998043 335.83410226836 180.61375250998043 C 340.90138408304404 180.61375250998043 343.43502499038607 180.61375250998043 348.50230680507 180.61375250998043 C 353.569588619758 180.61375250998043 356.10322952710203 180.61375250998043 361.17051134179 180.61375250998043 C 366.237793156478 180.61375250998043 368.771434063822 178.17958159281127 373.83871587851 175.83905473852982 C 378.9059976931941 173.49852788425017 381.439638600536 170.9172620104732 386.50692041522 168.91111823857767 C 391.574202229908 166.90497446668059 394.10784313725196 167.01607225682042 399.17512495194 165.80833587904831 C 404.24240676662805 164.60059950127618 406.776047673972 162.8724363497171 411.84332948866 162.8724363497171 C 416.910611303344 162.8724363497171 419.44425221068593 162.8724363497171 424.51153402537 162.8724363497171 C 429.578815840058 162.8724363497171 432.112456747402 162.8724363497171 437.17973856209 162.8724363497171 C 442.247020376778 162.8724363497171 444.780661284122 162.8724363497171 449.84794309881 162.8724363497171 C 454.91522491349394 162.8724363497171 457.448865820836 155.5643015928205 462.51614763552 155.5643015928205 C 467.583429450208 155.5643015928205 470.117070357552 155.5643015928205 475.18435217224 155.5643015928205 C 480.251633986928 155.5643015928205 482.785274894272 155.5643015928205 487.85255670896 155.5643015928205 C 492.91983852364393 155.5643015928205 495.45347943098596 152.3453357021468 500.52076124567 147.16583484111044 C 505.588043060358 141.98633398006996 508.12168396770204 129.66679728762836 513.18896578239 129.66679728762836 C 518.2562475970781 129.66679728762836 520.789888504422 129.66679728762836 525.85717031911 129.66679728762836 C 530.9244521337939 129.66679728762836 533.458093041136 129.66679728762836 538.52537485582 129.66679728762836 C 543.5926566705081 129.66679728762836 546.126297577852 120.43270814556138 551.19357939254 120.43270814556138 C 556.2608612072279 120.43270814556138 558.794502114572 120.43270814556138 563.86178392926 120.43270814556138 C 568.929065743944 120.43270814556138 571.462706651286 120.43270814556138 576.52998846597 120.43270814556138 C 581.597270280658 120.43270814556138 584.1309111880021 120.43270814556138 589.19819300269 120.43270814556138 C 594.2654748173779 120.43270814556138 596.7991157247219 117.39250245542547 601.86639753941 115.59651658290142 C 606.933679354094 113.80053071037877 609.467320261436 111.45277878294468 614.53460207612 111.45277878294468 C 619.601883890808 111.45277878294468 622.1355247981521 111.45277878294468 627.20280661284 111.45277878294468 C 632.270088427528 111.45277878294468 639.87101114956 103.90622504315911 639.87101114956 103.90622504315911 L 649.87101114956 103.90622504315911"
                                visibility="visible"
                                data-z-index="2"
                                class="highcharts-tracker-line"
                            ></path>
                        </g>
                        <g
                            data-z-index="0.1"
                            class="highcharts-markers highcharts-series-2 highcharts-spline-series highcharts-color-2 pm-passive-series highcharts-tracker"
                            transform="translate(84,10) scale(1 1)"
                            clip-path="none"
                        ></g>
                        <g
                            data-z-index="0.1"
                            class="highcharts-series highcharts-series-3 highcharts-spline-series highcharts-color-3 pm-passive-series"
                            transform="translate(84,10) scale(1 1)"
                            clip-path="url(http://localhost:8080/experiment/32#highcharts-i2gfoa5-129)"
                        >
                            <path
                                d="M 31.797193387159 281.5858398812587 C 31.797193387159 281.5858398812587 39.3981161091886 245.1255402020852 44.465397923875 245.1255402020852 C 49.532679738561804 245.1255402020852 52.06632064590521 245.1255402020852 57.133602460592 245.1255402020852 C 62.2008842752788 245.1255402020852 64.7345251826222 245.1255402020852 69.801806997309 245.1255402020852 C 74.8690888119954 245.1255402020852 77.40272971933861 245.1255402020852 82.470011534025 245.1255402020852 C 87.53729334871181 245.1255402020852 90.0709342560552 245.1255402020852 95.138216070742 245.1255402020852 C 100.2054978854292 245.1255402020852 102.73913879277279 245.1255402020852 107.80642060746 243.47910814321762 C 112.873702422148 241.83267608435003 115.40734332949201 233.47105879058685 120.47462514418 233.47105879058685 C 125.541906958864 233.47105879058685 128.075547866206 233.47105879058685 133.14282968089 233.47105879058685 C 138.210111495578 233.47105879058685 140.743752402922 233.47105879058685 145.81103421761 233.47105879058685 C 150.87831603229802 233.47105879058685 153.411956939642 233.47105879058685 158.47923875433 233.47105879058685 C 163.546520569014 233.47105879058685 166.08016147635598 233.47105879058685 171.14744329104 233.47105879058685 C 176.21472510572798 233.47105879058685 178.74836601307197 212.3571243247647 183.81564782776 212.3571243247647 C 188.88292964244798 212.3571243247647 191.416570549792 212.3571243247647 196.48385236448 212.3571243247647 C 201.551134179164 212.3571243247647 204.084775086506 196.00386585515415 209.15205690119 196.00386585515415 C 214.219338715878 196.00386585515415 216.75297962322202 196.00386585515415 221.82026143791 196.00386585515415 C 226.88754325259802 196.00386585515415 229.42118415994202 196.00386585515415 234.48846597463 196.00386585515415 C 239.555747789314 196.00386585515415 242.089388696656 194.12057076350231 247.15667051134 194.12057076350231 C 252.22395232602798 194.12057076350231 254.75759323337198 194.12057076350231 259.82487504806 194.12057076350231 C 264.89215686274804 194.12057076350231 267.425797770092 194.12057076350231 272.49307958478 194.12057076350231 C 277.560361399464 194.12057076350231 280.094002306806 175.62244137665476 285.16128412149 175.62244137665476 C 290.228565936178 175.62244137665476 292.762206843522 175.62244137665476 297.82948865821 175.62244137665476 C 302.89677047289797 175.62244137665476 305.430411380242 175.62244137665476 310.49769319493 175.62244137665476 C 315.56497500961393 175.62244137665476 318.09861591695596 149.66745415397907 323.16589773164 149.66745415397907 C 328.233179546328 149.66745415397907 330.766820453672 149.66745415397907 335.83410226836 149.66745415397907 C 340.90138408304404 149.66745415397907 343.43502499038607 149.66745415397907 348.50230680507 149.66745415397907 C 353.569588619758 149.66745415397907 356.10322952710203 135.28462120350247 361.17051134179 135.28462120350247 C 366.237793156478 135.28462120350247 368.771434063822 135.28462120350247 373.83871587851 135.28462120350247 C 378.9059976931941 135.28462120350247 381.439638600536 131.52580367621493 386.50692041522 130.10479919146889 C 391.574202229908 128.6837947067217 394.10784313725196 128.17959877976938 399.17512495194 128.17959877976938 C 404.24240676662805 128.17959877976938 406.776047673972 128.17959877976938 411.84332948866 128.17959877976938 C 416.910611303344 128.17959877976938 419.44425221068593 128.17959877976938 424.51153402537 128.17959877976938 C 429.578815840058 128.17959877976938 432.112456747402 109.02346912306052 437.17973856209 109.02346912306052 C 442.247020376778 109.02346912306052 444.780661284122 109.02346912306052 449.84794309881 109.02346912306052 C 454.91522491349394 109.02346912306052 457.448865820836 97.48455733940108 462.51614763552 97.48455733940108 C 467.583429450208 97.48455733940108 470.117070357552 97.48455733940108 475.18435217224 97.48455733940108 C 480.251633986928 97.48455733940108 482.785274894272 97.48455733940108 487.85255670896 97.48455733940108 C 492.91983852364393 97.48455733940108 495.45347943098596 97.48455733940108 500.52076124567 97.48455733940108 C 505.588043060358 97.48455733940108 508.12168396770204 97.48455733940108 513.18896578239 97.48455733940108 C 518.2562475970781 97.48455733940108 520.789888504422 83.20458720077816 525.85717031911 77.00592829829611 C 530.9244521337939 70.80726939581892 533.458093041136 66.49126282700297 538.52537485582 66.49126282700297 C 543.5926566705081 66.49126282700297 551.19357939254 66.49126282700297 551.19357939254 66.49126282700297"
                                class="highcharts-graph"
                                data-z-index="1"
                            ></path>
                            <path
                                d="M 21.797193387159 281.5858398812587 L 31.797193387159 281.5858398812587 C 31.797193387159 281.5858398812587 39.3981161091886 245.1255402020852 44.465397923875 245.1255402020852 C 49.532679738561804 245.1255402020852 52.06632064590521 245.1255402020852 57.133602460592 245.1255402020852 C 62.2008842752788 245.1255402020852 64.7345251826222 245.1255402020852 69.801806997309 245.1255402020852 C 74.8690888119954 245.1255402020852 77.40272971933861 245.1255402020852 82.470011534025 245.1255402020852 C 87.53729334871181 245.1255402020852 90.0709342560552 245.1255402020852 95.138216070742 245.1255402020852 C 100.2054978854292 245.1255402020852 102.73913879277279 245.1255402020852 107.80642060746 243.47910814321762 C 112.873702422148 241.83267608435003 115.40734332949201 233.47105879058685 120.47462514418 233.47105879058685 C 125.541906958864 233.47105879058685 128.075547866206 233.47105879058685 133.14282968089 233.47105879058685 C 138.210111495578 233.47105879058685 140.743752402922 233.47105879058685 145.81103421761 233.47105879058685 C 150.87831603229802 233.47105879058685 153.411956939642 233.47105879058685 158.47923875433 233.47105879058685 C 163.546520569014 233.47105879058685 166.08016147635598 233.47105879058685 171.14744329104 233.47105879058685 C 176.21472510572798 233.47105879058685 178.74836601307197 212.3571243247647 183.81564782776 212.3571243247647 C 188.88292964244798 212.3571243247647 191.416570549792 212.3571243247647 196.48385236448 212.3571243247647 C 201.551134179164 212.3571243247647 204.084775086506 196.00386585515415 209.15205690119 196.00386585515415 C 214.219338715878 196.00386585515415 216.75297962322202 196.00386585515415 221.82026143791 196.00386585515415 C 226.88754325259802 196.00386585515415 229.42118415994202 196.00386585515415 234.48846597463 196.00386585515415 C 239.555747789314 196.00386585515415 242.089388696656 194.12057076350231 247.15667051134 194.12057076350231 C 252.22395232602798 194.12057076350231 254.75759323337198 194.12057076350231 259.82487504806 194.12057076350231 C 264.89215686274804 194.12057076350231 267.425797770092 194.12057076350231 272.49307958478 194.12057076350231 C 277.560361399464 194.12057076350231 280.094002306806 175.62244137665476 285.16128412149 175.62244137665476 C 290.228565936178 175.62244137665476 292.762206843522 175.62244137665476 297.82948865821 175.62244137665476 C 302.89677047289797 175.62244137665476 305.430411380242 175.62244137665476 310.49769319493 175.62244137665476 C 315.56497500961393 175.62244137665476 318.09861591695596 149.66745415397907 323.16589773164 149.66745415397907 C 328.233179546328 149.66745415397907 330.766820453672 149.66745415397907 335.83410226836 149.66745415397907 C 340.90138408304404 149.66745415397907 343.43502499038607 149.66745415397907 348.50230680507 149.66745415397907 C 353.569588619758 149.66745415397907 356.10322952710203 135.28462120350247 361.17051134179 135.28462120350247 C 366.237793156478 135.28462120350247 368.771434063822 135.28462120350247 373.83871587851 135.28462120350247 C 378.9059976931941 135.28462120350247 381.439638600536 131.52580367621493 386.50692041522 130.10479919146889 C 391.574202229908 128.6837947067217 394.10784313725196 128.17959877976938 399.17512495194 128.17959877976938 C 404.24240676662805 128.17959877976938 406.776047673972 128.17959877976938 411.84332948866 128.17959877976938 C 416.910611303344 128.17959877976938 419.44425221068593 128.17959877976938 424.51153402537 128.17959877976938 C 429.578815840058 128.17959877976938 432.112456747402 109.02346912306052 437.17973856209 109.02346912306052 C 442.247020376778 109.02346912306052 444.780661284122 109.02346912306052 449.84794309881 109.02346912306052 C 454.91522491349394 109.02346912306052 457.448865820836 97.48455733940108 462.51614763552 97.48455733940108 C 467.583429450208 97.48455733940108 470.117070357552 97.48455733940108 475.18435217224 97.48455733940108 C 480.251633986928 97.48455733940108 482.785274894272 97.48455733940108 487.85255670896 97.48455733940108 C 492.91983852364393 97.48455733940108 495.45347943098596 97.48455733940108 500.52076124567 97.48455733940108 C 505.588043060358 97.48455733940108 508.12168396770204 97.48455733940108 513.18896578239 97.48455733940108 C 518.2562475970781 97.48455733940108 520.789888504422 83.20458720077816 525.85717031911 77.00592829829611 C 530.9244521337939 70.80726939581892 533.458093041136 66.49126282700297 538.52537485582 66.49126282700297 C 543.5926566705081 66.49126282700297 551.19357939254 66.49126282700297 551.19357939254 66.49126282700297 L 561.19357939254 66.49126282700297"
                                visibility="visible"
                                data-z-index="2"
                                class="highcharts-tracker-line"
                            ></path>
                        </g>
                        <g
                            data-z-index="0.1"
                            class="highcharts-markers highcharts-series-3 highcharts-spline-series highcharts-color-3 pm-passive-series highcharts-tracker"
                            transform="translate(84,10) scale(1 1)"
                            clip-path="none"
                        ></g>
                        <g
                            data-z-index="0.1"
                            class="highcharts-series highcharts-series-4 highcharts-spline-series highcharts-color-4 pm-passive-series"
                            transform="translate(84,10) scale(1 1)"
                            clip-path="url(http://localhost:8080/experiment/32#highcharts-i2gfoa5-129)"
                        >
                            <path
                                d="M 31.797193387159 293.12312336176734 C 31.797193387159 293.12312336176734 39.3981161091886 293.12312336176734 44.465397923875 293.12312336176734 C 49.532679738561804 293.12312336176734 52.06632064590521 293.12312336176734 57.133602460592 293.12312336176734 C 62.2008842752788 293.12312336176734 64.7345251826222 293.12312336176734 69.801806997309 293.12312336176734 C 74.8690888119954 293.12312336176734 77.40272971933861 293.12312336176734 82.470011534025 293.12312336176734 C 87.53729334871181 293.12312336176734 90.0709342560552 293.12312336176734 95.138216070742 293.12312336176734 C 100.2054978854292 293.12312336176734 102.73913879277279 271.9389953550001 107.80642060746 271.9389953550001 C 112.873702422148 271.9389953550001 115.40734332949201 271.9389953550001 120.47462514418 271.9389953550001 C 125.541906958864 271.9389953550001 128.075547866206 271.9389953550001 133.14282968089 271.9389953550001 C 138.210111495578 271.9389953550001 140.743752402922 271.9389953550001 145.81103421761 271.9389953550001 C 150.87831603229802 271.9389953550001 153.411956939642 271.9389953550001 158.47923875433 271.9389953550001 C 163.546520569014 271.9389953550001 166.08016147635598 271.9389953550001 171.14744329104 271.9389953550001 C 176.21472510572798 271.9389953550001 178.74836601307197 296.173021664664 183.81564782776 296.173021664664 C 188.88292964244798 296.173021664664 191.416570549792 296.173021664664 196.48385236448 296.173021664664 C 201.551134179164 296.173021664664 204.084775086506 296.173021664664 209.15205690119 296.173021664664 C 214.219338715878 296.173021664664 216.75297962322202 296.173021664664 221.82026143791 296.173021664664 C 226.88754325259802 296.173021664664 229.42118415994202 296.173021664664 234.48846597463 296.173021664664 C 239.555747789314 296.173021664664 242.089388696656 296.173021664664 247.15667051134 296.173021664664 C 252.22395232602798 296.173021664664 254.75759323337198 287.21745774601794 259.82487504806 283.21542504332695 C 264.89215686274804 279.21339234063595 267.425797770092 276.1628581512092 272.49307958478 276.1628581512092 C 277.560361399464 276.1628581512092 280.094002306806 276.1628581512092 285.16128412149 276.1628581512092 C 290.228565936178 276.1628581512092 292.762206843522 276.1628581512092 297.82948865821 276.1628581512092 C 302.89677047289797 276.1628581512092 305.430411380242 276.1628581512092 310.49769319493 276.1628581512092 C 315.56497500961393 276.1628581512092 318.09861591695596 265.2819471646261 323.16589773164 259.2012994921399 C 328.233179546328 253.12065181964894 330.766820453672 247.70531319143 335.83410226836 245.75961978876632 C 340.90138408304404 243.81392638610262 343.43502499038607 243.81392638610262 348.50230680507 243.81392638610262 C 353.569588619758 243.81392638610262 356.10322952710203 243.81392638610262 361.17051134179 243.81392638610262 C 366.237793156478 243.81392638610262 368.771434063822 243.81392638610262 373.83871587851 243.81392638610262 C 378.9059976931941 243.81392638610262 381.439638600536 243.81392638610262 386.50692041522 243.81392638610262 C 391.574202229908 243.81392638610262 394.10784313725196 230.53136098708063 399.17512495194 227.9550278287819 C 404.24240676662805 225.37869467048318 406.776047673972 227.9550278287819 411.84332948866 225.37869467048318 C 416.910611303344 222.80236151218446 419.44425221068593 208.81816520799265 424.51153402537 208.81816520799265 C 429.578815840058 208.81816520799265 432.112456747402 208.81816520799265 437.17973856209 208.81816520799265 C 442.247020376778 208.81816520799265 444.780661284122 208.81816520799265 449.84794309881 208.81816520799265 C 454.91522491349394 208.81816520799265 457.448865820836 208.81816520799265 462.51614763552 208.81816520799265 C 467.583429450208 208.81816520799265 470.117070357552 203.71363509691446 475.18435217224 203.71363509691446 C 480.251633986928 203.71363509691446 482.785274894272 203.71363509691446 487.85255670896 203.71363509691446 C 492.91983852364393 203.71363509691446 500.52076124567 207.23012430810647 500.52076124567 207.23012430810647"
                                class="highcharts-graph"
                                data-z-index="1"
                            ></path>
                            <path
                                d="M 21.797193387159 293.12312336176734 L 31.797193387159 293.12312336176734 C 31.797193387159 293.12312336176734 39.3981161091886 293.12312336176734 44.465397923875 293.12312336176734 C 49.532679738561804 293.12312336176734 52.06632064590521 293.12312336176734 57.133602460592 293.12312336176734 C 62.2008842752788 293.12312336176734 64.7345251826222 293.12312336176734 69.801806997309 293.12312336176734 C 74.8690888119954 293.12312336176734 77.40272971933861 293.12312336176734 82.470011534025 293.12312336176734 C 87.53729334871181 293.12312336176734 90.0709342560552 293.12312336176734 95.138216070742 293.12312336176734 C 100.2054978854292 293.12312336176734 102.73913879277279 271.9389953550001 107.80642060746 271.9389953550001 C 112.873702422148 271.9389953550001 115.40734332949201 271.9389953550001 120.47462514418 271.9389953550001 C 125.541906958864 271.9389953550001 128.075547866206 271.9389953550001 133.14282968089 271.9389953550001 C 138.210111495578 271.9389953550001 140.743752402922 271.9389953550001 145.81103421761 271.9389953550001 C 150.87831603229802 271.9389953550001 153.411956939642 271.9389953550001 158.47923875433 271.9389953550001 C 163.546520569014 271.9389953550001 166.08016147635598 271.9389953550001 171.14744329104 271.9389953550001 C 176.21472510572798 271.9389953550001 178.74836601307197 296.173021664664 183.81564782776 296.173021664664 C 188.88292964244798 296.173021664664 191.416570549792 296.173021664664 196.48385236448 296.173021664664 C 201.551134179164 296.173021664664 204.084775086506 296.173021664664 209.15205690119 296.173021664664 C 214.219338715878 296.173021664664 216.75297962322202 296.173021664664 221.82026143791 296.173021664664 C 226.88754325259802 296.173021664664 229.42118415994202 296.173021664664 234.48846597463 296.173021664664 C 239.555747789314 296.173021664664 242.089388696656 296.173021664664 247.15667051134 296.173021664664 C 252.22395232602798 296.173021664664 254.75759323337198 287.21745774601794 259.82487504806 283.21542504332695 C 264.89215686274804 279.21339234063595 267.425797770092 276.1628581512092 272.49307958478 276.1628581512092 C 277.560361399464 276.1628581512092 280.094002306806 276.1628581512092 285.16128412149 276.1628581512092 C 290.228565936178 276.1628581512092 292.762206843522 276.1628581512092 297.82948865821 276.1628581512092 C 302.89677047289797 276.1628581512092 305.430411380242 276.1628581512092 310.49769319493 276.1628581512092 C 315.56497500961393 276.1628581512092 318.09861591695596 265.2819471646261 323.16589773164 259.2012994921399 C 328.233179546328 253.12065181964894 330.766820453672 247.70531319143 335.83410226836 245.75961978876632 C 340.90138408304404 243.81392638610262 343.43502499038607 243.81392638610262 348.50230680507 243.81392638610262 C 353.569588619758 243.81392638610262 356.10322952710203 243.81392638610262 361.17051134179 243.81392638610262 C 366.237793156478 243.81392638610262 368.771434063822 243.81392638610262 373.83871587851 243.81392638610262 C 378.9059976931941 243.81392638610262 381.439638600536 243.81392638610262 386.50692041522 243.81392638610262 C 391.574202229908 243.81392638610262 394.10784313725196 230.53136098708063 399.17512495194 227.9550278287819 C 404.24240676662805 225.37869467048318 406.776047673972 227.9550278287819 411.84332948866 225.37869467048318 C 416.910611303344 222.80236151218446 419.44425221068593 208.81816520799265 424.51153402537 208.81816520799265 C 429.578815840058 208.81816520799265 432.112456747402 208.81816520799265 437.17973856209 208.81816520799265 C 442.247020376778 208.81816520799265 444.780661284122 208.81816520799265 449.84794309881 208.81816520799265 C 454.91522491349394 208.81816520799265 457.448865820836 208.81816520799265 462.51614763552 208.81816520799265 C 467.583429450208 208.81816520799265 470.117070357552 203.71363509691446 475.18435217224 203.71363509691446 C 480.251633986928 203.71363509691446 482.785274894272 203.71363509691446 487.85255670896 203.71363509691446 C 492.91983852364393 203.71363509691446 500.52076124567 207.23012430810647 500.52076124567 207.23012430810647 L 510.52076124567 207.23012430810647"
                                visibility="visible"
                                data-z-index="2"
                                class="highcharts-tracker-line"
                            ></path>
                        </g>
                        <g
                            data-z-index="0.1"
                            class="highcharts-markers highcharts-series-4 highcharts-spline-series highcharts-color-4 pm-passive-series highcharts-tracker"
                            transform="translate(84,10) scale(1 1)"
                            clip-path="none"
                        ></g>
                        <g
                            data-z-index="0.1"
                            class="highcharts-series highcharts-series-5 highcharts-spline-series highcharts-color-5 pm-passive-series"
                            transform="translate(84,10) scale(1 1)"
                            clip-path="url(http://localhost:8080/experiment/32#highcharts-i2gfoa5-129)"
                        >
                            <path
                                d="M 31.797193387159 326.14896710120263 C 31.797193387159 326.14896710120263 39.3981161091886 307.57973977866584 44.465397923875 307.57973977866584 C 49.532679738561804 307.57973977866584 52.06632064590521 307.57973977866584 57.133602460592 307.57973977866584 C 62.2008842752788 307.57973977866584 64.7345251826222 307.57973977866584 69.801806997309 307.57973977866584 C 74.8690888119954 307.57973977866584 77.40272971933861 307.57973977866584 82.470011534025 307.57973977866584 C 87.53729334871181 307.57973977866584 90.0709342560552 307.57973977866584 95.138216070742 307.57973977866584 C 100.2054978854292 307.57973977866584 102.73913879277279 266.14482306113393 107.80642060746 249.83725648579122 C 112.873702422148 233.52968991044588 115.40734332949201 226.04190690194557 120.47462514418 226.04190690194557 C 125.541906958864 226.04190690194557 128.075547866206 226.04190690194557 133.14282968089 226.04190690194557 C 138.210111495578 226.04190690194557 140.743752402922 226.04190690194557 145.81103421761 226.04190690194557 C 150.87831603229802 226.04190690194557 153.411956939642 226.04190690194557 158.47923875433 226.04190690194557 C 163.546520569014 226.04190690194557 166.08016147635598 226.04190690194557 171.14744329104 226.04190690194557 C 176.21472510572798 226.04190690194557 178.74836601307197 214.11052025784 183.81564782776 202.45799764307625 C 188.88292964244798 190.80547502831257 191.416570549792 167.77929382812698 196.48385236448 167.77929382812698 C 201.551134179164 167.77929382812698 204.084775086506 167.77929382812698 209.15205690119 167.77929382812698 C 214.219338715878 167.77929382812698 216.75297962322202 167.77929382812698 221.82026143791 167.77929382812698 C 226.88754325259802 167.77929382812698 229.42118415994202 167.77929382812698 234.48846597463 167.77929382812698 C 239.555747789314 167.77929382812698 242.089388696656 149.2157392341375 247.15667051134 149.2157392341375 C 252.22395232602798 149.2157392341375 254.75759323337198 149.2157392341375 259.82487504806 149.2157392341375 C 264.89215686274804 149.2157392341375 267.425797770092 149.2157392341375 272.49307958478 149.2157392341375 C 277.560361399464 149.2157392341375 280.094002306806 178.24411292024394 285.16128412149 178.24411292024394 C 290.228565936178 178.24411292024394 292.762206843522 178.24411292024394 297.82948865821 178.24411292024394 C 302.89677047289797 178.24411292024394 305.430411380242 178.24411292024394 310.49769319493 178.24411292024394 C 315.56497500961393 178.24411292024394 318.09861591695596 166.21578510232646 323.16589773164 166.21578510232646 C 328.233179546328 166.21578510232646 330.766820453672 166.21578510232646 335.83410226836 166.21578510232646 C 340.90138408304404 166.21578510232646 343.43502499038607 166.21578510232646 348.50230680507 166.21578510232646 C 353.569588619758 166.21578510232646 356.10322952710203 140.5613245258328 361.17051134179 140.5613245258328 C 366.237793156478 140.5613245258328 368.771434063822 140.5613245258328 373.83871587851 140.5613245258328 C 378.9059976931941 140.5613245258328 381.439638600536 135.80592070142745 386.50692041522 129.9518015244755 C 391.574202229908 124.09768234751897 394.10784313725196 111.29072864106155 399.17512495194 111.29072864106155 C 404.24240676662805 111.29072864106155 406.776047673972 111.29072864106155 411.84332948866 111.29072864106155 C 416.910611303344 111.29072864106155 419.44425221068593 111.29072864106155 424.51153402537 111.29072864106155 C 429.578815840058 111.29072864106155 432.112456747402 104.244731980371 437.17973856209 104.244731980371 C 442.247020376778 104.244731980371 444.780661284122 104.244731980371 449.84794309881 104.244731980371 C 454.91522491349394 104.244731980371 457.448865820836 73.8370485907841 462.51614763552 73.8370485907841 C 467.583429450208 73.8370485907841 470.117070357552 73.8370485907841 475.18435217224 73.8370485907841 C 480.251633986928 73.8370485907841 482.785274894272 73.8370485907841 487.85255670896 73.8370485907841 C 492.91983852364393 73.8370485907841 500.52076124567 73.8370485907841 500.52076124567 73.8370485907841"
                                class="highcharts-graph"
                                data-z-index="1"
                            ></path>
                            <path
                                d="M 21.797193387159 326.14896710120263 L 31.797193387159 326.14896710120263 C 31.797193387159 326.14896710120263 39.3981161091886 307.57973977866584 44.465397923875 307.57973977866584 C 49.532679738561804 307.57973977866584 52.06632064590521 307.57973977866584 57.133602460592 307.57973977866584 C 62.2008842752788 307.57973977866584 64.7345251826222 307.57973977866584 69.801806997309 307.57973977866584 C 74.8690888119954 307.57973977866584 77.40272971933861 307.57973977866584 82.470011534025 307.57973977866584 C 87.53729334871181 307.57973977866584 90.0709342560552 307.57973977866584 95.138216070742 307.57973977866584 C 100.2054978854292 307.57973977866584 102.73913879277279 266.14482306113393 107.80642060746 249.83725648579122 C 112.873702422148 233.52968991044588 115.40734332949201 226.04190690194557 120.47462514418 226.04190690194557 C 125.541906958864 226.04190690194557 128.075547866206 226.04190690194557 133.14282968089 226.04190690194557 C 138.210111495578 226.04190690194557 140.743752402922 226.04190690194557 145.81103421761 226.04190690194557 C 150.87831603229802 226.04190690194557 153.411956939642 226.04190690194557 158.47923875433 226.04190690194557 C 163.546520569014 226.04190690194557 166.08016147635598 226.04190690194557 171.14744329104 226.04190690194557 C 176.21472510572798 226.04190690194557 178.74836601307197 214.11052025784 183.81564782776 202.45799764307625 C 188.88292964244798 190.80547502831257 191.416570549792 167.77929382812698 196.48385236448 167.77929382812698 C 201.551134179164 167.77929382812698 204.084775086506 167.77929382812698 209.15205690119 167.77929382812698 C 214.219338715878 167.77929382812698 216.75297962322202 167.77929382812698 221.82026143791 167.77929382812698 C 226.88754325259802 167.77929382812698 229.42118415994202 167.77929382812698 234.48846597463 167.77929382812698 C 239.555747789314 167.77929382812698 242.089388696656 149.2157392341375 247.15667051134 149.2157392341375 C 252.22395232602798 149.2157392341375 254.75759323337198 149.2157392341375 259.82487504806 149.2157392341375 C 264.89215686274804 149.2157392341375 267.425797770092 149.2157392341375 272.49307958478 149.2157392341375 C 277.560361399464 149.2157392341375 280.094002306806 178.24411292024394 285.16128412149 178.24411292024394 C 290.228565936178 178.24411292024394 292.762206843522 178.24411292024394 297.82948865821 178.24411292024394 C 302.89677047289797 178.24411292024394 305.430411380242 178.24411292024394 310.49769319493 178.24411292024394 C 315.56497500961393 178.24411292024394 318.09861591695596 166.21578510232646 323.16589773164 166.21578510232646 C 328.233179546328 166.21578510232646 330.766820453672 166.21578510232646 335.83410226836 166.21578510232646 C 340.90138408304404 166.21578510232646 343.43502499038607 166.21578510232646 348.50230680507 166.21578510232646 C 353.569588619758 166.21578510232646 356.10322952710203 140.5613245258328 361.17051134179 140.5613245258328 C 366.237793156478 140.5613245258328 368.771434063822 140.5613245258328 373.83871587851 140.5613245258328 C 378.9059976931941 140.5613245258328 381.439638600536 135.80592070142745 386.50692041522 129.9518015244755 C 391.574202229908 124.09768234751897 394.10784313725196 111.29072864106155 399.17512495194 111.29072864106155 C 404.24240676662805 111.29072864106155 406.776047673972 111.29072864106155 411.84332948866 111.29072864106155 C 416.910611303344 111.29072864106155 419.44425221068593 111.29072864106155 424.51153402537 111.29072864106155 C 429.578815840058 111.29072864106155 432.112456747402 104.244731980371 437.17973856209 104.244731980371 C 442.247020376778 104.244731980371 444.780661284122 104.244731980371 449.84794309881 104.244731980371 C 454.91522491349394 104.244731980371 457.448865820836 73.8370485907841 462.51614763552 73.8370485907841 C 467.583429450208 73.8370485907841 470.117070357552 73.8370485907841 475.18435217224 73.8370485907841 C 480.251633986928 73.8370485907841 482.785274894272 73.8370485907841 487.85255670896 73.8370485907841 C 492.91983852364393 73.8370485907841 500.52076124567 73.8370485907841 500.52076124567 73.8370485907841 L 510.52076124567 73.8370485907841"
                                visibility="visible"
                                data-z-index="2"
                                class="highcharts-tracker-line"
                            ></path>
                        </g>
                        <g
                            data-z-index="0.1"
                            class="highcharts-markers highcharts-series-5 highcharts-spline-series highcharts-color-5 pm-passive-series highcharts-tracker"
                            transform="translate(84,10) scale(1 1)"
                            clip-path="none"
                        ></g>
                        <g
                            data-z-index="0.1"
                            class="highcharts-series highcharts-series-6 highcharts-spline-series highcharts-color-6 pm-passive-series"
                            transform="translate(84,10) scale(1 1)"
                            clip-path="url(http://localhost:8080/experiment/32#highcharts-i2gfoa5-129)"
                        >
                            <path
                                d="M 31.797193387159 177.95532989225546 C 31.797193387159 177.95532989225546 39.3981161091886 177.95532989225546 44.465397923875 177.95532989225546 C 49.532679738561804 177.95532989225546 52.06632064590521 177.95532989225546 57.133602460592 177.95532989225546 C 62.2008842752788 177.95532989225546 64.7345251826222 177.95532989225546 69.801806997309 177.95532989225546 C 74.8690888119954 177.95532989225546 77.40272971933861 177.95532989225546 82.470011534025 177.95532989225546 C 87.53729334871181 177.95532989225546 90.0709342560552 177.95532989225546 95.138216070742 177.95532989225546 C 100.2054978854292 177.95532989225546 102.73913879277279 162.08794433454145 107.80642060746 153.06717911421376 C 112.873702422148 144.04641389388468 115.40734332949201 132.85150379061355 120.47462514418 132.85150379061355 C 125.541906958864 132.85150379061355 128.075547866206 132.85150379061355 133.14282968089 132.85150379061355 C 138.210111495578 132.85150379061355 140.743752402922 132.85150379061355 145.81103421761 132.85150379061355 C 150.87831603229802 132.85150379061355 153.411956939642 132.85150379061355 158.47923875433 132.85150379061355 C 163.546520569014 132.85150379061355 166.08016147635598 134.3801863391517 171.14744329104 134.3801863391517 C 176.21472510572798 134.3801863391517 178.74836601307197 128.72141320924072 183.81564782776 128.72141320924072 C 188.88292964244798 128.72141320924072 191.416570549792 129.35024719505634 196.48385236448 129.35024719505634 C 201.551134179164 129.35024719505634 204.084775086506 129.35024719505634 209.15205690119 129.35024719505634 C 214.219338715878 129.35024719505634 216.75297962322202 129.35024719505634 221.82026143791 129.35024719505634 C 226.88754325259802 129.35024719505634 229.42118415994202 129.35024719505634 234.48846597463 129.35024719505634 C 239.555747789314 129.35024719505634 242.089388696656 135.3853382901113 247.15667051134 138.30557334809237 C 252.22395232602798 141.2258084060757 254.75759323337198 143.9514224849674 259.82487504806 143.9514224849674 C 264.89215686274804 143.9514224849674 267.425797770092 132.79181340893695 272.49307958478 132.79181340893695 C 277.560361399464 132.79181340893695 280.094002306806 132.79181340893695 285.16128412149 132.79181340893695 C 290.228565936178 132.79181340893695 292.762206843522 132.79181340893695 297.82948865821 132.79181340893695 C 302.89677047289797 132.79181340893695 305.430411380242 132.79181340893695 310.49769319493 132.79181340893695 C 315.56497500961393 132.79181340893695 318.09861591695596 124.6709549478498 323.16589773164 124.6709549478498 C 328.233179546328 124.6709549478498 330.766820453672 126.65744842345322 335.83410226836 126.65744842345322 C 340.90138408304404 126.65744842345322 343.43502499038607 121.26618249757132 348.50230680507 121.26618249757132 C 353.569588619758 121.26618249757132 356.10322952710203 121.26618249757132 361.17051134179 121.26618249757132 C 366.237793156478 121.26618249757132 368.771434063822 121.26618249757132 373.83871587851 121.26618249757132 C 378.9059976931941 121.26618249757132 381.439638600536 129.28001283392098 386.50692041522 130.1597199456727 C 391.574202229908 131.0394270574244 394.10784313725196 131.0394270574244 399.17512495194 131.0394270574244 C 404.24240676662805 131.0394270574244 406.776047673972 131.0394270574244 411.84332948866 131.0394270574244 C 416.910611303344 131.0394270574244 419.44425221068593 131.0394270574244 424.51153402537 131.0394270574244 C 429.578815840058 131.0394270574244 432.112456747402 138.50709550191584 437.17973856209 138.50709550191584 C 442.247020376778 138.50709550191584 444.780661284122 138.50709550191584 449.84794309881 138.50709550191584 C 454.91522491349394 138.50709550191584 457.448865820836 137.4397428010416 462.51614763552 135.94010008767572 C 467.583429450208 134.44045737430866 470.117070357552 131.00888193508354 475.18435217224 131.00888193508354 C 480.251633986928 131.00888193508354 482.785274894272 131.00888193508354 487.85255670896 131.00888193508354 C 492.91983852364393 131.00888193508354 495.45347943098596 131.00888193508354 500.52076124567 131.00888193508354 C 505.588043060358 131.00888193508354 508.12168396770204 131.1306685253561 513.18896578239 131.1306685253561 C 518.2562475970781 131.1306685253561 520.789888504422 131.1306685253561 525.85717031911 131.1306685253561 C 530.9244521337939 131.1306685253561 533.458093041136 128.6499985952155 538.52537485582 128.6499985952155 C 543.5926566705081 128.6499985952155 546.126297577852 133.67329066753297 551.19357939254 133.67329066753297 C 556.2608612072279 133.67329066753297 558.794502114572 133.67329066753297 563.86178392926 133.67329066753297 C 568.929065743944 133.67329066753297 571.462706651286 133.67329066753297 576.52998846597 133.67329066753297 C 581.597270280658 133.67329066753297 584.1309111880021 133.37847156659905 589.19819300269 133.37847156659905 C 594.2654748173779 133.37847156659905 596.7991157247219 135.88049772694956 601.86639753941 135.88049772694956 C 606.933679354094 135.88049772694956 609.467320261436 135.88049772694956 614.53460207612 135.88049772694956 C 619.601883890808 135.88049772694956 622.1355247981521 136.57132006446312 627.20280661284 136.57132006446312 C 632.270088427528 136.57132006446312 634.803729334872 136.57132006446312 639.87101114956 136.57132006446312 C 644.938292964244 136.57132006446312 652.53921568627 136.57132006446312 652.53921568627 136.57132006446312"
                                class="highcharts-graph"
                                data-z-index="1"
                            ></path>
                            <path
                                d="M 21.797193387159 177.95532989225546 L 31.797193387159 177.95532989225546 C 31.797193387159 177.95532989225546 39.3981161091886 177.95532989225546 44.465397923875 177.95532989225546 C 49.532679738561804 177.95532989225546 52.06632064590521 177.95532989225546 57.133602460592 177.95532989225546 C 62.2008842752788 177.95532989225546 64.7345251826222 177.95532989225546 69.801806997309 177.95532989225546 C 74.8690888119954 177.95532989225546 77.40272971933861 177.95532989225546 82.470011534025 177.95532989225546 C 87.53729334871181 177.95532989225546 90.0709342560552 177.95532989225546 95.138216070742 177.95532989225546 C 100.2054978854292 177.95532989225546 102.73913879277279 162.08794433454145 107.80642060746 153.06717911421376 C 112.873702422148 144.04641389388468 115.40734332949201 132.85150379061355 120.47462514418 132.85150379061355 C 125.541906958864 132.85150379061355 128.075547866206 132.85150379061355 133.14282968089 132.85150379061355 C 138.210111495578 132.85150379061355 140.743752402922 132.85150379061355 145.81103421761 132.85150379061355 C 150.87831603229802 132.85150379061355 153.411956939642 132.85150379061355 158.47923875433 132.85150379061355 C 163.546520569014 132.85150379061355 166.08016147635598 134.3801863391517 171.14744329104 134.3801863391517 C 176.21472510572798 134.3801863391517 178.74836601307197 128.72141320924072 183.81564782776 128.72141320924072 C 188.88292964244798 128.72141320924072 191.416570549792 129.35024719505634 196.48385236448 129.35024719505634 C 201.551134179164 129.35024719505634 204.084775086506 129.35024719505634 209.15205690119 129.35024719505634 C 214.219338715878 129.35024719505634 216.75297962322202 129.35024719505634 221.82026143791 129.35024719505634 C 226.88754325259802 129.35024719505634 229.42118415994202 129.35024719505634 234.48846597463 129.35024719505634 C 239.555747789314 129.35024719505634 242.089388696656 135.3853382901113 247.15667051134 138.30557334809237 C 252.22395232602798 141.2258084060757 254.75759323337198 143.9514224849674 259.82487504806 143.9514224849674 C 264.89215686274804 143.9514224849674 267.425797770092 132.79181340893695 272.49307958478 132.79181340893695 C 277.560361399464 132.79181340893695 280.094002306806 132.79181340893695 285.16128412149 132.79181340893695 C 290.228565936178 132.79181340893695 292.762206843522 132.79181340893695 297.82948865821 132.79181340893695 C 302.89677047289797 132.79181340893695 305.430411380242 132.79181340893695 310.49769319493 132.79181340893695 C 315.56497500961393 132.79181340893695 318.09861591695596 124.6709549478498 323.16589773164 124.6709549478498 C 328.233179546328 124.6709549478498 330.766820453672 126.65744842345322 335.83410226836 126.65744842345322 C 340.90138408304404 126.65744842345322 343.43502499038607 121.26618249757132 348.50230680507 121.26618249757132 C 353.569588619758 121.26618249757132 356.10322952710203 121.26618249757132 361.17051134179 121.26618249757132 C 366.237793156478 121.26618249757132 368.771434063822 121.26618249757132 373.83871587851 121.26618249757132 C 378.9059976931941 121.26618249757132 381.439638600536 129.28001283392098 386.50692041522 130.1597199456727 C 391.574202229908 131.0394270574244 394.10784313725196 131.0394270574244 399.17512495194 131.0394270574244 C 404.24240676662805 131.0394270574244 406.776047673972 131.0394270574244 411.84332948866 131.0394270574244 C 416.910611303344 131.0394270574244 419.44425221068593 131.0394270574244 424.51153402537 131.0394270574244 C 429.578815840058 131.0394270574244 432.112456747402 138.50709550191584 437.17973856209 138.50709550191584 C 442.247020376778 138.50709550191584 444.780661284122 138.50709550191584 449.84794309881 138.50709550191584 C 454.91522491349394 138.50709550191584 457.448865820836 137.4397428010416 462.51614763552 135.94010008767572 C 467.583429450208 134.44045737430866 470.117070357552 131.00888193508354 475.18435217224 131.00888193508354 C 480.251633986928 131.00888193508354 482.785274894272 131.00888193508354 487.85255670896 131.00888193508354 C 492.91983852364393 131.00888193508354 495.45347943098596 131.00888193508354 500.52076124567 131.00888193508354 C 505.588043060358 131.00888193508354 508.12168396770204 131.1306685253561 513.18896578239 131.1306685253561 C 518.2562475970781 131.1306685253561 520.789888504422 131.1306685253561 525.85717031911 131.1306685253561 C 530.9244521337939 131.1306685253561 533.458093041136 128.6499985952155 538.52537485582 128.6499985952155 C 543.5926566705081 128.6499985952155 546.126297577852 133.67329066753297 551.19357939254 133.67329066753297 C 556.2608612072279 133.67329066753297 558.794502114572 133.67329066753297 563.86178392926 133.67329066753297 C 568.929065743944 133.67329066753297 571.462706651286 133.67329066753297 576.52998846597 133.67329066753297 C 581.597270280658 133.67329066753297 584.1309111880021 133.37847156659905 589.19819300269 133.37847156659905 C 594.2654748173779 133.37847156659905 596.7991157247219 135.88049772694956 601.86639753941 135.88049772694956 C 606.933679354094 135.88049772694956 609.467320261436 135.88049772694956 614.53460207612 135.88049772694956 C 619.601883890808 135.88049772694956 622.1355247981521 136.57132006446312 627.20280661284 136.57132006446312 C 632.270088427528 136.57132006446312 634.803729334872 136.57132006446312 639.87101114956 136.57132006446312 C 644.938292964244 136.57132006446312 652.53921568627 136.57132006446312 652.53921568627 136.57132006446312 L 662.53921568627 136.57132006446312"
                                visibility="visible"
                                data-z-index="2"
                                class="highcharts-tracker-line"
                            ></path>
                        </g>
                        <g
                            data-z-index="0.1"
                            class="highcharts-markers highcharts-series-6 highcharts-spline-series highcharts-color-6 pm-passive-series highcharts-tracker"
                            transform="translate(84,10) scale(1 1)"
                            clip-path="none"
                        ></g>
                        <g
                            data-z-index="0.1"
                            class="highcharts-series highcharts-series-7 highcharts-spline-series highcharts-color-7 pm-passive-series highcharts-series-hover"
                            transform="translate(84,10) scale(1 1)"
                            clip-path="url(http://localhost:8080/experiment/32#highcharts-i2gfoa5-129)"
                        >
                            <path
                                d="M 31.797193387159 266.8245860444632 C 31.797193387159 266.8245860444632 39.3981161091886 289.45748642433523 44.465397923875 289.45748642433523 C 49.532679738561804 289.45748642433523 52.06632064590521 289.45748642433523 57.133602460592 289.45748642433523 C 62.2008842752788 289.45748642433523 64.7345251826222 289.45748642433523 69.801806997309 289.45748642433523 C 74.8690888119954 289.45748642433523 77.40272971933861 289.45748642433523 82.470011534025 289.45748642433523 C 87.53729334871181 289.45748642433523 90.0709342560552 289.45748642433523 95.138216070742 289.45748642433523 C 100.2054978854292 289.45748642433523 102.73913879277279 286.4922489969872 107.80642060746 286.4922489969872 C 112.873702422148 286.4922489969872 115.40734332949201 305.7249695202065 120.47462514418 305.7249695202065 C 125.541906958864 305.7249695202065 128.075547866206 305.7249695202065 133.14282968089 305.7249695202065 C 138.210111495578 305.7249695202065 140.743752402922 305.7249695202065 145.81103421761 305.7249695202065 C 150.87831603229802 305.7249695202065 153.411956939642 305.7249695202065 158.47923875433 305.7249695202065 C 163.546520569014 305.7249695202065 166.08016147635598 305.7249695202065 171.14744329104 305.7249695202065 C 176.21472510572798 305.7249695202065 178.74836601307197 300.3867895938573 183.81564782776 300.3867895938573 C 188.88292964244798 300.3867895938573 191.416570549792 300.3867895938573 196.48385236448 300.3867895938573 C 201.551134179164 300.3867895938573 204.084775086506 294.847394439144 209.15205690119 294.847394439144 C 214.219338715878 294.847394439144 216.75297962322202 294.847394439144 221.82026143791 294.847394439144 C 226.88754325259802 294.847394439144 229.42118415994202 294.847394439144 234.48846597463 294.847394439144 C 239.555747789314 294.847394439144 242.089388696656 285.5341760442163 247.15667051134 285.5341760442163 C 252.22395232602798 285.5341760442163 254.75759323337198 285.5341760442163 259.82487504806 285.5341760442163 C 264.89215686274804 285.5341760442163 267.425797770092 285.5341760442163 272.49307958478 285.5341760442163 C 277.560361399464 285.5341760442163 280.094002306806 279.23345111306725 285.16128412149 279.23345111306725 C 290.228565936178 279.23345111306725 292.762206843522 279.23345111306725 297.82948865821 279.23345111306725 C 302.89677047289797 279.23345111306725 305.430411380242 279.23345111306725 310.49769319493 279.23345111306725 C 315.56497500961393 279.23345111306725 318.09861591695596 264.5890432186973 323.16589773164 264.5890432186973 C 328.233179546328 264.5890432186973 330.766820453672 264.5890432186973 335.83410226836 264.5890432186973 C 340.90138408304404 264.5890432186973 343.43502499038607 264.5890432186973 348.50230680507 264.5890432186973 C 353.569588619758 264.5890432186973 356.10322952710203 264.5890432186973 361.17051134179 264.5890432186973 C 366.237793156478 264.5890432186973 368.771434063822 271.32765610251687 373.83871587851 271.32765610251687 C 378.9059976931941 271.32765610251687 381.439638600536 264.51043766301456 386.50692041522 262.9044273810139 C 391.574202229908 261.2984170990132 394.10784313725196 261.2984170990132 399.17512495194 261.2984170990132 C 404.24240676662805 261.2984170990132 406.776047673972 261.2984170990132 411.84332948866 261.2984170990132 C 416.910611303344 261.2984170990132 419.44425221068593 261.2984170990132 424.51153402537 261.2984170990132 C 429.578815840058 261.2984170990132 432.112456747402 261.2984170990132 437.17973856209 261.2984170990132 C 442.247020376778 261.2984170990132 444.780661284122 261.27403569389617 449.84794309881 260.7337018353711 C 454.91522491349394 260.1933679768465 457.448865820836 259.33923753035384 462.51614763552 258.596747806389 C 467.583429450208 257.8542580824235 470.117070357552 257.02125321554524 475.18435217224 257.02125321554524 C 480.251633986928 257.02125321554524 482.785274894272 257.02125321554524 487.85255670896 257.02125321554524 C 492.91983852364393 257.02125321554524 495.45347943098596 257.02125321554524 500.52076124567 257.02125321554524 C 505.588043060358 257.02125321554524 508.12168396770204 257.02125321554524 513.18896578239 257.02125321554524 C 518.2562475970781 257.02125321554524 520.789888504422 256.3144844411401 525.85717031911 254.06889860083004 C 530.9244521337939 251.82331276052173 533.458093041136 245.79332401399924 538.52537485582 245.79332401399924 C 543.5926566705081 245.79332401399924 546.126297577852 245.79332401399924 551.19357939254 245.79332401399924 C 556.2608612072279 245.79332401399924 558.794502114572 245.79332401399924 563.86178392926 245.79332401399924 C 568.929065743944 245.79332401399924 571.462706651286 245.79332401399924 576.52998846597 245.79332401399924 C 581.597270280658 245.79332401399924 584.1309111880021 245.79332401399924 589.19819300269 245.79332401399924 C 594.2654748173779 245.79332401399924 596.7991157247219 245.79332401399924 601.86639753941 244.80079886244906 C 606.933679354094 243.80827371089887 609.467320261436 235.94410947711268 614.53460207612 235.94410947711268 C 619.601883890808 235.94410947711268 622.1355247981521 235.94410947711268 627.20280661284 235.94410947711268 C 632.270088427528 235.94410947711268 634.803729334872 235.94410947711268 639.87101114956 235.94410947711268 C 644.938292964244 235.94410947711268 652.53921568627 235.94410947711268 652.53921568627 235.94410947711268"
                                class="highcharts-graph"
                                data-z-index="1"
                            ></path>
                            <path
                                d="M 21.797193387159 266.8245860444632 L 31.797193387159 266.8245860444632 C 31.797193387159 266.8245860444632 39.3981161091886 289.45748642433523 44.465397923875 289.45748642433523 C 49.532679738561804 289.45748642433523 52.06632064590521 289.45748642433523 57.133602460592 289.45748642433523 C 62.2008842752788 289.45748642433523 64.7345251826222 289.45748642433523 69.801806997309 289.45748642433523 C 74.8690888119954 289.45748642433523 77.40272971933861 289.45748642433523 82.470011534025 289.45748642433523 C 87.53729334871181 289.45748642433523 90.0709342560552 289.45748642433523 95.138216070742 289.45748642433523 C 100.2054978854292 289.45748642433523 102.73913879277279 286.4922489969872 107.80642060746 286.4922489969872 C 112.873702422148 286.4922489969872 115.40734332949201 305.7249695202065 120.47462514418 305.7249695202065 C 125.541906958864 305.7249695202065 128.075547866206 305.7249695202065 133.14282968089 305.7249695202065 C 138.210111495578 305.7249695202065 140.743752402922 305.7249695202065 145.81103421761 305.7249695202065 C 150.87831603229802 305.7249695202065 153.411956939642 305.7249695202065 158.47923875433 305.7249695202065 C 163.546520569014 305.7249695202065 166.08016147635598 305.7249695202065 171.14744329104 305.7249695202065 C 176.21472510572798 305.7249695202065 178.74836601307197 300.3867895938573 183.81564782776 300.3867895938573 C 188.88292964244798 300.3867895938573 191.416570549792 300.3867895938573 196.48385236448 300.3867895938573 C 201.551134179164 300.3867895938573 204.084775086506 294.847394439144 209.15205690119 294.847394439144 C 214.219338715878 294.847394439144 216.75297962322202 294.847394439144 221.82026143791 294.847394439144 C 226.88754325259802 294.847394439144 229.42118415994202 294.847394439144 234.48846597463 294.847394439144 C 239.555747789314 294.847394439144 242.089388696656 285.5341760442163 247.15667051134 285.5341760442163 C 252.22395232602798 285.5341760442163 254.75759323337198 285.5341760442163 259.82487504806 285.5341760442163 C 264.89215686274804 285.5341760442163 267.425797770092 285.5341760442163 272.49307958478 285.5341760442163 C 277.560361399464 285.5341760442163 280.094002306806 279.23345111306725 285.16128412149 279.23345111306725 C 290.228565936178 279.23345111306725 292.762206843522 279.23345111306725 297.82948865821 279.23345111306725 C 302.89677047289797 279.23345111306725 305.430411380242 279.23345111306725 310.49769319493 279.23345111306725 C 315.56497500961393 279.23345111306725 318.09861591695596 264.5890432186973 323.16589773164 264.5890432186973 C 328.233179546328 264.5890432186973 330.766820453672 264.5890432186973 335.83410226836 264.5890432186973 C 340.90138408304404 264.5890432186973 343.43502499038607 264.5890432186973 348.50230680507 264.5890432186973 C 353.569588619758 264.5890432186973 356.10322952710203 264.5890432186973 361.17051134179 264.5890432186973 C 366.237793156478 264.5890432186973 368.771434063822 271.32765610251687 373.83871587851 271.32765610251687 C 378.9059976931941 271.32765610251687 381.439638600536 264.51043766301456 386.50692041522 262.9044273810139 C 391.574202229908 261.2984170990132 394.10784313725196 261.2984170990132 399.17512495194 261.2984170990132 C 404.24240676662805 261.2984170990132 406.776047673972 261.2984170990132 411.84332948866 261.2984170990132 C 416.910611303344 261.2984170990132 419.44425221068593 261.2984170990132 424.51153402537 261.2984170990132 C 429.578815840058 261.2984170990132 432.112456747402 261.2984170990132 437.17973856209 261.2984170990132 C 442.247020376778 261.2984170990132 444.780661284122 261.27403569389617 449.84794309881 260.7337018353711 C 454.91522491349394 260.1933679768465 457.448865820836 259.33923753035384 462.51614763552 258.596747806389 C 467.583429450208 257.8542580824235 470.117070357552 257.02125321554524 475.18435217224 257.02125321554524 C 480.251633986928 257.02125321554524 482.785274894272 257.02125321554524 487.85255670896 257.02125321554524 C 492.91983852364393 257.02125321554524 495.45347943098596 257.02125321554524 500.52076124567 257.02125321554524 C 505.588043060358 257.02125321554524 508.12168396770204 257.02125321554524 513.18896578239 257.02125321554524 C 518.2562475970781 257.02125321554524 520.789888504422 256.3144844411401 525.85717031911 254.06889860083004 C 530.9244521337939 251.82331276052173 533.458093041136 245.79332401399924 538.52537485582 245.79332401399924 C 543.5926566705081 245.79332401399924 546.126297577852 245.79332401399924 551.19357939254 245.79332401399924 C 556.2608612072279 245.79332401399924 558.794502114572 245.79332401399924 563.86178392926 245.79332401399924 C 568.929065743944 245.79332401399924 571.462706651286 245.79332401399924 576.52998846597 245.79332401399924 C 581.597270280658 245.79332401399924 584.1309111880021 245.79332401399924 589.19819300269 245.79332401399924 C 594.2654748173779 245.79332401399924 596.7991157247219 245.79332401399924 601.86639753941 244.80079886244906 C 606.933679354094 243.80827371089887 609.467320261436 235.94410947711268 614.53460207612 235.94410947711268 C 619.601883890808 235.94410947711268 622.1355247981521 235.94410947711268 627.20280661284 235.94410947711268 C 632.270088427528 235.94410947711268 634.803729334872 235.94410947711268 639.87101114956 235.94410947711268 C 644.938292964244 235.94410947711268 652.53921568627 235.94410947711268 652.53921568627 235.94410947711268 L 662.53921568627 235.94410947711268"
                                visibility="visible"
                                data-z-index="2"
                                class="highcharts-tracker-line"
                            ></path>
                        </g>
                        <g
                            data-z-index="0.1"
                            class="highcharts-markers highcharts-series-7 highcharts-spline-series highcharts-color-7 pm-passive-series highcharts-tracker highcharts-series-hover"
                            transform="translate(84,10) scale(1 1)"
                            clip-path="none"
                        ></g>
                        <g
                            data-z-index="0.1"
                            class="highcharts-series highcharts-series-8 highcharts-spline-series highcharts-color-8 pm-passive-series"
                            transform="translate(84,10) scale(1 1)"
                            clip-path="url(http://localhost:8080/experiment/32#highcharts-i2gfoa5-129)"
                        >
                            <path
                                d="M 31.797193387159 270.051167115556 C 31.797193387159 270.051167115556 39.3981161091886 263.264672333572 44.465397923875 263.264672333572 C 49.532679738561804 263.264672333572 52.06632064590521 263.264672333572 57.133602460592 263.264672333572 C 62.2008842752788 263.264672333572 64.7345251826222 263.264672333572 69.801806997309 263.264672333572 C 74.8690888119954 263.264672333572 77.40272971933861 263.264672333572 82.470011534025 263.264672333572 C 87.53729334871181 263.264672333572 90.0709342560552 263.264672333572 95.138216070742 263.264672333572 C 100.2054978854292 263.264672333572 102.73913879277279 227.2218787345935 107.80642060746 225.76197734611037 C 112.873702422148 224.30207595762724 115.40734332949201 224.30207595762724 120.47462514418 224.30207595762724 C 125.541906958864 224.30207595762724 128.075547866206 224.30207595762724 133.14282968089 224.30207595762724 C 138.210111495578 224.30207595762724 140.743752402922 224.30207595762724 145.81103421761 224.30207595762724 C 150.87831603229802 224.30207595762724 153.411956939642 224.30207595762724 158.47923875433 224.30207595762724 C 163.546520569014 224.30207595762724 166.08016147635598 218.245022053676 171.14744329104 214.70519207561892 C 176.21472510572798 211.16536209755907 178.74836601307197 209.2232124963987 183.81564782776 206.60292606733486 C 188.88292964244798 203.98263963827102 191.416570549792 201.60375993029967 196.48385236448 201.60375993029967 C 201.551134179164 201.60375993029967 204.084775086506 201.60375993029967 209.15205690119 201.60375993029967 C 214.219338715878 201.60375993029967 216.75297962322202 201.60375993029967 221.82026143791 201.60375993029967 C 226.88754325259802 201.60375993029967 229.42118415994202 201.60375993029967 234.48846597463 201.60375993029967 C 239.555747789314 201.60375993029967 242.089388696656 197.046503312789 247.15667051134 191.73317957775407 C 252.22395232602798 186.41985584271498 254.75759323337198 180.2116706864738 259.82487504806 175.03714125511465 C 264.89215686274804 169.86261182375546 267.425797770092 165.86053242095824 272.49307958478 165.86053242095824 C 277.560361399464 165.86053242095824 280.094002306806 165.86053242095824 285.16128412149 165.86053242095824 C 290.228565936178 165.86053242095824 292.762206843522 165.86053242095824 297.82948865821 165.86053242095824 C 302.89677047289797 165.86053242095824 305.430411380242 165.86053242095824 310.49769319493 165.86053242095824 C 315.56497500961393 165.86053242095824 318.09861591695596 135.63638430533075 323.16589773164 135.63638430533075 C 328.233179546328 135.63638430533075 330.766820453672 135.63638430533075 335.83410226836 135.63638430533075 C 340.90138408304404 135.63638430533075 343.43502499038607 129.83357279544464 348.50230680507 129.83357279544464 C 353.569588619758 129.83357279544464 356.10322952710203 129.83357279544464 361.17051134179 129.83357279544464 C 366.237793156478 129.83357279544464 368.771434063822 129.83357279544464 373.83871587851 129.83357279544464 C 378.9059976931941 129.83357279544464 381.439638600536 123.2382289714276 386.50692041522 119.5475354407593 C 391.574202229908 115.85684191008811 394.10784313725196 111.38010514209594 399.17512495194 111.38010514209594 C 404.24240676662805 111.38010514209594 406.776047673972 111.38010514209594 411.84332948866 111.38010514209594 C 416.910611303344 111.38010514209594 419.44425221068593 107.29124020895824 424.51153402537 107.29124020895824 C 429.578815840058 107.29124020895824 432.112456747402 107.29124020895824 437.17973856209 107.29124020895824 C 442.247020376778 107.29124020895824 444.780661284122 107.29124020895824 449.84794309881 107.29124020895824 C 454.91522491349394 107.29124020895824 457.448865820836 102.17438840298809 462.51614763552 98.13770389289226 C 467.583429450208 94.10101938279325 470.117070357552 87.10781765847116 475.18435217224 87.10781765847116 C 480.251633986928 87.10781765847116 482.785274894272 87.10781765847116 487.85255670896 87.10781765847116 C 492.91983852364393 87.10781765847116 500.52076124567 80.61392423329369 500.52076124567 80.61392423329369"
                                class="highcharts-graph"
                                data-z-index="1"
                            ></path>
                            <path
                                d="M 21.797193387159 270.051167115556 L 31.797193387159 270.051167115556 C 31.797193387159 270.051167115556 39.3981161091886 263.264672333572 44.465397923875 263.264672333572 C 49.532679738561804 263.264672333572 52.06632064590521 263.264672333572 57.133602460592 263.264672333572 C 62.2008842752788 263.264672333572 64.7345251826222 263.264672333572 69.801806997309 263.264672333572 C 74.8690888119954 263.264672333572 77.40272971933861 263.264672333572 82.470011534025 263.264672333572 C 87.53729334871181 263.264672333572 90.0709342560552 263.264672333572 95.138216070742 263.264672333572 C 100.2054978854292 263.264672333572 102.73913879277279 227.2218787345935 107.80642060746 225.76197734611037 C 112.873702422148 224.30207595762724 115.40734332949201 224.30207595762724 120.47462514418 224.30207595762724 C 125.541906958864 224.30207595762724 128.075547866206 224.30207595762724 133.14282968089 224.30207595762724 C 138.210111495578 224.30207595762724 140.743752402922 224.30207595762724 145.81103421761 224.30207595762724 C 150.87831603229802 224.30207595762724 153.411956939642 224.30207595762724 158.47923875433 224.30207595762724 C 163.546520569014 224.30207595762724 166.08016147635598 218.245022053676 171.14744329104 214.70519207561892 C 176.21472510572798 211.16536209755907 178.74836601307197 209.2232124963987 183.81564782776 206.60292606733486 C 188.88292964244798 203.98263963827102 191.416570549792 201.60375993029967 196.48385236448 201.60375993029967 C 201.551134179164 201.60375993029967 204.084775086506 201.60375993029967 209.15205690119 201.60375993029967 C 214.219338715878 201.60375993029967 216.75297962322202 201.60375993029967 221.82026143791 201.60375993029967 C 226.88754325259802 201.60375993029967 229.42118415994202 201.60375993029967 234.48846597463 201.60375993029967 C 239.555747789314 201.60375993029967 242.089388696656 197.046503312789 247.15667051134 191.73317957775407 C 252.22395232602798 186.41985584271498 254.75759323337198 180.2116706864738 259.82487504806 175.03714125511465 C 264.89215686274804 169.86261182375546 267.425797770092 165.86053242095824 272.49307958478 165.86053242095824 C 277.560361399464 165.86053242095824 280.094002306806 165.86053242095824 285.16128412149 165.86053242095824 C 290.228565936178 165.86053242095824 292.762206843522 165.86053242095824 297.82948865821 165.86053242095824 C 302.89677047289797 165.86053242095824 305.430411380242 165.86053242095824 310.49769319493 165.86053242095824 C 315.56497500961393 165.86053242095824 318.09861591695596 135.63638430533075 323.16589773164 135.63638430533075 C 328.233179546328 135.63638430533075 330.766820453672 135.63638430533075 335.83410226836 135.63638430533075 C 340.90138408304404 135.63638430533075 343.43502499038607 129.83357279544464 348.50230680507 129.83357279544464 C 353.569588619758 129.83357279544464 356.10322952710203 129.83357279544464 361.17051134179 129.83357279544464 C 366.237793156478 129.83357279544464 368.771434063822 129.83357279544464 373.83871587851 129.83357279544464 C 378.9059976931941 129.83357279544464 381.439638600536 123.2382289714276 386.50692041522 119.5475354407593 C 391.574202229908 115.85684191008811 394.10784313725196 111.38010514209594 399.17512495194 111.38010514209594 C 404.24240676662805 111.38010514209594 406.776047673972 111.38010514209594 411.84332948866 111.38010514209594 C 416.910611303344 111.38010514209594 419.44425221068593 107.29124020895824 424.51153402537 107.29124020895824 C 429.578815840058 107.29124020895824 432.112456747402 107.29124020895824 437.17973856209 107.29124020895824 C 442.247020376778 107.29124020895824 444.780661284122 107.29124020895824 449.84794309881 107.29124020895824 C 454.91522491349394 107.29124020895824 457.448865820836 102.17438840298809 462.51614763552 98.13770389289226 C 467.583429450208 94.10101938279325 470.117070357552 87.10781765847116 475.18435217224 87.10781765847116 C 480.251633986928 87.10781765847116 482.785274894272 87.10781765847116 487.85255670896 87.10781765847116 C 492.91983852364393 87.10781765847116 500.52076124567 80.61392423329369 500.52076124567 80.61392423329369 L 510.52076124567 80.61392423329369"
                                visibility="visible"
                                data-z-index="2"
                                class="highcharts-tracker-line"
                            ></path>
                        </g>
                        <g
                            data-z-index="0.1"
                            class="highcharts-markers highcharts-series-8 highcharts-spline-series highcharts-color-8 pm-passive-series highcharts-tracker"
                            transform="translate(84,10) scale(1 1)"
                            clip-path="none"
                        ></g>
                        <g
                            data-z-index="0.1"
                            class="highcharts-series highcharts-series-9 highcharts-spline-series highcharts-color-9 pm-active-series"
                            transform="translate(84,10) scale(1 1)"
                            clip-path="url(http://localhost:8080/experiment/32#highcharts-i2gfoa5-129)"
                        >
                            <path
                                d="M 6.4607843137255 327.3102544827813 C 6.4607843137255 327.3102544827813 14.061707035755399 327.3102544827813 19.128988850442 327.3102544827813 C 24.1962706651288 327.3102544827813 26.7299115724722 327.3102544827813 31.797193387159 327.3102544827813 C 36.8644752018454 327.3102544827813 39.3981161091886 327.3102544827813 44.465397923875 327.3102544827813 C 49.532679738561804 327.3102544827813 52.06632064590521 216.2209670665977 57.133602460592 216.2209670665977 C 62.2008842752788 216.2209670665977 64.7345251826222 232.06247753122582 69.801806997309 232.06247753122582 C 74.8690888119954 232.06247753122582 77.40272971933861 232.06247753122582 82.470011534025 232.06247753122582 C 87.53729334871181 232.06247753122582 90.0709342560552 232.06247753122582 95.138216070742 232.06247753122582 C 100.2054978854292 232.06247753122582 102.73913879277279 186.05704248808257 107.80642060746 186.05704248808257 C 112.873702422148 186.05704248808257 115.40734332949201 186.05704248808257 120.47462514418 186.05704248808257 C 125.541906958864 186.05704248808257 128.075547866206 186.05704248808257 133.14282968089 186.05704248808257 C 138.210111495578 186.05704248808257 140.743752402922 170.69474486638154 145.81103421761 170.69474486638154 C 150.87831603229802 170.69474486638154 153.411956939642 170.69474486638154 158.47923875433 170.69474486638154 C 163.546520569014 170.69474486638154 166.08016147635598 165.29825629721643 171.14744329104 165.29825629721643 C 176.21472510572798 165.29825629721643 178.74836601307197 165.29825629721643 183.81564782776 165.29825629721643 C 188.88292964244798 165.29825629721643 191.416570549792 169.4341849894609 196.48385236448 169.4341849894609 C 201.551134179164 169.4341849894609 204.084775086506 169.4341849894609 209.15205690119 169.4341849894609 C 214.219338715878 169.4341849894609 216.75297962322202 169.4341849894609 221.82026143791 169.4341849894609 C 226.88754325259802 169.4341849894609 229.42118415994202 169.4341849894609 234.48846597463 169.4341849894609 C 239.555747789314 169.4341849894609 242.089388696656 157.69893398399753 247.15667051134 157.69893398399753 C 252.22395232602798 157.69893398399753 254.75759323337198 157.69893398399753 259.82487504806 157.69893398399753 C 264.89215686274804 157.69893398399753 267.425797770092 157.69893398399753 272.49307958478 157.69893398399753 C 277.560361399464 157.69893398399753 280.094002306806 150.65014310166245 285.16128412149 150.65014310166245 C 290.228565936178 150.65014310166245 292.762206843522 150.65014310166245 297.82948865821 150.65014310166245 C 302.89677047289797 150.65014310166245 305.430411380242 150.65014310166245 310.49769319493 150.65014310166245 C 315.56497500961393 150.65014310166245 318.09861591695596 142.7861537398408 323.16589773164 142.7861537398408 C 328.233179546328 142.7861537398408 330.766820453672 142.7861537398408 335.83410226836 142.7861537398408 C 340.90138408304404 142.7861537398408 343.43502499038607 142.7861537398408 348.50230680507 142.7861537398408 C 353.569588619758 142.7861537398408 356.10322952710203 142.7861537398408 361.17051134179 142.7861537398408 C 366.237793156478 142.7861537398408 368.771434063822 141.88607723631935 373.83871587851 137.29509819344474 C 378.9059976931941 132.70411915057372 381.439638600536 119.8312585254767 386.50692041522 119.8312585254767 C 391.574202229908 119.8312585254767 394.10784313725196 119.8312585254767 399.17512495194 119.8312585254767 C 404.24240676662805 119.8312585254767 406.776047673972 119.03949256052218 411.84332948866 116.09665395994324 C 416.910611303344 113.1538153593666 419.44425221068593 105.11706552258778 424.51153402537 105.11706552258778 C 429.578815840058 105.11706552258778 432.112456747402 105.11706552258778 437.17973856209 105.11706552258778 C 442.247020376778 105.11706552258778 444.780661284122 105.11706552258778 449.84794309881 105.11706552258778 C 454.91522491349394 105.11706552258778 457.448865820836 97.02856762755738 462.51614763552 97.02856762755738 C 467.583429450208 97.02856762755738 470.117070357552 97.02856762755738 475.18435217224 97.02856762755738 C 480.251633986928 97.02856762755738 482.785274894272 97.02856762755738 487.85255670896 97.02856762755738 C 492.91983852364393 97.02856762755738 495.45347943098596 83.68970192680524 500.52076124567 83.68970192680524 C 505.588043060358 83.68970192680524 508.12168396770204 83.68970192680524 513.18896578239 83.68970192680524 C 518.2562475970781 83.68970192680524 520.789888504422 83.11224551776041 525.85717031911 83.11224551776041 C 530.9244521337939 83.11224551776041 533.458093041136 83.11224551776041 538.52537485582 83.11224551776041 C 543.5926566705081 83.11224551776041 546.126297577852 75.23500882113444 551.19357939254 75.23500882113444 C 556.2608612072279 75.23500882113444 558.794502114572 75.23500882113444 563.86178392926 75.23500882113444 C 568.929065743944 75.23500882113444 571.462706651286 75.23500882113444 576.52998846597 75.23500882113444 C 581.597270280658 75.23500882113444 584.1309111880021 70.45247585831186 589.19819300269 67.62910115054285 C 594.2654748173779 64.80572644277385 596.7991157247219 61.118135282289415 601.86639753941 61.118135282289415 C 606.933679354094 61.118135282289415 614.53460207612 61.118135282289415 614.53460207612 61.118135282289415"
                                class="highcharts-graph"
                                data-z-index="1"
                            ></path>
                            <path
                                d="M -3.5392156862745 327.3102544827813 L 6.4607843137255 327.3102544827813 C 6.4607843137255 327.3102544827813 14.061707035755399 327.3102544827813 19.128988850442 327.3102544827813 C 24.1962706651288 327.3102544827813 26.7299115724722 327.3102544827813 31.797193387159 327.3102544827813 C 36.8644752018454 327.3102544827813 39.3981161091886 327.3102544827813 44.465397923875 327.3102544827813 C 49.532679738561804 327.3102544827813 52.06632064590521 216.2209670665977 57.133602460592 216.2209670665977 C 62.2008842752788 216.2209670665977 64.7345251826222 232.06247753122582 69.801806997309 232.06247753122582 C 74.8690888119954 232.06247753122582 77.40272971933861 232.06247753122582 82.470011534025 232.06247753122582 C 87.53729334871181 232.06247753122582 90.0709342560552 232.06247753122582 95.138216070742 232.06247753122582 C 100.2054978854292 232.06247753122582 102.73913879277279 186.05704248808257 107.80642060746 186.05704248808257 C 112.873702422148 186.05704248808257 115.40734332949201 186.05704248808257 120.47462514418 186.05704248808257 C 125.541906958864 186.05704248808257 128.075547866206 186.05704248808257 133.14282968089 186.05704248808257 C 138.210111495578 186.05704248808257 140.743752402922 170.69474486638154 145.81103421761 170.69474486638154 C 150.87831603229802 170.69474486638154 153.411956939642 170.69474486638154 158.47923875433 170.69474486638154 C 163.546520569014 170.69474486638154 166.08016147635598 165.29825629721643 171.14744329104 165.29825629721643 C 176.21472510572798 165.29825629721643 178.74836601307197 165.29825629721643 183.81564782776 165.29825629721643 C 188.88292964244798 165.29825629721643 191.416570549792 169.4341849894609 196.48385236448 169.4341849894609 C 201.551134179164 169.4341849894609 204.084775086506 169.4341849894609 209.15205690119 169.4341849894609 C 214.219338715878 169.4341849894609 216.75297962322202 169.4341849894609 221.82026143791 169.4341849894609 C 226.88754325259802 169.4341849894609 229.42118415994202 169.4341849894609 234.48846597463 169.4341849894609 C 239.555747789314 169.4341849894609 242.089388696656 157.69893398399753 247.15667051134 157.69893398399753 C 252.22395232602798 157.69893398399753 254.75759323337198 157.69893398399753 259.82487504806 157.69893398399753 C 264.89215686274804 157.69893398399753 267.425797770092 157.69893398399753 272.49307958478 157.69893398399753 C 277.560361399464 157.69893398399753 280.094002306806 150.65014310166245 285.16128412149 150.65014310166245 C 290.228565936178 150.65014310166245 292.762206843522 150.65014310166245 297.82948865821 150.65014310166245 C 302.89677047289797 150.65014310166245 305.430411380242 150.65014310166245 310.49769319493 150.65014310166245 C 315.56497500961393 150.65014310166245 318.09861591695596 142.7861537398408 323.16589773164 142.7861537398408 C 328.233179546328 142.7861537398408 330.766820453672 142.7861537398408 335.83410226836 142.7861537398408 C 340.90138408304404 142.7861537398408 343.43502499038607 142.7861537398408 348.50230680507 142.7861537398408 C 353.569588619758 142.7861537398408 356.10322952710203 142.7861537398408 361.17051134179 142.7861537398408 C 366.237793156478 142.7861537398408 368.771434063822 141.88607723631935 373.83871587851 137.29509819344474 C 378.9059976931941 132.70411915057372 381.439638600536 119.8312585254767 386.50692041522 119.8312585254767 C 391.574202229908 119.8312585254767 394.10784313725196 119.8312585254767 399.17512495194 119.8312585254767 C 404.24240676662805 119.8312585254767 406.776047673972 119.03949256052218 411.84332948866 116.09665395994324 C 416.910611303344 113.1538153593666 419.44425221068593 105.11706552258778 424.51153402537 105.11706552258778 C 429.578815840058 105.11706552258778 432.112456747402 105.11706552258778 437.17973856209 105.11706552258778 C 442.247020376778 105.11706552258778 444.780661284122 105.11706552258778 449.84794309881 105.11706552258778 C 454.91522491349394 105.11706552258778 457.448865820836 97.02856762755738 462.51614763552 97.02856762755738 C 467.583429450208 97.02856762755738 470.117070357552 97.02856762755738 475.18435217224 97.02856762755738 C 480.251633986928 97.02856762755738 482.785274894272 97.02856762755738 487.85255670896 97.02856762755738 C 492.91983852364393 97.02856762755738 495.45347943098596 83.68970192680524 500.52076124567 83.68970192680524 C 505.588043060358 83.68970192680524 508.12168396770204 83.68970192680524 513.18896578239 83.68970192680524 C 518.2562475970781 83.68970192680524 520.789888504422 83.11224551776041 525.85717031911 83.11224551776041 C 530.9244521337939 83.11224551776041 533.458093041136 83.11224551776041 538.52537485582 83.11224551776041 C 543.5926566705081 83.11224551776041 546.126297577852 75.23500882113444 551.19357939254 75.23500882113444 C 556.2608612072279 75.23500882113444 558.794502114572 75.23500882113444 563.86178392926 75.23500882113444 C 568.929065743944 75.23500882113444 571.462706651286 75.23500882113444 576.52998846597 75.23500882113444 C 581.597270280658 75.23500882113444 584.1309111880021 70.45247585831186 589.19819300269 67.62910115054285 C 594.2654748173779 64.80572644277385 596.7991157247219 61.118135282289415 601.86639753941 61.118135282289415 C 606.933679354094 61.118135282289415 614.53460207612 61.118135282289415 614.53460207612 61.118135282289415 L 624.53460207612 61.118135282289415"
                                visibility="visible"
                                data-z-index="2"
                                class="highcharts-tracker-line"
                            ></path>
                        </g>
                        <g
                            data-z-index="0.1"
                            class="highcharts-markers highcharts-series-9 highcharts-spline-series highcharts-color-9 pm-active-series highcharts-tracker"
                            transform="translate(84,10) scale(1 1)"
                            clip-path="none"
                        ></g>
                    </g>
                    <text x="377" text-anchor="middle" class="highcharts-title" data-z-index="4" y="24"></text>
                    <text x="377" text-anchor="middle" class="highcharts-subtitle" data-z-index="4" y="24"></text>
                    <g class="highcharts-axis-labels highcharts-xaxis-labels" data-z-index="7">
                        <text x="166.47001153403" text-anchor="middle" transform="translate(0,0)" y="425" opacity="1">
                            10
                        </text>
                        <text x="293.15205690119" text-anchor="middle" transform="translate(0,0)" y="425" opacity="1">
                            20
                        </text>
                        <text x="419.83410226836" text-anchor="middle" transform="translate(0,0)" y="425" opacity="1">
                            30
                        </text>
                        <text x="546.51614763552" text-anchor="middle" transform="translate(0,0)" y="425" opacity="1">
                            40
                        </text>
                        <text x="673.19819300269" text-anchor="middle" transform="translate(0,0)" y="425" opacity="1">
                            50
                        </text>
                    </g>
                    <g class="highcharts-axis-labels highcharts-yaxis-labels" data-z-index="7">
                        <text x="69" text-anchor="end" transform="translate(0,0)" y="409" opacity="1">
                            -0.22
                        </text>
                        <text x="69" text-anchor="end" transform="translate(0,0)" y="352" opacity="1">
                            -0.2
                        </text>
                        <text x="69" text-anchor="end" transform="translate(0,0)" y="295" opacity="1">
                            -0.18
                        </text>
                        <text x="69" text-anchor="end" transform="translate(0,0)" y="238" opacity="1">
                            -0.16
                        </text>
                        <text x="69" text-anchor="end" transform="translate(0,0)" y="182" opacity="1">
                            -0.14
                        </text>
                        <text x="69" text-anchor="end" transform="translate(0,0)" y="125" opacity="1">
                            -0.12
                        </text>
                        <text x="69" text-anchor="end" transform="translate(0,0)" y="68" opacity="1">
                            -0.1
                        </text>
                        <text x="69" text-anchor="end" transform="translate(0,0)" y="12" opacity="1">
                            -0.08
                        </text>
                    </g>
                </svg>
            </div>
        `;
    }
}

customElements.define(ChartPlaceholder.is, ChartPlaceholder);
