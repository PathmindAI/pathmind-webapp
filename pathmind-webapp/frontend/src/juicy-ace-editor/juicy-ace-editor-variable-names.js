var element;
var editor;
var rewardVariables = {};
var lineValidationCache = {};
var variableCount;

function createVariableNameHints() {
    ensureLineValidationCache();
    for (var i = 0; i < editor.session.getLength(); i++) {
        createHintsForLine(i);
    }
    calculateValidationsAndDispatchEvent();
}
function createHintsForLine(line) {
    let existingFolds = editor.session.getFoldLine(line, line);
    if (existingFolds) {
        editor.session.removeFolds(existingFolds.folds);
    }
    let value = editor.session.getLine(line);
    let matchInfo = value.match(/\[[\-0-9 ]+\]/);
    let index = 0;
    lineValidationCache[line] = [];
    while (matchInfo) {
        index = index + matchInfo.index;
        matchVariableNameAndFold(matchInfo[0], index, line);
        value = value.substring(matchInfo.index + matchInfo[0].length);
        index += matchInfo[0].length;
        matchInfo = value.match(/\[[\-0-9 ]+\]/);
    }
}

function matchVariableNameAndFold(matchingValue, index, row) {
    const matchInfo = matchingValue.match(/[\-0-9]+/);
    if (matchInfo) {
        const variableIndex = matchInfo[0];
        if (variableIndex < 0 || variableIndex >= variableCount) {
            lineValidationCache[row].push(variableIndex);
        }
        if (rewardVariables[variableIndex]) {
            const variableName = variableIndex + " " + rewardVariables[variableIndex];
            const foldLocation = index + matchInfo.index;
            const fold = editor.session.addFold(
                variableName,
                new Range(row, foldLocation, row, foldLocation + matchInfo[0].length)
            );
            fold.preventExpand = true;
        }
    }
}

function pushTokensForPlaceholder(renderTokens, placeholder) {
    const matchInfo = placeholder.match(/^[0-9]+ */);
    if (matchInfo) {
        const variableIndex = Number(matchInfo[0]);
        const variableName = placeholder.substring(matchInfo[0].length);
        renderTokens.push(
            {
                type: "constant ace_numeric",
                value: variableIndex + " "
            },
            {
                type: "reward_variable variable-color-" + (variableIndex % 10),
                value: variableName
            }
        );
    } else {
        renderTokens.push({
            type: "fold",
            value: placeholder
        });
    }
}

function onChange(e) {
    ensureLineValidationCache();
    for (var i = e.start.row; i <= e.end.row; i++) {
        var existingFolds = editor.session.getFoldLine(i, i);
        if (existingFolds) {
            editor.session.removeFolds(existingFolds.folds);
        }
        createHintsForLine(i);
    }
    calculateValidationsAndDispatchEvent();
}
function calculateValidationsAndDispatchEvent() {
    var valid = true;
    var invalidIndexes = [];
    for (var i = 0; i < editor.session.getLength(); i++) {
        valid = valid && Object.keys(lineValidationCache[i]).length == 0;
        invalidIndexes.push(lineValidationCache[i]);
    }
    var evt = new CustomEvent("reward-function-validation", {
        detail: { valid: valid, invalidIndexes: invalidIndexes }
    });
    element.dispatchEvent(evt);
}
function ensureLineValidationCache() {
    for (var i = 0; i < editor.session.getLength(); i++) {
        if (lineValidationCache[i] === undefined) lineValidationCache[i] = [];
    }
}
function expandFold(fold) {
    if (fold.preventExpand) {
        return;
    }
    this.removeFold(fold);
    fold.subFolds.forEach(function(subFold) {
        fold.restoreRange(subFold);
        this.addFold(subFold);
    }, this);
    if (fold.collapseChildren > 0) {
        this.foldAll(fold.start.row + 1, fold.end.row, fold.collapseChildren - 1);
    }
    fold.subFolds = [];
}
function getFoldLineTokens(row, foldLine) {
    const session = this.session;
    var renderTokens = [];

    function addTokens(tokens, from, to) {
        let idx = 0,
            col = 0;
        while (col + tokens[idx].value.length < from) {
            col += tokens[idx].value.length;
            idx++;

            if (idx == tokens.length) return;
        }
        if (col != from) {
            let value = tokens[idx].value.substring(from - col);
            if (value.length > to - from) value = value.substring(0, to - from);

            renderTokens.push({
                type: tokens[idx].type,
                value: value
            });

            col = from + value.length;
            idx += 1;
        }

        while (col < to && idx < tokens.length) {
            let value = tokens[idx].value;
            if (value.length + col > to) {
                renderTokens.push({
                    type: tokens[idx].type,
                    value: value.substring(0, to - col)
                });
            } else renderTokens.push(tokens[idx]);
            col += value.length;
            idx += 1;
        }
    }

    let tokens = session.getTokens(row);
    foldLine.walk(
        function(placeholder, row, column, lastColumn, isNewRow) {
            if (placeholder != null) {
                pushTokensForPlaceholder(renderTokens, placeholder);
            } else {
                if (isNewRow) tokens = session.getTokens(row);

                if (tokens.length) addTokens(tokens, lastColumn, column);
            }
        },
        foldLine.end.row,
        this.session.getLine(foldLine.end.row).length
    );

    return renderTokens;
}

function computeWidth(force) {
    if (this.$modified || force) {
        this.$modified = false;

        if (this.$useWrapMode) return (this.screenWidth = this.$wrapLimit);

        var lines = this.doc.getAllLines();
        var cache = this.$rowLengthCache;
        var longestScreenLine = 0;
        var foldIndex = 0;
        var foldLine = this.$foldData[foldIndex];
        var foldStart = foldLine ? foldLine.start.row : Infinity;
        var len = lines.length;

        for (var i = 0; i < len; i++) {
            if (i > foldStart) {
                i = foldLine.end.row + 1;
                if (i >= len) break;
                foldLine = this.$foldData[foldIndex++];
                foldStart = foldLine ? foldLine.start.row : Infinity;
            }

            if (cache[i] == null) cache[i] = this.$getStringScreenWidth(lines[i])[0] + getFoldAdditionalWidth(i);

            if (cache[i] > longestScreenLine) longestScreenLine = cache[i];
        }
        this.screenWidth = longestScreenLine;
    }
}
function getFoldAdditionalWidth(row) {
    var foldLine = editor.session.getFoldLine(row, row);
    var additionalWidth = 0;
    if (foldLine && foldLine.folds) {
        for (var i = 0; i < foldLine.folds.length; i++) {
            var fold = foldLine.folds[i];
            if (fold.placeholder) {
                additionalWidth += fold.placeholder.length - (fold.end.column - fold.start.column);
            }
        }
    }
    return additionalWidth;
}

if (!window.Pathmind) {
    window.Pathmind = {};
}
window.Pathmind.CodeEditor = {
    addVariableNamesSupport: function(editorWrapper) {
        element = editorWrapper;
        editor = editorWrapper.editor;
        Range = ace.require("ace/range").Range;
        editor.session.expandFold = expandFold;
        editor.session.$computeWidth = computeWidth;
        editor.renderer.$textLayer.$getFoldLineTokens = getFoldLineTokens;
        createVariableNameHints();
        editor.session.on("change", e => onChange(e));
    },
    setVariableNames: function(vars, vcount) {
        rewardVariables = vars;
        variableCount = vcount;
    }
};
