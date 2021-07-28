import { LitElement, html, css, property } from "lit-element";
import * as diff from "diff";

class CodeViewer extends LitElement {
  @property({type: String})
  codeSnippet = "";

  @property({type: String})
  comparisonCodeSnippet;

  @property({type: Boolean, reflect: true, attribute: "show-copy-button"})
  showCopyButton = true;

  @property({type: Boolean, reflect: true, attribute: "show-border"})
  showBorder = true;
  
  @property({type: String})
  rewardVariables = "";

  updated(changedProperties) {
    changedProperties.forEach((oldValue, name) => {
        if (name === "codeSnippet" || name === "comparisonCodeSnippet") {
            this.renderCode();
        }
    });
  }

  firstUpdated() {
    const codeElement = this.shadowRoot.querySelector("code");
    codeElement.addEventListener("copy", event => {
      // This will handle the clipboard data to eliminate extra linebreak at the end of the string
      const selection = document.getSelection().toString();
      if (event.clipboardData) {
        event.clipboardData.setData("text/plain", selection);
      } else {
        (window as any).clipboardData.setData("text", selection);
      }
      event.preventDefault();
    });

    const copyButton = this.shadowRoot.querySelector("vaadin-button");
    copyButton.addEventListener("click", event => {
      const copyIcon = copyButton.querySelector("iron-icon:first-child");
      const checkmarkIcon = copyButton.querySelector("iron-icon:last-child");
      const range = document.createRange();
      range.selectNode(codeElement);
      const select = window.getSelection();
      select.removeAllRanges();
      select.addRange(range);
      document.execCommand("copy");
      select.removeAllRanges();

      copyIcon.removeAttribute("active");
      checkmarkIcon.setAttribute("active", "true");
      setTimeout(function() {
        copyIcon.setAttribute("active", "true");
        checkmarkIcon.removeAttribute("active");
      }, 800);
    });
  }

  renderCode() {
    const codeElement = this.shadowRoot.querySelector("code");
    if (codeElement) {
        const commentRe = /\/\*(.|[\r\n])*?\*\/|(\/\/((?!\<span)(?!\<\/span\>).)+)|\/\//g;
        const numberRe = /[0-9]+/g;
        let codeSnippet = this.codeSnippet;
    
        if (this.comparisonCodeSnippet) {
            this.classList.add("comparison");
        } else {
            this.classList.remove("comparison");
        }
    
        if (this.codeSnippet && this.comparisonCodeSnippet) {
          const comparisonCodeSnippet = this.comparisonCodeSnippet.replaceAll("\r\n", "\n");
          const codeDiff = diff.diffWords(codeSnippet.replaceAll("\r\n", "\n"), comparisonCodeSnippet);
          let processedCodeSnippet = "";
          codeDiff.forEach(part => {
            if (part.removed) {
              processedCodeSnippet += `<span class="highlight-label">${part.value}</span>`;
            } else if (!part.added) {
              processedCodeSnippet += part.value;
            }
          });
          codeSnippet = processedCodeSnippet;
        }
    
        codeSnippet = renderToken(codeSnippet, commentRe, "comment");
        codeSnippet = renderToken(codeSnippet, numberRe, "number");
        codeElement.innerHTML = codeSnippet;
    }

    function renderToken(target, regexCondition, className) {
      return target.replace(regexCondition, `<span class="token-${className}">$&</span>`);
    }
  }

  static get styles() {
      return css`
        /* Customized Scrollbar for WebKit Browsers */
        ::-webkit-scrollbar {
            width: 6px;
        }
        
        ::-webkit-scrollbar-track {
        background-color: var(--pm-app-bg-color);
        }
        
        ::-webkit-scrollbar-thumb {
        background-color: var(--pm-grey-color-light);
        }
        :host {
            position: relative;
            box-sizing: border-box;
            flex: 1;
            width: 100%;
            font-size: 0.8125rem;
        }
        :host([show-border]) {
            border: 1px solid var(--pm-grey-color);
        }
        code {
            box-sizing: border-box;
            display: block;
            width: 100%;
            height: 100%;
            white-space: pre-wrap;
            font-family: var(--code-font-family);
            line-height: 1.8;
            padding: var(--lumo-space-xs) var(--lumo-space-s);
            margin: 0;
        }
        :host(.comparison) code {
            height: 16rem;
        }
        :host([show-border]) code {
            overflow: auto;
        }
        vaadin-button {
            display: none;
            position: absolute;
            width: 28px;
            min-width: auto;
            height: 28px;
            top: -1px;
            right: -1px;
            padding: 0;
            background-color: rgba(200,200,200,0.6);
            border-radius: 0;
            margin: 0;
        }
        :host([show-copy-button]) vaadin-button {
            display: block;
        }
        iron-icon {
            position: absolute;
            width: 24px;
            height: 24px;
            top: 2px;
            left: 2px;
            opacity: 0;
            transition: opacity 0.3s;
        }
        iron-icon[active] {
            opacity: 1;
        }
        .token-operator {
            color: rgb(127, 0, 85);
        }
        .token-number {
            color: var(--pm-blue-color-dark);
        }
        .token-comment,
        .token-comment * {
            color: rgb(113, 150, 130);
        }
        .highlight-label {
            background-color: var(--pm-yellow-color);
        }
      `;
  }

  render() {
    return html`
      <code></code>
      <vaadin-button>
          <iron-icon icon="vaadin:copy-o" active></iron-icon>
          <iron-icon icon="vaadin:check"></iron-icon>
      </vaadin-button>
    `;
  }
}

customElements.define("code-viewer", CodeViewer);
