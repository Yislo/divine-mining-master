package com.divine.warehouse.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.expression.engine.rhino.RhinoEngine;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.divine.common.core.exception.base.BusinessException;
import com.divine.warehouse.domain.dto.ItemCategoryDto;
import com.divine.warehouse.domain.entity.Item;
import com.divine.warehouse.domain.entity.ItemCategory;
import com.divine.warehouse.domain.vo.ItemCategoryVo;
import com.divine.warehouse.domain.vo.ItemTypeTreeSelectVo;
import com.divine.warehouse.mapper.ItemCategoryMapper;
import com.divine.warehouse.mapper.ItemMapper;
import com.divine.warehouse.service.ItemCategoryService;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.hutool.core.lang.Validator.isNotNull;

@RequiredArgsConstructor
@Service
public class ItemCategoryServiceImpl extends ServiceImpl<ItemCategoryMapper, ItemCategory> implements ItemCategoryService {

    private final ItemCategoryMapper itemCategoryMapper;
    private final ItemMapper itemMapper;

    /**
     * 查询物料类型
     */
    @Override
    public ItemCategoryVo queryById(Long id) {
        return itemCategoryMapper.selectVoById(id);
    }

    /**
     * 查询物料类型列表
     */
    @Override
    public PageInfoRes<ItemCategoryVo> queryPageList(ItemCategoryDto dto, BasePage basePage) {
        LambdaQueryWrapper<ItemCategory> lqw = buildQueryWrapper(dto);
        Page<ItemCategoryVo> result = itemCategoryMapper.selectVoPage(basePage.build(), lqw);
        return PageInfoRes.build(result);
    }

    /**
     * 查询物料类型列表
     */
    @Override
    public List<ItemCategoryVo> queryList(ItemCategoryDto dto) {
        LambdaQueryWrapper<ItemCategory> lqw = buildQueryWrapper(dto);
        lqw.orderByAsc(ItemCategory::getSort);
        return itemCategoryMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ItemCategory> buildQueryWrapper(ItemCategoryDto dto) {
        Map<String, Object> params = dto.getParams();
        LambdaQueryWrapper<ItemCategory> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getParentId() != null, ItemCategory::getParentId, dto.getParentId());
        lqw.like(StrUtil.isNotBlank(dto.getCategoryName()), ItemCategory::getCategoryName, dto.getCategoryName());
        lqw.eq(dto.getSort() != null, ItemCategory::getSort, dto.getSort());
        lqw.eq(StrUtil.isNotBlank(dto.getStatus()), ItemCategory::getStatus, dto.getStatus());
        return lqw;
    }

    /**
     * 新增物料类型
     */
    @Override
    public void insertByBo(ItemCategoryDto dto) {
        validateItemTypeName(dto);
        ItemCategory add = MapstructUtils.convert(dto, ItemCategory.class);
        LambdaQueryWrapper<ItemCategory> wrapper = new LambdaQueryWrapper<>();
        if (dto.getParentId() != null) {
            wrapper.eq(ItemCategory::getParentId, dto.getParentId());
        } else {
            wrapper.eq(ItemCategory::getParentId, 0L);
        }
        //查当前级别排序最大值
        wrapper.orderByDesc(ItemCategory::getSort);
        wrapper.last("limit 1");
        ItemCategory itemType = itemCategoryMapper.selectOne(wrapper);
        add.setSort(itemType == null ? 0 : itemType.getSort() + 1);
        itemCategoryMapper.insert(add);
    }

    /**
     * 修改物料类型
     */
    @Override
    public void updateByBo(ItemCategoryDto dto) {
        Long parentId = ObjUtil.isNull(dto.getParentId()) ? 0 : dto.getParentId();
        if (ObjUtil.equals(parentId, dto.getId())) {
            throw new BusinessException("上级分类不能选择自己");
        }
        validateItemTypeName(dto);
        ItemCategory update = MapstructUtils.convert(dto, ItemCategory.class);
        update.setParentId(parentId);
        itemCategoryMapper.updateById(update);
    }

    private void validateItemTypeName(ItemCategoryDto dto) {
        LambdaQueryWrapper<ItemCategory> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ItemCategory::getCategoryName, dto.getCategoryName());
        queryWrapper.ne(dto.getId() != null, ItemCategory::getId, dto.getId());
        if (itemCategoryMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException("分类名重复");
        }
    }

    /**
     * 批量删除物料类型
     */
    @Override
    public void deleteByIds(List<Long> ids) {
        // 有子分类不能删
        LambdaQueryWrapper<ItemCategory> itemCategoryLqw = new LambdaQueryWrapper<>();
        itemCategoryLqw.in(ItemCategory::getParentId, ids);
        if (itemCategoryMapper.selectCount(itemCategoryLqw) > 0) {
            throw new BusinessException("删除失败！请先删除该分类下的子分类！");
        }
        // 被物品应用了不能删
        LambdaQueryWrapper<Item> itemLqw = Wrappers.lambdaQuery();
        itemLqw.in(Item::getItemCategory, ids);
        if (itemMapper.selectCount(itemLqw) > 0) {
            throw new BusinessException("删除失败！分类已被物品使用！");
        }
        // 删除
        LambdaQueryWrapper<ItemCategory> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.in(ItemCategory::getId, ids);
        itemCategoryMapper.delete(deleteWrapper);
    }

    /**
     * @param itemTypes
     * @return
     */
    @Override
    public List<ItemTypeTreeSelectVo> buildItemTypeTreeSelect(List<ItemCategoryVo> itemTypes) {
        List<ItemCategoryVo> itemTypeTrees = buildDeptTree(itemTypes);
        return itemTypeTrees.stream().map(ItemTypeTreeSelectVo::new).collect(Collectors.toList());
    }

    /**
     * 构建前端所需要树结构
     *
     * @param itemTypes 部门列表
     * @return 树结构列表
     */
    private List<ItemCategoryVo> buildDeptTree(List<ItemCategoryVo> itemTypes) {
        List<ItemCategoryVo> returnList = new ArrayList<>();
        List<Long> tempList = new ArrayList<Long>();
        for (ItemCategoryVo dept : itemTypes) {
            tempList.add(dept.getId());
        }
        for (ItemCategoryVo dept : itemTypes) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId())) {
                recursionFn(itemTypes, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty()) {
            returnList = itemTypes;
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<ItemCategoryVo> list, ItemCategoryVo t) {
        // 得到子节点列表
        List<ItemCategoryVo> childList = getChildList(list, t);
        t.setChildren(childList);
        for (ItemCategoryVo tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<ItemCategoryVo> getChildList(List<ItemCategoryVo> list, ItemCategoryVo t) {
        List<ItemCategoryVo> tlist = new ArrayList<>();
        for (ItemCategoryVo n : list) {
            if (isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<ItemCategoryVo> list, ItemCategoryVo t) {
        return getChildList(list, t).size() > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSort(List<ItemTypeTreeSelectVo> tree) {
        List<ItemCategory> updateList = new ArrayList<>();
        for (int i = 0; i < tree.size(); i++) {
            ItemCategory itemType = new ItemCategory();
            itemType.setId(tree.get(i).getId());
            itemType.setSort(i);
            updateList.add(itemType);
        }
        saveOrUpdateBatch(updateList);
    }
}
