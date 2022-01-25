package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.BdPersonIdTypeDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.platform.core.service.BdPersonIdTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 人员证件类型接口访问层
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-04-10 18:57:46
 */

@RestController
@RequestMapping("/bdPersonIdTypes")
@Api("人员证件类型管理")
public class BdPersonIdTypeController extends BaseController {
    @Autowired
    private BdPersonIdTypeService bdPersonIdTypeService;

    /**
     * 获取单个人员证件类型
     *
     * @param id 人员证件类型id
     * @return com.csicit.ace.common.utils.server.R
     * @author shanwj
     * @date 2019/4/11 18:25
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个人员证件类型", httpMethod = "GET", notes = "获取单个人员证件类型")
    @AceAuth("获取单个人员证件类型")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        BdPersonIdTypeDO instance = bdPersonIdTypeService.getById(id);
        return R.ok().put("instance", instance);
    }

    /**
     * 获取人员证件类型列表并分页
     *
     * @param params 请求参数map对象
     * @return com.csicit.ace.common.utils.server.R 人员证件类型列表
     * @author yansiyang
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "获取人员证件类型列表并分页", httpMethod = "GET")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取人员证件类型列表并分页")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public R listWithPage(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        String groupId = (String) params.get("groupId");
        if (StringUtils.isBlank(groupId)) {
            return R.error(String.format(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM"), "groupId"));
        }
        Page<BdPersonIdTypeDO> page = new Page<>(current, size);
        IPage list = bdPersonIdTypeService.page(page, new QueryWrapper<BdPersonIdTypeDO>().orderByAsc
                ("code").eq("group_id", groupId));
        return R.ok().put("page", list);
    }

    /**
     * 获取人员证件类型列表
     *
     * @param groupId 请求参数map对象
     * @return com.csicit.ace.common.utils.server.R 人员证件类型列表
     * @author yansiyang
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "获取人员证件类型列表", httpMethod = "GET")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取人员证件类型列表")
    @RequestMapping(value = "/action/listNoPage/{groupId}", method = RequestMethod.GET)
    public R list(@PathVariable("groupId") String groupId) {
        if (StringUtils.isBlank(groupId)) {
            return R.error(String.format(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM"), "groupId"));
        }
        List<BdPersonIdTypeDO> list = bdPersonIdTypeService.list(new QueryWrapper<BdPersonIdTypeDO>().orderByAsc
                ("code").eq("group_id",
                groupId));
        return R.ok().put("list", list);
    }

    /**
     * 保存人员证件类型
     *
     * @param personIdType 人员证件类型对象
     * @return com.csicit.ace.common.utils.server.R 保存响应结果
     * @author yansiyang
     * @date 2019/4/11 18:26
     */
    @ApiOperation(value = "保存人员证件类型", httpMethod = "POST")
    @ApiImplicitParam(name = "personIdType", value = "人员证件类型实体", required = true, dataType = "BdPersonIdTypeDO")
    @AceAuth("保存人员证件类型")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public R save(@RequestBody BdPersonIdTypeDO personIdType) {
        if (bdPersonIdTypeService.count(new QueryWrapper<BdPersonIdTypeDO>()
                .eq("name", personIdType.getName()).eq("group_id", personIdType.getGroupId())) > 0) {
            return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                    new String[]{InternationUtils.getInternationalMsg("NAME"), personIdType.getName()}
            ));
        }
        if (bdPersonIdTypeService.count(new QueryWrapper<BdPersonIdTypeDO>()
                .eq("code", personIdType.getCode()).eq("group_id", personIdType.getGroupId())) > 0) {
            return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                    new String[]{InternationUtils.getInternationalMsg("CODE"), personIdType.getCode()}
            ));
        }
        return bdPersonIdTypeService.insert(personIdType);
    }

    /**
     * 修改人员证件类型
     *
     * @param personIdType 人员证件类型对象
     * @return com.csicit.ace.common.utils.server.R 更新结果
     * @author shanwj
     * @date 2019/4/11 18:27
     */
    @ApiOperation(value = "修改人员证件类型", httpMethod = "PUT")
    @ApiImplicitParam(name = "personIdType", value = "人员证件类型实体", required = true, dataType = "BdPersonIdTypeDO")
    @AceAuth("修改人员证件类型")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public R update(@RequestBody BdPersonIdTypeDO personIdType) {
        BdPersonIdTypeDO old = bdPersonIdTypeService.getById(personIdType);
        if (!Objects.equals(old.getName(), personIdType.getName())) {
            if (bdPersonIdTypeService.count(new QueryWrapper<BdPersonIdTypeDO>()
                    .eq("name", personIdType.getName()).eq("group_id", personIdType.getGroupId())) > 0) {
                return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                        new String[]{InternationUtils.getInternationalMsg("NAME"), personIdType.getName()}
                ));
            }
        }
        if (!Objects.equals(old.getCode(), personIdType.getCode())) {
            if (bdPersonIdTypeService.count(new QueryWrapper<BdPersonIdTypeDO>()
                    .eq("code", personIdType.getCode()).eq("group_id", personIdType.getGroupId())) > 0) {
                return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                        new String[]{InternationUtils.getInternationalMsg("CODE"), personIdType.getCode()}
                ));
            }
        }
        return bdPersonIdTypeService.update(personIdType);
    }

    /**
     * 删除人员证件类型
     *
     * @param personIdTypeIds
     * @return
     * @author yansiyang
     * @date 2019/4/12 9:12
     */
    @ApiOperation(value = "删除人员证件类型", httpMethod = "DELETE")
    @ApiImplicitParam(name = "personIdTypeIds", value = "人员证件类型ID数组", required = true, allowMultiple = true, dataType
            = "String")
    @AceAuth("删除人员证件类型")
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public R delete(@RequestBody List<String> personIdTypeIds) {
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(personIdTypeIds)) {
            List<String> codes = bdPersonIdTypeService.listByIds(personIdTypeIds).stream().map
                    (BdPersonIdTypeDO::getCode).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(codes)) {
                if (codes.stream().anyMatch(id -> Objects.equals(id, "cardno") || Objects.equals(id, "junguanzheng"))) {
                    return R.error(InternationUtils.getInternationalMsg("NOT_TO_DELETE_CARD"));
                }
            }
        }
        return bdPersonIdTypeService.delete(personIdTypeIds);
    }
}
