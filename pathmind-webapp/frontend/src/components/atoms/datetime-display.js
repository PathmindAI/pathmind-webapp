import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import { format, register } from "timeago.js";

class DatetimeDisplay extends PolymerElement {
    static get is() {
        return "datetime-display";
    }

    static get properties() {
        return {
            datetime: {
                type: String,
            },
            date: {
                type: String,
                computed: `_computeDate(datetime)`,
                observer: `_createTooltip`,
            },
            displaytext: {
                type: String,
                computed: `_computeDisplayText(datetime)`,
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

    _computeDate(datetime) {
        return new Date(datetime).toLocaleDateString(window.navigator.language, { 
            year: 'numeric', 
            month: 'short', 
            day: 'numeric', 
        });
    }

    _createTooltip() {
        const userTimezone = Intl.DateTimeFormat().resolvedOptions().timeZone;
        this.title = new Date(this.datetime).toLocaleString(window.navigator.language, { 
            weekday: 'short', 
            year: 'numeric', 
            month: 'short', 
            day: 'numeric', 
            hour: '2-digit',
            minute: '2-digit',
            timeZone: userTimezone,
            timeZoneName: 'short',
        });
    }

    _computeDisplayText(datetime) {
        return new Date(datetime) <= new Date().setDate(new Date().getDate()-30) ? this.date : format(datetime, 'my-locale');
    }
}

customElements.define(DatetimeDisplay.is, DatetimeDisplay);