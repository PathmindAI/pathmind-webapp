
var APP_ID = PATHMIND_APP_ID;
var current_user_name = PATHMIND_USER_NAME;
var current_user_email = PATHMIND_USER_EMAIL;
var current_user_id = PATHMIND_USER_ID;

window.intercomSettings = {
    app_id: APP_ID,
    name: current_user_name,
    email: current_user_email,
    user_id: current_user_id,
    created_at: +new Date
};

(function(){var w=window;var ic=w.Intercom;if(typeof ic==="function"){ic('reattach_activator');ic('update',w.intercomSettings);}else{var d=document;var i=function(){i.c(arguments);};i.q=[];i.c=function(args){i.q.push(args);};w.Intercom=i;var l=function(){var s=d.createElement('script');s.type='text/javascript';s.async=true;s.src='https://widget.intercom.io/widget/' + APP_ID;var x=d.getElementsByTagName('script')[0];x.parentNode.insertBefore(s, x);};if(document.readyState==='complete'){l();}else if(w.attachEvent){w.attachEvent('onload',l);}else{w.addEventListener('load',l,false);}}})();
