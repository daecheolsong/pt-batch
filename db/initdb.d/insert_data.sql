INSERT INTO package (package_name, count, period, created_at)
VALUES ('Starter PT 10회', 10, 60, '2022-08-01 00:00:00'),
       ('Starter PT 20회', 20, 120, '2022-08-01 00:00:00'),
       ('Starter PT 30회', 30, 180, '2022-08-01 00:00:00'),
       ('무료 이벤트 필라테스 1회', 1, NULL, '2022-08-01 00:00:00'),
       ('바디 챌린지 PT 4주', NULL, 28, '2022-08-01 00:00:00'),
       ('바디 챌린지 PT 8주', NULL, 48, '2022-08-01 00:00:00'),
       ('인바디 상담', NULL, NULL, '2022-08-01 00:00:00');

INSERT INTO `user` (user_id, user_name, status, phone, meta, created_at)
VALUES ('A1000000', '우영우', 'ACTIVE', '01011112222', NULL, '2022-08-01 00:00:00'),
       ('A1000001', '최수연', 'ACTIVE', '01033334444', NULL, '2022-08-01 00:00:00'),
       ('A1000002', '이준호', 'INACTIVE', '01055556666', NULL, '2022-08-01 00:00:00'),
       ('B1000010', '권민우', 'ACTIVE', '01077778888', NULL, '2022-08-01 00:00:00'),
       ('B1000011', '동그라미', 'INACTIVE', '01088889999', NULL, '2022-08-01 00:00:00'),
       ('B2000000', '한선영', 'ACTIVE', '01099990000', NULL, '2022-08-01 00:00:00'),
       ('B2000001', '태수미', 'ACTIVE', '01000001111', NULL, '2022-08-01 00:00:00');

INSERT INTO `user_group` (user_group_id, user_group_name, description)
VALUES ('HANBADA', '한바다', '한바다 임직원 그룹'),
       ('TAESAN', '태산', '태산 임직원 그룹');

INSERT INTO user_group_mapping (user_group_id, user_id, created_at)
VALUES ('HANBADA', 'A1000000', '2022-08-01 00:00:00'),
       ('HANBADA', 'A1000001', '2022-08-01 00:00:00'),
       ('HANBADA', 'A1000002', '2022-08-01 00:00:00'),
       ('HANBADA', 'B1000010', '2022-08-01 00:00:00'),
       ('HANBADA', 'B2000000', '2022-08-01 00:00:00'),
       ('TAESAN', 'B2000001', '2022-08-01 00:00:00');