package com.csicit.ace.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.vo.TreeVO;
import com.csicit.ace.dbplus.service.impl.BaseServiceImpl;
import com.csicit.ace.demo.domain.BookTypeDO;
import com.csicit.ace.demo.mapper.BookTypeMapper;
import com.csicit.ace.demo.service.BookTypeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shanwj
 * @date 2019/10/16 15:17
 */
@Service
public class BookTypeServiceImpl extends BaseServiceImpl<BookTypeMapper,BookTypeDO> implements BookTypeService {

    @Override
    public boolean saveBookType(BookTypeDO intance) {
        if(save(intance)){
            return true;
        }
        return false;
    }

    @Override
    public TreeVO getBookTypeTree(String appId) {
        List<BookTypeDO> types =
                list(new QueryWrapper<BookTypeDO>()
                        .eq("app_id",appId)
                        .orderByAsc("sort"));
        List<TreeVO> list = new ArrayList<>(16);
        types.stream().forEach(type->{
            TreeVO tree = new TreeVO();
            tree.setId(type.getId());
            tree.setLabel(type.getName());
            tree.setType("childNode");
            list.add(tree);
        });
        TreeVO tr = new TreeVO();
        tr.setId("0");
        tr.setLabel("所有书籍");
        tr.setType("parentNode");
        tr.setChildren(list);
        return tr;
    }
}
