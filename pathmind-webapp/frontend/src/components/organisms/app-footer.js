import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class AppFooter extends PolymerElement {
    static get is() {
        return "app-footer";
    }

    static get template() {
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
                    margin: 0 var(--lumo-space-m);
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
                    margin-left: calc(var(--lumo-space-xxl) * 2);
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
                        <a href="{{privacylink}" target="_blank">
                            Privacy Policy
                        </a>
                    </li>
                    <li>
                        <a href="{{termslink}}" target="_blank">
                            Terms of Use
                        </a>
                    </li>
                    <li>
                        <a class="support" href="{{contactlink}}">
                            <iron-icon icon="vaadin:envelope-o"></iron-icon><span>Support</span>
                        </a>
                    </li>
                </ul>
                <span class="copyright">
                    © {{year}} Pathmind
                </span>
            </vaadin-horizontal-layout>
        `;
    }

    _attachDom(dom) {
      this.appendChild(dom);
    }

    getLogoLink(isLogoLinkToWebapp) {
        return isLogoLinkToWebapp ? "/" : "https://pathmind.com/";
    }

    static get properties() {
      return {
          privacylink: {
              type: String,
          },
          termslink: {
              type: String,
          },
          contactlink: {
              type: String,
          },
          year: {
              type: String,
              computed: "_getYear()",
          },
      };
    }

    _getYear() {
        return new Date().getFullYear();
    }
}

customElements.define(AppFooter.is, AppFooter);