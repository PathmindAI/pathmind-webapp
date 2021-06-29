import { LitElement, html } from "lit-element";

class AnimatedProgressBar extends LitElement {
  static get properties() {
    return {
      duration: {type: Number} // in seconds
    }
  }
  connectedCallback() {
    super.connectedCallback();
    this.start = new Date().getTime();
    window.makeProgress = this.makeProgress.bind(this);
    makeProgress();
  }
  firstUpdated() {
    this.bar = this.renderRoot.querySelector("#bar");
  }
  makeProgress() {
    if (this.bar == undefined) {
        requestAnimationFrame(makeProgress);
        return;
    }
    const now = new Date().getTime();
    if (this.bar.value < 100) {
      if (now - this.start >= this.duration*1000/100) {
        this.bar.value += 1;
        this.start = now;
      }
      requestAnimationFrame(makeProgress);
    }
  }
  render() {
    return html`<vaadin-progress-bar id="bar" min="0" max="100" value="0"></vaadin-progress-bar>`;
  }
}

customElements.define("animated-progress-bar", AnimatedProgressBar);