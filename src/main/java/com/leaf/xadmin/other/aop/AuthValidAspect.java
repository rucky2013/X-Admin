package com.leaf.xadmin.aop;

import com.leaf.xadmin.entity.Permission;
import com.leaf.xadmin.entity.Resource;
import com.leaf.xadmin.entity.Role;
import com.leaf.xadmin.service.IResourceService;
import com.leaf.xadmin.utils.request.RequestResolveUtil;
import com.leaf.xadmin.vo.RequestResourceVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 *
 * @author leaf
 * <p>date: 2018-01-21 18:23</p>
 * <p>version: 1.0</p>
 */
@Component
@Aspect
@Slf4j
public class AuthValidAspect {

    @Pointcut("execution(* com.leaf.xadmin.controller..*.*(..))")
    private void allowValidAspect() {}

    @Pointcut("!execution(* com.leaf.xadmin.controller.ErrorController.*(..)) && !execution(* com.leaf.xadmin.controller.UserController.login(..))")
    private void denyValidAspect() {}

    @Pointcut("allowValidAspect() && denyValidAspect()")
    private void validAspect() {}

    @Autowired
    private IResourceService resourceService;

    @Autowired
    private RequestResolveUtil requestResolveUtil;


    /**
     * 方法开始时执行
     */
    @Before("validAspect()")
    public void doBefore() {
    }

    /**
     * 方法结束时执行
     */
    @After("validAspect()")
    public void doAfter() {
        log.debug("aop结束执行...");
    }

    /**
     * 方法结束执行
     */
    @AfterReturning("validAspect()")
    public void afterReturning() {
        log.debug("aop结束后执行...");
    }

    /**
     * 方法异常时执行
     */
    @AfterThrowing("validAspect()")
    public void afterThrowing() {

    }

    /**
     * 执行方法体
     *
     * @param jp
     * @return
     */
    @Around("validAspect()")
    public Object around(ProceedingJoinPoint jp) throws Throwable {
        // 获取当前执行方法对象
        Signature sig = jp.getSignature();
        MethodSignature msig = (MethodSignature) sig;
        Method currentMethod = jp.getTarget().getClass().getMethod(msig.getName(), msig.getParameterTypes());

        // 获取请求路径相关权限信息
        List<RequestResourceVO> requestResourceVOS = requestResolveUtil.methodResolver(currentMethod);
        Resource resource = RequestResolveUtil.pathMergeUpgrade(requestResourceVOS);
        Set<Role> roles = resourceService.queryRolesByPath(resource.getPath());
        Set<Permission> permissions = resourceService.queryPermissionsByPath(resource.getPath());
        Subject subject = SecurityUtils.getSubject();

        // 检查此资源是否允许用户访问
        boolean allowFlag = false;
        for (Role role : roles) {
            if (subject.hasRole(role.getName())) {
                allowFlag = true;
                break;
            }
        }
        if (!allowFlag) {
            for(Permission permission : permissions) {
                if (subject.isPermitted(permission.getName())) {
                    allowFlag = true;
                    break;
                }
            }
        }
        if (!allowFlag) {
            throw new UnauthorizedException();
        }

        return jp.proceed();
    }

}
