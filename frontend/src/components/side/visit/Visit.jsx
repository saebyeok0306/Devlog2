"use client";
import { get_blog_visit_daily_api } from "@/api/info";
import { generateDateRange } from "@/utils/generateDateRange";
import moment from "moment-timezone";
import React, { useEffect, useState } from "react";

import "./Visit.scss";

function Visit() {
  const [visit, setVisit] = useState({
    today: 0,
    yesterday: 0,
    total: 0,
  });

  useEffect(() => {
    const getVisitCount = async () => {
      const start_date = moment.tz("Asia/Seoul");
      const end_date = moment.tz("Asia/Seoul");

      start_date.subtract(1, "days");
      const start = start_date.format("YYYY-MM-DD");
      const end = end_date.format("YYYY-MM-DD");

      await get_blog_visit_daily_api(start, end)
        .then((res) => {
          const labels = generateDateRange(start, end);
          const visitCounts = labels.map((date) => {
            const entry = res.visitCounts.find(
              (item) => item.viewDate === date
            );
            return entry ? entry.viewCount : 0;
          });
          setVisit({
            ...visit,
            today: visitCounts[visitCounts.length - 1] | 0,
            yesterday: visitCounts[visitCounts.length - 2] | 0,
            total: res.totalCount | 0,
          });
        })
        .catch((err) => {
          console.error("Failed to get blog visit:", err);
        });
    };

    getVisitCount();
    // eslint-disable-next-line
  }, []);
  return (
    <div className="blog-visit-box">
      <div className="container">
        <div className="today">
          <span className="name">오늘</span>
          <span className="value">{visit.today.toLocaleString()}명</span>
        </div>
        <div className="yesterday">
          <span className="name">어제</span>
          <span className="value">{visit.yesterday.toLocaleString()}명</span>
        </div>
        <div className="total">
          <span className="name">방문</span>
          <span className="value">{visit.total.toLocaleString()}명</span>
        </div>
      </div>
    </div>
  );
}

export default Visit;
