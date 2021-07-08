import { LitElement, html, css, property } from "lit-element";

class TagLabel extends LitElement {

    @property({type: String, reflect: true})
    text = "";

    @property({type: Boolean, reflect: true})
    outline = false;

    @property({type: String, reflect: true})
    size = "";

    attributeChangedCallback(name, oldval, newval) {
      super.attributeChangedCallback(name, oldval, newval);
      if (name === "text") {
        this._textChanged();
      }
    }

    _textChanged() {
      if (this.text.length === 0) {
        this.setAttribute("hidden", "true");
      } else {
        this.removeAttribute("hidden");
      }
    }

    static get styles() {
      return css`
        :host {
            box-sizing: border-box;
            display: inline-block;
            color: var(--pm-grey-color-dark);
            font-size: var(--lumo-font-size-s);
            line-height: 1.3;
            padding: var(--lumo-space-xxxs) var(--lumo-space-xs);
            background: var(--lumo-contrast-10pct);
            border: 1px solid transparent;
            border-radius: var(--lumo-border-radius);
            margin: 0 var(--lumo-space-xxxs);
        }
        :host([outline]) {
            background: transparent;
            border: 1px solid;
        }
        :host([size="small"]) {
            padding: 0 var(--lumo-space-xxs);
            border-radius: var(--lumo-border-radius-s);
        }`;
    }

    render() {
        return html`${this.text}`;
    }
}

customElements.define("tag-label", TagLabel);