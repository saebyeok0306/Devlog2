const POST_STORE = new Map();
const INFO_STORE = new Map();
const CATEGORY_STORE = new Map();
const REFRESH_STORE = new Map();
const ETC_STORE = new Map();

const clearAllCacheStore = () => {
  POST_STORE.clear();
  INFO_STORE.clear();
  CATEGORY_STORE.clear();
  REFRESH_STORE.clear();
  ETC_STORE.clear();
};

const showAllCacheStore = () => {
  console.log("POST_STORE", POST_STORE);
  console.log("INFO_STORE", INFO_STORE);
  console.log("CATEGORY_STORE", CATEGORY_STORE);
  console.log("REFRESH_STORE", REFRESH_STORE);
};

export {
  POST_STORE,
  INFO_STORE,
  CATEGORY_STORE,
  REFRESH_STORE,
  ETC_STORE,
  clearAllCacheStore,
  showAllCacheStore,
};
