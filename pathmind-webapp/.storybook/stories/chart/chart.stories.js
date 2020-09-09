import { storiesOf } from "@storybook/polymer";
import { hljs, hlJava, hlConfig, copyCodeBlock, storyParamConfig } from "../../utils";

import "../../../frontend/src/components/atoms/data-chart.js";

// import code from "./codeSnippets";

hljs.registerLanguage('java', hlJava);

storiesOf('Charts', module)
.addDecorator(storyFn => `<div class="story-wrapper">
    ${storyFn()}
</div>`)
.add('Curved Line Chart', () => `
    <h1>Curved Line Chart</h1>
    <div class="block-wrapper">
        <p>This is based on the Line Chart type from <a href="https://www.webcomponents.org/element/@google-web-components/google-chart/elements/google-chart" target="_blank">Google Chart</a>.</p>
    </div>
    <div class="block-wrapper">
        <h3>Area Chart</h3>
        <div class="col-wrapper">
            <data-chart
                type='area'
                data='[
                    ["Year", "Sales", "Expenses"],
                    ["2013",  1000,      400],
                    ["2014",  1170,      460],
                    ["2015",  660,       1120],
                    ["2016",  1030,      540]
                ]'
                options='{
                    "title": "Company Performance",
                    "hAxis": {"title": "Year"},
                    "vAxis": {"minValue": 0}
                }'
            ></data-chart>
        </div>
        <h3>Line Chart (Reward Score Chart)</h3>
        <div class="col-wrapper">
            <data-chart
                type='line'
                cols='[
                    {"label":"Iteration", "type":"number"},
                    {"label":"Policy 1", "type":"number"}, {"role": "tooltip", "type":"string", "p": {"html": true}},
                    {"label":"Policy 2", "type":"number"}, {"role": "tooltip", "type":"string", "p": {"html": true}}
                ]'
                rows='[
                    [1,  69.3, "<div><b>Iteration #</b>1<br><b>Mean Reward</b> 69.3<br><b>Episode Count</b> 20</div>",  86.7, "what"],
                    [2,  18.2, "nah",  53.8, "what"],
                    [3,  -13.7, "nah", -8.4, "what"],
                    [4,  -23.0, "nah", -45.5, "what"],
                    [5,  -14.5, "nah", -60.4, "what"],
                    [6,  -2.1, "nah", -69.4, "what"],
                    [7,  9.3, "nah", -70.3, "what"],
                    [8,  18.4, "nah", -69.0, "what"]
                ]'
                options='{
                    "tooltip": { "isHtml": true },
                    "curveType": "function",
                    "hAxis": {"title": "Iteration", "titleTextStyle": {"italic": false}},
                    "vAxis": {"title": "Mean Reward Score over All Episodes", "titleTextStyle": {"italic": false}},
                    "legend": {"position": "none"}
                }'
            ></data-chart>
        </div>
    </div>
`, storyParamConfig);