import {LitElement, html, css, property} from 'lit-element';

class SortableRowWrapper extends LitElement {
  @property({type: Boolean, reflect: true})
  sortable = true;
  @property({type: Object})
  sortableNode;

  static get styles() {
    return css`
      :host {
        box-sizing: border-box;
        display: flex;
        width: 100%;
        font-size: var(--lumo-font-size-s);
      }
      :host(:hover:not(.onDrag)),
      :host(.sortable-chosen) {
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
      :host([sortable]:hover:not(.onDrag)) .draggable-icon,
      :host([sortable]:hover:not(.onDrag)) .draggable-icon,
      :host([sortable].sortable-chosen) .draggable-icon {
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
        flex-shrink: 0;
        width: 14px;
        height: 14px;
        color: var(--pm-grey-color);
        margin: 7px var(--lumo-space-xs) 7px var(--lumo-space-s);
        cursor: pointer;
        visibility: hidden;
      }
      iron-icon:hover {
        color: var(--lumo-primary-color);
      }
      :host(:hover) iron-icon {
        visibility: visible;
      }
    `;
  }
  connectedCallback() {
    super.connectedCallback();
    if (this.sortable) {
      this.sortableNode = this.parentNode.parentNode;
      this._getConnector = this._getConnector.bind(this);
      window.requestAnimationFrame(this._getConnector);
    }
  }
  _getConnector() {
    const onDragClassName = "onDrag";
    const sortableNode = this.sortableNode;
    const sortableRows = this.parentNode.childNodes;
    const sortableConnecter = sortableNode.$connector;
    if (typeof sortableConnecter === "undefined") {
      window.requestAnimationFrame(this._getConnector);
    } else {
      sortableConnecter.setOption("onStart", function(event) {
        sortableNode.classList.add(onDragClassName);
        sortableRows.forEach((el: HTMLElement) => el.classList.add(onDragClassName));
      });
      sortableConnecter.setOption("onEnd", function(event) {
        if (sortableNode.classList.contains(onDragClassName)) {
          sortableNode.classList.remove(onDragClassName);
          sortableRows.forEach((el: HTMLElement) => el.classList.remove(onDragClassName));
        }
      });
    }
  }
  render() {
    return html`
      <span class="draggable-icon"></span>
      <slot></slot>
      <iron-icon icon="vaadin:close-big" @click="${e => (this as any).$server.removeRow()}"></iron-icon>
    `;
  }
}

customElements.define('sortable-row-wrapper', SortableRowWrapper);