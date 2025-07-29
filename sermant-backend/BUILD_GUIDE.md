# Sermant Backend 构建指南

## 概述

Sermant Backend 现在支持通过 Maven Profile 来指定构建类型，可以构建为 JAR 包或 WAR 包，满足不同的部署需求。

## 构建类型

### 1. JAR 包构建（独立运行）

构建可执行的 JAR 包，适合独立部署和运行：

```bash
# 构建 JAR 包
mvn clean package -Pjar -DskipTests

# 运行 JAR 包
java -jar target/sermant-backend-1.0.0.jar
```

**特点：**
- ✅ 包含内嵌的 Tomcat 服务器
- ✅ 可直接通过 `java -jar` 命令运行
- ✅ 适合容器化部署（Docker）
- ✅ 适合云原生环境

### 2. WAR 包构建（Servlet 容器部署）

构建 WAR 包，可部署到传统的 Servlet 容器中：

```bash
# 构建 WAR 包
mvn clean package -Pwar -DskipTests

# 部署到 Tomcat
cp target/sermant-backend-1.0.0.war $TOMCAT_HOME/webapps/
```

**特点：**
- ✅ 可部署到 Tomcat、JBoss、WebLogic 等容器
- ✅ 适合企业级环境
- ✅ 支持传统的 Servlet 容器管理
- ✅ 包含完整的前端资源

## 构建命令对比

| 构建类型 | 命令 | 输出文件 | 部署方式 |
|---------|------|----------|----------|
| JAR 包 | `mvn package -Pjar` | `sermant-backend-1.0.0.jar` | `java -jar` |
| WAR 包 | `mvn package -Pwar` | `sermant-backend-1.0.0.war` | Servlet 容器 |

## 默认构建

如果不指定 Profile，默认构建为 WAR 包：

```bash
# 默认构建（WAR 包）
mvn clean package -DskipTests
```

## 技术实现

### 1. 动态打包类型

通过 Maven 属性实现动态打包类型：

```xml
<packaging>${packaging.type}</packaging>
<properties>
    <packaging.type>war</packaging.type>
</properties>
```

### 2. 条件化 WAR 支持

使用 Spring 条件注解实现智能的 WAR 支持：

```java
@Conditional(WarDeploymentCondition.class)
class WarDeploymentConfig extends SpringBootServletInitializer {
    // WAR 部署配置
}
```

**部署模式控制：**
- 系统属性：`-Dsermant.deployment.mode=war`
- 环境变量：`SERMANT_DEPLOYMENT_MODE=war`
- 默认行为：自动检测 Servlet 容器环境

### 3. Profile 配置

**JAR Profile：**
```xml
<profile>
    <id>jar</id>
    <properties>
        <packaging.type>jar</packaging.type>
    </properties>
    <!-- Spring Boot 插件配置 -->
</profile>
```

**WAR Profile：**
```xml
<profile>
    <id>war</id>
    <properties>
        <packaging.type>war</packaging.type>
    </properties>
    <!-- Maven WAR 插件配置 -->
</profile>
```

## 部署示例

### JAR 包部署

**1. 构建 JAR 包：**
```bash
mvn clean package -Pjar -DskipTests
```

**2. 运行应用：**
```bash
# 直接运行
java -jar target/sermant-backend-1.0.0.jar

# 指定端口
java -jar target/sermant-backend-1.0.0.jar --server.port=8080

# 指定配置文件
java -jar target/sermant-backend-1.0.0.jar --spring.config.location=classpath:/application-prod.yml
```

**3. Docker 部署：**
```dockerfile
FROM openjdk:8-jre-alpine
COPY target/sermant-backend-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### WAR 包部署

**1. 构建 WAR 包：**
```bash
mvn clean package -Pwar -DskipTests
```

**2. Tomcat 部署：**
```bash
# 复制 WAR 文件
cp target/sermant-backend-1.0.0.war $TOMCAT_HOME/webapps/

# 启动 Tomcat
$TOMCAT_HOME/bin/startup.sh

# 访问应用
open http://localhost:8080/sermant-backend-1.0.0/
```

**3. JBoss/WildFly 部署：**
```bash
# 启动 JBoss
$JBOSS_HOME/bin/standalone.sh

# 部署 WAR 包
$JBOSS_HOME/bin/jboss-cli.sh --connect --command="deploy target/sermant-backend-1.0.0.war"
```

## 环境配置

### 开发环境

```bash
# 开发时使用 JAR 包，便于调试
mvn spring-boot:run -Pjar
```

### 测试环境

```bash
# 测试环境使用 JAR 包
mvn package -Pjar -DskipTests
```

### 生产环境

```bash
# 生产环境根据需求选择
# 容器化部署：JAR 包
mvn package -Pjar -DskipTests

# 传统部署：WAR 包
mvn package -Pwar -DskipTests
```

## 验证构建

### 验证 JAR 包

```bash
# 检查 JAR 包内容
jar -tf target/sermant-backend-1.0.0.jar | head -10

# 验证可执行性
java -jar target/sermant-backend-1.0.0.jar --version
```

### 验证 WAR 包

```bash
# 检查 WAR 包内容
jar -tf target/sermant-backend-1.0.0.war | grep -E "(WEB-INF|web.xml)"

# 验证 Servlet 配置
jar -xf target/sermant-backend-1.0.0.war WEB-INF/web.xml
cat WEB-INF/web.xml
```

## 故障排除

### 常见问题

**1. 构建失败**
```bash
# 清理并重新构建
mvn clean package -Pjar -DskipTests

# 检查依赖
mvn dependency:tree
```

**2. 运行时错误**
```bash
# 检查日志
tail -f logs/sermant/backend/app/*/backend-*.log

# 启用调试模式
java -jar target/sermant-backend-1.0.0.jar --debug
```

**3. 端口冲突**
```bash
# 指定端口
java -jar target/sermant-backend-1.0.0.jar --server.port=8081
```

## 最佳实践

### 1. CI/CD 集成

**Jenkins Pipeline：**
```groovy
pipeline {
    agent any
    stages {
        stage('Build JAR') {
            steps {
                sh 'mvn clean package -Pjar -DskipTests'
            }
        }
        stage('Build WAR') {
            steps {
                sh 'mvn clean package -Pwar -DskipTests'
            }
        }
    }
}
```

### 2. 多环境配置

```bash
# 开发环境
mvn package -Pjar -Dspring.profiles.active=dev

# 测试环境
mvn package -Pjar -Dspring.profiles.active=test

# 生产环境
mvn package -Pwar -Dspring.profiles.active=prod
```

### 3. 自动化脚本

**构建脚本：**
```bash
#!/bin/bash
case $1 in
    "jar")
        mvn clean package -Pjar -DskipTests
        echo "JAR 包构建完成：target/sermant-backend-1.0.0.jar"
        ;;
    "war")
        mvn clean package -Pwar -DskipTests
        echo "WAR 包构建完成：target/sermant-backend-1.0.0.war"
        ;;
    *)
        echo "用法: $0 {jar|war}"
        exit 1
        ;;
esac
```

## 总结

通过 Maven Profile 机制，Sermant Backend 现在支持灵活的构建方式：

- **JAR 包**：适合现代化部署，支持容器化和云原生
- **WAR 包**：适合传统企业环境，支持 Servlet 容器部署

根据不同的部署需求，选择合适的构建类型，实现最佳的部署效果。 