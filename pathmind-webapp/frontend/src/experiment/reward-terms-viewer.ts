import { LitElement, html, css, property } from "lit-element";
import * as diff from "diff";

class RewardTermsViewer extends LitElement {
  @property({type: Boolean})
  isWithRewardTerms = false;
  @property({type: Boolean})
  comparisonIsWithRewardTerms = false;
  @property({type: String})
  codeSnippet = "";
  @property({type: String})
  comparisonCodeSnippet;
  @property({type: Boolean})
  hideHeaderRow = false;
  @property({type: Array})
  rewardTermsList = [];

  updated(changedProperties) {
    changedProperties.forEach((oldValue, name) => {
        if (name === "rewardTermsList") {
            this.renderRewardTerms();
        }
        // if (name === "codeSnippet" || name === "comparisonCodeSnippet" ||
        //     name === "isWithRewardTerms" || name === "comparisonIsWithRewardTerms") {
        //     this.renderCode();
        // }
    });
  }

  setRewardTerms(rewardTermsList) {
    this.rewardTermsList = rewardTermsList;
    console.log("rewardTermsList: "+rewardTermsList)
  }

  renderRewardTerms() {
    const codeElement = this.shadowRoot.querySelector("code");
    let codeSnippet = "";
    this.rewardTermsList.forEach((rewardTerm, index) => {
      codeSnippet += "Reward Term " + rewardTerm.index + ": ";
      if (rewardTerm.rewardSnippet.length <= 0) {
        // reward variable
        const goalText = rewardTerm.goalCondition === "LTE" ? "Minimize" : "Maximize";
        codeSnippet += rewardTerm.rewardVariableIndex + goalText + " x " + rewardTerm.weight+"\n";
      } else {
        // custom input through editor row
        codeSnippet += rewardTerm.rewardSnippet;
      }
    });
    this.codeSnippet = codeSnippet;
    codeElement.innerHTML = codeSnippet;
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
    
        if (this.codeSnippet && this.comparisonCodeSnippet && this.isWithRewardTerms === this.comparisonIsWithRewardTerms) {
          const comparisonCodeSnippet = this.comparisonCodeSnippet.replaceAll("\r\n", "\n");
          const codeDiff = diff.diffWordsWithSpace(codeSnippet.replaceAll("\r\n", "\n"), comparisonCodeSnippet);
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
        .header-row {
          position: sticky;
          top: 0;
          width: calc(100% - var(--lumo-space-xs) * 2);
          line-height: 1;
          font-size: var(--lumo-font-size-s);
          color: var(--pm-secondary-text-color);
          background-color: white;
          padding: var(--lumo-space-xs);
          border-bottom: 1px solid var(--pm-grey-color-lightest);
          margin: 0 var(--lumo-space-xs);
          z-index: 100;
        }
        .header-row span:nth-child(1) {
          flex: 1 0 auto;
          padding-left: 1.1rem;
        }
        .header-row span {
          width: calc(var(--lumo-font-size-s) * 12);
        }
      `;
  }

  render() {
    return html`
      <vaadin-horizontal-layout class="header-row" ?hidden="${this.hideHeaderRow}">
        <span>Metric</span>
        <span>Goal</span>
        <span>Weight</span>
      </vaadin-horizontal-layout>
      <code>
      </code>
    `;
  }
}

customElements.define("reward-terms-viewer", RewardTermsViewer);
