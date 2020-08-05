ace.define("ace/theme/eclipse", ["require", "exports", "module", "ace/lib/dom"], function(require, exports, module) {
    "use strict";

    exports.isDark = false;
    exports.cssText =
        '.ace-eclipse .ace_gutter-layer {\
contain: none;\
}\
.ace-eclipse .ace_gutter {\
background: var(--pm-app-bg-color);\
color: rgb(136, 136, 136);\
border-right: 1px solid var(--pm-gray-color-dark);\
border-top-left-radius: var(--lumo-border-radius);\
}\
.ace-eclipse .ace_fold {\
background-color: rgb(60, 76, 114);\
}\
.ace-eclipse .ace_print-margin {\
width: 1px;\
background: #ebebeb;\
}\
.ace-eclipse {\
background-color: #FFFFFF;\
border: 1px solid var(--pm-gray-color);\
border-radius: var(--lumo-border-radius);\
color: black;\
font-size: var(--lumo-font-size-s);\
line-height: 1.6rem;\
}\
.ace-eclipse .ace_cursor {\
color: black;\
}\
.ace-eclipse .ace_storage,\
.ace-eclipse .ace_keyword,\
.ace-eclipse .ace_variable {\
color: rgb(127, 0, 85);\
}\
.ace-eclipse .ace_constant.ace_buildin {\
color: rgb(88, 72, 246);\
}\
.ace-eclipse .ace_constant.ace_library {\
color: rgb(6, 150, 14);\
}\
.ace-eclipse .ace_function {\
color: rgb(60, 76, 114);\
}\
.ace-eclipse .ace_string {\
color: rgb(42, 0, 255);\
}\
.ace-eclipse .ace_comment {\
color: rgb(113, 150, 130);\
}\
.ace-eclipse .ace_comment.ace_doc {\
color: rgb(63, 95, 191);\
}\
.ace-eclipse .ace_comment.ace_doc.ace_tag {\
color: rgb(127, 159, 191);\
}\
.ace-eclipse .ace_constant.ace_numeric {\
color: #1900ff;\
}\
.ace-eclipse .ace_tag {\
color: rgb(25, 118, 116);\
}\
.ace-eclipse .ace_type {\
color: rgb(127, 0, 127);\
}\
.ace-eclipse .ace_xml-pe {\
color: rgb(104, 104, 91);\
}\
.ace-eclipse .ace_marker-layer .ace_selection {\
background: rgb(181, 213, 255);\
}\
.ace-eclipse .ace_marker-layer .ace_bracket {\
margin: -1px 0 0 -1px;\
border: 1px solid rgb(192, 192, 192);\
}\
.ace-eclipse .ace_meta.ace_tag {\
color:rgb(25, 118, 116);\
}\
.ace-eclipse .ace_invisible {\
color: #ddd;\
}\
.ace-eclipse .ace_entity.ace_other.ace_attribute-name {\
color:rgb(127, 0, 127);\
}\
.ace-eclipse .ace_marker-layer .ace_step {\
background: rgb(255, 255, 0);\
}\
.ace-eclipse .ace_active-line {\
background: var(--lumo-primary-color-10pct);\
}\
.ace-eclipse .ace_gutter-active-line {\
background-color : var(--lumo-primary-color-10pct);\
}\
.ace-eclipse .ace_marker-layer .ace_selected-word {\
border: 1px solid rgb(181, 213, 255);\
}\
.ace-eclipse .ace_indent-guide {\
background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAACCAYAAACZgbYnAAAAE0lEQVQImWP4////f4bLly//BwAmVgd1/w11/gAAAABJRU5ErkJggg==") right repeat-y;\
}\
.ace-eclipse .ace_reward_variable{\
  padding: .14em 0;\
  border-radius: var(--lumo-border-radius-s);\
}\
.variable-color-0 {\
  color: #000;\
  background-color: var(--variable-color-0);\
  box-shadow: -.15em 0 var(--variable-color-0), .15em 0 var(--variable-color-0);\
}\
.variable-color-1 {\
  color: #fff;\
  background-color: var(--variable-color-1);\
  box-shadow: -.15em 0 var(--variable-color-1), .15em 0 var(--variable-color-1);\
}\
.variable-color-2 {\
  color: #000;\
  background-color: var(--variable-color-2);\
  box-shadow: -.15em 0 var(--variable-color-2), .15em 0 var(--variable-color-2);\
}\
.variable-color-3 {\
  color: #fff;\
  background-color: var(--variable-color-3);\
  box-shadow: -.15em 0 var(--variable-color-3), .15em 0 var(--variable-color-3);\
}\
.variable-color-4 {\
  color: #000;\
  background-color: var(--variable-color-4);\
  box-shadow: -.15em 0 var(--variable-color-4), .15em 0 var(--variable-color-4);\
}\
.variable-color-5 {\
  color: #000;\
  background-color: var(--variable-color-5);\
  box-shadow: -.15em 0 var(--variable-color-5), .15em 0 var(--variable-color-5);\
}\
.variable-color-6 {\
  color: #000;\
  background-color: var(--variable-color-6);\
  box-shadow: -.15em 0 var(--variable-color-6), .15em 0 var(--variable-color-6);\
}\
.variable-color-7 {\
  color: #fff;\
  background-color: var(--variable-color-7);\
  box-shadow: -.15em 0 var(--variable-color-7), .15em 0 var(--variable-color-7);\
}\
.variable-color-8 {\
  color: #fff;\
  background-color: var(--variable-color-8);\
  box-shadow: -.15em 0 var(--variable-color-8), .15em 0 var(--variable-color-8);\
}\
.variable-color-9 {\
  color: #000;\
  background-color: var(--variable-color-9);\
  box-shadow: -.15em 0 var(--variable-color-9), .15em 0 var(--variable-color-9);\
}';

    exports.cssClass = "ace-eclipse";

    var dom = require("../lib/dom");
    dom.importCssString(exports.cssText, exports.cssClass);
});
(function() {
    ace.require(["ace/theme/eclipse"], function(m) {
        if (typeof module == "object" && typeof exports == "object" && module) {
            module.exports = m;
        }
    });
})();
