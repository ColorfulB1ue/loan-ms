package com.young.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.young.common.PageQuery;
import com.young.common.PageResult;
import com.young.common.RequireRole;
import com.young.common.Result;
import com.young.pojo.UserProfile;
import com.young.service.UserProfileService;
import com.young.utils.DesensitizeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "实名认证档案管理")
@RestController
@RequestMapping("/api/kyc")
public class UserProfileController {

    @Autowired
    private UserProfileService profileService;

    @Operation(summary = "提交实名认证材料")
    @PostMapping("/submit")
    public Result<?> submit(@RequestAttribute("userId") Long userId, @RequestBody UserProfile data) {
        data.setUserId(userId);
        profileService.submitKyc(data);
        return Result.success("实名认证材料已提交，请等待人工审核");
    }

    @Operation(summary = "查询我的实名认证信息")
    @GetMapping("/my")
    public Result<UserProfile> getMyKyc(@RequestAttribute("userId") Long userId) {
        // 用户查看自己的信息，返回完整数据
        return Result.success(profileService.getMyProfile(userId));
    }

    @Operation(summary = "查询待审核档案列表（管理端）")
    @RequireRole
    @GetMapping("/pending")
    public Result<PageResult<UserProfile>> listPending(PageQuery pageQuery) {
        pageQuery.normalize();
        PageResult<UserProfile> page = profileService.getPendingKycList(pageQuery);
        // 管理端查看他人信息，需要脱敏
        return Result.success(desensitizePage(page));
    }

    @Operation(summary = "查询全部档案列表")
    @RequireRole
    @GetMapping("/all")
    public Result<PageResult<UserProfile>> listAll(PageQuery pageQuery) {
        pageQuery.normalize();
        PageResult<UserProfile> page = profileService.getAllProfileList(pageQuery);
        // 管理端查看他人信息，需要脱敏
        return Result.success(desensitizePage(page));
    }

    @Operation(summary = "审批实名认证档案")
    @RequireRole
    @PostMapping("/audit/{id}")
    public Result<?> audit(@PathVariable Long id,
                           @RequestParam boolean isPass,
                           @RequestAttribute("userId") Long adminId) {
        profileService.auditKyc(adminId, id, isPass);
        return Result.success(isPass ? "实名审核已通过" : "实名审核已驳回");
    }

    /**
     * 对分页结果进行脱敏处理
     */
    private PageResult<UserProfile> desensitizePage(PageResult<UserProfile> page) {
        if (page == null || page.getList() == null) {
            return page;
        }
        List<UserProfile> desensitizedList = page.getList().stream()
                .map(this::desensitizeProfile)
                .collect(Collectors.toList());
        page.setList(desensitizedList);
        return page;
    }

    /**
     * 对单个用户档案进行脱敏处理
     */
    private UserProfile desensitizeProfile(UserProfile profile) {
        if (profile == null) {
            return null;
        }
        UserProfile desensitized = new UserProfile();
        desensitized.setId(profile.getId());
        desensitized.setUserId(profile.getUserId());
        desensitized.setUsername(profile.getUsername()); // 保留用户名
        desensitized.setRealName(DesensitizeUtils.desensitizeName(profile.getRealName()));
        desensitized.setIdCard(DesensitizeUtils.desensitizeIdCard(profile.getIdCard()));
        desensitized.setIdCardFront(profile.getIdCardFront());
        desensitized.setIdCardBack(profile.getIdCardBack());
        desensitized.setBankName(profile.getBankName());
        desensitized.setBankCard(DesensitizeUtils.desensitizeBankCard(profile.getBankCard()));
        desensitized.setPhone(DesensitizeUtils.desensitizePhone(profile.getPhone()));
        desensitized.setEmail(DesensitizeUtils.desensitizeEmail(profile.getEmail()));
        desensitized.setStatus(profile.getStatus());
        desensitized.setAuditTime(profile.getAuditTime());
        desensitized.setCreateTime(profile.getCreateTime());
        desensitized.setUpdateTime(profile.getUpdateTime());
        return desensitized;
    }
}