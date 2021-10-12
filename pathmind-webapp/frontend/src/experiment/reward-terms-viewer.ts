import { LitElement, html, css, property } from "lit-element";
import * as diff from "diff";

class RewardTermsViewer extends LitElement {
  @property({type: Boolean})
  isWithRewardTerms = false;
  @property({type: Boolean})
  comparisonIsWithRewardTerms = false;
  @property({type: Array})
  rewardTermsList = [];
  @property({type: Array})
  comparisonRewardTermsList = [];

  setRewardTerms(rewardTermsList) {
    this.rewardTermsList = rewardTermsList;
  }

  setComparisonRewardTerms(comparisonRewardTermsList) {
    this.comparisonRewardTermsList = comparisonRewardTermsList;
  }

  colorCode(snippet, index) {
    const commentRe = /\/\*(.|[\r\n])*?\*\/|(\/\/((?!\<span)(?!\<\/span\>).)+)|\/\//g;
    const numberRe = /[0-9]+/g;

    let codeSnippet = this.highlightDiff("rewardSnippet", snippet, index);

    codeSnippet = renderToken(codeSnippet, commentRe, "comment");
    codeSnippet = renderToken(codeSnippet, numberRe, "number");
    return document.createRange().createContextualFragment(codeSnippet);

    function renderToken(target, regexCondition, className) {
      return target.replace(regexCondition, `<span class="token-${className}">$&</span>`);
    }
  }

  highlightDiff(type, text, index) {
    let prop = "";
    switch (type) {
      case "weight":
        prop = "weight";
        break;
      case "goal":
        prop = "goalCondition";
        break;
      case "rewardSnippet":
        prop = "rewardSnippet";
        break;
      case "name":
      default:
        prop = "rewardVariable";
    }
    let finalText = text;
    if (this.rewardTermsList.length > 1 && this.comparisonRewardTermsList.length > 1 && this.isWithRewardTerms === this.comparisonIsWithRewardTerms) {
      let comparisonText = "";
      if (this.comparisonRewardTermsList[index]) {
        if (type === "goal") {
          comparisonText = this.comparisonRewardTermsList[index][prop] === "LTE" ? "Minimize" : "Maximize";
        } else {
          comparisonText = this.comparisonRewardTermsList[index][prop].replaceAll("\r\n", "\n");
        }
      }
      console.log(`%c${finalText} ${comparisonText}`, "background: yellow")
      const textDiff = diff.diffWordsWithSpace(text.replaceAll("\r\n", "\n"), comparisonText);
      let processedText = "";
      textDiff.forEach(part => {
        if (part.removed) {
          processedText += `<span class="highlight-label">${part.value}</span>`;
        } else if (!part.added) {
          processedText += part.value;
        }
      });
      finalText = processedText;
    }
    if (type == "rewardSnippet") {
      return finalText;
    }
    return document.createRange().createContextualFragment(finalText);
  }

  highlightGoalDiff(text, index) {
    return this.highlightDiff("goal", text, index);
  }

  highlightWeightDiff(text, index) {
    return this.highlightDiff("weight", text, index);
  }

  highlightNameDiff(text, index) {
    return this.highlightDiff("name", text, index);
  }

  static get styles() {
      return css`
        /* Customized Scrollbar for WebKit Browsers */
        ::-webkit-scrollbar {
          width: 6px;
          height: 6px;
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
          font-size: var(--lumo-font-size-xs);
          font-family: var(--code-font-family);
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
        .reward-terms-wrapper {
          padding: var(--lumo-space-xs) 0;
        }
        .reward-terms-wrapper vaadin-horizontal-layout {
          counter-increment: number;
          width: 100%;
          line-height: 1.3;
          padding: var(--lumo-space-xxxs);
        }
        .reward-terms-wrapper vaadin-horizontal-layout::before {
          content: counter(number);
          font-size: var(--lumo-font-size-s);
          color: var(--pm-grey-color);
          margin: 0 var(--lumo-space-xs) 0 var(--lumo-space-xxs);
        }
        .reward-terms-wrapper vaadin-horizontal-layout > span {
          margin: 0 var(--lumo-space-xs);
        }
        .reward-terms-wrapper vaadin-horizontal-layout > span:first-child {
          flex: 1 0 auto;
          margin-left: 0;
        }
        .reward-terms-wrapper vaadin-horizontal-layout .code-wrapper {
          flex: 1 1 auto;
          white-space: pre-wrap;
        }
      `;
  }

  render() {
    return html`
      <vaadin-vertical-layout class="reward-terms-wrapper">
        ${this.rewardTermsList.map((rewardTerm, index) => {
          if (rewardTerm.rewardSnippet.length <= 0) {
            // reward variable
            const goalText = rewardTerm.goalCondition === "LTE" ? "Minimize" : "Maximize";
            return html`
              <vaadin-horizontal-layout>
                <span>${this.highlightNameDiff(rewardTerm.rewardVariable, index)}</span><span>${this.highlightGoalDiff(goalText, index)}</span><span>x</span><span>${this.highlightWeightDiff(rewardTerm.weight, index)}</span>
              </vaadin-horizontal-layout>`
          } else {
            // custom input through editor row
            return html`
              <vaadin-horizontal-layout>
                <div class="code-wrapper">${this.colorCode(rewardTerm.rewardSnippet, index)}</div><span>x</span><span>${rewardTerm.weight}</span>
              </vaadin-horizontal-layout>`
          }
        })}
      </vaadin-vertical-layout>
    `;
  }
}

customElements.define("reward-terms-viewer", RewardTermsViewer);
