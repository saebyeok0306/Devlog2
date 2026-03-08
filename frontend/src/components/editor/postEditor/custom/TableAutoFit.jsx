"use client";
import { ButtonView, Plugin } from "ckeditor5";

export default class TableAutoFit extends Plugin {
  init() {
    const editor = this.editor;
    editor.ui.componentFactory.add("tableAutoFit", () => {
      const button = new ButtonView();

      button.set({
        label: "AutoFit",
        withText: true,
      });

      button.on("execute", () => {
        const model = editor.model;
        const selection = model.document.selection;
        const tableElement = selection.getFirstPosition().findAncestor("table");
        if (!tableElement) {
          return;
        }

        editor.model.change((writer) => {
          const colGroup = tableElement
            .getChildren()
            .find((child) => child.name === "tableColumnGroup");

          if (!colGroup) {
            return;
          }
          const cols = Array.from(colGroup.getChildren());
          const rate = 100 / cols.length;
          colGroup.getChildren().forEach((col) => {
            writer.setAttribute("columnWidth", `${rate}%`, col);
          });
        });
      });

      return button;
    });
  }
}
