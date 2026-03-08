"use client";
import React, { useEffect, useRef, useState } from "react";
import { Dropdown, Pagination } from "flowbite-react";
import { useRecoilState, useRecoilValue } from "recoil";

import "./Posts.scss";
import { onErrorImg } from "@/utils/defaultImg";
import { get_posts_api } from "@/api/posts";
import { categoryAtom } from "@/recoil/categoryAtom";
import { paginationCustomTheme } from "@/styles/theme/pagination";
import { getDatetime } from "@/utils/getDatetime";
import { authAtom } from "@/recoil/authAtom";

import { MasonryGrid } from "@egjs/react-grid";
import classNames from "classnames";
import { windowAtom } from "@/recoil/windowAtom";
import Link from "next/link";
import { postPagingAtom } from "@/recoil/postPagingAtom";

const scrollToTopHandler = () => {
  window.scrollTo({
    top: 0,
    behavior: "smooth",
  });
};

function PostCard(idx, post, setSelectCategory) {
  const createdAtFormat = getDatetime(post.createdAt);
  return (
    <div className="post" key={idx}>
      <Link href={`/post/${post.url}`}>
        <div className="post-item-top">
          {post.previewUrl ? (
            <img
              className="post-item-img"
              src={post.previewUrl}
              alt="post"
              onError={onErrorImg}
            />
          ) : null}
        </div>
        <div className="post-item-bottom">
          <div className="post-item-content">
            {/* <button
            className="post-item-category"
            onClick={() => setSelectCategory(post.category.id)}
          >
            {post.category.name}
          </button> */}
            <div className="post-item-category">{post.category.name}</div>
            {/* <Link to={`/post/${post.url}`} className="post-item-title">
            {post.title}
          </Link> */}
            <div className="post-item-title">{post.title}</div>
          </div>
          <div className="post-item-chore">
            <div className="post-item-create">{createdAtFormat}</div>
            <div className="post-item-views">{`조회 ${post.views}`}</div>
          </div>
        </div>
        {/* <div className="author">by {post.author}</div> */}
        {/* <div className="content">{post.content}</div> */}
      </Link>
    </div>
  );
}

function Posts() {
  const gridRef = useRef(null);
  const authDto = useRecoilValue(authAtom);
  const windows = useRecoilValue(windowAtom);
  const [selectCategory, setSelectCategory] = useRecoilState(categoryAtom);
  const [posts, setPosts] = useState([]);
  const [page, setPage] = useRecoilState(postPagingAtom);
  const [viewSize, setViewSize] = useState(10);
  const [rendering, setRendering] = useState(true);
  const [renderer, setRenderer] = useState(0);

  useEffect(() => {
    PostEnvetHandler(selectCategory, page.currentPage, viewSize);
    // eslint-disable-next-line
  }, [selectCategory, authDto]);

  const PostListHandler = (res_posts) => {
    setPosts(res_posts.posts);
    setPage({
      totalPages: res_posts.totalPages,
      currentPage: res_posts.currentPage,
      totalElements: res_posts.totalElements,
    });
    setRenderer((prev) => prev + 1);
  };

  const PostEnvetHandler = async (category, curpage, view) => {
    await get_posts_api(category, curpage, view)
      .then((response) => {
        PostListHandler(response);
      })
      .catch((error) => {
        console.error(error);
      });
  };

  const onDropdownHandler = (e, value) => {
    // viewSize 변경시 무조건 첫번째 페이지로 이동
    setRendering(false);
    setViewSize(value);
    setPage({
      ...page,
      currentPage: 0,
    });
    PostEnvetHandler(selectCategory, 0, value);
  };

  const onPaginationHandler = (next_page) => {
    scrollToTopHandler();
    setRendering(false);
    setPage({
      ...page,
      currentPage: next_page - 1,
    });
    PostEnvetHandler(selectCategory, next_page - 1, viewSize);
  };

  if (page.totalElements === 0) {
    return (
      <div className="posts-container">
        <div className="posts-empty">
          <img src="/images/nopost.png" alt="profile" onError={onErrorImg} />
          <p>글이 없네요.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="posts-container">
      <div className="posts-header">
        <h1 className="post-count">{page.totalElements} 포스트</h1>
        <div className="post-detail">
          <Dropdown label={`${viewSize}개 보기`} inline>
            <Dropdown.Item onClick={(e) => onDropdownHandler(e, 6)}>
              6개 보기
            </Dropdown.Item>
            <Dropdown.Item onClick={(e) => onDropdownHandler(e, 10)}>
              10개 보기
            </Dropdown.Item>
            <Dropdown.Item onClick={(e) => onDropdownHandler(e, 20)}>
              20개 보기
            </Dropdown.Item>
            <Dropdown.Item onClick={(e) => onDropdownHandler(e, 50)}>
              50개 보기
            </Dropdown.Item>
          </Dropdown>
        </div>
      </div>

      <MasonryGrid
        key={renderer}
        ref={gridRef}
        className={classNames("posts", { render: rendering })}
        align="center"
        column={windows.width > 768 ? "2" : "1"}
        gap={10}
        onRenderComplete={(e) => setRendering(true)}
        // onRequestAppend={(e) => {
        //   const nextGroupKey = (e.groupKey || 0) + 1;
        //   console.log(e);
        // }}
      >
        {posts.map((val, idx) => PostCard(idx, val, setSelectCategory))}
      </MasonryGrid>
      <div className="flex overflow-x-auto sm:justify-center post-pageination">
        <Pagination
          theme={paginationCustomTheme}
          layout="pagination"
          currentPage={page.currentPage + 1}
          totalPages={page.totalPages}
          onPageChange={onPaginationHandler}
          previousLabel="이전"
          nextLabel="다음"
          showIcons
        />
      </div>
    </div>
  );
}

export default Posts;
