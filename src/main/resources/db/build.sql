create table t_msg
(
    msg_id         int auto_increment
        primary key,
    msg_type       varchar(64) null,
    msg_content    text        null,
    msg_extend     text        null,
    msg_url_list   text        null,
    msg_video_list text        null,
    msg_img_list   text        null,
    is_delete      int         null,
    create_time    timestamp   null,
    modify_time    timestamp   null
);

create table t_query_user
(
    q_id      int auto_increment
        primary key,
    q_user_id varchar(255) null,
    q_type    varchar(64)  null comment 'twitter',
    is_delete int          null comment '<0 is had bean deleted flag'
);

