INSERT INTO `authorities` (`id`, `name`) VALUES
	(1, 'ROLE_ADMIN'),
	(2, 'ROLE_USER');


	INSERT INTO `users` (`id`, `created_date`, `delete_dat`, `last_modified_date`, `email`, `first_name`, `last_name`, `login`, `password_hash`) VALUES
    	(3, '2022-11-27 17:01:13', NULL, '2022-11-27 17:01:13', 'admin@openweb.com', 'Liz Smith', 'marries', 'admin@openweb.com', '$2a$10$kEQGg5s5LfWJxtvI38.go.djnOdznjlc3RPUuHoTkj5sOekAHhFFy');

	INSERT INTO `users` (`id`, `created_date`, `delete_dat`, `last_modified_date`, `email`, `first_name`, `last_name`, `login`, `password_hash`) VALUES
    	(4, '2022-11-27 17:01:13', NULL, '2022-11-27 17:01:13', 'user@openweb.com', 'Smith', 'Mary Elizabeth', 'user@openweb.com', '$2a$10$kEQGg5s5LfWJxtvI38.go.djnOdznjlc3RPUuHoTkj5sOekAHhFFy');


INSERT INTO `user_authority` (`user_id`, `authority_id`) VALUES
	(3, 1);

INSERT INTO `user_authority` (`user_id`, `authority_id`) VALUES
    (4, 2);
