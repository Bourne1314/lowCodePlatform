package com.csicit.ace.dev.service.impl;

import com.csicit.ace.common.pojo.domain.dev.ProModelAssociationDO;
import com.csicit.ace.data.persistent.mapper.ProModelAssociationMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.dev.service.LiquibaseService;
import com.csicit.ace.dev.service.ProModelAssociationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


/**
 * 数据关联定义 实例对象访问接口实现
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-11-07 10:31:39
 */
@Service("proModelAssociationService")
public class ProModelAssociationServiceImpl extends BaseServiceImpl<ProModelAssociationMapper, ProModelAssociationDO>
        implements ProModelAssociationService {

//    @Autowired
//    private ProModelIndexService proModelIndexService;
    @Autowired
    private LiquibaseService liquibaseService;

    @Override
    public boolean saveAssociation(ProModelAssociationDO ass) {
        ass.setCreateTime(LocalDateTime.now());

        if (!liquibaseService.addLiquibaseAss(ass)) {
            return false;
        }

//        ProModelIndexDO proModelIndexDO = new ProModelIndexDO();
//        if (Objects.equals(1, ass.getCreateIdx())) {
//            proModelIndexDO.setCreateTime(LocalDateTime.now());
//            proModelIndexDO.setId(UuidUtils.createUUID());
//            proModelIndexDO.setModelId(ass.getModelId());
//            proModelIndexDO.setName(ass.getIndexName());
//            proModelIndexDO.setOnlyOne(0);
//            proModelIndexDO.setCols(ass.getColNames());
//            proModelIndexDO.setAssAction(1);
//            if (!liquibaseService.addLiquibaseIndex(proModelIndexDO)) {
//                return false;
//            }
//
//        }

//        if (Objects.equals(1, ass.getCreateIdx())) {
//            if (!proModelIndexService.save(proModelIndexDO)) {
//                return false;
//            }
//            ass.setIndexId(proModelIndexDO.getId());
//        }

        if (!save(ass)) {
            return false;
        }

        return true;
    }

    @Override
    public boolean updateAssociation(ProModelAssociationDO ass) {
        // 只支持修改外键名
        if (!liquibaseService.updLiquibaseAss(ass)) {
            return false;
        }
//        ProModelAssociationDO oldAss = getById(ass.getId());
//        if (Objects.equals(1, ass.getCreateIdx())) {
//            if (!Objects.equals(ass.getIndexName(), oldAss.getIndexName())) {
//                ProModelIndexDO proModelIndexDO = proModelIndexService.getById(ass.getIndexId());
//                proModelIndexDO.setName(ass.getIndexName());
//                if (!liquibaseService.updLiquibaseIndex
//                        (proModelIndexDO)) {
//                    return false;
//                }
//                if (!proModelIndexService.updateById(proModelIndexDO)) {
//                    return false;
//                }
//            }
//        }
        if (!updateById(ass)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteAssociation(String id) {
        ProModelAssociationDO proModelAssociationDO = getById(id);
        if (!liquibaseService.delLiquibaseAss(proModelAssociationDO)) {
            return false;
        }

//        if (Objects.equals(1, proModelAssociationDO.getCreateIdx())) {
//            if (!liquibaseService
//                    .delLiquibaseIndex
//                            (proModelIndexService.getById(proModelAssociationDO.getIndexId()))) {
//                return false;
//            }
//        }

//        if (Objects.equals(1, proModelAssociationDO.getCreateIdx())) {
//            // 删除索引
//            if (!proModelIndexService.removeById(proModelAssociationDO.getIndexId())) {
//                return false;
//            }
//        }
        if (!removeById(id)) {
            return false;
        }


        return true;
    }
}
