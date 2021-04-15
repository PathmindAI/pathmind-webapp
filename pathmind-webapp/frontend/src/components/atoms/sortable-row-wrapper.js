import {LitElement, html, css, property} from 'lit-element';

class SortableRowWrapper extends LitElement {
  static get styles() {
    return css`
      :host {
        box-sizing: border-box;
        display: flex;
        width: 100%;
        font-size: var(--lumo-font-size-s);
        border: 1px solid transparent;
        border-radius: var(--lumo-border-radius);
      }
      :host(:hover:not(.onDrag)),
      :host(.sortable-chosen) {
        border-color: var(--pm-grey-color);
        z-index: 1;
      }
      .draggable-icon {
        display: inline-flex;
        position: relative;
        width: 6px;
        height: 14px;
        margin: 7px var(--lumo-space-s) 7px var(--lumo-space-xs);
        cursor: pointer;
        opacity: 0;
      }
      :host(:hover:not(.onDrag)) .draggable-icon,
      :host(:hover:not(.onDrag)) .draggable-icon,
      :host(.sortable-chosen) .draggable-icon {
        opacity: 1;
      }
      .draggable-icon:after {
        content: "";
        position: absolute;
        width: 2px;
        height: 2px;
        top: 0;
        left: 0;
        background-color: var(--pm-grey-color);
        box-shadow: 4px 0 0 var(--pm-grey-color), 0 4px 0 var(--pm-grey-color), 
            4px 4px 0 var(--pm-grey-color), 0 8px 0 var(--pm-grey-color), 
            4px 8px 0 var(--pm-grey-color), 0 12px 0 var(--pm-grey-color), 
            4px 12px 0 var(--pm-grey-color);
      }
      iron-icon {
        width: 14px;
        height: 14px;
        color: var(--pm-grey-color);
        margin: 7px var(--lumo-space-s);
        cursor: pointer;
      }
      iron-icon:hover {
        color: var(--lumo-primary-color);
      }
    `;
  }
  constructor() {
    super();
  }
  connectedCallback() {
    super.connectedCallback();
    this.sortableNode = this.parentNode.parentNode;
    this._getConnector = this._getConnector.bind(this);
    window.requestAnimationFrame(this._getConnector);
  }
  _getConnector() {
    const onDragClassName = "onDrag";
    const sortableNode = this.sortableNode;
    const sortableRows = this.parentNode.childNodes;
    const sortableConnecter = sortableNode.$connector;
    if (typeof sortableConnecter === "undefined") {
      window.requestAnimationFrame(this.getConnector);
    } else {
      sortableConnecter.setOption("onStart", function(event) {
        sortableNode.classList.add(onDragClassName);
        sortableRows.forEach(el => el.classList.add(onDragClassName));
      });
      sortableConnecter.setOption("onEnd", function(event) {
        if (sortableNode.classList.contains(onDragClassName)) {
          sortableNode.classList.remove(onDragClassName);
          sortableRows.forEach(el => el.classList.remove(onDragClassName));
        }
      });
    }
  }
  render() {
    return html`
      <span class="draggable-icon"></span>
      <slot></slot>
      <iron-icon icon="vaadin:close-big" @click="${e => this.$server.removeRow()}"></iron-icon>
    `;
  }
}

customElements.define('sortable-row-wrapper', SortableRowWrapper);