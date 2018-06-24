package com.study.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by user on 2018/3/20.
 */
public class JwtHelper {
    public static Claims parseJWT(String jsonWebToken, String base64Security) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
                    .parseClaimsJws(jsonWebToken).getBody();
            return claims;
        } catch (Exception ex) {
            return null;
        }
    }

    public static String createJWT(String name, String userId, String role,
                                   String audience, String issuer, long TTLMillis, String base64Security) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Security);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .claim("role", role)//自定义属性
                .claim("username", name)//自定义属性
                .claim("userid", userId)//自定义属性
                .setIssuer(issuer)//签发者
                .setAudience(audience)//接受者
                .signWith(signatureAlgorithm, signingKey);//签名算法以及秘钥
        //添加Token过期时间
        /*if (TTLMillis >= 0) {
            long zero=nowMillis/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
            long twelve=zero+24*60*60*1000-1;//今天23点59分59秒的毫秒数
            //每天23:59:59秒token过期
            long expMillis = twelve;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp).setNotBefore(now);//过期时间
        }*/
        if (TTLMillis >= 0) {
            long expMillis = nowMillis + TTLMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp).setNotBefore(now);//过期时间
        }

        //生成JWT
        return builder.compact();
    }

    /**
     * @return token中包含的用户名
     */
    public static String getUsername(String token,String base64Security) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
                .parseClaimsJws(token).getBody();
        return claims.get("userid").toString();
    }

}
