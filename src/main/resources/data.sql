INSERT INTO ROLE(role, text, parent_role) VALUES ('ROLE_ADMIN'                   , '관리자'        , null);
INSERT INTO ROLE(role, text, parent_role) VALUES ('ROLE_USER'                    , '일반 사용자'    , 'ROLE_ADMIN');
INSERT INTO ROLE(role, text, parent_role) VALUES ('ROLE_IT_TEAM_CAPTAIN'         , 'IT팀 팀장'     , 'ROLE_ADMIN');
INSERT INTO ROLE(role, text, parent_role) VALUES ('ROLE_IT_TEAM_DEVELOPER'       , 'IT팀 개발자'    , 'ROLE_IT_TEAM_CAPTAIN');
INSERT INTO ROLE(role, text, parent_role) VALUES ('ROLE_IT_TEAM_PLANNER'         , 'IT팀 기획자'    , 'ROLE_IT_TEAM_CAPTAIN');
INSERT INTO ROLE(role, text, parent_role) VALUES ('ROLE_MANAGEMENT_TEAM_CAPTAIN' , '경영팀 팀장'    , 'ROLE_ADMIN');
INSERT INTO ROLE(role, text, parent_role) VALUES ('ROLE_MANAGEMENT_TEAM_PERSONAL', '경영팀 인사직원' , 'ROLE_MANAGEMENT_TEAM_CAPTAIN');
INSERT INTO ROLE(role, text, parent_role) VALUES ('ROLE_MANAGEMENT_TEAM_AFFAIRS' , '경영팀 총무직원' , 'ROLE_MANAGEMENT_TEAM_CAPTAIN');

INSERT INTO USERS (id, email, password, role) VALUES (1, 'harden@naver.com', '$2a$10$zctnk7NWV62JrmeR1T7i1.C6xpzKTospDjfFoubTo6F7PiuHQ1k72', 'ROLE_ADMIN'); -- 1234
INSERT INTO USERS (id, email, password, role) VALUES (2, 'test1234@naver.com', '$2a$10$zctnk7NWV62JrmeR1T7i1.C6xpzKTospDjfFoubTo6F7PiuHQ1k72', 'ROLE_USER'); -- 1234

INSERT INTO RESOURCE(resource_id, url, method, permit_type) VALUES (1, '/api/admin'          , 'GET', 'hierarchy');
INSERT INTO RESOURCE(resource_id, url, method, permit_type) VALUES (2, '/api/user'           , 'GET', 'hierarchy');
INSERT INTO RESOURCE(resource_id, url, method, permit_type) VALUES (3, '/api/it-team'        , 'GET', 'hierarchy');
INSERT INTO RESOURCE(resource_id, url, method, permit_type) VALUES (4, '/api/management-team', 'GET', 'hierarchy');
INSERT INTO RESOURCE(resource_id, url, method, permit_type) VALUES (5, '/api/home'           , 'GET', 'all');
INSERT INTO RESOURCE(resource_id, url, method, permit_type) VALUES (6, '/api/auth'           , 'GET', 'authenticated');
INSERT INTO RESOURCE(resource_id, url, method, permit_type) VALUES (7, '/api/anonymous'      , 'GET', 'anonymous');

INSERT INTO RESOURCE_ROLE(id, resource_id, role) VALUES (1, 1, 'ROLE_ADMIN');
INSERT INTO RESOURCE_ROLE(id, resource_id, role) VALUES (2, 2, 'ROLE_USER');
INSERT INTO RESOURCE_ROLE(id, resource_id, role) VALUES (3, 3, 'ROLE_IT_TEAM_DEVELOPER');
INSERT INTO RESOURCE_ROLE(id, resource_id, role) VALUES (4, 3, 'ROLE_IT_TEAM_PLANNER');
INSERT INTO RESOURCE_ROLE(id, resource_id, role) VALUES (5, 4, 'ROLE_MANAGEMENT_TEAM_PERSONAL');
INSERT INTO RESOURCE_ROLE(id, resource_id, role) VALUES (6, 4, 'ROLE_MANAGEMENT_TEAM_AFFAIRS');
INSERT INTO RESOURCE_ROLE(id, resource_id, role) VALUES (7, 5, null);
INSERT INTO RESOURCE_ROLE(id, resource_id, role) VALUES (8, 6, null);
INSERT INTO RESOURCE_ROLE(id, resource_id, role) VALUES (9, 7, null);















