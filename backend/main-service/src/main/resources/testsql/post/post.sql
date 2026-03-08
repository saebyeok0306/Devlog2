INSERT INTO category(id, LAYER, NAME, WRITE_POST_AUTH, WRITE_COMMENT_AUTH, READ_CATEGORY_AUTH, CREATED_AT) VALUES (1, 1, 'category1', 0, 100, 0, '2024-07-18 00:00:00.000000');
INSERT INTO category(id, LAYER, NAME, WRITE_POST_AUTH, WRITE_COMMENT_AUTH, READ_CATEGORY_AUTH, CREATED_AT) VALUES (2, 2, 'category2', 100, 0, 900, '2024-07-18 00:00:00.000000');

INSERT INTO users(id, email, password, username, role, modified_at, created_at) VALUES (1, 'westreed@naver.com', '$2a$10$cMHXwKT6/./cIZM07jXrr.IT7doJwnr6benYByx25Pzo6ZKS8Mcui', '갈대', 'ADMIN', '2024-07-18 00:00:00.000000', '2024-07-18 00:00:00.000000');
INSERT INTO users(id, email, password, username, role, modified_at, created_at) VALUES (2, 'test@naver.com', '$2a$10$cMHXwKT6/./cIZM07jXrr.IT7doJwnr6benYByx25Pzo6ZKS8Mcui', 'test1', 'USER', '2024-07-18 00:00:00.000000', '2024-07-18 00:00:00.000000');

INSERT INTO post(id, url, title, content, preview_url, user_id, category_id, views, hidden, modified_at, created_at) VALUES (1, 'url1', 'title', 'content', '', 1, 1, 0, 0, '2024-07-18 00:00:00.000000', '2024-07-18 00:00:00.000000');
INSERT INTO post(id, url, title, content, preview_url, user_id, category_id, views, hidden, modified_at, created_at) VALUES (2, 'url2', 'title', 'content', '', 1, 2, 0, 0, '2024-07-18 00:00:00.000000', '2024-07-18 00:00:00.000000');
INSERT INTO post(id, url, title, content, preview_url, user_id, category_id, views, hidden, modified_at, created_at) VALUES (3, 'url3', 'title', 'content', '', 1, 1, 0, 0, '2024-07-18 00:00:00.000000', '2024-07-18 00:00:00.000000');
INSERT INTO post(id, url, title, content, preview_url, user_id, category_id, views, hidden, modified_at, created_at) VALUES (4, 'url4', 'title', 'content', '', 2, 1, 0, 1, '2024-07-18 00:00:00.000000', '2024-07-18 00:00:00.000000');
INSERT INTO post(id, url, title, content, preview_url, user_id, category_id, views, hidden, modified_at, created_at) VALUES (5, 'url5', 'title', 'content', '', 1, 1, 0, 1, '2024-07-18 00:00:00.000000', '2024-07-18 00:00:00.000000');
INSERT INTO post(id, url, title, content, preview_url, user_id, category_id, views, hidden, modified_at, created_at) VALUES (6, 'url6', 'title', 'content', '', 1, 1, 0, 0, '2024-07-18 00:00:00.000000', '2024-07-18 00:00:00.000000');
INSERT INTO post(id, url, title, content, preview_url, user_id, category_id, views, hidden, modified_at, created_at) VALUES (7, 'url7', 'title', 'content', '', 1, 1, 0, 0, '2024-07-18 00:00:00.000000', '2024-07-18 00:00:00.000000');
