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
        <p>This is based on the APIs from <a href="https://www.webcomponents.org/element/@google-web-components/google-chart/elements/google-chart" target="_blank">Google Chart</a>.</p>
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
                    "isStacked": true,
                    "curveType": "function",
                    "hAxis": {"title": "Iteration", "titleTextStyle": {"italic": false}},
                    "vAxis": {"title": "Mean Reward Score over All Episodes", "titleTextStyle": {"italic": false}},
                    "legend": {"position": "none"}
                }'
            ></data-chart>
        </div>
        <h3>Sparkline Chart (Metric with Goal <=22, starts from 0)</h3>
        <div class="col-wrapper">
            <div style="width: 100px; height: 32px">
                <data-chart
                    type='combo'
                    cols='[
                        {"label":"Iteration", "type":"number"},
                        {"label":"Mean Metric Value", "type":"number"},
                        {"label":"goal base", "type": "number"},
                        {"label":"goal", "type": "number"}
                    ]'
                    rows='[
                        [1,  134.9, 0, 22],
                        [2,  125, 0, 22],
                        [3,  100, 0, 22],
                        [4,  90, 0, 22],
                        [5,  65, 0, 22],
                        [6,  60, 0, 22],
                        [7,  55, 0, 22],
                        [8,  54, 0, 22]
                    ]'
                    options='{
                        "seriesType": "area",
                        "tooltip": { "trigger": "none" },
                        "hAxis": {"textPosition": "none", "ticks": []},
                        "vAxis": {
                            "textPosition": "none",
                            "ticks": [],
                            "viewWindowMode": "maximized"
                        },
                        "legend": {"position": "none"},
                        "series": {
                            "0": {
                                "type": "line",
                                "enableInteractivity": true
                            },
                            "1": {
                                "lineWidth": 0,
                                "enableInteractivity": false,
                                "color": "transparent"
                            },
                            "2": {
                                "lineWidth": 0,
                                "type": "area",
                                "enableInteractivity": false,
                                "color": "green"
                            }
                        }
                    }'
                ></data-chart>
            </div>
        </div>
        <h3>Sparkline Chart (Metric with Goal >=5200, calibrated)</h3>
        <div class="col-wrapper">
            <div style="width: 100px; height: 32px">
                <data-chart
                    type='combo'
                    cols='[
                        {"label":"Iteration", "type":"number"},
                        {"label":"Mean Metric Value", "type":"number"},
                        {"label":"goal base", "type": "number"},
                        {"label":"goal", "type": "number"}
                    ]'
                    rows='[
                        [1,  5340.9, 5200, 300],
                        [2,  5340, 5200, 300],
                        [3,  5210, 5200, 300],
                        [4,  5150, 5200, 300],
                        [5,  5325, 5200, 300],
                        [6,  5123, 5200, 300],
                        [7,  5500, 5200, 300],
                        [8,  5400, 5200, 300]
                    ]'
                    options='{
                        "seriesType": "area",
                        "tooltip": { "trigger": "none" },
                        "isStacked": true,
                        "hAxis": {"textPosition": "none", "ticks": []},
                        "vAxis": {
                            "textPosition": "none",
                            "ticks": [],
                            "viewWindow": {
                                "max": 5500,
                                "min": 5120
                            }
                        },
                        "legend": {"position": "none"},
                        "series": {
                            "0": {
                                "type": "line",
                                "enableInteractivity": true
                            },
                            "1": {
                                "lineWidth": 0,
                                "enableInteractivity": false,
                                "color": "transparent"
                            },
                            "2": {
                                "lineWidth": 0,
                                "enableInteractivity": false,
                                "color": "green"
                            }
                        }
                    }'
                ></data-chart>
            </div>
        </div>
        <h3>Metric Chart (Metric with Goal <=22, calibrated)</h3>
        <div class="col-wrapper">
            <data-chart
                type='combo'
                cols='[
                    {"label":"Iteration", "type":"number"},
                    {"label":"Mean Metric Value", "type":"number"}, {"role": "tooltip", "type":"string", "p": {"html": true}},
                    {"label":"goal base", "type": "number"},
                    {"label":"goal", "type": "number"}
                ]'
                rows='[
                    [1,  134.9, "<div><b>Iteration #</b>1<br><b>Mean Reward</b> 134.9<br><b>Episode Count</b> 20</div>", 0, 22],
                    [2,  125, "nah", 0, 22],
                    [3,  100, "nah", 0, 22],
                    [4,  90, "nah", 0, 22],
                    [5,  65, "nah", 0, 22],
                    [6,  60, "nah", 0, 22],
                    [7,  55, "nah", 0, 22],
                    [8,  54, "nah", 0, 22]
                ]'
                options='{
                    "seriesType": "area",
                    "tooltip": { "isHtml": true },
                    "isStacked": true,
                    "hAxis": {"title": "Iteration", "titleTextStyle": {"italic": false}},
                    "vAxis": {
                        "title": "Mean Metric Value", 
                        "titleTextStyle": {"italic": false},
                        "viewWindow": {
                            "min": 0
                        }
                    },
                    "legend": {"position": "none"},
                    "series": {
                        "0": {
                            "type": "line",
                            "enableInteractivity": true
                        },
                        "1": {
                            "lineWidth": 0,
                            "enableInteractivity": false,
                            "color": "transparent"
                        },
                        "2": {
                            "lineWidth": 0,
                            "type": "area",
                            "enableInteractivity": false,
                            "color": "green"
                        }
                    }
                }'
            ></data-chart>
        </div>
        <h3>Metric Chart (Metric with Goal >=5200, calibrated)</h3>
        <div class="col-wrapper">
            <data-chart
                type='combo'
                cols='[
                    {"label":"Iteration", "type":"number"},
                    {"label":"Mean Metric Value", "type":"number"}, {"role": "tooltip", "type":"string", "p": {"html": true}},
                    {"label":"goal base", "type": "number"},
                    {"label":"goal", "type": "number"}
                ]'
                rows='[
                    [1,  5340.9, "<div><b>Iteration #</b>1<br><b>Mean Reward</b> 134.9<br><b>Episode Count</b> 20</div>", 5200, 300],
                    [2,  5340, "nah", 5200, 300],
                    [3,  5210, "nah", 5200, 300],
                    [4,  5150, "nah", 5200, 300],
                    [5,  5325, "nah", 5200, 300],
                    [6,  5123, "nah", 5200, 300],
                    [7,  5500, "nah", 5200, 300],
                    [8,  5400, "nah", 5200, 300]
                ]'
                options='{
                    "seriesType": "area",
                    "tooltip": { "isHtml": true },
                    "isStacked": true,
                    "hAxis": {
                        "title": "Iteration", 
                        "titleTextStyle": {"italic": false}
                    },
                    "vAxis": {
                        "title": "Mean Metric Value", 
                        "titleTextStyle": {"italic": false},
                        "viewWindow": {
                            "min": 5120
                        }
                    },
                    "legend": {"position": "none"},
                    "series": {
                        "0": {
                            "type": "line",
                            "enableInteractivity": true
                        },
                        "1": {
                            "lineWidth": 0,
                            "enableInteractivity": false,
                            "color": "transparent"
                        },
                        "2": {
                            "lineWidth": 0,
                            "enableInteractivity": false,
                            "color": "green"
                        }
                    }
                }'
            ></data-chart>
        </div>
    </div>
`, storyParamConfig);

// "viewWindowMode": "maximized"