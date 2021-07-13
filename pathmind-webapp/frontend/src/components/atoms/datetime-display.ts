import { LitElement, html, property } from "lit-element";
import { render as timeAgoRender, register } from "timeago.js";

class DatetimeDisplay extends LitElement {
    @property({type: String})
    datetime = "";

    @property({type: String})
    serverTimeZoneOffsetFromUTC = "";

    @property({type: String})
    datetimeWithTimezone = "";

    @property({type: String})
    date = "";

    constructor() {
        super();
        // register your locale with timeago
        register('my-locale', this.localeFunc);
    }

    renderDate() {
        const datetimeAsDate = new Date(this.datetimeWithTimezone);
        const thirtyDaysAgo = new Date();
        thirtyDaysAgo.setDate(new Date().getDate()-30);
        if (datetimeAsDate > thirtyDaysAgo) {
            timeAgoRender(this.shadowRoot.querySelector("span"), 'my-locale');
        }
    }

    updated(changedProperties) {
        console.log(changedProperties)
        changedProperties.forEach((oldValue, name) => {
            if (name === "datetime" || name === "serverTimeZoneOffsetFromUTC") {
              this.datetimeWithTimezone = this._computeDatetimeWithTimezone();
            }
            if (name === "datetimeWithTimezone") {
              this.date = this._computeDate();
              this._createTooltip();
            }
        })
        this.renderDate();
    }

    render() {
        return html`<span datetime="${this.datetimeWithTimezone}">${this.date}</span>`;
    }
    
    localeFunc(number : number, index : number, totalSec : number) : [string, string] {
        // number: the timeago / timein number;
        // index: the index of array below;
        // totalSec: total seconds between date to be formatted and today's date;
        var EN_US = ['second', 'minute', 'hour', 'day', 'day', 'month', 'year'];
        if (index === 0) {
            return ['just now', 'right now'];
        }
        if ([8,9].indexOf(index) !== -1) {
            number = Math.round(totalSec / 86400);
        }
        var unit = EN_US[Math.floor(index / 2)];
        if (number > 1) {
            unit += 's';
        }
        return [number + " " + unit + " ago", "in " + number + " " + unit];
    }

    _computeDate() {
        return new Date(this.datetimeWithTimezone).toLocaleDateString(window.navigator.language, { 
            year: 'numeric', 
            month: 'short', 
            day: 'numeric', 
        });
    }

    _createTooltip() {
        const userTimezone = Intl.DateTimeFormat().resolvedOptions().timeZone;
        this.title = new Date(this.datetimeWithTimezone).toLocaleString(window.navigator.language, { 
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

    _computeDatetimeWithTimezone() {
        return this.datetime+this.serverTimeZoneOffsetFromUTC;
    }

}

customElements.define("datetime-display", DatetimeDisplay);