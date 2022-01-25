package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.BdJobDO;
import com.csicit.ace.common.pojo.domain.BdPersonJobDO;
import com.csicit.ace.common.pojo.domain.BdPostDO;
import com.csicit.ace.common.pojo.domain.OrgOrganizationDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.platform.core.service.BdJobService;
import com.csicit.ace.platform.core.service.BdPersonJobService;
import com.csicit.ace.platform.core.service.BdPostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 人员职务管理
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019/7/15 20:19
 */
@RestController
@RequestMapping("/bdPersonJobs")
@Api("人员职务管理")
public class BdPersonJobController extends BaseController {

    @Autowired
    BdPersonJobService bdPersonJobService;

    @Autowired
    BdPostService bdPostService;

    @Autowired
    BdJobService bdJobService;

    /**
     * 获取职务
     *
     * @param params
     * @return
     * @author yansiyang
     * @date 2019/7/15 20:20
     */
    @ApiOperation(value = "获取人员职务列表", httpMethod = "GET")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    //@AceAuth("获取人员职务列表")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public R list(@RequestParam Map<String, String> params) {
        String personDocId = params.get("personDocId");
        if (StringUtils.isNotBlank(personDocId)) {
            List<BdPersonJobDO> list = bdPersonJobService.list(new QueryWrapper<BdPersonJobDO>().eq("person_doc_id",
                    personDocId).orderByAsc("sort_index"));
            if (org.apache.commons.collections.CollectionUtils.isEmpty(list)) {
                return R.ok().put("list", list);
            }
            // 查询职务的集团 组织 部门
            Set<String> orgIds = new HashSet<>();

            Set<String> postIds = list.stream().map(BdPersonJobDO::getPostId).collect(Collectors.toSet());
            Map<String,String> postIdAndNames = new HashMap<>();
            if (!org.apache.commons.collections.CollectionUtils.isEmpty(postIds)) {
                List<BdPostDO> postDOS = bdPostService.list(new QueryWrapper<BdPostDO>().select("id", "name").in("id", postIds));
                if (!org.apache.commons.collections.CollectionUtils.isEmpty(postDOS)) {
                    postDOS.forEach(post -> {
                        postIdAndNames.put(post.getId(), post.getName());
                    });
                }
            }

            Set<String> jobIds = list.stream().map(BdPersonJobDO::getJobId).collect(Collectors.toSet());
            Map<String, String> jobIdAndNames = new HashMap<>();
            if (!org.apache.commons.collections.CollectionUtils.isEmpty(jobIds)) {
                List<BdJobDO> jobDOS = bdJobService.list(new QueryWrapper<BdJobDO>().select("id", "name").in("id", jobIds));
                if (!org.apache.commons.collections.CollectionUtils.isEmpty(jobDOS)) {
                    jobDOS.forEach(job -> {
                        jobIdAndNames.put(job.getId(), job.getName());
                    });
                }
            }


            list.forEach(job -> {
                orgIds.add(job.getDepartmentId());
                orgIds.add(job.getOrganizationId());
                orgIds.add(job.getGroupId());
            });
            List<OrgOrganizationDO> orgs = orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>().eq("is_delete",0).select
                    ("id", "name").in("id", orgIds));
            Map<String, String> orgNames = new HashMap<>();
            orgs.forEach(org -> {
                orgNames.put(org.getId(), org.getName());
            });
            list.forEach(job -> {
                job.setDepartmentName(orgNames.get(job.getDepartmentId()));
                job.setOrganizationName(orgNames.get(job.getOrganizationId()));
                job.setGroupName(orgNames.get(job.getGroupId()));
                job.setPostName(postIdAndNames.get(job.getPostId()));
                job.setJobName(jobIdAndNames.get(job.getJobId()));
            });
            // 检索出主职务
            BdPersonJobDO mainJob = null;
            try {
                Object[] jobs = list.stream().filter(job -> Objects.equals(job.getMainJob(), 1)).toArray();
                mainJob = (BdPersonJobDO) jobs[0];
                mainJob.setLongJobName(mainJob.getGroupName() + "-" + mainJob.getOrganizationName() + "-" + mainJob
                        .getDepartmentName() + "-" +
                        mainJob.getJobName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return R.ok().put("list", list).put("mainJob", mainJob);
        }
        return R.error(InternationUtils.getInternationalMsg("EMPTY_ARG"));
    }

    /**
     * 设置主职务
     *
     * @param params
     * @return
     * @author yansiyang
     * @date 2019/7/16 8:50
     */
    @ApiOperation(value = "设置主职务", httpMethod = "POST")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    //@AceAuth("设置主职务")
    @RequestMapping(value = "/action/setMainJob", method = RequestMethod.POST)
    public R setMainJob(@RequestBody Map<String, String> params) {
        String personDocId = params.get("personDocId");
        String personJobId = params.get("personJobId");
        if (bdPersonJobService.setMainJob(personDocId, personJobId)) {
            return R.ok(InternationUtils.getInternationalMsg("OPERATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
    }


    /**
     * 获取单个职务信息
     *
     * @param jobId
     * @return
     * @author yansiyang
     * @date 2019/7/15 20:20
     */
    @ApiOperation(value = "获取单个职务信息", httpMethod = "GET")
    @ApiImplicitParam(name = "jobId", value = "职务主键", required = true, dataType = "String")
    //@AceAuth("获取单个职务信息")
    @RequestMapping(value = "/{jobId}", method = RequestMethod.GET)
    public R info(@PathVariable("jobId") String jobId) {
        BdPersonJobDO job = bdPersonJobService.getById(jobId);
        Set<String> orgIds = new HashSet<>();
        orgIds.add(job.getDepartmentId());
        orgIds.add(job.getOrganizationId());
        orgIds.add(job.getGroupId());
        List<OrgOrganizationDO> orgs = orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>().eq("is_delete",0).select
                ("id", "name").in("id", orgIds));
        Map<String, String> orgNames = new HashMap<>();
        orgs.forEach(org -> {
            orgNames.put(org.getId(), org.getName());
        });
        job.setDepartmentName(orgNames.get(job.getDepartmentId()));
        job.setOrganizationName(orgNames.get(job.getOrganizationId()));
        job.setGroupName(orgNames.get(job.getGroupId()));
        //插入 岗位名称
        return R.ok().put("job", job);
    }


    /**
     * 保存职务
     *
     * @param job
     * @return
     * @author yansiyang
     * @date 2019/7/15 20:20
     */
    @ApiOperation(value = "保存职务", httpMethod = "POST")
    @ApiImplicitParam(name = "job", value = "职务对象", required = true, dataType = "BdPersonJobDO")
    //@AceAuth("保存职务")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public R save(@RequestBody BdPersonJobDO job) {
        if (bdPersonJobService.savePersonJob(job)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 更新职务
     *
     * @param job
     * @return
     * @author yansiyang
     * @date 2019/7/15 20:20
     */
    @ApiOperation(value = "更新职务", httpMethod = "PUT")
    @ApiImplicitParam(name = "job", value = "职务对象", required = true, dataType = "BdPersonJobDO")
    // @AceAuth("更新职务")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public R update(@RequestBody BdPersonJobDO job) {
        if (bdPersonJobService.updatePersonJob(job)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * 删除职务
     *
     * @param ids
     * @return
     * @author yansiyang
     * @date 2019/7/15 20:20
     */
    @ApiOperation(value = "删除职务", httpMethod = "DELETE")
    @ApiImplicitParam(name = "ids", value = "职务主键列表", required = true, dataType = "List")
    //@AceAuth("删除职务")
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public R delete(@RequestBody List<String> ids) {
        if (bdPersonJobService.deletePersonJob(ids)) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }
}
