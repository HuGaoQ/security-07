package com.ncamc.config;

import com.ncamc.utils.RsaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

@Component
@ConfigurationProperties(prefix = "ncamc.jwt")
public class JwtProperties {
    private String secret;//: ncamc@Login(Auth}*^31)&ncamcjava% # 登录校验的密钥
    private String pubKeyPath;//: D:\\tmp\\rsa\\rsa.pub # 公钥地址
    private String priKeyPath;//: D:\\tmp\\rsa\\rsa.pri # 私钥地址
    private Integer expire;//: 30 # 过期时间,单位分钟
    private String cookieName;//: NCAMC_TOKEN
    private Integer cookieMaxAge;//: 30
    private PublicKey publicKey;//公钥对象
    private PrivateKey privateKey;//私钥对象

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtProperties.class);


    /**
     * @PostConstruct注解 是发生在构造方法执行之前，
     */
    @PostConstruct
    public void initKey() {
        //1.判断公钥和私有文件是否生成
        File publicKeyFile = new File(pubKeyPath);
        File privateKeyFile = new File(priKeyPath);
        //2.判断文件是否存在
        if (!privateKeyFile.exists() || !publicKeyFile.exists()) {
            //3.生成公钥和私钥
            try {
                RsaUtils.generateKey(pubKeyPath, priKeyPath, this.secret);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("公钥和私钥生成失败{}", e.getMessage());
            }
        }
        //4.将文件中的数据读取并转化为publicKey对象和privateKey
        try {
            privateKey = RsaUtils.getPrivateKey(priKeyPath);
            publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("公钥和私钥初始化失败{}", e.getMessage());
        }
    }

    public JwtProperties() {
    }

    public JwtProperties(String secret, String pubKeyPath, String priKeyPath, Integer expire, String cookieName, Integer cookieMaxAge, PublicKey publicKey, PrivateKey privateKey) {
        this.secret = secret;
        this.pubKeyPath = pubKeyPath;
        this.priKeyPath = priKeyPath;
        this.expire = expire;
        this.cookieName = cookieName;
        this.cookieMaxAge = cookieMaxAge;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
    }

    public String getPriKeyPath() {
        return priKeyPath;
    }

    public void setPriKeyPath(String priKeyPath) {
        this.priKeyPath = priKeyPath;
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public Integer getCookieMaxAge() {
        return cookieMaxAge;
    }

    public void setCookieMaxAge(Integer cookieMaxAge) {
        this.cookieMaxAge = cookieMaxAge;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public static Logger getLOGGER() {
        return LOGGER;
    }
}
