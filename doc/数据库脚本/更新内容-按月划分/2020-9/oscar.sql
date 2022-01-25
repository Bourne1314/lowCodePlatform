/**
修改菜单图标
 */
update sys_menu set icon=NULL where app_id='platform';
update sys_menu set icon ='aceicon aceicon-apartment' where id='bc316279a4a9416a8e20019821e640af';
update sys_menu set icon ='aceicon aceicon-desktop' where id='e3b15ac90286450fbe5a721c9f542451';
update sys_menu set icon ='aceicon aceicon-setting' where id='11316279a4a9416a8e20019821e640af';
update sys_menu set icon ='aceicon aceicon-home' where id='e3b15ac90286450fbe5a721c9f542433';
update sys_menu set icon ='aceicon aceicon-cloud-server' where id='e3b15ac90286450fbe5a721c9f542344';
update sys_menu set icon ='aceicon aceicon-branches' where id='3333ba3204344d37929abd4ad9a12222';
update sys_menu set icon ='aceicon aceicon-View' where id='twerba3204344d37929abd4ad9a1dfgh';


/**
 *  FK_SYS_USER_ID_ORG_ID，FK_SYS_USER_ID_GROUP_ID级联删除
 * @param null
 * @return
 * @author zuogang
 * @date 2020/9/28 11:50
 */
alter table "SYS_USER" DROP CONSTRAINT FK_SYS_USER_ID_GROUP_ID;
ALTER TABLE "SYS_USER"
  ADD CONSTRAINT "FK_SYS_USER_ID_GROUP_ID" FOREIGN KEY ("GROUP_ID") REFERENCES "ORG_GROUP"("ID") ON DELETE CASCADE;

alter table "SYS_USER" DROP CONSTRAINT FK_SYS_USER_ID_ORG_ID;
ALTER TABLE "SYS_USER"
  ADD CONSTRAINT "FK_SYS_USER_ID_ORG_ID" FOREIGN KEY ("ORGANIZATION_ID") REFERENCES "ORG_ORGANIZATION"("ID") ON DELETE CASCADE;












