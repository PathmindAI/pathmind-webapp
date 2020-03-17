var editor;
var rewardVariables = {}

function createVariableNameHints(){
	for (var i = 0; i < editor.session.getLength(); i++){
		createHintsForLine(i);
	}
}
function createHintsForLine(line){
	let value = editor.session.getLine(line);
	let matchInfo = value.match(/\[[0-9 ]+\]/);
	let index = 0;
	while (matchInfo){
		index = index + matchInfo.index;
		matchVariableNameAndFold(matchInfo[0], index, line);
		value = value.substring(matchInfo.index + matchInfo[0].length);
		index += matchInfo[0].length;
		matchInfo = value.match(/\[[0-9 ]+\]/);
	}
}

function matchVariableNameAndFold(matchingValue, index, row){
	let matchInfo = matchingValue.match(/[0-9]+/);
	if (matchInfo){
		let variableIndex = matchInfo[0];
		if (rewardVariables[variableIndex]){
			let variableName = "\"" + rewardVariables[variableIndex] + "\" " + variableIndex;
			let foldLocation = index + matchInfo.index;
			let fold = editor.session.addFold(variableName, new Range(row, foldLocation, row, foldLocation + matchInfo[0].length));
			fold.preventExpand = true;
		}
	}
}

function onChange(e) {
	var changedValue = e.lines[0];
	if (changedValue.match(/[\[0-9\]]/)){
		var row = e.start.row;
		var existingFolds = editor.session.getFoldLine(row, row);
		if (existingFolds){
			editor.session.removeFolds(existingFolds.folds);
		}
		createHintsForLine(row);
	}
}

function expandFold(fold) {
	if (fold.preventExpand){
		return;
	}
    this.removeFold(fold);
    fold.subFolds.forEach(function(subFold) {
        fold.restoreRange(subFold);
        this.addFold(subFold);
    }, this);
    if (fold.collapseChildren > 0) {
        this.foldAll(fold.start.row+1, fold.end.row, fold.collapseChildren-1);
    }
    fold.subFolds = [];
}
if (!window.Pathmind){
	window.Pathmind = {};
}
window.Pathmind.CodeEditor = {
	    addVariableNamesSupport: function(editorWrapper) {
	    	editor = editorWrapper.editor;
	    	Range=ace.require("ace/range").Range;
	    	editor.session.expandFold = expandFold;
	    	createVariableNameHints();
	    	editor.session.on("change", e => onChange(e));
	    },
	    setVariableNames: function(vars){
	    	rewardVariables = vars;
	    }

};
