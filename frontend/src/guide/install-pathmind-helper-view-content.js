import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";

class InstallPathmindHelperViewContent extends PolymerElement {
  static get template() {
    return html`
      <style include="pathmind-dialog-view guide-view"></style>
      <div class="content">
        <div class="logo-wrapper">
          <img
            src="/frontend/images/pathmind-logo-100x100.png"
            alt="Pathmind Helper"
          />
        </div>
        <h1>Install Pathmind Helper</h1>
        <p>
          The Pathmind Helper is an
          <a href="[[arrayItem(links.*, 0, 'url')]]" target="_blank"
            >[[arrayItem(links.*, 0, 'text')]]</a
          >
          that enables you to apply reinforcement learning to your simulations.
          You need to install and configure the
          <a target="_blank" href="[[arrayItem(links.*, 1, 'url')]]"
            >[[arrayItem(links.*, 1, 'text')]]</a
          >
          inside your existing AnyLogic simulation.
        </p>
        <p>
          <a href="[[arrayItem(links.*, 2, 'url')]]" target="_blank"
            >[[arrayItem(links.*, 2, 'text')]]</a
          >
        </p>
        <div class="video-wrapper">
          <iframe
            src$="[[videoLink]]"
            frameborder="0"
            webkitallowfullscreen
            mozallowfullscreen
            allowfullscreen
          ></iframe>
        </div>
        <vaadin-button id="nextBtn" theme="secondary">
          Pathmind Helper Installed
        </vaadin-button>
        <vaadin-button id="backBtn" theme="tertiary">
          Back to Overview
        </vaadin-button>
      </div>
    `;
  }

  static get is() {
    return "install-pathmind-helper-view-content";
  }

  static get properties() {
    return {
      links: {
        value() {
          return [
            {
              text: "AnyLogic palette item",
              url:
                "https://help.anylogic.com/topic/com.anylogic.help/html/ui/Palette_View.html"
            },
            {
              text: "Pathmind Helper",
              url:
                "https://pathminddownloads.blob.core.windows.net/pathmind-download/PathmindHelper.jar"
            },
            {
              text: "Learn more about the Pathmind Helper",
              url:
                "https://help.pathmind.com/en/articles/3329544-getting-started"
            }
          ];
        }
      },
      videoLink: {
        value() {
          return `https://fast.wistia.net/embed/iframe/zujiq74yfe`;
        }
      }
    };
  }

  arrayItem(change, index, path) {
    return this.get(path, change.base[index]);
  }
}

customElements.define(
  InstallPathmindHelperViewContent.is,
  InstallPathmindHelperViewContent
);
