"use client";
import { upload_file_api } from "@/api/file";

import {
  AccessibilityHelp,
  Alignment,
  Autoformat,
  AutoImage,
  Autosave,
  BalloonToolbar,
  BlockQuote,
  Bold,
  Code,
  CodeBlock,
  Essentials,
  FindAndReplace,
  FontBackgroundColor,
  FontColor,
  FontFamily,
  FontSize,
  Heading,
  Highlight,
  HorizontalLine,
  ImageBlock,
  ImageCaption,
  ImageInline,
  ImageInsert,
  ImageInsertViaUrl,
  ImageResize,
  ImageStyle,
  ImageTextAlternative,
  ImageToolbar,
  ImageUpload,
  Indent,
  IndentBlock,
  Italic,
  Link,
  LinkImage,
  MediaEmbed,
  Mention,
  Paragraph,
  PasteFromOffice,
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
  Subscript,
  Superscript,
  Table,
  TableCaption,
  TableCellProperties,
  TableColumnResize,
  TableProperties,
  TableToolbar,
  TextTransformation,
  Title,
  Underline,
  Undo,
  FileRepository,
  ListProperties,
  List,
} from "ckeditor5";

import translations from "ckeditor5/translations/ko.js";
import TableAutoFit from "./custom/TableAutoFit";
import UpgradeSentence from "./custom/UpgradeSentence";
import TitleSuggestion from "./custom/TitleSuggestion";
import FileUploader from "./custom/FileUploader";
import { toast } from "react-toastify";

const editorConfig = {
  toolbar: {
    items: [
      "undo",
      "redo",
      "|",
      "findAndReplace",
      "|",
      "heading",
      "|",
      "fontSize",
      "fontFamily",
      "fontColor",
      "fontBackgroundColor",
      "|",
      "bold",
      "italic",
      "underline",
      "strikethrough",
      "subscript",
      "superscript",
      "code",
      "removeFormat",
      "-",
      "specialCharacters",
      "horizontalLine",
      "link",
      "insertImage",
      "mediaEmbed",
      "insertTable",
      "highlight",
      "blockQuote",
      "codeBlock",
      "|",
      "alignment",
      "bulletedList",
      "numberedList",
      "todoList",
      "|",
      "outdent",
      "indent",
      "fileUploader",
    ],
    shouldNotGroupWhenFull: true,
  },
  plugins: [
    AccessibilityHelp,
    Alignment,
    Autoformat,
    AutoImage,
    Autosave,
    BalloonToolbar,
    BlockQuote,
    Bold,
    Code,
    CodeBlock,
    Essentials,
    FindAndReplace,
    FontBackgroundColor,
    FontColor,
    FontFamily,
    FontSize,
    Heading,
    Highlight,
    HorizontalLine,
    ImageBlock,
    ImageCaption,
    ImageInline,
    ImageInsert,
    ImageInsertViaUrl,
    ImageResize,
    ImageStyle,
    ImageTextAlternative,
    ImageToolbar,
    ImageUpload,
    Indent,
    IndentBlock,
    Italic,
    Link,
    LinkImage,
    List,
    ListProperties,
    MediaEmbed,
    Mention,
    Paragraph,
    PasteFromOffice,
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
    Subscript,
    Superscript,
    Table,
    TableCaption,
    TableCellProperties,
    TableColumnResize,
    TableProperties,
    TableToolbar,
    TextTransformation,
    Title,
    Underline,
    Undo,
    FileRepository,
    TableAutoFit,
    UpgradeSentence,
    TitleSuggestion,
    FileUploader,
  ],
  balloonToolbar: [
    "bold",
    "italic",
    "|",
    "link",
    "insertImage",
    "|",
    "upgradeSentence",
    "titleSuggestion",
  ],
  fontFamily: {
    supportAllValues: true,
  },
  fontSize: {
    options: [10, 12, 14, "default", 18, 20, 22],
    supportAllValues: true,
  },
  heading: {
    options: [
      {
        model: "paragraph",
        title: "Paragraph",
        class: "ck-heading_paragraph",
      },
      {
        model: "heading1",
        view: "h1",
        title: "Heading 1",
        class: "ck-heading_heading1",
      },
      {
        model: "heading2",
        view: "h2",
        title: "Heading 2",
        class: "ck-heading_heading2",
      },
      {
        model: "heading3",
        view: "h3",
        title: "Heading 3",
        class: "ck-heading_heading3",
      },
      {
        model: "heading4",
        view: "h4",
        title: "Heading 4",
        class: "ck-heading_heading4",
      },
    ],
  },
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
  placeholder: "본문 내용을 작성해주세요",
  table: {
    contentToolbar: [
      "tableColumn",
      "tableRow",
      "mergeTableCells",
      "tableProperties",
      "tableCellProperties",
      "tableAutoFit",
    ],
  },
  translations: [translations],
  mediaEmbed: {
    previewsInData: true,
  },
  // simpleUpload: {
  //   // The URL that the images are uploaded to.
  //   uploadUrl: `${process.env.NEXT_PUBLIC_API_ENDPOINT}/files`,

  //   // Enable the XMLHttpRequest.withCredentials property.
  //   withCredentials: true,
  // },
};

class CustomUploadAdapter {
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
            await this.setFiles((prev) => ({
              ...prev,
              files: [...prev.files, payload],
            }));
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
  }
}

export { editorConfig, CustomUploadAdapter };
