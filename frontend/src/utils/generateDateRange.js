export const generateDateRange = (startDate, endDate) => {
  const dateArray = [];
  const currentDate = new Date(startDate);

  while (currentDate <= new Date(endDate)) {
    dateArray.push(currentDate.toISOString().split("T")[0]); // YYYY-MM-DD 형식
    currentDate.setDate(currentDate.getDate() + 1);
  }

  return dateArray;
};
