import { LitElement, html } from "lit-element";

class AnimatedProgressBar extends LitElement {
  static get properties() {
      return {
        duration: {type: Number} // in second
      }
  }
  constructor() {
    super();
    window.makeProgress = this.makeProgress.bind(this);
  }
  makeProgress() {
    if (this.$.bar.value < 100) {
      this.$.bar.value += 1;
    }
  }
  render() {
    return html`<vaadin-progress-bar id="bar" min="0" max="100"></vaadin-progress-bar>`;
  }
}

customElements.define("animated-progress-bar", AnimatedProgressBar);