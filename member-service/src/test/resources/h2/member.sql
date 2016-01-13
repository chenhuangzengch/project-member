/*==============================================================*/
/* Table: cas_login                                             */
/*==============================================================*/
create table cas_login
(
   login_id             varchar(100) not null comment '登录号',
   password             varchar(100) comment '登录密码',
   login_type           int not null comment '登录类型',
   encryption           int comment '加密规则',
   salt                 varchar(100) comment '盐',
   user_id              varchar(32) comment '默认学乐号',
   primary key (login_id, login_type)
);

/*==============================================================*/
/* Table: cas_login_log                                         */
/*==============================================================*/
create table cas_login_log
(
   login_id             varchar(32) not null comment '登录号',
   login_type           int not null comment '登录类型',
   user_id              varchar(32) comment '学乐号',
   login_timestamp      timestamp comment '登录时间',
   session_id           varchar(20) comment '会话ID',
   primary key (login_id, login_type)
);

/*==============================================================*/
/* Table: d_areas                                               */
/*==============================================================*/
create table d_areas
(
   code                 varchar(20) not null comment '区划编号',
   name                 varchar(50) comment '区划名称',
   primary key (code)
);

/*==============================================================*/
/* Table: d_familly_members                                     */
/*==============================================================*/
create table d_familly_members
(
   member_id            varchar(32) not null comment '成员编号',
   member_name          varchar(100) comment '成员称谓',
   primary key (member_id)
);

/*==============================================================*/
/* Table: d_identity                                            */
/*==============================================================*/
create table d_identity
(
   id                   varchar(32) not null comment '身份id',
   name                 varchar(50) comment '身份名称',
   primary key (id)
);

/*==============================================================*/
/* Table: d_subject                                             */
/*==============================================================*/
create table d_subject
(
   subject_id           varchar(32) not null comment '科目ID',
   subject_name         varchar(100) comment '科目名称',
   primary key (subject_id)
);

/*==============================================================*/
/* Table: m_class                                               */
/*==============================================================*/
create table m_class
(
   class_id             varchar(32) not null comment '班级ID',
   school_id            varchar(32) comment '所属学校ID',
   school_name          varchar(100) comment '所属学校名称',
   charge_id            varchar(32) comment '班主任ID',
   charge_name          varchar(100) comment '班主任名称',
   years                int comment '学界',
   code_sharing         int comment '班级号,根据当前年级递增生成',
   m_image              varchar(100) comment '图标',
   cover                varchar(100) comment '封面',
   alias_name           varchar(100) comment '班级别名',
   name                 varchar(100) comment '班级名称',
   explains             varchar(500) comment '班级说明',
   create_time          datetime comment '创建时间',
   creator_id           varchar(32) comment '创建者ID',
   creator_name         varchar(100) comment '创建者名称',
   status               int comment '状态',
   primary key (class_id)
);

/*==============================================================*/
/* Table: m_education                                           */
/*==============================================================*/
create table m_education
(
   user_id              varchar(32) not null comment '学乐号',
   educational_id       varchar(32) comment '所属教育机构ID',
   educational_name     varchar(100) comment '所属教育机构',
   duty_id              varchar(32) comment '职务',
   duty_name            varchar(100) comment '职务名称',
   primary key (user_id)
);

/*==============================================================*/
/* Table: m_education_manager                                   */
/*==============================================================*/
create table m_education_manager
(
   user_id              varchar(32) not null comment '学乐号',
   educational_id       varchar(32) comment '所属教育机构ID',
   educational_name     varchar(100) comment '所属教育机构',
   primary key (user_id)
);

/*==============================================================*/
/* Table: m_education_organization                              */
/*==============================================================*/
create table m_education_organization
(
   org_id               varchar(32) not null comment '机构ID',
   name                 varchar(100) comment '名称',
   sort                 varchar(20) comment '排序码',
   level                int comment '层级',
   p_id                 varchar(32) comment '父机构ID',
   p_name               varchar(100) comment '父机构名称',
   image                varchar(100) comment '图标',
   cover                varchar(100) comment '封面',
   explains             varchar(500) comment '介绍',
   manager_id           varchar(32) comment '管理员ID',
   manager_name         varchar(100) comment '管理员名称',
   areas_name           varchar(50) comment '所属行政区划名称',
   areas                varchar(20) comment '所属行政区划',
   o_type               int comment '类型',
   status               int comment '状态',
   primary key (org_id)
);


/*==============================================================*/
/* Table: m_familly_relation                                    */
/*==============================================================*/
create table m_familly_relation
(
   id                   varchar(32) not null comment '序列号',
   user_id              varchar(32) comment '用户ID',
   target_user_id       varchar(32) comment '目标用户ID',
   member_id            varchar(32) comment '成员编号',
   member_name          varchar(100) comment '成员称谓',
   primary key (id)
);

/*==============================================================*/
/* Table: m_login_user                                          */
/*==============================================================*/
create table m_login_user
(
   login_id             varchar(32) not null comment '登录号',
   login_type           int not null comment '登录类型',
   user_id              varchar(32) not null comment '学乐号',
   primary key (login_id, login_type, user_id)
);

/*==============================================================*/
/* Table: m_parents                                             */
/*==============================================================*/
create table m_parents
(
   user_id              varchar(32) not null comment '用户ID',
   appellation          varchar(50) comment '家长称谓',
   primary key (user_id)
);

/*==============================================================*/
/* Table: m_position                                            */
/*==============================================================*/
create table m_position
(
   position_id          varchar(32) not null comment '职务ID',
   name                 varchar(50) comment '职务名称',
   description          varchar(100) comment '职务说明',
   position_type        int comment '类型（类型0表示系统职务，1表示自定义职务）',
   school_id            varchar(32) comment '所属学校ID',
   primary key (position_id)
);

/*==============================================================*/
/* Table: m_resources                                           */
/*==============================================================*/
create table m_resources
(
   resource_id          varchar(32) not null comment '资源ID',
   name                 varchar(100) comment '资源名称',
   url                  varchar(400) comment '资源地址',
   r_type               int comment '资源类型',
   status               int comment '状态(0有效;1无效)',
   pid                  varchar(32) comment '父ID',
   sort                 varchar(20) comment '排序码',
   level                int comment '层级',
   primary key (resource_id)
);


/*==============================================================*/
/* Table: m_role                                                */
/*==============================================================*/
create table m_role
(
   role_id              varchar(32) not null comment '角色id',
   role_name            varchar(100) comment '角色名称',
   status               int comment '状态',
   primary key (role_id)
);


/*==============================================================*/
/* Table: m_role_resource                                       */
/*==============================================================*/
create table m_role_resource
(
   role_id              varchar(32) not null comment '角色id',
   resource_id          varchar(32) not null comment '资源id',
   primary key (role_id, resource_id)
);


/*==============================================================*/
/* Table: m_school                                              */
/*==============================================================*/
create table m_school
(
   id                   varchar(32) not null comment '学校ID',
   name                 varchar(100) comment '学校名称',
   english_name         varchar(100) comment '学校英文名称',
   s_type               integer comment '学校类别',
   address              varchar(100) comment '学校地址',
   about                text comment '学校简介',
   tel                  varchar(50) comment '联系电话',
   motto                varchar(100) comment '校训',
   web                  varchar(100) comment '学校网址',
   length_of_school     int comment '学校学制总和(小学学制+初中学制+高中学制)',
   badge                varchar(100) comment '校徽',
   cover                varchar(100) comment '学校封面',
   manager_name         varchar(100) comment '管理员名称',
   manager              varchar(32) comment '管理员',
   school_time          datetime comment '建校时间',
   add_time             datetime comment '注册时间',
   area                 varchar(12) comment '地区编号',
   area_name            varchar(100) comment '地区编号名称',
   status               integer comment '状态',
   primary key (id)
);


/*==============================================================*/
/* Table: m_school_manager                                      */
/*==============================================================*/
create table m_school_manager
(
   user_id              varchar(32) not null comment '学乐号',
   school_id            varchar(32) comment '所属学校ID',
   school_name          varchar(100) comment '所属学校名称',
   primary key (user_id)
);


/*==============================================================*/
/* Table: m_school_period                                       */
/*==============================================================*/
create table m_school_period
(
   id                   varchar(32) not null comment '非业务序列主键',
   section              int comment '学段(0,1,2)',
   section_display      varchar(20) comment '学段说明(0,小学,1初中,2.高中)',
   length               int comment '学制(3年制,4年制,5年制)',
   school_id            varchar(32) comment '关联学校ID',
   primary key (id)
);


/*==============================================================*/
/* Table: m_student                                             */
/*==============================================================*/
create table m_student
(
   user_id              varchar(32) not null comment '学乐号',
   class_id             varchar(32) comment '所属班级id',
   class_name           varchar(100) comment '所属班级名称',
   class_alias_name     varchar(100) comment '所属班级别名',
   student_number       varchar(50) comment '学籍号',
   family_name          varchar(100) comment '家庭名称',
   family_cover         varchar(100) comment '家庭封面',
   school_id            varchar(32) comment '所属学校ID',
   school_name          varchar(100) comment '所属学校名称',
   primary key (user_id)
);


/*==============================================================*/
/* Table: m_teacher                                             */
/*==============================================================*/
create table m_teacher
(
   user_id              varchar(32) not null comment '学乐号',
   school_id            varchar(32) comment '所属学校ID',
   school_name          varchar(100) comment '所属学校名称',
   subject_id           varchar(32) comment '主授科目',
   subject_name         varchar(100) comment '主授科目名称',
   position_id          varchar(32) comment '学校职务ID（引用m_position字典)',
   position_name        varchar(50) comment '学校职务名称',
   is_manager           int comment '是否为管理层(0不是，1是）',
   primary key (user_id)
);


/*==============================================================*/
/* Table: m_teacher_class                                       */
/*==============================================================*/
create table m_teacher_class
(
   teacher_id           varchar(32) not null comment '老师id',
   class_id             varchar(32) not null comment '班级id',
   primary key (teacher_id, class_id)
);


/*==============================================================*/
/* Table: m_teacher_subject                                     */
/*==============================================================*/
create table m_teacher_subject
(
   user_id              varchar(32) not null comment '学乐号',
   subject_id           varchar(32) not null comment '科目id',
   subject_name         varchar(100) comment '科目名称',
   primary key (user_id, subject_id)
);


/*==============================================================*/
/* Table: m_user_role                                           */
/*==============================================================*/
create table m_user_role
(
   user_id              varchar(32) not null comment '学乐号',
   role_id              varchar(32) not null comment '角色ID',
   primary key (user_id, role_id)
);


/*==============================================================*/
/* Table: m_users                                               */
/*==============================================================*/
create table m_users
(
   user_id              varchar(32) not null comment '学乐号',
   real_name            varchar(10) comment '真实姓名',
   sex                  int comment '性别',
   birthday             date comment '生日',
   icon                 varchar(32) comment '头像',
   qq                   varchar(20) comment 'qq',
   r_type               int comment '资源类型',
   add_time             datetime comment '注册时间',
   reg_type             int comment '注册来源',
   reg_ip               varchar(20) comment '注册IP',
   status               int comment '用户状态,[0,有效;1,未初始化;2,离校;]',
   tel                  varchar(20) comment '电话号码',
   mobile               varchar(20) comment '手机',
   email                varchar(50) comment '邮箱',
   signature            varchar(300) comment '个人签名',
   cover                varchar(100) comment '封面,hdfs地址',
   update_time          timestamp comment '修改时间,修改的当前数据库时间',
   idcard               varchar(18) comment '身份证',
   identity_id          varchar(32) comment '身份类型,引用d_identity',
   identity_description varchar(300) comment '身份描述:用于切换视图.',
   account_id           varchar(32) comment '资金账号ID',
   primary key (user_id)
);


/*==============================================================*/
/* Table: n_attachment                                          */
/*==============================================================*/
create table n_attachment
(
   ATTA_ID              varchar(32) not null comment '附件ID',
   NAME                 varchar(100) comment '附件名称',
   MESSAGE_ID           varchar(32) comment '通知ID',
   ATTA_ADDRESS         varchar(100) comment '附件地址',
   A_TYPE               int comment '附件类型',
   A_SIZE               bigint comment '附件大小',
   SUFFIX               varchar(10) comment '附件后缀',
   F_TYPE               int comment '文件类型',
   primary key (ATTA_ID)
);


/*==============================================================*/
/* Table: n_message                                             */
/*==============================================================*/
create table n_message
(
   ID                   varchar(32) not null comment '序列ID',
   MESSAGE_ID           varchar(32) comment '通知ID',
   SEND_ID              varchar(32) comment '发送者',
   SEND_TIME            datetime comment '发送时间',
   STATUS               int comment '发送状态',
   TITLE                varchar(200) comment '标题',
   CONTENT              varchar(500) comment '内容',
   READ_TIME            datetime comment '接收时间',
   RECEIVER_NOTE        varchar(500) comment '接收者说明',
   RECEIVER             varchar(32) comment '接收者',
   THEME                int comment '主题',
   N_TYPE               int comment '类型',
   RECEIVER_STATUS      int comment '接收状态',
   RECEIVER_TIME        datetime comment '接收者已读时间',
   UPDATE_TIME          datetime comment '修改时间',
   primary key (ID)
);


