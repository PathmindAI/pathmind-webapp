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
      codeSnippet += "Reward Term " + index + ": ";
      if (rewardTerm.rewardSnippet.length <= 0) {
        // reward variable
        const goalText = rewardTerm.goalCondition === "LTE" ? "Minimize" : "Maximize";
        codeSnippet += rewardTerm.rewardVariable + goalText + " x " + rewardTerm.weight+"\n";
      } else {
        // custom input through editor row
        codeSnippet += rewardTerm.rewardSnippet;
      }
    });
    this.codeSnippet = codeSnippet;
    codeElement.innerHTML = codeSnippet;
  }

  colorCode(snippet) {
    const commentRe = /\/\*(.|[\r\n])*?\*\/|(\/\/((?!\<span)(?!\<\/span\>).)+)|\/\//g;
    const numberRe = /[0-9]+/g;
    let codeSnippet = snippet;

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
    return document.createRange().createContextualFragment(codeSnippet);

    function renderToken(target, regexCondition, className) {
      return target.replace(regexCondition, `<span class="token-${className}">$&</span>`);
    }
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
          flex: 1 1 0%;
          width: 100%;
          font-size: var(--lumo-font-size-s);
          overflow: auto;
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
        .reward-terms-wrapper vaadin-horizontal-layout {
          counter-increment: number;
          width: 100%;
          line-height: 1.3;
          padding: var(--lumo-space-xxs);
        }
        .reward-terms-wrapper vaadin-horizontal-layout::before {
          content: counter(number);
          font-size: var(--lumo-font-size-s);
          color: var(--pm-grey-color);
          margin: 0 var(--lumo-space-xs) 0 var(--lumo-space-xxs);
        }
        .reward-terms-wrapper vaadin-horizontal-layout span {
          margin: 0 var(--lumo-space-xs);
        }
        .reward-terms-wrapper vaadin-horizontal-layout span:first-child {
          flex: 1 0 auto;
          margin-left: 0;
        }
        .reward-terms-wrapper vaadin-horizontal-layout .code-wrapper {
          flex: 1 0 auto;
          white-space: pre-wrap;
        }
      `;
  }

  render() {
    return html`
      <vaadin-vertical-layout class="reward-terms-wrapper">
        ${this.rewardTermsList.map(rewardTerm => {
          if (rewardTerm.rewardSnippet.length <= 0) {
            // reward variable
            const goalText = rewardTerm.goalCondition === "LTE" ? "Minimize" : "Maximize";
            return html`
              <vaadin-horizontal-layout>
                <span>${rewardTerm.rewardVariable}</span><span>${goalText}</span><span>x</span><span>${rewardTerm.weight}</span>
              </vaadin-horizontal-layout>`
          } else {
            // custom input through editor row
            return html`
              <vaadin-horizontal-layout>
                <div class="code-wrapper">${this.colorCode(rewardTerm.rewardSnippet)}</div><span>x</span><span>${rewardTerm.weight}</span>
              </vaadin-horizontal-layout>`
          }
        })}
      </vaadin-vertical-layout>
    `;
  }
}

customElements.define("reward-terms-viewer", RewardTermsViewer);
