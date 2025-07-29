#!/bin/bash

# Sermant Backend 构建脚本
# 用法: ./build.sh {jar|war|both}

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 打印带颜色的消息
print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 构建 JAR 包
build_jar() {
    print_info "开始构建 JAR 包..."
    mvn clean package -Pjar -DskipTests
    print_success "JAR 包构建完成：target/sermant-backend-1.0.0.jar"
}

# 构建 WAR 包
build_war() {
    print_info "开始构建 WAR 包..."
    mvn clean package -Pwar -DskipTests
    print_success "WAR 包构建完成：target/sermant-backend-1.0.0.war"
}

# 运行 JAR 包（指定 JAR 模式）
run_jar() {
    print_info "以 JAR 模式运行应用..."
    java -Dsermant.deployment.mode=jar -jar target/sermant-backend-1.0.0.jar
}

# 运行 WAR 包（指定 WAR 模式）
run_war() {
    print_info "以 WAR 模式运行应用..."
    java -Dsermant.deployment.mode=war -jar target/sermant-backend-1.0.0.jar
}

# 构建两种包
build_both() {
    print_info "开始构建 JAR 包..."
    mvn clean package -Pjar -DskipTests
    print_success "JAR 包构建完成：target/sermant-backend-1.0.0.jar"
    
    print_info "开始构建 WAR 包..."
    mvn clean package -Pwar -DskipTests
    print_success "WAR 包构建完成：target/sermant-backend-1.0.0.war"
}

# 显示帮助信息
show_help() {
    echo "Sermant Backend 构建脚本"
    echo ""
    echo "用法: $0 {jar|war|both|run-jar|run-war|help}"
    echo ""
    echo "参数说明:"
    echo "  jar      - 构建 JAR 包（独立运行）"
    echo "  war      - 构建 WAR 包（Servlet 容器部署）"
    echo "  both     - 同时构建 JAR 和 WAR 包"
    echo "  run-jar  - 以 JAR 模式运行应用"
    echo "  run-war  - 以 WAR 模式运行应用"
    echo "  help     - 显示此帮助信息"
    echo ""
    echo "示例:"
    echo "  $0 jar      # 构建 JAR 包"
    echo "  $0 war      # 构建 WAR 包"
    echo "  $0 both     # 构建两种包"
    echo "  $0 run-jar  # 以 JAR 模式运行"
    echo "  $0 run-war  # 以 WAR 模式运行"
    echo ""
    echo "部署模式控制:"
    echo "  -Dsermant.deployment.mode=jar  # 强制 JAR 模式"
    echo "  -Dsermant.deployment.mode=war  # 强制 WAR 模式"
    echo ""
    echo "构建完成后，可以运行以下命令验证:"
    echo "  # 运行 JAR 包"
    echo "  java -jar target/sermant-backend-1.0.0.jar"
    echo ""
    echo "  # 部署 WAR 包到 Tomcat"
    echo "  cp target/sermant-backend-1.0.0.war \$TOMCAT_HOME/webapps/"
}

# 验证构建结果
verify_build() {
    local build_type=$1
    
    if [ "$build_type" = "jar" ] || [ "$build_type" = "both" ]; then
        if [ -f "target/sermant-backend-1.0.0.jar" ]; then
            print_success "JAR 包验证成功"
            print_info "文件大小: $(ls -lh target/sermant-backend-1.0.0.jar | awk '{print $5}')"
        else
            print_error "JAR 包构建失败"
            exit 1
        fi
    fi
    
    if [ "$build_type" = "war" ] || [ "$build_type" = "both" ]; then
        if [ -f "target/sermant-backend-1.0.0.war" ]; then
            print_success "WAR 包验证成功"
            print_info "文件大小: $(ls -lh target/sermant-backend-1.0.0.war | awk '{print $5}')"
        else
            print_error "WAR 包构建失败"
            exit 1
        fi
    fi
}

# 主函数
main() {
    case $1 in
        "jar")
            build_jar
            verify_build "jar"
            ;;
        "war")
            build_war
            verify_build "war"
            ;;
        "both")
            build_both
            verify_build "both"
            ;;
        "run-jar")
            if [ ! -f "target/sermant-backend-1.0.0.jar" ]; then
                print_warning "JAR 包不存在，先构建 JAR 包..."
                build_jar
            fi
            run_jar
            ;;
        "run-war")
            if [ ! -f "target/sermant-backend-1.0.0.jar" ]; then
                print_warning "JAR 包不存在，先构建 JAR 包..."
                build_jar
            fi
            run_war
            ;;
        "help"|"-h"|"--help")
            show_help
            ;;
        "")
            print_error "请指定构建类型"
            echo ""
            show_help
            exit 1
            ;;
        *)
            print_error "未知参数: $1"
            echo ""
            show_help
            exit 1
            ;;
    esac
    
    print_success "构建完成！"
    echo ""
    print_info "构建文件位置:"
    if [ "$1" = "jar" ] || [ "$1" = "both" ]; then
        echo "  JAR: target/sermant-backend-1.0.0.jar"
    fi
    if [ "$1" = "war" ] || [ "$1" = "both" ]; then
        echo "  WAR: target/sermant-backend-1.0.0.war"
    fi
}

# 检查 Maven 是否安装
if ! command -v mvn &> /dev/null; then
    print_error "Maven 未安装或不在 PATH 中"
    print_info "请安装 Maven 后重试"
    exit 1
fi

# 检查是否在正确的目录
if [ ! -f "pom.xml" ]; then
    print_error "当前目录不是 Maven 项目根目录"
    print_info "请切换到包含 pom.xml 的目录"
    exit 1
fi

# 执行主函数
main "$@" 