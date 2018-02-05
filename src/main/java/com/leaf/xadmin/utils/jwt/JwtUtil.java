package com.leaf.xadmin.utils.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.leaf.xadmin.constants.GlobalConstants;
import com.leaf.xadmin.enums.ErrorStatus;
import com.leaf.xadmin.exception.TokenDecodeException;
import com.leaf.xadmin.exception.TokenExpiredException;
import com.leaf.xadmin.exception.TokenInvalidException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT(Json Web Token)是一种基于json实现的用户合法性校验的协议。基本思路就是用户提供用户名和密码给认证服务器，
 * 服务器验证用户提交信息信息的合法性；如果验证成功，会产生并返回一个Token（令牌），用户可以使用这个token访问服务器上受保护的资源。
 *
 * @author leaf
 * <p>date: 2017-12-28 18:33</p>
 */
public class JwtUtil {

    /**
     * 创建token
     *
     * @return
     * @throws Exception
     */
    public static String generateToken(String id, String name, String pass) {
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE, GlobalConstants.JWT_TOKEN_TIMEOUT);
        Date expireDate = nowTime.getTime();

        Map<String, Object> map = new HashMap<>(10);
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        String token = null;
        try {
            token = JWT.create()
                    .withHeader(map)
                    .withClaim("id", id)
                    .withClaim("name", name)
                    .withClaim("pass", pass)
                    .withExpiresAt(expireDate)
                    .sign(Algorithm.HMAC256(GlobalConstants.JWT_SECRET));
        } catch (UnsupportedEncodingException e) {
            throw new TokenDecodeException(ErrorStatus.TOKEN_DECODE_ERROR);
        }


        return token;
    }

    /**
     * 验证token，并返回参数解析结果
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static Map<String, Claim> verifyToken(String token) {
        JWTVerifier verifier = null;
        try {
            verifier = JWT.require(Algorithm.HMAC256(GlobalConstants.JWT_SECRET))
                    .build();
        } catch (UnsupportedEncodingException e) {
            throw new TokenDecodeException(ErrorStatus.TOKEN_DECODE_ERROR);
        }

        DecodedJWT jwt;
        try {
            jwt = verifier.verify(token);
        } catch (com.auth0.jwt.exceptions.TokenExpiredException e) {
            throw new TokenExpiredException(ErrorStatus.TOKEN_EXPIRE_ERROR);
        } catch (Exception e1) {
            throw new TokenInvalidException(ErrorStatus.TOKEN_INVALID_ERROR);
        }

        return jwt.getClaims();
    }

    /**
     * 从request中解析token参数
     *
     * @param request
     * @return
     */
    public static String getTokenFromRequest(HttpServletRequest request) {
        // 先检测header是否存在
        String token = request.getHeader(GlobalConstants.JWT_TOKEN_NAME);

        // 然后解析cookie中是否存在
        Cookie[] cookies = request.getCookies();
        boolean flag = (token == null || token.isEmpty()) && cookies != null;
        if (flag) {
            for (Cookie cookie : cookies) {
                if (GlobalConstants.JWT_TOKEN_NAME.equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // 最后检测参数是否传递
        if (token == null || token.isEmpty()) {
            token = request.getParameter(GlobalConstants.JWT_TOKEN_NAME);
        }

        return token;
    }

}
