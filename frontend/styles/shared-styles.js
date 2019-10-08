import '@polymer/polymer/lib/elements/custom-style.js';
const $_documentContainer = document.createElement('template');

$_documentContainer.innerHTML = `

<dom-module id="skymind-login" theme-for="vaadin-login-overlay-wrapper">
    <template>
        <style>
            [part="brand"] {
                background-image: url("data:image/svg+xml;base64,PHN2ZyB3aWR0aD0nODQnIGhlaWdodD0nNDgnIHZpZXdCb3g9JzAgMCA4NCA0OCcgeG1sbnM9J2h0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnJz48cGF0aCBkPSdNMCAwaDEydjZIMFYwem0yOCA4aDEydjZIMjhWOHptMTQtOGgxMnY2SDQyVjB6bTE0IDBoMTJ2Nkg1NlYwem0wIDhoMTJ2Nkg1NlY4ek00MiA4aDEydjZINDJWOHptMCAxNmgxMnY2SDQydi02em0xNC04aDEydjZINTZ2LTZ6bTE0IDBoMTJ2Nkg3MHYtNnptMC0xNmgxMnY2SDcwVjB6TTI4IDMyaDEydjZIMjh2LTZ6TTE0IDE2aDEydjZIMTR2LTZ6TTAgMjRoMTJ2Nkgwdi02em0wIDhoMTJ2Nkgwdi02em0xNCAwaDEydjZIMTR2LTZ6bTE0IDhoMTJ2NkgyOHYtNnptLTE0IDBoMTJ2NkgxNHYtNnptMjggMGgxMnY2SDQydi02em0xNC04aDEydjZINTZ2LTZ6bTAtOGgxMnY2SDU2di02em0xNCA4aDEydjZINzB2LTZ6bTAgOGgxMnY2SDcwdi02ek0xNCAyNGgxMnY2SDE0di02em0xNC04aDEydjZIMjh2LTZ6TTE0IDhoMTJ2NkgxNFY4ek0wIDhoMTJ2NkgwVjh6JyBmaWxsPSdyZ2IoNjMsIDgxLCAxODEpJyBmaWxsLW9wYWNpdHk9JzAuNCcgZmlsbC1ydWxlPSdldmVub2RkJy8+PC9zdmc+");
            }

            [part="card"] {
                width: calc(var(--lumo-size-m) * 12);
            }
        </style>
    </template>
</dom-module>

<dom-module id="skymind-app-layout" theme-for="vaadin-app-layout">
    <template>
        <style>
            :host {
            }

            [part="navbar"] {
                padding: 8px;
            }

            [part="navbar"] {

            }

            slot[name="menu"] {
                align-content: flex-end;
            }

            [part="content"] {

            }

        </style>
    </template>
</dom-module>
 


<custom-style>
  <style include="style">
    /* latin-ext */
   
   html {
        --lumo-font-family: Arial, "Helvetica Neue", Helvetica, sans-serif;
        --lumo-size-xl: 3rem;
        --lumo-size-l: 2.5rem;
        --lumo-size-m: 2rem;
        --lumo-size-s: 1.75rem;
        --lumo-size-xs: 1.5rem;
        --lumo-space-xl: 2.5rem;
        --lumo-space-l: 1.75rem;
        --lumo-space-m: 1.125rem;
        --lumo-space-s: 0.75rem;
        --lumo-space-xs: 0.375rem;
        --lumo-success-color-50pct: rgba(53, 151, 106, 0.5);
        --lumo-success-color-10pct: rgba(53, 151, 106, 0.1);
        --lumo-success-color: #35976A;
        --lumo-error-text-color: #D80947;
        --lumo-error-color-50pct: rgba(216, 9, 71, 0.5);
        --lumo-error-color-10pct: rgba(216, 9, 71, 0.1);
        --lumo-error-color: #D80947;
        --lumo-header-text-color: #000000;
        --lumo-body-text-color: #2B2B2B;
        --lumo-secondary-text-color: #888888;
        --lumo-tertiary-text-color: #D7D7D7;
        --lumo-disabled-text-color: #FFFFFF;
        --lumo-base-color: #FFFFFF;
        --lumo-primary-text-color: #5889F7;
        --lumo-primary-color-50pct: rgba(88, 137, 247, 0.5);
        --lumo-primary-color-10pct: rgba(88, 137, 247, 0.1);
        --lumo-primary-color: #5889F7;
    }
    
    body {

    }
    
    
  </style>
</custom-style>`;

document.head.appendChild($_documentContainer.content);
