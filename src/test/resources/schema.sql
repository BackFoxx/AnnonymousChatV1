create table USER_TABLE
(
    ID        BIGINT auto_increment
        primary key,
    USEREMAIL VARCHAR(30) not null,
    PASSWORD  VARCHAR(20) not null
);

create table CHAT
(
    ID         BIGINT auto_increment primary key,
    CONTENT    VARCHAR(140) not null,
    CREATEDATE TIMESTAMP default CURRENT_TIMESTAMP not null,
    USER_ID    BIGINT not null,
    constraint CHAT_USER_TABLE_ID_FK
        foreign key (USER_ID) references USER_TABLE
            on delete cascade
);

create table REPLYCHAT
(
    ID         BIGINT auto_increment
        primary key,
    CONTENT    VARCHAR(140) not null,
    CREATEDATE TIMESTAMP default CURRENT_TIMESTAMP not null,
    CHAT_ID    BIGINT                              not null,
    USER_ID    BIGINT                              not null,
    constraint REPLYCHAT_CHAT_ID_FK
        foreign key (CHAT_ID) references CHAT
            on delete cascade,
    constraint REPLYCHAT_USER_TABLE_ID_FK
        foreign key (USER_ID) references USER_TABLE
            on delete cascade
);