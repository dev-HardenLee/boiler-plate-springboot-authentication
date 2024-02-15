INSERT INTO ROLE(role, text) VALUES ('ADMIN', '관리자');
INSERT INTO ROLE(role, text) VALUES ('USER', '일반 사용자');

INSERT INTO USERS (id, email, password, role) VALUES (1, 'harden@naver.com', 1234, 'ADMIN');
INSERT INTO USERS (id, email, password, role) VALUES (2, 'test1234@naver.com', 1234, 'USER');