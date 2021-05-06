import { LitElement, html } from "lit-element";

class LocalstorageHelper extends LitElement {
  connectedCallback() {
    super.connectedCallback();
  }
  render() {
      return html``;
  }
  setItem(itemKey, itemValue) {
    window.localStorage.setItem(itemKey, itemValue);
  }
  setItemInObject(itemKey, objectFieldKey, objectFieldValue) {
    const currentObject = this.getItemAsObject(itemKey);
    currentObject[objectFieldKey] = objectFieldValue;
    window.localStorage.setItem(itemKey, JSON.stringify(currentObject));
  }
  getItemAsObject(itemKey) {
    return JSON.parse(window.localStorage.getItem(itemKey)) || {};
  }
}

customElements.define("localstorage-helper", LocalstorageHelper);