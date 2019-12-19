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
      [[_formattedTime]]
    `;
  }

  static get properties() {
    return {
      offset: {
        type: Number,
        value: 0
      },
      currentTime: {
        type: Number,
        notify: true,
        value: 0
      },
      isRunning: {
        type: Boolean,
        reflectToAttribute: true,
        notify: true,
        value: false
      },
      _elapsedTime: {
        type: Number,
        value: 0
      },
      _formattedTime: {
        type: String,
        value: "0"
      }
    };
  }

  constructor() {
    super();
  }

  startTimer(time) {
    this.offset = time;
    this._elapsedTime = performance.now() / 1000;
    this.isRunning = true;
    window.requestAnimationFrame(this._increaseTimer.bind(this));
  }

  stopTimer(time) {
    this.isRunning = false;
    this._formattedTime = this._formatTime(time);
  }

  setTimer(time) {
    this.offset = time;
    if (!this.isRunning) {
      this.start(time);
    }
    this._formattedTime = this._formatTime(time);
  }

  _increaseTimer(timestamp) {
    if (!this.isRunning) {
      return;
    }
    const now = timestamp / 1000;
    const progress = now - this._elapsedTime;
    this.currentTime = this.currentTime + progress;
    this._formattedTime = this._formatTime(this.currentTime);
    this._elapsedTime = now;
    window.requestAnimationFrame(this._increaseTimer.bind(this));
  }

  _formatTime(time) {
    const timeString = time.toString().split(".");
    let secs = timeString[0];
    secs = +secs + +this.offset;

    let mins = Math.floor(secs / 60);
    const hours = Math.floor(mins / 60);
    secs = secs % 60;
    mins = mins % 60;

    let formattedText = "";
    if (hours > 0) formattedText += `${hours} h `;
    if (mins > 0) formattedText += `${mins} min `;
    formattedText += `${secs} sec`;
    return formattedText;
  }
}

customElements.define(ElapsedTimer.is, ElapsedTimer);
