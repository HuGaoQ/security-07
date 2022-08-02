# 基础镜像，即运行环境 JDK 8（与SpringBoot项目中的JDK版本尽量保持一致）
FROM java:8
# SpringBoot内嵌的Tomcat容器默认使用该 /tmp 作为工作目录
VOLUME /tmp
# 拷贝文件（SpringBoot项目打包的Jar包名）并重命名（另起一个新名字）
ADD security-07-1.0-SNAPSHOT.jar security.jar
# 不是真正的访问端口（标识SpringBoot项目访问端口，告知部署人员要开发的端口号）（只是标记作用，可无）
EXPOSE 8080
# 容器启动时的命令（等价于 java -jar xxxx.jar）
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/security.jar"]