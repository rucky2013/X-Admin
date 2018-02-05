package com.leaf.xadmin.utils.request;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.leaf.xadmin.entity.Resource;
import com.leaf.xadmin.vo.RequestResourceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author leaf
 * <p>date: 2018-01-25 21:22</p>
 * <p>version: 1.0</p>
 */
@Component
@Slf4j
public class RequestResolveUtil {
    private List<Class> clazzs = Lists.newArrayList(RequestMapping.class, PostMapping.class, GetMapping.class, PutMapping.class, DeleteMapping.class);

    /**
     * request注解解析工具
     *
     * @param classOrMethod
     * @param isClass
     * @return
     */
    private Map<String, Object> requestAnnotationResolver(Object classOrMethod, boolean isClass) {
        Map<String, Object> requestMap = new HashMap<>(10);
        Annotation[] declaredAnnotations = isClass ? ((Class) classOrMethod).getDeclaredAnnotations() : ((Method) classOrMethod).getDeclaredAnnotations();
        List<Annotation> collect = Arrays.asList(declaredAnnotations).stream()
                .filter(annotation -> clazzs.contains(annotation.annotationType()))
                .collect(Collectors.toList());
        if (collect.size() == 1) {
            Annotation annotation = collect.get(0);
            Class annotationType = annotation.annotationType();
            if (annotationType.isAnnotation()) {
                requestMap.put("type", clazzs.indexOf(annotationType));
                String[] requestPaths;
                if (annotation instanceof PostMapping) {
                    requestPaths = ((PostMapping) annotation).value();
                } else if (annotation instanceof GetMapping) {
                    requestPaths = ((GetMapping) annotation).value();
                } else if (annotation instanceof PutMapping) {
                    requestPaths = ((PutMapping) annotation).value();
                } else if (annotation instanceof DeleteMapping) {
                    requestPaths = ((DeleteMapping) annotation).value();
                } else {
                    requestPaths = ((RequestMapping) annotation).value();
                }
                requestMap.put("paths", requestPaths);

            }
        }
        return requestMap;
    }

    /**
     * api注解解析工具
     *
     * @param classOrMethod
     * @param isClass
     * @return
     */
    private String apiAnnotationResolver(Object classOrMethod, boolean isClass) {
        String value = null;
        if (isClass) {
            Annotation annotation = ((Class) classOrMethod).getDeclaredAnnotation(Api.class);
            if (annotation instanceof Api) {
                Api api = (Api) annotation;
                value = api.value();
            }
        } else {
            ApiOperation apiOperation = ((Method) classOrMethod).getDeclaredAnnotation(ApiOperation.class);
            value = apiOperation.value();
        }
        return value;
    }

    /**
     * request方法解析工具
     *
     * @param method
     */
    public List<RequestResourceVO> methodResolver(Method method) {
        List<RequestResourceVO> resourceList = new LinkedList<>();
        // 解析方法和类中注解
        Map<String, Object> classMap = requestAnnotationResolver(method.getDeclaringClass(), true);
        Map<String, Object> methodMap = requestAnnotationResolver(method, false);
        // 解析方法和类中api
        String desc = apiAnnotationResolver(method, false);
        String[] parentPaths = (String[]) classMap.get("paths");
        String[] childPaths = (String[]) methodMap.get("paths");
        int type = (int) methodMap.get("type");
        for (String parentPath : parentPaths) {
            for (String childPath : childPaths) {
                RequestResourceVO resource = RequestResourceVO.builder()
                        .parentPath(parentPath)
                        .childPath(childPath)
                        .type(type)
                        .name(method.getName())
                        .desc(desc).build();
                resourceList.add(resource);
            }
        }

        return resourceList;
    }

    /**
     * 路径合并算法(路径+需要顺序排列)
     *
     * @param paths
     * @return
     */
    public static String pathMerge(String... paths) {
        StringBuilder sb = new StringBuilder();
        for (String path : paths) {
            sb.append(path.startsWith("/") ? path : Strings.padStart(path, path.length() + 1, '/'));
        }
        return sb.toString();
    }

    /**
     * 路径合并算法升级版
     *
     * @return
     */
    public static Resource pathMergeUpgrade(List<RequestResourceVO> requestResourceVOList) {
        if (CollectionUtils.isEmpty(requestResourceVOList)) {
            return null;
        }
        RequestResourceVO requestResourceVO = requestResourceVOList.get(0);
        StringBuilder sb = new StringBuilder();
        for (int i = 0, length = requestResourceVOList.size(); i < length; i++) {
            sb.append(pathMerge(requestResourceVOList.get(i).getParentPath(), requestResourceVOList.get(i).getChildPath()));
            if (i != length - 1) {
                sb.append(",");
            }
        }
        return Resource.builder().name(requestResourceVO.getName())
                .desc(requestResourceVO.getDesc())
                .path(sb.toString())
                .type(requestResourceVO.getType())
                .build();
    }
}
