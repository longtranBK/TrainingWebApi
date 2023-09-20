CREATE TABLE `users` (
  `id` varchar(36) PRIMARY KEY,
  `full_name` varchar(128),
  `avatar_url` varchar(256),
  `username` varchar(100),
  `password` varchar(100),
  `reset_pasword_token` varchar(30),
  `create_ts` timestamp,
  `update_ts` timestamp
);

CREATE TABLE `user_infor` (
  `id` varchar(36) PRIMARY KEY,
  `is_active` boolean,
  `sex` char(1),
  `study_at` varchar(128),
  `working_at` varchar(128),
  `favorites` varchar(1024),
  `other_infor` varchar(1024),
  `date_of_birth` date,
  `create_ts` timestamp,
  `update_ts` timestamp
);

CREATE TABLE `user_friend` (
  `id` varchar(36) PRIMARY KEY,
  `user1` varchar(36),
  `user2` varchar(36)
);

CREATE TABLE `posts` (
  `id` varchar(36) PRIMARY KEY,
  `user_id` varchar(36),
  `content` varchar(5000),
  `status` int,
  `create_ts` timestamp,
  `update_ts` timestamp
);

CREATE TABLE `likes` (
  `id` varchar(36) PRIMARY KEY,
  `user_id` varchar(36),
  `post_id` varchar(36)
);

CREATE TABLE `captures` (
  `id` varchar(36) PRIMARY KEY,
  `post_id` varchar(36),
  `capture_url` varchar(256)
);

CREATE TABLE `comment` (
  `id` varchar(36) PRIMARY KEY,
  `post_id` varchar(36),
  `content` varchar(1024),
  `create_ts` timestamp,
  `update_ts` timestamp
);

CREATE TABLE `user_roles` (
  `user_id` varchar(36),
  `role_id` varchar(36)
);

CREATE TABLE `roles` (
  `id` varchar(36),
  `role_name` varchar(50)
);

ALTER TABLE `user_infor` ADD FOREIGN KEY (`id`) REFERENCES `users` (`id`);

ALTER TABLE `user_friend` ADD FOREIGN KEY (`user1`) REFERENCES `users` (`id`);

ALTER TABLE `user_friend` ADD FOREIGN KEY (`user2`) REFERENCES `users` (`id`);

ALTER TABLE `posts` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `likes` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `likes` ADD FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`);

ALTER TABLE `comment` ADD FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`);

ALTER TABLE `captures` ADD FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`);

ALTER TABLE `user_roles` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `user_roles` ADD FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`);
