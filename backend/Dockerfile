# 后端 Dockerfile
# 多阶段构建，减小镜像体积

# 第一阶段：构建
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

# 复制 pom.xml 并下载依赖（利用Docker缓存层）
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 复制源代码并打包
COPY src ./src
RUN mvn package -DskipTests -B

# 第二阶段：运行
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# 创建非root用户
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# 创建上传目录
RUN mkdir -p /app/uploads && chown -R appuser:appgroup /app

# 从构建阶段复制jar包
COPY --from=builder /app/target/*.jar app.jar

# 切换到非root用户
USER appuser

# 暴露端口
EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]