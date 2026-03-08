"use client";
import { upload_file_api } from "@/api/file";

import {
  AccessibilityHelp,
  Autoformat,
  AutoImage,
  Autosave,
  BalloonToolbar,
  Bold,
  CloudServices,
  Code,
  CodeBlock,
  Essentials,
  Highlight,
  HorizontalLine,
  ImageBlock,
  ImageCaption,
  ImageInline,
  ImageInsertViaUrl,
  ImageResize,
  ImageStyle,
  ImageTextAlternative,
  ImageToolbar,
  ImageUpload,
  Italic,
  Link,
  LinkImage,
  List,
  ListProperties,
  Paragraph,
  RemoveFormat,
  SelectAll,
  SpecialCharacters,
  SpecialCharactersArrows,
  SpecialCharactersCurrency,
  SpecialCharactersEssentials,
  SpecialCharactersLatin,
  SpecialCharactersMathematical,
  SpecialCharactersText,
  Strikethrough,
  Table,
  TableCaption,
  TableCellProperties,
  TableColumnResize,
  TableProperties,
  TableToolbar,
  TextTransformation,
  TodoList,
  Underline,
  Undo,
  MediaEmbed,
} from "ckeditor5";

import translations from "ckeditor5/translations/ko.js";
import TableAutoFit from "../postEditor/custom/TableAutoFit";
import { toast } from "react-toastify";

const commentEditorConfig = {
  toolbar: {
    items: [
      "undo",
      "redo",
      "|",
      "bold",
      "italic",
      "underline",
      "strikethrough",
      "code",
      "removeFormat",
      "|",
      "specialCharacters",
      "horizontalLine",
      "link",
      "insertImage",
      "insertTable",
      "highlight",
      "codeBlock",
      "|",
      "bulletedList",
      "numberedList",
      "todoList",
    ],
    shouldNotGroupWhenFull: true,
  },
  plugins: [
    AccessibilityHelp,
    Autoformat,
    AutoImage,
    Autosave,
    BalloonToolbar,
    Bold,
    CloudServices,
    Code,
    CodeBlock,
    Essentials,
    Highlight,
    HorizontalLine,
    ImageBlock,
    ImageCaption,
    ImageInline,
    ImageInsertViaUrl,
    ImageResize,
    ImageStyle,
    ImageTextAlternative,
    ImageToolbar,
    ImageUpload,
    Italic,
    Link,
    LinkImage,
    List,
    ListProperties,
    Paragraph,
    RemoveFormat,
    SelectAll,
    SpecialCharacters,
    SpecialCharactersArrows,
    SpecialCharactersCurrency,
    SpecialCharactersEssentials,
    SpecialCharactersLatin,
    SpecialCharactersMathematical,
    SpecialCharactersText,
    Strikethrough,
    Table,
    TableCaption,
    TableCellProperties,
    TableColumnResize,
    TableProperties,
    TableToolbar,
    TextTransformation,
    TodoList,
    Underline,
    Undo,
    MediaEmbed,
    TableAutoFit,
  ],
  balloonToolbar: ["bold", "italic", "|", "link", "insertImage"],
  image: {
    toolbar: [
      "toggleImageCaption",
      "imageTextAlternative",
      "|",
      // "imageStyle:inline",
      // "imageStyle:wrapText",
      "imageStyle:breakText",
      "|",
      "resizeImage",
    ],
    insert: { type: "block" },
  },
  language: "ko",
  link: {
    addTargetToExternalLinks: true,
    defaultProtocol: "https://",
    decorators: {
      toggleDownloadable: {
        mode: "manual",
        label: "Downloadable",
        attributes: {
          download: "file",
        },
      },
    },
  },
  list: {
    properties: {
      styles: true,
      startIndex: true,
      reversed: true,
    },
  },
  mention: {
    feeds: [
      {
        marker: "@",
        feed: [
          /* See: https://ckeditor.com/docs/ckeditor5/latest/features/mentions.html */
        ],
      },
    ],
  },
  placeholder: "댓글을 작성해주세요",
  table: {
    contentToolbar: [
      "tableColumn",
      "tableRow",
      "mergeTableCells",
      "tableProperties",
      "tableCellProperties",
      "TableAutoFit",
    ],
  },
  translations: [translations],
  mediaEmbed: {
    previewsInData: true,
  },
  // codeBlock: {
  //   languages: [
  //     { model: "plaintext", label: "Plain text" },
  //     { model: "javascript", label: "JavaScript" },
  //     { model: "typescript", label: "TypeScript" },
  //     { model: "python", label: "Python" },
  //     { model: "java", label: "Java" },
  //     { model: "kotlin", label: "Kotlin" },
  //     { model: "swift", label: "Swift" },
  //     { model: "c", label: "C" },
  //     { model: "cpp", label: "C++" },
  //     { model: "csharp", label: "C#" },
  //     { model: "ruby", label: "Ruby" },
  //     { model: "php", label: "PHP" },
  //     { model: "go", label: "Go" },
  //     { model: "html", label: "HTML" },
  //   ],
  // },
};

class CustomCommentUploadAdapter {
  constructor(loader, setFiles) {
    this.loader = loader;
    this.setFiles = setFiles;
  }

  upload() {
    return this.loader.file.then(
      (file) =>
        new Promise(async (resolve, reject) => {
          try {
            const payload = await upload_file_api(file);
            await this.setFiles((prev) => [...prev, payload]);
            resolve({
              default: `${process.env.NEXT_PUBLIC_API_FILE_URL}/${payload.filePath}/${payload.fileUrl}`,
            });
          } catch (error) {
            toast.warning(
              error?.response ? error.response.data.error : error.message,
              { position: "bottom-center" }
            );
            reject();
          }
        })
    );
  }

  abort() {
    // 파일 업로드 중단 시 처리 로직
    console.log("abort()");
  }
}

export { commentEditorConfig, CustomCommentUploadAdapter };
