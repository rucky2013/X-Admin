package com.leaf.xadmin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.leaf.xadmin.entity.Permission;
import com.leaf.xadmin.entity.Resource;
import com.leaf.xadmin.entity.Role;
import com.leaf.xadmin.mapper.ResourceMapper;
import com.leaf.xadmin.service.IResourceService;
import org.springframework.stereotype.Service;

import java.sql.SQLDataException;
import java.util.List;
import java.util.Set;

/**
 * @author leaf
 * <p>date: 2018-02-04 20:59</p>
 * <p>version: 1.0</p>
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements IResourceService {

    @Override
    public boolean addBatch(List<Resource> resourceList) {
        return insertBatch(resourceList);
    }

    @Override
    public boolean addOrUpdateBatch(List<Resource> resourceList) {
        return insertOrUpdateBatch(resourceList);
    }

    @Override
    public Resource queryOneByPath(String path) throws SQLDataException {
        List<Resource> resourceList = baseMapper.selectList(new EntityWrapper<Resource>().eq("path", path));
        if (resourceList.size() != 1) {
            throw new SQLDataException();
        }
        return resourceList.get(0);
    }

    @Override
    public Set<Role> queryRolesByPath(String path) {
        return baseMapper.selectResourceRolesByPath(path);
    }

    @Override
    public Set<Permission> queryPermissionsByPath(String path) {
        return baseMapper.selectResourcePermissionsByPath(path);
    }
}
