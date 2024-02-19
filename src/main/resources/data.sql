INSERT INTO ROLE(role, text) VALUES ('ROLE_ADMIN', '관리자');
INSERT INTO ROLE(role, text) VALUES ('ROLE_USER', '일반 사용자');

INSERT INTO USERS (id, email, password, role) VALUES (1, 'harden@naver.com', '$2a$10$zctnk7NWV62JrmeR1T7i1.C6xpzKTospDjfFoubTo6F7PiuHQ1k72', 'ROLE_ADMIN'); -- 1234
INSERT INTO USERS (id, email, password, role) VALUES (2, 'test1234@naver.com', '$2a$10$zctnk7NWV62JrmeR1T7i1.C6xpzKTospDjfFoubTo6F7PiuHQ1k72', 'ROLE_USER'); -- 1234