import {html, PolymerElement} from '@polymer/polymer/polymer-element.js';

/**
 * `segment-integrator`
 * 
 * SegmentIntegrator element.
 * 
 * @customElement
 * @polymer
 */
class SegmentIntegrator extends PolymerElement {

    static get template() {
        return html`
            <style include="shared-styles">
                :host {
                    display: block;
                }
            </style>
        `;
    }

    static get is() {
        return 'segment-integrator';
    }

    static get properties() {
        return {
        	sourceKey: {
        		type: String
        	},
        	user: {
        		type: Object
        	}
        };
    }
    
    static get observers() {
    	return [ '_userIdentified(user.*)', '_sourceKeyDefined(sourceKey)' ];
    }
    
    constructor() {
        super();
        var analytics=window.analytics=window.analytics||[];if(!analytics.initialize)if(analytics.invoked)window.console&&console.error&&console.error("Segment snippet included twice.");else{analytics.invoked=!0;analytics.methods=["trackSubmit","trackClick","trackLink","trackForm","pageview","identify","reset","group","track","ready","alias","debug","page","once","off","on"];analytics.factory=function(t){return function(){var e=Array.prototype.slice.call(arguments);e.unshift(t);analytics.push(e);return analytics}};for(var t=0;t<analytics.methods.length;t++){var e=analytics.methods[t];analytics[e]=analytics.factory(e)}analytics.load=function(t,e){var n=document.createElement("script");n.type="text/javascript";n.async=!0;n.src="https://cdn.segment.com/analytics.js/v1/"+t+"/analytics.min.js";var a=document.getElementsByTagName("script")[0];a.parentNode.insertBefore(n,a);analytics._loadOptions=e};analytics.SNIPPET_VERSION="4.1.0";}
    }
    
    _userIdentified(user) {
    	if (user != null){
    		window.analytics.identify(user.value.id, {
    			  name: user.value.name,
    			  email: user.value.email
    		});
    	}
    }
    
    _sourceKeyDefined(key){
    	window.analytics.load(key);
    }
    
    track(action, props){
    	window.analytics.track(action, props);
    }
    page(){
    	window.analytics.page();
    }
}

customElements.define(SegmentIntegrator.is, SegmentIntegrator);
