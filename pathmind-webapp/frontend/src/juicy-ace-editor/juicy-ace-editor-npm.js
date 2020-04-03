/**
    Custom Element with Ace code editor
    http://juicy.github.io/juicy-ace-editor/
    version: 2.2.1
    @demo index.html
    @license MIT
    @author
*/
import "ace-builds/src-noconflict/ace.js";

import "ace-builds/src-noconflict/ext-searchbox.js";
import "ace-builds/src-noconflict/ext-beautify.js";

const $template = document.createElement("template");
$template.innerHTML = `<template id="juicy-ace-editor">
    <style>
        :host{
            display: flex;
            min-height: 1em;
            flex-direction: column;
        }
        #juicy-ace-editor-container{
            flex: 1;
            width: 100%;
            height: 100%;
        }
    </style>
    <div id="juicy-ace-editor-container"></div>
</template>`;

document.head.appendChild($template.content);

// Gets content from <template>
var template = document.querySelector("template#juicy-ace-editor").content;

// Creates an object based in the HTML Element prototype
window.customElements.define(
  "juicy-ace-editor",
  class JuicyAceEditor extends HTMLElement {
    // getter/setter for value property
    get value() {
      return (this.editor && this.editor.getValue()) || this.textContent;
    }
    set value(val) {
      if (this.editor) {
        this._ignoreChange = true;
        this.editor.setValue(val);
        this._ignoreChange = false;
      } else {
        this.textContent = val;
      }
    }
    // list of observable attributes
    static get observedAttributes() {
      return [
        "theme",
        "mode",
        "fontsize",
        "softtabs",
        "tabsize",
        "readonly",
        "wrapmode",
        "min-lines",
        "max-lines",
        "shadow-style"
      ];
    }

    // Fires when an instance of the element is created
    constructor() {
      super();
      this._ignoreChange = false;
      // Creates the shadow root
      var shadowRoot = this.attachShadow({ mode: "open" });
      // Adds a template clone into shadow root
      var clone = document.importNode(template, true);
      // getElementById may not be polyfilled yet
      this.container = clone.querySelector("#juicy-ace-editor-container");
      shadowRoot.appendChild(clone);
    }

    connectedCallback() {
      var text = this.childNodes[0];
      var container = this.container;
      var element = this;
      var editor;

      if (this.editor) {
        editor = this.editor;
      } else {
        // container.appendChild(text);
        container.textContent = this.value;
        const options = {};
        // support autoresizing
        this.hasAttribute("max-lines") &&
          (options.maxLines = Number(this.getAttribute("max-lines")));
        this.hasAttribute("min-lines") &&
          (options.minLines = Number(this.getAttribute("min-lines")));

        editor = ace.edit(container, options);
        this.dispatchEvent(
          new CustomEvent("editor-ready", {
            bubbles: true,
            composed: true,
            detail: editor
          })
        );
        this.editor = editor;

        // inject base editor styles
        this.injectTheme("#ace_editor\\.css");
        this.injectTheme("#ace-tm");
        this.injectTheme("#ace_searchbox");

        editor.getSession().on("change", event => {
          if (!this._ignoreChange) {
            element.dispatchEvent(
              new CustomEvent("change", {
                bubbles: true,
                composed: true,
                detail: event
              })
            );
          }
        });

        this._beautify = ace.require("ace/ext/beautify");
      }

      // handle theme changes
      editor.renderer.addEventListener(
        "themeLoaded",
        this.onThemeLoaded.bind(this)
      );

      // initial attributes
      editor.setTheme(this.getAttribute("theme"));
      editor.setFontSize(Number(this.getAttribute("fontsize")) || 12);
      editor.setReadOnly(this.hasAttribute("readonly"));
      var session = editor.getSession();
      session.setMode(this.getAttribute("mode"));
      session.setUseSoftTabs(this.getAttribute("softtabs"));
      this.getAttribute("tabsize") &&
        session.setTabSize(this.getAttribute("tabsize"));
      session.setUseWrapMode(this.hasAttribute("wrapmode"));
      // non ace specific
      this.hasAttribute("shadow-style") &&
        this.injectTheme(this.getAttribute("shadow-style"));

      // Observe input textNode changes
      // Could be buggy as editor was also added to Light DOM;
      var observer = new MutationObserver(function(mutations) {
        mutations.forEach(function(mutation) {
          // console.log("observation", mutation.type, arguments, mutations, editor, text);
          if (mutation.type == "characterData") {
            element.value = text.data;
          }
        });
      });
      text && observer.observe(text, { characterData: true });
      // container.appendChild(text);
      this._attached = true;
    }
    beautify() {
      this._beautify.beautify(this.editor.session);
    }
    disconnectedCallback() {
      this._attached = false;
    }
    attributeChangedCallback(attr, oldVal, newVal) {
      if (!this._attached) {
        return false;
      }
      switch (attr) {
        case "theme":
          this.editor.setTheme(newVal);
          break;
        case "mode":
          this.editor.getSession().setMode(newVal);
          break;
        case "fontsize":
          this.editor.setFontSize(newVal);
          break;
        case "softtabs":
          this.editor.getSession().setUseSoftTabs(newVal);
          break;
        case "tabsize":
          this.editor.getSession().setTabSize(newVal);
          break;
        case "readonly":
          this.editor.setReadOnly(newVal === "" || newVal);
          break;
        case "wrapmode":
          this.editor.getSession().setUseWrapMode(newVal !== null);
          break;
        case "max-lines":
          this.editor && (this.editor.renderer.$maxLines = Number(newVal));
          break;
        case "min-lines":
          this.editor && (this.editor.renderer.$minLines = Number(newVal));
          break;
        // non-Ace specific
        case "shadow-style":
          if (oldVal) {
            this.shadowRoot.querySelector(oldVal).remove();
          }
          this.injectTheme(newVal);
      }
    }
    onThemeLoaded(e) {
      var themeId = "#" + e.theme.cssClass;
      this.injectTheme(themeId);
      // Workaround Chrome stable bug, force repaint
      this.container.style.display = "none";
      this.container.offsetHeight;
      this.container.style.display = "";
    }
    /**
     * Injects a style element into juicy-ace-editor's shadow root
     * @param {CSSSelector} selector for an element in the same shadow tree or document as `juicy-ace-editor`
     */
    injectTheme(selector) {
      const lightStyle =
        this.getRootNode().querySelector(selector) ||
        document.querySelector(selector);
      this.shadowRoot.appendChild(lightStyle.cloneNode(true));
    }
  }
);
