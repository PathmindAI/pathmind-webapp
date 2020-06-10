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

        // text in pseudo elements i.e. ::before and ::after are unselectable and uncopyable on
        // modern browsers like Chrome, Firefox, and Edge, however IE11 doesn't behave in the same way,
        // (user can paste the pseudo element text, but document.getSelection() excludes the pseudo element text)
        // so this copy eventlistener is needed. Once we remove support for IE11, this can be removed.
        const codeElement = this.shadowRoot.querySelector("code");
        codeElement.addEventListener("copy", event => {
            const selection = document.getSelection().toString();
            if (event.clipboardData) {
                event.clipboardData.setData("text/plain", selection);
            } else {
                window.clipboardData.setData("text", selection);
            }
            event.preventDefault();
        });
    }

    setValue(codeSnippet, rewardVariables = {}) {
        const codeElement = this.shadowRoot.querySelector("code");
        const operatorRe = /([\+\-\%\>\<\&\=\!\|]\=?)(?!(.+\*\/))|(?!\/)\/(?![\/\*]|\*(?![\/]))/g;
        const commentRe = /(\/\*([^\*\/]+)*.+\*\/)|(\/\/.+)/g;
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
                    if (rewardVarName) {
                        return `<span class="token-${className}">${indexNumber}</span><span class="variable-color-${indexNumber %
                            10}" data-content="${rewardVarName}"></span>`;
                    }
                    return `<span class="token-${className}">${indexNumber}</span>`;
                });
            });
        }
    }

    static get properties() {}

    static get template() {
        return html`
            <style>
                :host {
                    flex: 1;
                    width: 100%;
                    font-size: var(--lumo-font-size-s);
                    border: 1px solid var(--pm-gray-color);
                    border-radius: var(--lumo-border-radius);
                }
                code {
                    box-sizing: border-box;
                    display: block;
                    width: 100%;
                    max-height: calc(1.8em * 20);
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
                    color: rgb(113, 150, 130);
                }
                span[class|="variable-color"] {
                    -webkit-user-select: none;
                    -ms-user-select: none;
                    user-select: none;
                    padding: 0.12em 0.3em;
                    border-radius: 4px;
                    margin: 0 0 0 0.285em;
                    cursor: default;
                }
                span[class|="variable-color"]::before {
                    content: attr(data-content);
                }
                .variable-color-0 {
                    color: #000;
                    background-color: var(--variable-color-0);
                }
                .variable-color-1 {
                    color: #fff;
                    background-color: var(--variable-color-1);
                }
                .variable-color-2 {
                    color: #000;
                    background-color: var(--variable-color-2);
                }
                .variable-color-3 {
                    color: #fff;
                    background-color: var(--variable-color-3);
                }
                .variable-color-4 {
                    color: #000;
                    background-color: var(--variable-color-4);
                }
                .variable-color-5 {
                    color: #000;
                    background-color: var(--variable-color-5);
                }
                .variable-color-6 {
                    color: #000;
                    background-color: var(--variable-color-6);
                }
                .variable-color-7 {
                    color: #fff;
                    background-color: var(--variable-color-7);
                }
                .variable-color-8 {
                    color: #fff;
                    background-color: var(--variable-color-8);
                }
                .variable-color-9 {
                    color: #000;
                    background-color: var(--variable-color-9);
                }
            </style>
            <code></code>
        `;
    }
}

customElements.define(CodeViewer.is, CodeViewer);
