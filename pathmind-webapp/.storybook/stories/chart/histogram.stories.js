import { storiesOf } from "@storybook/polymer";
import { storyParamConfig } from "../../utils";

import "../../../frontend/src/components/atoms/histogram-chart.js";

storiesOf('Charts', module)
.addDecorator(storyFn => `<div class="story-wrapper">
    ${storyFn()}
</div>`)
.add('Histogram', () => `
    <h1>Histogram</h1>
    <div class="block-wrapper">
        <p>This is based on the APIs from 
        <a href="https://www.webcomponents.org/element/@google-web-components/google-chart/elements/google-chart" target="_blank">Google Chart</a> and 
        <a href="https://developers.google.com/chart/interactive/docs/gallery/histogram">Google Charts Histogram</a>.</p>
    </div>
    <div class="block-wrapper">
        <h3>Histogram (Single)</h3>
        <div class="col-wrapper">
            <histogram-chart
                cols='[
                    {"label":"Vanilla Policy", "type":"number"}
                ]'
                rows='[
                    [15],
                    [18],
                    [16.4],
                    [15.4],
                    [17],
                    [17.8],
                    [18],
                    [18.2],
                    [16.4],
                    [16.2],
                    [17.8],
                    [17.6],
                    [16.3],
                    [15.5]
                ]'
                colors='["#1dadee"]'
                haxistitle='x-axis title'
                vaxistitle='y-axis title'
            ></histogram-chart>
        </div>
        <h3>Histogram (Multiple)</h3>
        <div class="col-wrapper">
            <histogram-chart
                cols='[
                    {"label":"Vanilla Policy", "type":"number"},
                    {"label":"Heuristic", "type":"number"}
                ]'
                rows='[
                    [15, 32],
                    [18, 31],
                    [16.4, 31.4],
                    [15.4, 32.5],
                    [17, 30.3],
                    [17.8, 31],
                    [18, 32.6],
                    [18.2, 32.9]
                ]'
                colors='[
                    "#a1c3e5",
                    "#1dadee"
                ]'
                haxistitle='x-axis title'
                vaxistitle='y-axis title'
                bucketsize='0.5'
            ></histogram-chart>
        </div>
    </div>
`, storyParamConfig);

