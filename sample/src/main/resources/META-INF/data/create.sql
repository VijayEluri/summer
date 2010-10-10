create table license (
    id integer generated by default as identity unique,
    name varchar(255),
    value varchar(255) not null unique,
    primary key (id)
);

create table status (
    id integer generated by default as identity unique,
    name varchar(255),
    value varchar(255) not null unique,
    primary key (id)
);

create table technology (
    id integer generated by default as identity unique,
    description varchar(512),
    homepage binary(255),
    image blob,
    name varchar(128),
    required bit,
    status binary(255),
    value varchar(255) not null unique,
    version varchar(255),
    primary key (id)
);

create table technology_license (
    technology_id integer not null,
    license_id integer not null
);

alter table technology_license 
    add constraint FKDC47E40ECE1CC532 
    foreign key (technology_id) 
    references technology;

alter table technology_license 
    add constraint FKDC47E40EF3E1A222 
    foreign key (license_id) 
    references license;