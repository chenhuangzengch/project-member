insert into m_users(user_id,real_name) VALUES('user1','user1');
insert into m_users(user_id,real_name) VALUES('user2','user2');
insert into m_role(role_id,role_name,status) VALUES('role1','role1',0);
insert into m_role(role_id,role_name,status) VALUES('role2','role2',0);
insert into m_user_role(user_id,role_id) VALUES ('user1','role1');
insert into m_user_role(user_id,role_id) VALUES('user2','role2');


INSERT INTO m_users
(user_id, status, identity_id, identity_description)
VALUES('admin', 1 ,'student', '老师');

INSERT INTO m_users
(user_id, status, identity_id, identity_description)
VALUES('teacher', 1 ,'teacher', '老师');


INSERT INTO m_teacher
(user_id, school_id, school_name, subject_id, subject_name, position_id, position_name, is_manager)
VALUES('admin', '所属学校ID', 'm_teacher', 's', 'subject_name', 'position_id', 'position_name', 1);

INSERT INTO m_student
(user_id, class_id, class_name, class_alias_name, student_number, family_name, family_cover, school_id, school_name)
VALUES('admin', 'class_id', 'class_name', 'class_alias_name', 'student_number', 'family_name', 'family_cover', 'school_id', 'school_name');

INSERT INTO m_school_manager
(user_id, school_id, school_name)
VALUES('admin', 'school_id', 'school_name');

INSERT INTO m_education_manager
(user_id, educational_id, educational_name)


VALUES('admin', 'educational_id', 'educational_name');

INSERT INTO m_education
(user_id, educational_id, educational_name, duty_id, duty_name)
VALUES('admin', 'educational_id', 'educational_name', 'duty_id', 'duty_name');

INSERT INTO m_parents
(user_id, appellation)
VALUES('admin', 'appellation');