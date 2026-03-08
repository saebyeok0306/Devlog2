"use client";
import { upgrade_sentence_api } from "@/api/ai";
import { ButtonView, Plugin, TextProxy } from "ckeditor5";

export default class UpgradeSentence extends Plugin {
  init() {
    const editor = this.editor;
    editor.ui.componentFactory.add("upgradeSentence", () => {
      const button = new ButtonView();

      button.set({
        label: "Upgrade Sentence",
        withText: true,
      });

      editor.model.document.selection.on("change", () => {
        const selection = editor.model.document.selection;
        if (selection.getFirstPosition().parent.name === "title-content") {
          button.isVisible = false;
        } else {
          button.isVisible = true;
        }
      });

      button.on("execute", async () => {
        const selection = editor.model.document.selection;
        const selectedText = this._getSelectedText(selection);
        const position = selection.getLastPosition();
        editor.model.change(async (writer) => {
          const viewFragment = editor.data.processor.toView(
            `<br><p><mark class="marker-yellow">Created By AI:</mark><br></p>`
          );
          const modelFragment = editor.data.toModel(viewFragment);
          const nextRange = editor.model.insertContent(modelFragment, position);
          editor.editing.view.focus();
          writer.setSelection(nextRange.end, "end");
        });
        await this._getLLM(editor, selectedText);
      });

      return button;
    });
  }

  delay(ms) {
    return new Promise((resolve) => setTimeout(resolve, ms));
  }

  async _getLLM(editor, selectedText) {
    try {
      const stream = await upgrade_sentence_api(selectedText);

      // consume response
      const reader = stream.pipeThrough(new TextDecoderStream()).getReader();
      while (true) {
        const { value, done } = await reader.read();
        if (done) break;
        const position = editor.model.document.selection.getLastPosition();
        editor.model.change(async (writer) => {
          let data = value.replace(/^data:\s/, "").replace(/\n\n$/, "");
          if (data.startsWith(" ")) {
            data = data.replace(/^ /, "&nbsp;");
          }
          const viewFragment = this.editor.data.processor.toView(
            `<p>${data}</p>`
          );
          const modelFragment = this.editor.data.toModel(viewFragment);
          const nextRange = editor.model.insertContent(modelFragment, position);
          writer.setSelection(nextRange.end, "end");
        });
        await this.delay(100);
      }
    } catch (error) {
      console.error(error);
    }
  }

  _getSelectedText(selection) {
    let selectedText = "";
    let idx = 0;
    const ranges = selection.getFirstRange();

    const _checkStyle = (idx, item) => {
      if (item instanceof TextProxy) {
        if (idx === 0) {
          _checkStyle(-1, item.parent);
        }
        selectedText += item.data;
      } else {
        if (item.name === "paragraph") {
          const attr = item._attrs;
          if (attr.get("listType")) {
            selectedText += "\n* ";
          } else {
            selectedText += "\n\n";
          }
        } else if (item.name === "softBreak") {
          selectedText += "\n";
        }
      }
    };

    for (const item of ranges.getItems()) {
      _checkStyle(idx, item);
      idx += 1;
    }
    console.log(selectedText);
    return selectedText;
  }
}
