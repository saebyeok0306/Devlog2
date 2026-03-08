import { atom } from "recoil";

// export class CommentState {
//   constructor(commentFlag = false) {
//     this.commentFlag = commentFlag;
//   }
// }
//
// export class CommentsData {
//   constructor(comments = [], commentCount = 0) {
//     this.comments = comments;
//     this.commentCount = commentCount;
//   }
// }

export const COMMENT_STATE_DEFAULT = {
  commentFlag: false,
};

export const COMMENTS_DATA_DEFAULT = {
  comments: [],
  commentCount: 0,
};

export const commentAtom = atom({
  key: "comment",
  default: { ...COMMENT_STATE_DEFAULT },
});

export const commentFilesAtom = atom({
  key: "commentFiles",
  default: [],
});

export const commentsAtom = atom({
  key: "comments",
  default: { ...COMMENTS_DATA_DEFAULT },
});
