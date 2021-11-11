import {css, html, LitElement, property} from "lit-element";

class ModelsNavbarItem extends LitElement {

  @property({type: String})
  modelName = "";
  @property({type: String})
  modelPackageName = "";
  @property({type: String})
  modelPackageNameInBrackets = "";
  @property({type: String})
  modelLink = "";
  @property({type: Boolean})
  isDraft = "";
  @property({type: Boolean, reflect: true, attribute: "is-archived"})
  isArchived = false;
  @property({type: Boolean, reflect: true, attribute: "is-current"})
  isCurrent = false;

  static get styles() {
    return css`
      :host {
        box-sizing: border-box;
        display: flex;
        align-items: center;
        flex-shrink: 0;
        width: 100%;
        padding: 0.45rem var(--lumo-space-s);
        border: 1px solid transparent;
        border-right: none;
        margin-top: -1px;
        cursor: pointer;
      }
      :host([is-current]),
      :host(:hover) {
        background-color: white;
        border-color: var(--pm-grey-color-lighter);
      }
      a {
        display: flex;
        align-items: center;
        flex-shrink: 0;
        width: 100%;
        color: var(--lumo-body-text-color);
        text-decoration: none;
      }
      .model-name {
        font-size: var(--lumo-font-size-s);
        line-height: 1em;
      }
      .model-name div {
        margin-bottom: var(--lumo-space-xxxs);
      }
      p {
        margin: 0;
      }
      p:nth-of-type(2) {
        font-size: var(--lumo-font-size-xs);
        font-family: var(--lumo-font-family-header); /* This font should usually be used on a header. This is an exception. */
        color: var(--pm-grey-color-dark);
      }
      tag-label {
        font-size: var(--lumo-font-size-xs);
        line-height: 1;
        margin-left: 0;
        margin-bottom: 1px;
      }
      tag-label[hidden] {
        display: none;
      }
    `;
  }

  render() {
    return html`
      <a @click="${this.handleRowClicked}" href="${this.modelLink}">
        <div class="model-name">
          <tag-label text="${this.isDraft ? "Draft" : ""}" size="small" outline="true"></tag-label>
          <p>Model #${this.modelName} ${this.modelPackageName ? `(${this.modelPackageName})` : ""}</p>
          <p>Created <slot></slot></p>
        </div>
      </a>`;
  }

  handleRowClicked(event) {
    event.preventDefault();
    (this as any).$server.handleRowClicked();
  }
}

customElements.define("models-navbar-item", ModelsNavbarItem);