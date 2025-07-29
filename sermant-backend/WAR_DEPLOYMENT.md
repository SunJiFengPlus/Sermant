# Sermant Backend WAR 包部署指南

## 概述

Sermant Backend 现在支持打包为 WAR 文件，可以在传统的 Servlet 容器（如 Tomcat、JBoss 等）中部署，提升了部署的灵活性。

## 构建 WAR 包

```bash
mvn clean package -DskipTests
```

构建完成后，WAR 文件位于：`target/sermant-backend-1.0.0.war`

## 支持的 Servlet 容器

- **Apache Tomcat** 9.0+
- **JBoss/WildFly** 20+
- **WebLogic** 12.2+
- **WebSphere** 9.0+

## 部署步骤

### 1. Apache Tomcat 部署

1. 下载并安装 Apache Tomcat 9.0 或更高版本
2. 将 `sermant-backend-1.0.0.war` 复制到 `$TOMCAT_HOME/webapps/` 目录
3. 启动 Tomcat：
   ```bash
   cd $TOMCAT_HOME
   ./bin/startup.sh
   ```
4. 访问应用：`http://localhost:8080/sermant-backend-1.0.0/`

### 2. JBoss/WildFly 部署

1. 下载并安装 JBoss/WildFly 20 或更高版本
2. 启动 JBoss/WildFly：
   ```bash
   cd $JBOSS_HOME
   ./bin/standalone.sh
   ```
3. 使用管理控制台或命令行部署 WAR 包：
   ```bash
   ./bin/jboss-cli.sh --connect --command="deploy sermant-backend-1.0.0.war"
   ```

### 3. WebLogic 部署

1. 启动 WebLogic 管理控制台
2. 创建新的 Web 应用程序
3. 上传并部署 WAR 文件
4. 启动应用程序

## 配置说明

### 端口配置

默认情况下，应用会在 Servlet 容器的端口上运行。如需修改端口，请配置 Servlet 容器：

**Tomcat 示例** (`$TOMCAT_HOME/conf/server.xml`)：
```xml
<Connector port="8080" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443" />
```

### 上下文路径

WAR 包部署后的上下文路径为：`/sermant-backend-1.0.0`

如需修改，可以：
1. 重命名 WAR 文件（如 `sermant.war`）
2. 或在 Servlet 容器中配置虚拟主机

### 数据库配置

确保在 Servlet 容器中配置了正确的数据库连接池和 JNDI 数据源。

## 特性

### 1. 双重启动模式

应用支持两种启动模式：
- **独立模式**：作为 Spring Boot 应用直接运行
- **WAR 模式**：部署到 Servlet 容器中运行

### 2. 自动配置

应用会自动检测运行环境并应用相应的配置：
- 在 Servlet 容器中运行时，使用 `web.xml` 配置
- 独立运行时，使用 Spring Boot 自动配置

### 3. 前端资源

WAR 包包含了完整的前端资源，无需额外部署。

## 故障排除

### 常见问题

1. **端口冲突**
   - 检查 Servlet 容器端口配置
   - 确保端口未被其他应用占用

2. **内存不足**
   - 增加 JVM 堆内存：`-Xmx2g -Xms1g`
   - 调整 Servlet 容器内存配置

3. **数据库连接失败**
   - 检查数据库连接池配置
   - 验证数据库服务状态

### 日志查看

- **Tomcat**：`$TOMCAT_HOME/logs/catalina.out`
- **JBoss**：`$JBOSS_HOME/standalone/log/server.log`
- **应用日志**：`./logs/sermant/backend/app/`

## 测试验证

部署完成后，可以通过以下方式验证：

1. **健康检查**：`http://localhost:8080/sermant-backend-1.0.0/actuator/health`
2. **前端界面**：`http://localhost:8080/sermant-backend-1.0.0/`
3. **API 接口**：`http://localhost:8080/sermant-backend-1.0.0/api/`

## 版本兼容性

- **Java 版本**：Java 8 或更高版本
- **Servlet 版本**：3.1+
- **Spring Boot 版本**：2.7.18

## 注意事项

1. 确保 Servlet 容器支持 Servlet 3.1 或更高版本
2. 在生产环境中，建议配置 HTTPS
3. 定期备份应用数据和配置文件
4. 监控应用性能和资源使用情况 