if (!window.Pathmind) {
    window.Pathmind = {};
}
window.Pathmind.autocomplete = {
    enableAutoComplete: function(editorWrapper, localVariables) {
        var element = editorWrapper;
        var editor = editorWrapper.editor;

        // fetch ace's language tools module:
        var langTools = ace.require('ace/ext/language_tools');
        
        var variablesCompleter = {
                getCompletions: function(editor, session, pos, prefix, callback) {
                    callback(null, localVariables);
                }
            };

        editor.completers = [variablesCompleter];

        editor.setOptions({
            enableLiveAutocompletion: true,
            enableBasicAutocompletion: true
        });
    }
};