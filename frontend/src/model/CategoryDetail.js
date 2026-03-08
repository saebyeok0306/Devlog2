export class CategoryDetail {
  constructor({
    layer,
    name = "새 카테고리",
    id = null,
    readCategoryAuth = "GUEST",
    writeCommentAuth = "GUEST",
    writePostAuth = "GUEST",
  }) {
    this.id = id;
    this.layer = layer;
    this.name = name;
    this.readCategoryAuth = readCategoryAuth;
    this.writeCommentAuth = writeCommentAuth;
    this.writePostAuth = writePostAuth;
  }

  toObject() {
    return {
      id: this.id,
      layer: this.layer,
      name: this.name,
      readCategoryAuth: this.readCategoryAuth,
      writeCommentAuth: this.writeCommentAuth,
      writePostAuth: this.writePostAuth,
    };
  }
}
