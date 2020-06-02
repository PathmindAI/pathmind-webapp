import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class ElapsedTimer extends PolymerElement {
  static get is() {
    return "elapsed-timer";
  }

  static get template() {
    return html`
      <style>
        :host {
          display: block;
        }
      </style>
      [[formattedTime]]
    `;
  }

  static get properties() {
    return {
      /**
       * Start time for the timer (in seconds)
       */
      offset: {
        type: Number,
        value: 0
      },

      /**
       * Actual time for the timer (including offset)
       */
      currentTime: {
        type: Number,
        notify: true,
        value: 0
      },

      /**
       * True if the timer is running
       */
      isRunning: {
        type: Boolean,
        reflectToAttribute: true,
        notify: true,
        value: false
      },

      /**
       * Time the timer has been running since it was started
       */
      elapsedTime: {
        type: Number,
        value: 0
      },

      /**
       * Formatted time to be rendered on UI
       */
      formattedTime: {
        type: String,
        value: "0"
      }
    };
  }

  constructor() {
    super();
  }

  updateTimer(time, isRunning) {
    isRunning ? this.startTimer(time) : this.stopTimer(time);
  }

  startTimer(time) {
    this.offset = time;
    this.elapsedTime = performance.now() / 1000;
    this.isRunning = true;
    window.requestAnimationFrame(this.increaseTimer.bind(this));
  }

  stopTimer(time) {
    this.isRunning = false;
    this.formattedTime = this.formatTime(time);
  }

  increaseTimer(timestamp) {
    if (!this.isRunning) {
      return;
    }
    const now = timestamp / 1000;
    this.currentTime = now - this.elapsedTime + +this.offset;
    this.formattedTime = this.formatTime(this.currentTime);
    window.requestAnimationFrame(this.increaseTimer.bind(this));
  }

  formatTime(time) {
    const timeString = time.toString().split(".");
    let secs = timeString[0];

    let mins = Math.floor(secs / 60);
    const hours = Math.floor(mins / 60);
    secs = secs % 60;
    mins = mins % 60;

    function addZero(originalNumericFigure) {
      const numberString = originalNumericFigure.toString();
      return numberString >= 10 ? numberString : "0" + numberString;
    }

    return `${addZero(hours)}h ${addZero(mins)}m ${addZero(secs)}s`;
  }
}

customElements.define(ElapsedTimer.is, ElapsedTimer);
