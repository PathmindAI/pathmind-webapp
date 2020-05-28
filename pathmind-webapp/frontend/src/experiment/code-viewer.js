import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class CodeViewer extends PolymerElement {
    static get is() {
        return "code-viewer";
    }

    constructor() {
        super();
    }

    ready() {
        super.ready();
    }

    setValue(codeSnippet, rewardVariables = {}) {
        const codeElement = this.shadowRoot.querySelector("code");
        const operatorRe = /(?<!\/)[\+\-^\/|\*](?!\/)\=?|\=/g;
        const commentRe = /\/\/.+/g;
        const indexNumberRe = /\[[0-9]+\]/g;
        codeSnippet = renderToken(operatorRe, "operator");
        codeSnippet = renderToken(commentRe, "comment");
        codeSnippet = renderVariableToken(indexNumberRe, "index", rewardVariables);
        codeElement.innerHTML = codeSnippet;

        function renderToken(regexCondition, className) {
            return codeSnippet.replace(regexCondition, `<span class="token-${className}">$&</span>`);
        }

        function renderVariableToken(regexCondition, className, varList) {
            return codeSnippet.replace(regexCondition, function(matchedIndex) {
                return matchedIndex.replace(/[0-9]+/, function(indexNumber) {
                    const rewardVarName = Object.keys(varList).length > 0 && varList[indexNumber];
                    return `<span class="token-${className}">${indexNumber}</span><span class="variable-color-${indexNumber % 10}">${rewardVarName}</span>`;
                });
            });
        }
    }

    static get properties() {}

    static get template() {
        return html`
            <style>
                :host {
                    width: 100%;
                    font-size: var(--lumo-font-size-s);
                    border: 1px solid var(--pm-gray-color);
                    border-radius: var(--lumo-border-radius);
                }
                code {
                    box-sizing: border-box;
                    display: block;
                    width: 100%;
                    max-height: calc(1.8em * 6);
                    white-space: pre;
                    font-family: var(--code-font-family);
                    line-height: 1.8;
                    padding: 0 var(--lumo-space-xs);
                    margin: 0;
                    overflow: auto;
                }
                .token-operator {
                    color: rgb(127, 0, 85);
                }
                .token-index {
                    color: var(--pm-blue-color);
                }
                .token-comment {
                    color: var(--pm-gray-color-dark);
                }
                [class|="variable-color"] {
                    padding: 0.12em 0.3em;
                    border-radius: 4px;
                    margin: 0 0 0 0.285em;
                }
                .variable-color-0 {
                    color: #000;
                    background-color: #67ea93;
                }
                .variable-color-1 {
                    color: #fff;
                    background-color: #214e96;
                }
                .variable-color-2 {
                    color: #000;
                    background-color: #9bf7ec;
                }
                .variable-color-3 {
                    color: #fff;
                    background-color: #7550e5;
                }
                .variable-color-4 {
                    color: #000;
                    background-color: #b0f78c;
                }
                .variable-color-5 {
                    color: #000;
                    background-color: #ef99a4;
                }
                .variable-color-6 {
                    color: #000;
                    background-color: #9bc0f7;
                }
                .variable-color-7 {
                    color: #fff;
                    background-color: #931901;
                }
                .variable-color-8 {
                    color: #fff;
                    background-color: #f413bc;
                }
                .variable-color-9 {
                    color: #000;
                    background-color: #d1b112;
                }
            </style>
            <code></code>
        `;
    }
}

customElements.define(CodeViewer.is, CodeViewer);
