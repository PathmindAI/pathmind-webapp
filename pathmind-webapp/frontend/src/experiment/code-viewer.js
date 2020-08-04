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

        const copyButton = this.shadowRoot.querySelector("vaadin-button");
        copyButton.addEventListener("click", event => {
            const copyIcon = copyButton.querySelector("iron-icon:first-child");
            const checkmarkIcon = copyButton.querySelector("iron-icon:last-child");
            const range = document.createRange();
            range.selectNode(codeElement);
            const select = window.getSelection();
            select.removeAllRanges();
            select.addRange(range);
            if (window.clipboardData) {
                // This is for IE11.
                window.clipboardData.setData("text", select.toString());
            } else {
                document.execCommand("copy");
            }
            select.removeAllRanges();

            copyIcon.removeAttribute("active");
            checkmarkIcon.setAttribute("active", true);
            setTimeout(function() {
                copyIcon.setAttribute("active", true);
                checkmarkIcon.removeAttribute("active");
            }, 800);
        });
    }

    setValue(codeSnippet, rewardVariables = {}) {
        const codeElement = this.shadowRoot.querySelector("code");
        const operatorRe = /([\+\-\%\>\<\&\=\!\|]\=?)(?!(.+\*\/))|(?!\/)\/(?![\/\*]|\*(?![\/]))/g;
        const commentRe = /\/\*(.|[\r\n])*?\*\/|(\/\/.+)/g;
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
                    position: relative;
                    box-sizing: border-box;
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
                    height: 100%;
                    white-space: pre;
                    font-family: var(--code-font-family);
                    line-height: 1.8;
                    padding: var(--lumo-space-xs) var(--lumo-space-s);
                    margin: 0;
                    overflow: auto;
                }
                vaadin-button {
                    position: absolute;
                    width: 28px;
                    min-width: auto;
                    height: 28px;
                    top: -1px;
                    right: -1px;
                    padding: 0;
                    background-color: rgba(200,200,200,0.6);
                    border-radius: 0 var(--lumo-border-radius) 0 0;
                    margin: 0;
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
                .token-index {
                    color: var(--pm-blue-color);
                }
                .token-comment,
                .token-comment * {
                    color: rgb(113, 150, 130);
                }
                span[class|="variable-color"] {
                    -webkit-user-select: none;
                    -ms-user-select: none;
                    user-select: none;
                    padding: 0.12em 0.3em;
                    border-radius: var(--lumo-border-radius-s);
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
            <vaadin-button>
                <iron-icon icon="vaadin:copy-o" active></iron-icon>
                <iron-icon icon="vaadin:check"></vaadin-button>
            </div>
        `;
    }
}

customElements.define(CodeViewer.is, CodeViewer);
