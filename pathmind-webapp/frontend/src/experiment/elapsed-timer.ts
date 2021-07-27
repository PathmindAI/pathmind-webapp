import { LitElement, html, css, property } from "lit-element";

class ElapsedTimer extends LitElement {
    /**
     * Start time for the timer (in seconds)
     */
    @property({type: Number})
    offset = 0;
    /** 
     * Actual time for the timer (including offset)
     */
    @property({type: Number})
    currentTime = 0;
    /**
     * True if the timer is running
     */
    @property({type: Boolean, reflect: true})
    isRunning = false;
    /**
     * Time the timer has been running since it was started
     */
    @property({type: Number})
    elapsedTime = 0;
    /**
     * Formatted time to be rendered on UI
     */
    @property({type: String})
    formattedTime = "";

    static get styles() {
        return css`
            :host {
                display: block;
            }`;
    }

    render() {
        return html`${this.formattedTime}`;
    }

    updateTimer(time: String, isRunning: boolean) {
        this.isRunning = isRunning;
        isRunning ? this.startTimer(time) : this.stopTimer(time);
    }

    startTimer(time: String) {
        this.offset = +time;
        this.elapsedTime = performance.now() / 1000;
        window.requestAnimationFrame(this.increaseTimer.bind(this));
    }

    stopTimer(time: String) {
        this.formattedTime = this.formatTime(time);
    }

    increaseTimer(timestamp) {
        if (!this.isRunning) {
            return;
        }
        const now = timestamp / 1000;
        this.currentTime = now - this.elapsedTime + this.offset;
        this.formattedTime = this.formatTime(this.currentTime.toString());
        window.requestAnimationFrame(this.increaseTimer.bind(this));
    }

    formatTime(time: String) {
        const timeString = time.toString().split(".");
        let secs: number = +timeString[0];

        let mins: number = Math.floor(secs / 60);
        const hours: number = Math.floor(mins / 60);
        secs = secs % 60;
        mins = mins % 60;

        function addZero(originalNumericFigure) {
            const numberString = originalNumericFigure.toString();
            return numberString >= 10 ? numberString : "0" + numberString;
        }

        if (hours == 0) {
            return `${addZero(mins)}m ${addZero(secs)}s`;
        }

        return `${addZero(hours)}h ${addZero(mins)}m ${addZero(secs)}s`;
    }
}

customElements.define("elapsed-timer", ElapsedTimer);
