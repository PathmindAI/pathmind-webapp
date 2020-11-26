import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class PublicHeaderMenu extends PolymerElement {
    static get is() {
        return "public-header-menu";
    }

    static get template() {
        return html`
            <style>
                public-header-menu {
                    box-sizing: border-box;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    position: relative;
                    width: 100%;
                    font-family: var(--lumo-font-family-header);
                    background-color: white;
                    padding: 0 var(--lumo-space-m);
                    border-bottom: 1px solid var(--pm-grey-color-lightest);
                }
                public-header-menu vaadin-horizontal-layout {
                    max-width: 1080px;
                    width: 100%;
                }
                public-header-menu .logo {
                    display: block;
                    width: 160px;
                    margin-right: var(--lumo-space-xl);
                }
                public-header-menu ul {
                    display: flex;
                    align-items: stretch;
                    flex: 1 1 auto;
                    list-style: none;
                    margin: 0;
                    padding: 0;
                }
                public-header-menu li {
                    margin: 0 var(--lumo-space-m);
                }
                public-header-menu a {
                    box-sizing: border-box;
                    display: flex;
                    align-items: center;
                    position: relative;
                    height: 100%;
                    font-size: var(--lumo-font-size-s);
                    font-weight: 600;
                    color: var(--lumo-body-text-color);
                    padding: var(--lumo-space-s) 0;
                    text-decoration: none;
                    overflow: hidden;
                }
                public-header-menu a:hover {
                    text-decoration: none;
                }
                public-header-menu ul a::after {
                    content: '';
                    display: block;
                    position: absolute;
                    width: 100%;
                    height: 3px;
                    left: -100%;
                    bottom: -1px;
                    background-color: var(--lumo-primary-color);
                    transition: all 0.2s;
                }
                public-header-menu ul a:hover::after {
                    left: 0;
                }
                public-header-menu .right-cta {
                    justify-self: flex-end;
                }
                public-header-menu .right-cta a.support {
                    display: flex;
                    color: var(--lumo-secondary-text-color);
                    text-decoration: none;
                    margin: 0;
                }
                public-header-menu .right-cta a.support:hover {
                    color: var(--pm-grey-color-darker);
                }
                public-header-menu .right-cta iron-icon {
                    --iron-icon-width: var(--lumo-font-size-m);
                    --iron-icon-height: var(--lumo-font-size-m);
                    margin-right: var(--lumo-space-xs);
                }
                @media screen and (max-width: 768px) {
                    public-header-menu .logo {
                      width: 120px;
                      margin-right: var(--lumo-space-m);
                    }
                    public-header-menu .support span {
                        display: none;
                    }
                    public-header-menu li {
                        margin: 0 var(--lumo-space-s);
                    }
                }
            </style>
            <vaadin-horizontal-layout>
                <a href="{{logoLink}}" class="logo-wrapper"><img
                    class="logo"
                    src="frontend/images/pathmind-logo.svg"
                    alt="Pathmind logo"
                /></a>
                <ul>
                    <li>
                        <a href="https://pathmind.com/">
                            What We Offer
                        </a>
                    </li>
                    <li>
                        <a href="https://pathmind.com/about/">
                            About Us
                        </a>
                    </li>
                </ul>
                <div class="right-cta">
                    <a class="support" href="{{contactlink}}">
                        <iron-icon icon="vaadin:envelope-o"></iron-icon><span>Support</span>
                    </a>
                </div>
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
          contactlink: {
              type: String,
          },
          linktowebapp: {
              type: Boolean,
              value: false,
          },
          logoLink: {
              type: String,
              computed: 'getLogoLink(linktowebapp)',
          },
      };
    }
}

customElements.define(PublicHeaderMenu.is, PublicHeaderMenu);