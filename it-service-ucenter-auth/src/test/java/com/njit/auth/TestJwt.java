package com.njit.auth;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dustdawn
 * @date 2020/4/10 14:08
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestJwt {

    //创建jwt令牌
    @Test
    public void testCreateJwt(){
        //密钥库文件
        String keystore = "itmooc.keystore";
        //密钥库的密码
        String keystore_password = "itmoockeystore";

        //密钥库文件路径
        ClassPathResource classPathResource = new ClassPathResource(keystore);
        //密钥别名
        String alias  = "itmoockey";
        //密钥的访问密码
        String key_password = "itmooc";
        //密钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(classPathResource,keystore_password.toCharArray());
        //密钥对（公钥和私钥）
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, key_password.toCharArray());
        //获取私钥
        RSAPrivateKey aPrivate = (RSAPrivateKey) keyPair.getPrivate();
        //jwt令牌的内容
        Map<String,String> body = new HashMap<>();
        body.put("name","dustdawn");
        String bodyString = JSON.toJSONString(body);
        //生成jwt令牌
        Jwt jwt = JwtHelper.encode(bodyString, new RsaSigner(aPrivate));
        //生成jwt令牌编码
        String encoded = jwt.getEncoded();
        System.out.println(encoded);

    }

    //校验jwt令牌
    @Test
    public void testVerify(){
        //公钥
        String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmKHGraK+Nh2ODDI3qcZHJ5fcz+snAKwZ43X63mHQShYk+xXUlIuVAgdJob2z4B7IL1Tx0eZ14UdaBfLb2ZHDRspPbaTNoj6KzJWyaTdgHpUYdyxsAx7/78VOhTlFQy7lTr9I+eoBC7JHhJYVGr3Y4wmfwnUcsNmhJtZRXpphfV3eEZ9oSeMVjWoZV0TUbG5VHbPsALepMRp6Qmd55iHjipC/nWqIwiivW+1VlQ+BG2P/2PqZDs4mD8htrUY9K2PXiF8M/esSZ43135XZsgCECFkfjaNlzeoHHESy4LwD7D2tQc7jbqgiaY94biwLAO7KjNwdt3EAOwXYrjRlgM7H+wIDAQAB-----END PUBLIC KEY-----";
        //jwt令牌
        String jwtString = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiZHVzdGRhd24ifQ.NA_J2js7DS_1ZPuum8NRyIxCQ6lcI5wuGkpJ-fPftZkX_Wet7eGJS1-_snigTonYhrg1ttea-U_k21Gh1BS_ubFd_bP6zbPlw7RPN3t1Wec0C_XWzhxHA2g1V5wo5NGb0J-S9mn9j4xD6R4UTXkIzcOhg7g3baQpkKm53zVNDC-icccUGlG3USDFiX0iUC44qCMFfZW_62t85SfFn4lxFqR-JkCYWnZ1AZWTANPJy6izBCfgbqeKTtSPDiyVNnq510mt5IxA2nfvYIp3Lyu71vYBWS-zXEf57Ki6z24wnI6V676eYM-KFV2ka_s1HGZGSlViZPd2n5tx2xgao4Az3A";
        //校验jwt令牌 参数令牌，签名校验算法
        Jwt jwt = JwtHelper.decodeAndVerify(jwtString, new RsaVerifier(publickey));
        //拿到jwt令牌中自定义的内容
        String claims = jwt.getClaims();
        System.out.println(claims);
    }
}
