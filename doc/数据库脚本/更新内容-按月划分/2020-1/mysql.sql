/**
2020/1/14
 */
ALTER TABLE `SYS_ROLE` CHANGE `EXPLAIN` `ROLE_EXPLAIN`varchar(200);

ALTER TABLE `SYS_DICT` CHANGE `EXPLAIN` `DICT_EXPLAIN`varchar(200);

UPDATE SYS_MENU set name='应用运行日志',icon='fa fa-window-restore' where id='99ea2ad7ea4a4e54acdaff4c84bb8371';

UPDATE SYS_MENU set name='用户操作日志' where id='19ea2ad7ea4a4e54acdaff4c84bb8370';

insert into `SYS_CONFIG`(`ID`,`NAME`,`VALUE`) values('ace-sql-version', 'ace-sql-version', '1.0.0');