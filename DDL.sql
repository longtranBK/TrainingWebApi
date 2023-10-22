DROP TABLE IF EXISTS avatar_image;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS comment_likes;
DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS post_image;
DROP TABLE IF EXISTS post_likes;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS user_friend;
DROP TABLE IF EXISTS user_infor;
DROP TABLE IF EXISTS user_roles;

CREATE TABLE `users` (
  `id` varchar(36) PRIMARY KEY,
  `username` varchar(100),
  `password` varchar(100),
  `email` varchar(100),
  `reset_password_token` varchar(100),
  `create_ts` timestamp DEFAULT CURRENT_TIMESTAMP,
  `update_ts` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `user_infor` (
  `id` varchar(36) PRIMARY KEY,
  `full_name` varchar(128),
  `avatar_url` varchar(256),
  `is_active` boolean DEFAULT true,
  `sex` varchar(1),
  `study_at` varchar(128),
  `working_at` varchar(128),
  `favorites` varchar(1024),
  `other_infor` varchar(1024),
  `date_of_birth` date,
  `create_ts` timestamp DEFAULT CURRENT_TIMESTAMP,
  `update_ts` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `user_friend` (
  `user1` varchar(36),
  `user2` varchar(36),
  `status` varchar(1),
  `create_ts` timestamp,
  `update_ts` timestamp,
  PRIMARY KEY (`user1`, `user2`)
);

CREATE TABLE `posts` (
  `id` varchar(36) PRIMARY KEY,
  `user_id` varchar(36),
  `content` varchar(5000),
  `delFlg` boolean DEFAULT false,
  `create_ts` timestamp DEFAULT CURRENT_TIMESTAMP,
  `update_ts` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `post_likes` (
  `user_id` varchar(36),
  `post_id` varchar(36),
`create_ts` timestamp DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`, `post_id`)
);

CREATE TABLE `comment_likes` (
  `user_id` varchar(36),
  `comment_id` varchar(36),
  `create_ts` timestamp DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`, `comment_id`)
);

CREATE TABLE `post_image` (
  `post_id` varchar(36),
  `image_url` varchar(256) PRIMARY KEY
);

CREATE TABLE `avatar_image` (
  `user_id` varchar(36) PRIMARY KEY,
  `image_url` varchar(256)
);

CREATE TABLE `comments` (
  `id` varchar(36),
  `user_id` varchar(36),
  `post_id` varchar(36),
  `content` varchar(1024),
  `delFlg` boolean DEFAULT false,
  `create_ts` timestamp DEFAULT CURRENT_TIMESTAMP,
  `update_ts` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `user_roles` (
  `user_id` varchar(36),
  `role_id` varchar(36),
  PRIMARY KEY (`user_id`, `role_id`)
);

CREATE TABLE `roles` (
  `id` varchar(36) PRIMARY KEY,
  `role_name` varchar(50)
);

ALTER TABLE `user_infor` ADD FOREIGN KEY (`id`) REFERENCES `users` (`id`);

ALTER TABLE `user_friend` ADD FOREIGN KEY (`user1`) REFERENCES `users` (`id`);

ALTER TABLE `user_friend` ADD FOREIGN KEY (`user2`) REFERENCES `users` (`id`);

ALTER TABLE `posts` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `comments` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `post_likes` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `comment_likes` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `avatar_image` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `post_image` ADD FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`);

ALTER TABLE `comments` ADD FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`);

ALTER TABLE `post_likes` ADD FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`);

ALTER TABLE `user_roles` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `user_roles` ADD FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`);

ALTER TABLE `comment_likes` ADD FOREIGN KEY (`comment_id`) REFERENCES `comments` (`id`);
