import { ButtonView, Plugin, TextProxy } from "ckeditor5";

export default class HighlightedLine extends Plugin {
  init() {
    const editor = this.editor;
    editor.ui.componentFactory.add("highlightedLine", () => {
      const button = new ButtonView();

      button.set({
        label: "하이라이팅",
        withText: true,
      });

      editor.model.document.selection.on("change", () => {
        const schema = editor.model.schema;
        const selection = editor.model.document.selection;
        if (selection.getFirstPosition().parent.name === "codeBlock") {
          button.isVisible = true;
        } else {
          button.isVisible = false;
        }
      });

      button.on("execute", () => {
        const model = editor.model;
        const selection = model.document.selection;

        const range = selection.getFirstRange();
        if (!range) return;
        editor.model.change((writer) => {
          writer.addMarker("highlightedLine", {
            range: range,
            usingOperation: true,
            affectsData: true,
          });
          // for (const range of selection.getRanges()) {
          //   for (const item of range.getItems()) {
          //     if (item instanceof TextProxy) {
          //       writer.setAttribute("highlighted-line", "true", item);
          //     }
          //   }
          // }
        });
      });

      return button;
    });
  }
}
