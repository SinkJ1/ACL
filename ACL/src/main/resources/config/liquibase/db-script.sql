create table acl_sid(
                        id bigserial not null primary key,
                        sid varchar(100) not null,
                        constraint unique_uk_1 unique(sid)
);

create table acl_class(
                          id bigserial not null primary key,
                          class varchar(100) not null,
                          constraint unique_uk_2 unique(class)
);

create table acl_object_identity(
                                    id bigserial primary key,
                                    object_id_class bigint not null,
                                    object_id_identity bigint not null,
                                    parent_object bigint,
                                    owner_sid bigint,
                                    entries_inheriting boolean not null,
                                    constraint unique_uk_3 unique(object_id_class,object_id_identity),
                                    constraint foreign_fk_2 foreign key(object_id_class)references acl_class(id),
                                    constraint foreign_fk_3 foreign key(owner_sid)references acl_sid(id)
);

create table acl_mask (
    id bigserial not null primary key,
    name varchar(100) not null
);

create table acl_entry(
                          id bigserial primary key,
                          acl_object_identity bigint not null,
                          sid bigint not null,
                          mask integer not null,
                          granting boolean not null,
                          constraint foreign_fk_4 foreign key(acl_object_identity) references acl_object_identity(id),
                          constraint foreign_fk_5 foreign key(sid) references acl_sid(id),
                          constraint foreign_fk_6 foreign key(mask) references acl_mask(id)
);
