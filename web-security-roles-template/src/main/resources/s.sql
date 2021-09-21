insert into users (username,password,enabled)
values 
('bruce','$2a$10$6hVsdIZzU70GbEDymCNmie/by.Qp3alKLgbfUEUsq3Or.vhMlMSpy', 1),
('clark','$2a$10$6hVsdIZzU70GbEDymCNmie/by.Qp3alKLgbfUEUsq3Or.vhMlMSpy', 1),
('diana','$2a$10$6hVsdIZzU70GbEDymCNmie/by.Qp3alKLgbfUEUsq3Or.vhMlMSpy', 1);


INSERT INTO AUTHORITIES (AUTHORITY) 
VALUES 
('ROLE_READ'),
('ROLE_WRITE'), 
('ROLE_USER'), 
('ROLE_GUEST');

insert into customer_authority_jointable values (1,1);