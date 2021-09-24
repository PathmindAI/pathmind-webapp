if (!window.Pathmind) {
    window.Pathmind = {};
}
window.Pathmind.placeholder = {
    setPlaceholder: function(editorWrapper, placeholder) {
        const element = editorWrapper;
        const editor = editorWrapper.editor;

        function update() {
          var shouldShow = !editor.session.getValue().length;
          var node = editor.renderer.emptyMessageNode;
          if (!shouldShow && node) {
              editor.renderer.scroller.removeChild(editor.renderer.emptyMessageNode);
              editor.renderer.emptyMessageNode = null;
          } else if (shouldShow && !node) {
              node = editor.renderer.emptyMessageNode = document.createElement("div");
              node.textContent = placeholder;
              node.className = "ace_emptyMessage";
              node.style.padding = "0 6px"
              node.style.position = "absolute";
              node.style.zIndex = 9;
              node.style.opacity = 0.3;
              editor.renderer.scroller.appendChild(node);
          }
        }

        // editor.session.on("input", update);
        editor.on("input", update);
        setTimeout(update, 100);
    },
};
