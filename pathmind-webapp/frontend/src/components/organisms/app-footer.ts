import { LitElement, html } from "lit-element";

class AppFooter extends LitElement {
    render() {
        return html`
            <style>
                app-footer {
                    box-sizing: border-box;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    position: relative;
                    width: 100%;
                    font-family: var(--lumo-font-family-header);
                    font-size: var(--lumo-font-size-s);
                    padding: 0 var(--lumo-space-m);
                }
                app-footer vaadin-horizontal-layout {
                    justify-content: center;
                    align-items: center;
                    width: 100%;
                }
                app-footer ul {
                    display: flex;
                    list-style: none;
                    padding: 0;
                    margin: 0;
                }
                app-footer li {
                    margin: 0 var(--lumo-space-s);
                }
                app-footer a {
                    box-sizing: border-box;
                    display: flex;
                    align-items: center;
                    position: relative;
                    height: 100%;
                    color: var(--lumo-body-text-color);
                    padding: var(--lumo-space-s) 0;
                    text-decoration: none;
                    overflow: hidden;
                }
                app-footer a:hover {
                    color: var(--lumo-secondary-text-color);
                }
                app-footer a.support {
                    display: flex;
                    color: var(--lumo-body-text-color);
                    text-decoration: none;
                    margin: 0;
                }
                app-footer a.support:hover {
                    color: var(--lumo-secondary-text-color);
                }
                app-footer a.support iron-icon {
                    --iron-icon-width: var(--lumo-font-size-m);
                    --iron-icon-height: var(--lumo-font-size-m);
                    margin-right: var(--lumo-space-xs);
                }
                app-footer .copyright {
                    display: inline-flex;
                    margin-left: var(--lumo-space-s);
                }
                @media screen and (max-width: 768px) {
                    app-footer .support span {
                        display: none;
                    }
                    app-footer li {
                        margin: 0 var(--lumo-space-s);
                    }
                }
            </style>
            <vaadin-horizontal-layout>
                <ul>
                    <li>
                        <a href="https://pathmind.com/privacy" target="_blank">
                            Privacy Policy
                        </a>
                    </li>
                    <li>
                        <a href="https://pathmind.com/subscription-agreement" target="_blank">
                            Terms of Use
                        </a>
                    </li>
                    <li>
                        <a class="support" href="mailto:support@pathmind.com">
                            <iron-icon icon="vaadin:envelope-o"></iron-icon><span>Support</span>
                        </a>
                    </li>
                </ul>
                <span class="copyright">
                    © ${this._getYear()} Pathmind
                </span>
            </vaadin-horizontal-layout>
        `;
    }

    createRenderRoot() {
        return this;
    }

    _getYear() {
        return new Date().getFullYear();
    }
}

customElements.define("app-footer", AppFooter);