# Sermant Backend 构建功能总结

## 功能概述

Sermant Backend 现在支持通过 Maven Profile 来灵活指定构建类型，可以构建为 JAR 包或 WAR 包，满足不同的部署需求。

## 主要特性

### ✅ 双重构建支持
- **JAR 包**：独立运行，包含内嵌 Tomcat
- **WAR 包**：Servlet 容器部署，支持传统企业环境

### ✅ 智能条件化配置
- 使用 Spring 条件注解实现智能的 WAR 支持
- 自动检测运行环境，选择合适的部署模式

### ✅ 便捷的构建脚本
- 提供 `build.sh` 脚本，简化构建过程
- 支持彩色输出和详细的构建信息

## 技术实现

### 1. Maven Profile 配置

**动态打包类型：**
```xml
<packaging>${packaging.type}</packaging>
<properties>
    <packaging.type>war</packaging.type>
</properties>
```

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

### 2. 条件化 WAR 支持

**条件类：**
```java
public class WarDeploymentCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // Check if we should enable WAR deployment mode
        // This can be controlled by system property or environment variable
        String deploymentMode = System.getProperty("sermant.deployment.mode");
        if (deploymentMode == null) {
            deploymentMode = System.getenv("SERMANT_DEPLOYMENT_MODE");
        }
        
        // If explicitly set to "war", enable WAR deployment
        if ("war".equalsIgnoreCase(deploymentMode)) {
            return true;
        }
        
        // If explicitly set to "jar", disable WAR deployment
        if ("jar".equalsIgnoreCase(deploymentMode)) {
            return false;
        }
        
        // Default behavior: check if running in a servlet container
        // by looking for ServletContext bean (only available in servlet containers)
        try {
            context.getBeanFactory().getBean("servletContext");
            return true;
        } catch (Exception e) {
            // If ServletContext bean is not available, we're running standalone
            return false;
        }
    }
}
```

**部署模式控制：**
- 系统属性：`-Dsermant.deployment.mode=war`
- 环境变量：`SERMANT_DEPLOYMENT_MODE=war`
- 默认行为：自动检测 Servlet 容器环境

**条件化配置：**
```java
@Conditional(WarDeploymentCondition.class)
class WarDeploymentConfig extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Backend.class);
    }
}
```

### 3. 构建脚本

**功能特性：**
- 支持 JAR、WAR、both 三种构建模式
- 彩色输出和详细的状态信息
- 自动验证构建结果
- 完整的错误处理和帮助信息

## 使用方法

### 命令行构建

```bash
# 构建 JAR 包
mvn clean package -Pjar -DskipTests

# 构建 WAR 包
mvn clean package -Pwar -DskipTests

# 默认构建（WAR 包）
mvn clean package -DskipTests
```

### 使用构建脚本

```bash
# 构建 JAR 包
./build.sh jar

# 构建 WAR 包
./build.sh war

# 同时构建两种包
./build.sh both

# 显示帮助信息
./build.sh help
```

## 部署方式

### JAR 包部署

```bash
# 直接运行
java -jar target/sermant-backend-1.0.0.jar

# 指定端口
java -jar target/sermant-backend-1.0.0.jar --server.port=8080

# Docker 部署
docker run -p 8080:8080 -v /path/to/config:/config sermant-backend
```

### WAR 包部署

```bash
# Tomcat 部署
cp target/sermant-backend-1.0.0.war $TOMCAT_HOME/webapps/

# JBoss 部署
$JBOSS_HOME/bin/jboss-cli.sh --connect --command="deploy target/sermant-backend-1.0.0.war"
```

## 测试验证

### 单元测试

创建了完整的测试用例来验证构建功能：

```java
@Test
public void testWarDeploymentConfiguration() {
    Backend backend = new Backend();
    assertThat(backend).isNotNull();
}

@Test
public void testConfigureMethodExists() {
    assertThat(WarDeploymentConfig.class.getDeclaredMethods())
            .anyMatch(method -> method.getName().equals("configure"));
}
```

### 构建验证

```bash
# 验证 JAR 包
jar -tf target/sermant-backend-1.0.0.jar | head -10

# 验证 WAR 包
jar -tf target/sermant-backend-1.0.0.war | grep -E "(WEB-INF|web.xml)"
```

## 文档支持

### 构建指南
- `BUILD_GUIDE.md`：详细的构建指南和部署说明
- 包含技术实现、最佳实践和故障排除

### 部署文档
- `WAR_DEPLOYMENT.md`：WAR 包部署的详细指南
- 包含支持的 Servlet 容器和配置说明

## 最佳实践

### 1. 环境配置

**开发环境：**
```bash
# 使用 JAR 包，便于调试
mvn spring-boot:run -Pjar
```

**测试环境：**
```bash
# 使用 JAR 包
mvn package -Pjar -DskipTests
```

**生产环境：**
```bash
# 根据需求选择
mvn package -Pjar -DskipTests  # 容器化部署
mvn package -Pwar -DskipTests  # 传统部署
```

### 2. CI/CD 集成

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

### 3. 自动化脚本

**构建脚本：**
```bash
#!/bin/bash
case $1 in
    "jar")
        mvn clean package -Pjar -DskipTests
        ;;
    "war")
        mvn clean package -Pwar -DskipTests
        ;;
    *)
        echo "用法: $0 {jar|war}"
        exit 1
        ;;
esac
```

## 优势总结

### 1. 灵活性
- 支持多种部署方式
- 适应不同的企业环境
- 满足容器化和传统部署需求

### 2. 兼容性
- 保持向后兼容
- 支持现有的 Spring Boot 功能
- 无缝集成现有系统

### 3. 易用性
- 简单的构建命令
- 详细的文档说明
- 便捷的构建脚本

### 4. 可维护性
- 清晰的代码结构
- 完整的测试覆盖
- 良好的文档支持

## 总结

通过 Maven Profile 机制和条件化配置，Sermant Backend 现在具备了灵活的构建能力：

- **JAR 包**：适合现代化部署，支持容器化和云原生
- **WAR 包**：适合传统企业环境，支持 Servlet 容器部署

这种设计既保持了 Spring Boot 的便利性，又提供了传统企业级部署的灵活性，满足了不同场景的部署需求。 