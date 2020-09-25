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
            {caption: 'Math.abs(double a) -> double', value: 'Math.abs', meta: 'static'},
            {caption: 'Math.abs(float a) -> float', value: 'Math.abs', meta: 'static'},
            {caption: 'Math.abs(int a) -> int', value: 'Math.abs', meta: 'static'},
            {caption: 'Math.abs(long a) -> long', value: 'Math.abs', meta: 'static'},
            {caption: 'Math.acos(double a) -> double', value: 'Math.acos', meta: 'static'},
            {caption: 'Math.addExact(int x, int y) -> int', value: 'Math.addExact', meta: 'static'},
            {caption: 'Math.addExact(long x, long y) -> long', value: 'Math.addExact', meta: 'static'},
            {caption: 'Math.asin(double a) -> double', value: 'Math.asin', meta: 'static'},
            {caption: 'Math.atan(double a) -> double', value: 'Math.atan', meta: 'static'},
            {caption: 'Math.atan2(double y, double x) -> double', value: 'Math.atan', meta: 'static'},
            {caption: 'Math.cbrt(double a) -> double', value: 'Math.cbrt', meta: 'static'},
            {caption: 'Math.ceil(double a) -> double', value: 'Math.ceil', meta: 'static'},
            {caption: 'Math.copySign(double magnitude, double sign) -> double', value: 'Math.copySign', meta: 'static'},
            {caption: 'Math.copySign(float magnitude, float sign) -> float', value: 'Math.copySign', meta: 'static'},
            {caption: 'Math.cos(double a) -> double', value: 'Math.cos', meta: 'static'},
            {caption: 'Math.cosh(double x) -> double', value: 'Math.cosh', meta: 'static'},
            {caption: 'Math.decrementExact(int a) -> int', value: 'Math.decrementExact', meta: 'static'},
            {caption: 'Math.decrementExact(long a) -> long', value: 'Math.decrementExact', meta: 'static'},
            {caption: 'Math.exp(double a) -> double', value: 'Math.exp', meta: 'static'},
            {caption: 'Math.expm1(double x) -> double', value: 'Math.expm', meta: 'static'},
            {caption: 'Math.floor(double a) -> double', value: 'Math.floor', meta: 'static'},
            {caption: 'Math.floorDiv(int x, int y) -> int', value: 'Math.floorDiv', meta: 'static'},
            {caption: 'Math.floorDiv(long x, long y) -> long', value: 'Math.floorDiv', meta: 'static'},
            {caption: 'Math.floorMod(int x, int y) -> int', value: 'Math.floorMod', meta: 'static'},
            {caption: 'Math.floorMod(long x, long y) -> long', value: 'Math.floorMod', meta: 'static'},
            {caption: 'Math.getExponent(double d) -> int', value: 'Math.getExponent', meta: 'static'},
            {caption: 'Math.getExponent(float f) -> int', value: 'Math.getExponent', meta: 'static'},
            {caption: 'Math.hypot(double x, double y) -> double', value: 'Math.hypot', meta: 'static'},
            {caption: 'Math.IEEEremainder(double f1, double f2) -> double', value: 'Math.IEEEremainder', meta: 'static'},
            {caption: 'Math.incrementExact(int a) -> int', value: 'Math.incrementExact', meta: 'static'},
            {caption: 'Math.incrementExact(long a) -> long', value: 'Math.incrementExact', meta: 'static'},
            {caption: 'Math.log(double a) -> double', value: 'Math.log', meta: 'static'},
            {caption: 'Math.log10(double a) -> double', value: 'Math.log', meta: 'static'},
            {caption: 'Math.log1p(double x) -> double', value: 'Math.log', meta: 'static'},
            {caption: 'Math.max(double a, double b) -> double', value: 'Math.max', meta: 'static'},
            {caption: 'Math.max(float a, float b) -> float', value: 'Math.max', meta: 'static'},
            {caption: 'Math.max(int a, int b) -> int', value: 'Math.max', meta: 'static'},
            {caption: 'Math.max(long a, long b) -> long', value: 'Math.max', meta: 'static'},
            {caption: 'Math.min(double a, double b) -> double', value: 'Math.min', meta: 'static'},
            {caption: 'Math.min(float a, float b) -> float', value: 'Math.min', meta: 'static'},
            {caption: 'Math.min(int a, int b) -> int', value: 'Math.min', meta: 'static'},
            {caption: 'Math.min(long a, long b) -> long', value: 'Math.min', meta: 'static'},
            {caption: 'Math.multiplyExact(int x, int y) -> int', value: 'Math.multiplyExact', meta: 'static'},
            {caption: 'Math.multiplyExact(long x, long y) -> long', value: 'Math.multiplyExact', meta: 'static'},
            {caption: 'Math.negateExact(int a) -> int', value: 'Math.negateExact', meta: 'static'},
            {caption: 'Math.negateExact(long a) -> long', value: 'Math.negateExact', meta: 'static'},
            {caption: 'Math.nextAfter(double start, double direction) -> double', value: 'Math.nextAfter', meta: 'static'},
            {caption: 'Math.nextAfter(float start, double direction) -> float', value: 'Math.nextAfter', meta: 'static'},
            {caption: 'Math.nextDown(double d) -> double', value: 'Math.nextDown', meta: 'static'},
            {caption: 'Math.nextDown(float f) -> float', value: 'Math.nextDown', meta: 'static'},
            {caption: 'Math.nextUp(double d) -> double', value: 'Math.nextUp', meta: 'static'},
            {caption: 'Math.nextUp(float f) -> float', value: 'Math.nextUp', meta: 'static'},
            {caption: 'Math.pow(double a, double b) -> double', value: 'Math.pow', meta: 'static'},
            {caption: 'Math.random() -> double', value: 'Math.random', meta: 'static'},
            {caption: 'Math.rint(double a) -> double', value: 'Math.rint', meta: 'static'},
            {caption: 'Math.round(double a) -> long', value: 'Math.round', meta: 'static'},
            {caption: 'Math.round(float a) -> int', value: 'Math.round', meta: 'static'},
            {caption: 'Math.scalb(double d, int scaleFactor) -> double', value: 'Math.scalb', meta: 'static'},
            {caption: 'Math.scalb(float f, int scaleFactor) -> float', value: 'Math.scalb', meta: 'static'},
            {caption: 'Math.signum(double d) -> double', value: 'Math.signum', meta: 'static'},
            {caption: 'Math.signum(float f) -> float', value: 'Math.signum', meta: 'static'},
            {caption: 'Math.sin(double a) -> double', value: 'Math.sin', meta: 'static'},
            {caption: 'Math.sinh(double x) -> double', value: 'Math.sinh', meta: 'static'},
            {caption: 'Math.sqrt(double a) -> double', value: 'Math.sqrt', meta: 'static'},
            {caption: 'Math.subtractExact(int x, int y) -> int', value: 'Math.subtractExact', meta: 'static'},
            {caption: 'Math.subtractExact(long x, long y) -> long', value: 'Math.subtractExact', meta: 'static'},
            {caption: 'Math.tan(double a) -> double', value: 'Math.tan', meta: 'static'},
            {caption: 'Math.tanh(double x) -> double', value: 'Math.tanh', meta: 'static'},
            {caption: 'Math.toDegrees(double angrad) -> double', value: 'Math.toDegrees', meta: 'static'},
            {caption: 'Math.toIntExact(long value) -> int', value: 'Math.toIntExact', meta: 'static'},
            {caption: 'Math.toRadians(double angdeg) -> double', value: 'Math.toRadians', meta: 'static'},
            {caption: 'Math.ulp(double d) -> double', value: 'Math.ulp', meta: 'static'},
            {caption: 'Math.ulp(float f) -> float', value: 'Math.ulp', meta: 'static'}
        ];

        var autocompleteData = localVariables.concat(mathData);

        var variablesCompleter = {
                identifierRegexps: [/[a-zA-Z_0-9\.\$\-\u00A2-\u2000\u2070-\uFFFF]/],
                getCompletions: function(editor, session, pos, prefix, callback) {
                    callback(null, autocompleteData);
                }
            };

        editor.completers = [variablesCompleter];

        editor.setOptions({
            enableLiveAutocompletion: true,
            enableBasicAutocompletion: true
        });
    }
};