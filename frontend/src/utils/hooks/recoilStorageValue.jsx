export const recoilStorageValue = (persistKey, atomKey) => {
  try {
    return JSON.parse(sessionStorage.getItem(persistKey))[atomKey];
  } catch {
    return null;
  }
};
