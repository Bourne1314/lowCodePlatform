package com.csicit.ace.platform.core.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.BladeVisualDO;
import com.csicit.ace.common.pojo.domain.SysAuthMixDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.platform.core.service.BladeVisualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping(value = "/visual")
public class VisualController extends BaseController {


    @Autowired
    BladeVisualService bladeVisualService;

    @GetMapping("/show/{appName}/{name}")
    public String visualShow(@PathVariable("appName") String appName,
                             @PathVariable("name") String name,
                             Model model) throws
            UnsupportedEncodingException {
        String title = URLDecoder.decode(name, "utf-8");
        BladeVisualDO visual =
                bladeVisualService.getOne(new QueryWrapper<BladeVisualDO>()
                        .eq("app_id", appName)
                        .eq("title", title));
        if (Objects.isNull(visual)) {
            model.addAttribute("msg", "当前大屏尚未设计！");
            return "error";
        }
        //验证权限
        if (!securityUtils.isAdmin() && StringUtils.isNotEmpty(visual.getAuthId())) {
            List<SysAuthMixDO> list = sysAuthMixService.list(
                    new QueryWrapper<SysAuthMixDO>()
                            .eq("auth_id", visual.getAuthId())
                            .eq("user_id", securityUtils.getCurrentUserId()));
            if (list == null || list.size() == 0) {
                model.addAttribute("msg", "当前用户没有查看权限！");
                return "error";
            }
        }
        model.addAttribute("id", visual.getId());
        return "show";

    }
}
