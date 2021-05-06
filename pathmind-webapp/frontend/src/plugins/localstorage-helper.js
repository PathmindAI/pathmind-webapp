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
  setItemInObjectOfObject(itemKey, objectFieldKey, objectFieldInObjectKey, objectFieldInObjectValue) {
    /*
     * itemKey: {
     *   objectFieldKey: {
     *     objectFieldinObjectKey: objectFieldInObjectValue
     *   }
     * }
     */
    const itemObject = this.getItemAsObject(itemKey);
    if (itemObject[objectFieldKey] == undefined) {
        itemObject[objectFieldKey] = {};
    }
    itemObject[objectFieldKey][objectFieldInObjectKey] = objectFieldInObjectValue;
    itemObject[objectFieldKey].last_changed = new Date();
    window.localStorage.setItem(itemKey, JSON.stringify(itemObject));
  }
  setItemInObject(itemKey, objectFieldKey, objectFieldValue) {
    const currentObject = this.getItemAsObject(itemKey);
    currentObject[objectFieldKey] = objectFieldValue;
    currentObject.last_changed = new Date();
    window.localStorage.setItem(itemKey, JSON.stringify(currentObject));
  }
  getItemAsObject(itemKey) {
    return JSON.parse(window.localStorage.getItem(itemKey)) || {};
  }
  checkLastChangedDaysDiff(lastChanged) {
    const now = new Date().getTime();
    const lastChangedTime = lastChanged.getTime();
    return Math.floor((now - lastChangedTime) / (1000*60*60*24));
  }
}

customElements.define("localstorage-helper", LocalstorageHelper);