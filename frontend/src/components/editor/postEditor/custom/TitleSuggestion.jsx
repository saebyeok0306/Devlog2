"use client";
import { title_suggestion_api } from "@/api/ai";
import { ButtonView, Plugin, TextProxy } from "ckeditor5";

export default class TitleSuggestion extends Plugin {
  init() {
    const editor = this.editor;
    editor.ui.componentFactory.add("titleSuggestion", () => {
      const button = new ButtonView();

      button.set({
        label: "Title Suggestion",
        withText: true,
      });

      editor.model.document.selection.on("change", () => {
        const selection = editor.model.document.selection;
        if (selection.getFirstPosition().parent.name === "title-content") {
          button.isVisible = true;
        } else {
          button.isVisible = false;
        }
      });

      button.on("execute", async () => {
        const content = editor.getData();
        const collected_message = await this._getLLM(content);

        const selection = editor.model.document.selection;
        const titleElement = selection.getFirstPosition().parent;
        const titleRange = editor.model.createRangeOn(titleElement);

        editor.editing.view.focus();
        editor.model.change((writer) => {
          writer.setSelection(titleRange, "end");
          editor.model.insertContent(writer.createText(collected_message));
        });
      });

      return button;
    });
  }

  _delay(ms) {
    return new Promise((resolve) => setTimeout(resolve, ms));
  }

  async _getLLM(selectedText) {
    let collected_message = "";
    try {
      const stream = await title_suggestion_api(selectedText);

      // consume response
      const reader = stream.pipeThrough(new TextDecoderStream()).getReader();
      while (true) {
        const { value, done } = await reader.read();
        if (done) break;
        let data = value.replace(/^data:\s/, "").replace(/\n\n$/, "");
        // if (data.startsWith(" ")) {
        //   data = data.replace(/^ /, "&nbsp;");
        // }
        collected_message += data;
        await this._delay(100);
      }
    } catch (error) {
      console.error(error);
    }
    return collected_message;
  }

  _getSelectedText(selection) {
    let selectedText = "";
    const ranges = selection.getFirstRange();
    for (const item of ranges.getItems()) {
      if (item instanceof TextProxy) {
        selectedText += item.data;
      } else {
        if (item.name === "paragraph") {
          selectedText += "\n\n";
        } else if (item.name === "softBreak") {
          selectedText += "\n";
        }
      }
    }
    return selectedText;
  }
}
