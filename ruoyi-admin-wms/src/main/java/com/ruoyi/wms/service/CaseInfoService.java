package com.ruoyi.wms.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.utils.MapstructUtils;
import com.ruoyi.common.mybatis.core.page.PageQuery;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.wms.domain.bo.CaseInfoBo;
import com.ruoyi.wms.domain.entity.CaseInfo;
import com.ruoyi.wms.domain.vo.CaseInfoVo;
import com.ruoyi.wms.mapper.CaseInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 案例信息Service业务层处理
 *
 * @author ruoyi
 */
@RequiredArgsConstructor
@Service
public class CaseInfoService {

    private final CaseInfoMapper caseInfoMapper;

    /**
     * 根据ID查询案例信息（主键精准查询）
     */
    public CaseInfoVo queryById(Long id) {
        return caseInfoMapper.selectVoById(id);
    }

    /**
     * 查询案例信息列表（分页查询）
     */
    public TableDataInfo<CaseInfoVo> queryPageList(CaseInfoBo bo, PageQuery pageQuery) {
        Map<String, Object> params = new HashMap<>();
        if (bo != null) {
            if (bo.getId() != null) params.put("id", bo.getId());
            if (bo.getCaseCode() != null) params.put("caseCode", bo.getCaseCode());
            if (bo.getCaseName() != null) params.put("caseName", bo.getCaseName());
            if (bo.getUndertakingUnit() != null) params.put("undertakingUnit", bo.getUndertakingUnit());
            if (bo.getCaseType() != null) params.put("caseType", bo.getCaseType());
            if (bo.getCaseSource() != null) params.put("caseSource", bo.getCaseSource());
            if (bo.getUnitStatus() != null) params.put("unitStatus", bo.getUnitStatus());
        }
        LambdaQueryWrapper<CaseInfo> lqw = buildQueryWrapper(params);
        Page<CaseInfoVo> result = caseInfoMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询案例信息列表（支持模糊查询）
     */
    public List<CaseInfoVo> queryList(CaseInfoBo bo) {
        Map<String, Object> params = new HashMap<>();
        if (bo != null) {
            if (bo.getId() != null) params.put("id", bo.getId());
            if (bo.getCaseCode() != null) params.put("caseCode", bo.getCaseCode());
            if (bo.getCaseName() != null) params.put("caseName", bo.getCaseName());
            if (bo.getUndertakingUnit() != null) params.put("undertakingUnit", bo.getUndertakingUnit());
            if (bo.getCaseType() != null) params.put("caseType", bo.getCaseType());
            if (bo.getCaseSource() != null) params.put("caseSource", bo.getCaseSource());
            if (bo.getUnitStatus() != null) params.put("unitStatus", bo.getUnitStatus());
        }
        LambdaQueryWrapper<CaseInfo> lqw = buildQueryWrapper(params);
        return caseInfoMapper.selectVoList(lqw);
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<CaseInfo> buildQueryWrapper(Map<String, Object> params) {
        LambdaQueryWrapper<CaseInfo> lqw = Wrappers.lambdaQuery();

        // 主键精准查询
        if (params.containsKey("id") && params.get("id") != null) {
            lqw.eq(CaseInfo::getId, params.get("id"));
        }

        // 案例编码模糊查询
        if (params.containsKey("caseCode") && StrUtil.isNotBlank((String) params.get("caseCode"))) {
            lqw.like(CaseInfo::getCaseCode, params.get("caseCode"));
        }

        // 案例名称模糊查询
        if (params.containsKey("caseName") && StrUtil.isNotBlank((String) params.get("caseName"))) {
            lqw.like(CaseInfo::getCaseName, params.get("caseName"));
        }

        // 承担单位模糊查询
        if (params.containsKey("undertakingUnit") && StrUtil.isNotBlank((String) params.get("undertakingUnit"))) {
            lqw.like(CaseInfo::getUndertakingUnit, params.get("undertakingUnit"));
        }

        // 案例类型精准查询
        if (params.containsKey("caseType") && StrUtil.isNotBlank((String) params.get("caseType"))) {
            lqw.eq(CaseInfo::getCaseType, params.get("caseType"));
        }

        // 案例来源精准查询
        if (params.containsKey("caseSource") && StrUtil.isNotBlank((String) params.get("caseSource"))) {
            lqw.eq(CaseInfo::getCaseSource, params.get("caseSource"));
        }

        // 单位状态精准查询
        if (params.containsKey("unitStatus") && params.get("unitStatus") != null) {
            lqw.eq(CaseInfo::getUnitStatus, params.get("unitStatus"));
        }

        return lqw;
    }

    /**
     * 新增案例信息
     */
    public int insert(CaseInfo caseInfo) {
        return caseInfoMapper.insert(caseInfo);
    }

    /**
     * 通过Bo新增案例信息
     */
    public int insertByBo(CaseInfoBo bo) {
        CaseInfo caseInfo = MapstructUtils.convert(bo, CaseInfo.class);
        return insert(caseInfo);
    }

    /**
     * 修改案例信息
     */
    public int update(CaseInfo caseInfo) {
        return caseInfoMapper.updateById(caseInfo);
    }

    /**
     * 通过Bo修改案例信息
     */
    public int updateByBo(CaseInfoBo bo) {
        CaseInfo caseInfo = MapstructUtils.convert(bo, CaseInfo.class);
        return update(caseInfo);
    }

    /**
     * 批量删除案例信息
     */
    public int deleteByIds(Collection<Long> ids) {
        return caseInfoMapper.deleteBatchIds(ids);
    }

    /**
     * 删除案例信息
     */
    public int deleteById(Long id) {
        return caseInfoMapper.deleteById(id);
    }
}
