if (!window.Pathmind) {
    window.Pathmind = {};
}
window.Pathmind.autocomplete = {
    enableAutoComplete: function(editorWrapper, localVariables) {
        var element = editorWrapper;
        var editor = editorWrapper.editor;

        // fetch ace's language tools module:
        var langTools = ace.require('ace/ext/language_tools');

        var mathData = [
            {caption: 'Math.abs(double a)', value: 'Math.abs()', meta: 'Math.abs(double a) -> double'},
            {caption: 'Math.abs(float a)', value: 'Math.abs()', meta: 'Math.abs(float a) -> float'},
            {caption: 'Math.abs(int a)', value: 'Math.abs()', meta: 'Math.abs(int a) -> int'},
            {caption: 'Math.abs(long a)', value: 'Math.abs()', meta: 'Math.abs(long a) -> long'},
            {caption: 'Math.acos(double a)', value: 'Math.acos()', meta: 'Math.acos(double a) -> double'},
            {caption: 'Math.addExact(int x, int y)', value: 'Math.addExact()', meta: 'Math.addExact(int x, int y) -> int'},
            {caption: 'Math.addExact(long x, long y)', value: 'Math.addExact()', meta: 'Math.addExact(long x, long y) -> long'},
            {caption: 'Math.asin(double a)', value: 'Math.asin()', meta: 'Math.asin(double a) -> double'},
            {caption: 'Math.atan(double a)', value: 'Math.atan()', meta: 'Math.atan(double a) -> double'},
            {caption: 'Math.atan2(double y, double x)', value: 'Math.atan()', meta: 'Math.atan2(double y, double x) -> double'},
            {caption: 'Math.cbrt(double a)', value: 'Math.cbrt()', meta: 'Math.cbrt(double a) -> double'},
            {caption: 'Math.ceil(double a)', value: 'Math.ceil()', meta: 'Math.ceil(double a) -> double'},
            {caption: 'Math.copySign(double magnitude, double sign)', value: 'Math.copySign()', meta: 'Math.copySign(double magnitude, double sign) -> double'},
            {caption: 'Math.copySign(float magnitude, float sign)', value: 'Math.copySign()', meta: 'Math.copySign(float magnitude, float sign) -> float'},
            {caption: 'Math.cos(double a)', value: 'Math.cos()', meta: 'Math.cos(double a) -> double'},
            {caption: 'Math.cosh(double x)', value: 'Math.cosh()', meta: 'Math.cosh(double x) -> double'},
            {caption: 'Math.decrementExact(int a)', value: 'Math.decrementExact()', meta: 'Math.decrementExact(int a) -> int'},
            {caption: 'Math.decrementExact(long a)', value: 'Math.decrementExact()', meta: 'Math.decrementExact(long a) -> long'},
            {caption: 'Math.exp(double a)', value: 'Math.exp()', meta: 'Math.exp(double a) -> double'},
            {caption: 'Math.expm1(double x)', value: 'Math.expm()', meta: 'Math.expm1(double x) -> double'},
            {caption: 'Math.floor(double a)', value: 'Math.floor()', meta: 'Math.floor(double a) -> double'},
            {caption: 'Math.floorDiv(int x, int y)', value: 'Math.floorDiv()', meta: 'Math.floorDiv(int x, int y) -> int'},
            {caption: 'Math.floorDiv(long x, long y)', value: 'Math.floorDiv()', meta: 'Math.floorDiv(long x, long y) -> long'},
            {caption: 'Math.floorMod(int x, int y)', value: 'Math.floorMod()', meta: 'Math.floorMod(int x, int y) -> int'},
            {caption: 'Math.floorMod(long x, long y)', value: 'Math.floorMod()', meta: 'Math.floorMod(long x, long y) -> long'},
            {caption: 'Math.getExponent(double d)', value: 'Math.getExponent()', meta: 'Math.getExponent(double d) -> int'},
            {caption: 'Math.getExponent(float f)', value: 'Math.getExponent()', meta: 'Math.getExponent(float f) -> int'},
            {caption: 'Math.hypot(double x, double y)', value: 'Math.hypot()', meta: 'Math.hypot(double x, double y) -> double'},
            {caption: 'Math.IEEEremainder(double f1, double f2)', value: 'Math.IEEEremainder()', meta: 'Math.IEEEremainder(double f1, double f2) -> double'},
            {caption: 'Math.incrementExact(int a)', value: 'Math.incrementExact()', meta: 'Math.incrementExact(int a) -> int'},
            {caption: 'Math.incrementExact(long a)', value: 'Math.incrementExact()', meta: 'Math.incrementExact(long a) -> long'},
            {caption: 'Math.log(double a)', value: 'Math.log()', meta: 'Math.log(double a) -> double'},
            {caption: 'Math.log10(double a)', value: 'Math.log()', meta: 'Math.log10(double a) -> double'},
            {caption: 'Math.log1p(double x)', value: 'Math.log()', meta: 'Math.log1p(double x) -> double'},
            {caption: 'Math.max(double a, double b)', value: 'Math.max()', meta: 'Math.max(double a, double b) -> double'},
            {caption: 'Math.max(float a, float b)', value: 'Math.max()', meta: 'Math.max(float a, float b) -> float'},
            {caption: 'Math.max(int a, int b)', value: 'Math.max()', meta: 'Math.max(int a, int b) -> int'},
            {caption: 'Math.max(long a, long b)', value: 'Math.max()', meta: 'Math.max(long a, long b) -> long'},
            {caption: 'Math.min(double a, double b)', value: 'Math.min()', meta: 'Math.min(double a, double b) -> double'},
            {caption: 'Math.min(float a, float b)', value: 'Math.min()', meta: 'Math.min(float a, float b) -> float'},
            {caption: 'Math.min(int a, int b)', value: 'Math.min()', meta: 'Math.min(int a, int b) -> int'},
            {caption: 'Math.min(long a, long b)', value: 'Math.min()', meta: 'Math.min(long a, long b) -> long'},
            {caption: 'Math.multiplyExact(int x, int y)', value: 'Math.multiplyExact()', meta: 'Math.multiplyExact(int x, int y) -> int'},
            {caption: 'Math.multiplyExact(long x, long y)', value: 'Math.multiplyExact()', meta: 'Math.multiplyExact(long x, long y) -> long'},
            {caption: 'Math.negateExact(int a)', value: 'Math.negateExact()', meta: 'Math.negateExact(int a) -> int'},
            {caption: 'Math.negateExact(long a)', value: 'Math.negateExact()', meta: 'Math.negateExact(long a) -> long'},
            {caption: 'Math.nextAfter(double start, double direction)', value: 'Math.nextAfter()', meta: 'Math.nextAfter(double start, double direction) -> double'},
            {caption: 'Math.nextAfter(float start, double direction)', value: 'Math.nextAfter()', meta: 'Math.nextAfter(float start, double direction) -> float'},
            {caption: 'Math.nextDown(double d)', value: 'Math.nextDown()', meta: 'Math.nextDown(double d) -> double'},
            {caption: 'Math.nextDown(float f)', value: 'Math.nextDown()', meta: 'Math.nextDown(float f) -> float'},
            {caption: 'Math.nextUp(double d)', value: 'Math.nextUp()', meta: 'Math.nextUp(double d) -> double'},
            {caption: 'Math.nextUp(float f)', value: 'Math.nextUp()', meta: 'Math.nextUp(float f) -> float'},
            {caption: 'Math.pow(double a, double b)', value: 'Math.pow()', meta: 'Math.pow(double a, double b) -> double'},
            {caption: 'Math.random()', value: 'Math.random()', meta: 'Math.random() -> double'},
            {caption: 'Math.rint(double a)', value: 'Math.rint()', meta: 'Math.rint(double a) -> double'},
            {caption: 'Math.round(double a)', value: 'Math.round()', meta: 'Math.round(double a) -> long'},
            {caption: 'Math.round(float a)', value: 'Math.round()', meta: 'Math.round(float a) -> int'},
            {caption: 'Math.scalb(double d, int scaleFactor)', value: 'Math.scalb()', meta: 'Math.scalb(double d, int scaleFactor) -> double'},
            {caption: 'Math.scalb(float f, int scaleFactor)', value: 'Math.scalb()', meta: 'Math.scalb(float f, int scaleFactor) -> float'},
            {caption: 'Math.signum(double d)', value: 'Math.signum()', meta: 'Math.signum(double d) -> double'},
            {caption: 'Math.signum(float f)', value: 'Math.signum()', meta: 'Math.signum(float f) -> float'},
            {caption: 'Math.sin(double a)', value: 'Math.sin()', meta: 'Math.sin(double a) -> double'},
            {caption: 'Math.sinh(double x)', value: 'Math.sinh()', meta: 'Math.sinh(double x) -> double'},
            {caption: 'Math.sqrt(double a)', value: 'Math.sqrt()', meta: 'Math.sqrt(double a) -> double'},
            {caption: 'Math.subtractExact(int x, int y)', value: 'Math.subtractExact()', meta: 'Math.subtractExact(int x, int y) -> int'},
            {caption: 'Math.subtractExact(long x, long y)', value: 'Math.subtractExact()', meta: 'Math.subtractExact(long x, long y) -> long'},
            {caption: 'Math.tan(double a)', value: 'Math.tan()', meta: 'Math.tan(double a) -> double'},
            {caption: 'Math.tanh(double x)', value: 'Math.tanh()', meta: 'Math.tanh(double x) -> double'},
            {caption: 'Math.toDegrees(double angrad)', value: 'Math.toDegrees()', meta: 'Math.toDegrees(double angrad) -> double'},
            {caption: 'Math.toIntExact(long value)', value: 'Math.toIntExact()', meta: 'Math.toIntExact(long value) -> int'},
            {caption: 'Math.toRadians(double angdeg)', value: 'Math.toRadians()', meta: 'Math.toRadians(double angdeg) -> double'},
            {caption: 'Math.ulp(double d)', value: 'Math.ulp()', meta: 'Math.ulp(double d) -> double'},
            {caption: 'Math.ulp(float f)', value: 'Math.ulp()', meta: 'Math.ulp(float f) -> float'}
        ];

        var autocompleteData = localVariables.concat(mathData);

        var variablesCompleter = {
                identifierRegexps: [/[a-zA-Z_0-9\.\$]/],
                getCompletions: function(editor, session, pos, prefix, callback) {
                    const currentLineText = editor.session.getLine(pos.row);
                    const isFullLineCommented = currentLineText[0] == "/" && currentLineText[1] == "/";
                    if (autocompleteData) {
                        autocompleteData.forEach(function(autocompleteSuggestion) {
                            let captionText = autocompleteSuggestion.caption;
                            if (captionText.includes("after.") || captionText.includes("before.")) {
                                const rewardVarName = captionText.split(/after\.|before\./)[1];
                                if (currentLineText.includes(rewardVarName)) {
                                    autocompleteSuggestion.score = 10;
                                } else {
                                    autocompleteSuggestion.score = 0;
                                }
                            }
                        });
                        if (isFullLineCommented ||
                            (!isFullLineCommented && currentLineText.includes("//") && currentLineText.indexOf("//") < pos.column)) {
                            // second case disables autocomplete when user starts typing a comment at the end of the line
                            // but the autocomplete will still be enabled when user types in the uncommented part of the same line
                            return;
                        }
                        callback(null, autocompleteData);
                    }
                }
            };

        editor.completers = [variablesCompleter];

        editor.setOptions({
            enableLiveAutocompletion: true,
            enableBasicAutocompletion: true
        });
    }
};