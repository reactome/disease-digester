
    create table disease (
       id bigint not null auto_increment,
        diseaseClass varchar(255),
        diseaseId varchar(255),
        diseaseName varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table disease_gene (
       diseaseItems_id bigint not null,
        geneItems_id bigint not null
    ) engine=InnoDB;

    create table gene (
       id bigint not null auto_increment,
        accessionNumber varchar(255),
        geneId varchar(255),
        geneSymbol varchar(255),
        primary key (id)
    ) engine=InnoDB;

    alter table disease_gene 
       add constraint FKab9qkinadhs28pki7p1nv6s6i 
       foreign key (geneItems_id) 
       references gene (id);

    alter table disease_gene 
       add constraint FK4t0hm537nbkettiqv3wmgm80m 
       foreign key (diseaseItems_id) 
       references disease (id);
