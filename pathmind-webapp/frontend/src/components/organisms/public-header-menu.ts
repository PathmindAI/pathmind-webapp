import { LitElement, html, property } from "lit-element";

class PublicHeaderMenu extends LitElement {

    @property({type: String})
    contactlink = "";

    @property({type: Boolean})
    linktowebapp = false;

    render() {
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
                <a href="${this.getLogoLink()}"
                    class="logo-wrapper"
                    target="${this.linktowebapp ? '_self' : '_blank'}">
                    <img
                        class="logo"
                        src="frontend/images/pathmind-logo.svg"
                        alt="Pathmind logo"
                    />
                </a>
                <ul>
                    <li>
                        <a href="https://pathmind.com/" target="_blank">
                            What We Offer
                        </a>
                    </li>
                    <li>
                        <a href="https://pathmind.com/about/" target="_blank">
                            About Us
                        </a>
                    </li>
                </ul>
                <div class="right-cta">
                    <a class="support" href="${this.contactlink}">
                        <iron-icon icon="vaadin:envelope-o"></iron-icon><span>Support</span>
                    </a>
                </div>
            </vaadin-horizontal-layout>
        `;
    }

    createRenderRoot() {
      return this;
    }

    getLogoLink() {
        return this.linktowebapp ? "/" : "https://pathmind.com/";
    }
}

customElements.define("public-header-menu", PublicHeaderMenu);