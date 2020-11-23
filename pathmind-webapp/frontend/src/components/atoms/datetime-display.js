import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import { format, register } from "timeago.js";

class DatetimeDisplay extends PolymerElement {
    static get is() {
        return "datetime-display";
    }

    static get properties() {
        return {
            date: {
                type: String,
            },
            datetime: {
                type: String,
            },
            displaytext: {
                type: String,
                computed: `_computeDisplayText(datetime)`,
            },
            tooltip: {
                type: String,
                observer: '_isTooltipChanged',
            },
        }
    }

    static get template() {
        return html`{{displaytext}}`;
    }
    
    localeFunc(number, index, totalSec) {
        // number: the timeago / timein number;
        // index: the index of array below;
        // totalSec: total seconds between date to be formatted and today's date;
        var EN_US = ['second', 'minute', 'hour', 'day', 'day', 'month', 'year'];
        if (index === 0) {
            return ['just now', 'right now'];
        }
        if ([8,9].indexOf(index) !== -1) {
            number = parseInt(totalSec / 86400);
        }
        var unit = EN_US[Math.floor(index / 2)];
        if (number > 1) {
            unit += 's';
        }
        return [number + " " + unit + " ago", "in " + number + " " + unit];
    }

    constructor() {
        super();
        // register your locale with timeago
        register('my-locale', this.localeFunc);
    }

    _isTooltipChanged(newValue) {
        this.title = newValue;
    }

    _computeDisplayText(datetime) {
        return new Date(datetime) <= new Date().setDate(new Date().getDate()-30) ? this.date : format(datetime, 'my-locale');
    }
}

customElements.define(DatetimeDisplay.is, DatetimeDisplay);