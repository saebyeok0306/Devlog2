export const replyTimelineTheme = {
  horizontal: "flex items-center",
  line: "hidden h-0.5 w-full bg-gray-200 dark:bg-gray-700 sm:flex",
  marker: {
    base: {
      horizontal:
        "absolute -left-1.5 h-3 w-3 rounded-full border border-white bg-gray-200 dark:border-gray-900 dark:bg-gray-700",
      vertical:
        "absolute -left-1.5 mt-1.5 h-3 w-3 rounded-full border border-white bg-gray-200 dark:border-gray-900 dark:bg-gray-700",
    },
    icon: {
      base: "h-3 w-3 text-yellow-600 dark:text-yellow-300",
      wrapper:
        "absolute -left-3 flex h-6 w-6 items-center justify-center rounded-full bg-yellow-200 dark:bg-yellow-900",
    },
  },
  vertical: "",
};
