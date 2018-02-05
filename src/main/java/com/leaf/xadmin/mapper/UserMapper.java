package com.leaf.xadmin.mapper;

import com.leaf.xadmin.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author leaf
 * <p>date: 2017/9/15 20:31</p>
 */
@Mapper
@Repository
public interface UserMapper extends SuperMapper<User> {

    /**
     * 查询指定用户名、密码用户
     *
     * @param name
     * @param pass
     * @return
     */
    User selectUserByNameAndPass(@Param("name") String name, @Param("pass") String pass);

    /**
     * 查询指定用户名用户
     *
     * @param name
     * @return
     */
    User selectUserByName(String name);

    /**
     * 查询指定id用户
     *
     * @param id
     * @return
     */
    User selectUserById(String id);

    /**
     * 查询所有用户列表
     *
     * @return
     */
    List<User> selectAllUsers();

    /**
     * 查询指定状态用户列表
     *
     * @param status
     * @return
     */
    List<User> selectStatusUsers(Integer status);
}
