window.experimentViewLoad = function() {
    const appLayoutSlot = document.querySelector('vaadin-app-layout').shadowRoot.querySelector('div[content] slot');
    // Need to use setTimeout to ensure the code runs after the dom elements are attached
    const experimentViewLoadListener = () => setTimeout(function(event) {
        const panelSplits = document.querySelector('localstorage-helper').getItemAsObject('panels_split');
        if (document.querySelector('.middle-panel')) {
            const exp_12 = panelSplits['exp_12'];
            if (exp_12) {
                document.querySelector('.middle-panel > [slot="primary"] > [slot="primary"]').style.flex = exp_12['primary'];
                document.querySelector('.middle-panel > [slot="primary"] > [slot="secondary"]').style.flex = exp_12['secondary'];
            }
            const exp_23 = panelSplits['exp_23'];
            if (exp_23) {
                document.querySelector('.middle-panel > [slot="primary"]').style.flex = exp_23['primary'];
                document.querySelector('.middle-panel > [slot="secondary"]').style.flex = exp_23['secondary'];
            }
        }
        appLayoutSlot.removeEventListener('slotchange', experimentViewLoadListener);
    }, 0);

    appLayoutSlot.addEventListener('slotchange', experimentViewLoadListener);
}