# 前端 Dockerfile
# 多阶段构建

# 第一阶段：构建
FROM node:18-alpine AS builder
WORKDIR /app

# 复制 package.json 并安装依赖
COPY package.json package-lock.json ./
RUN npm ci --registry=https://registry.npmmirror.com

# 复制源代码并构建
COPY . .
RUN npm run build

# 第二阶段：运行
FROM nginx:alpine
WORKDIR /usr/share/nginx/html

# 删除默认配置
RUN rm /etc/nginx/conf.d/default.conf

# 复制 nginx 配置
COPY nginx.conf /etc/nginx/conf.d/

# 从构建阶段复制构建产物
COPY --from=builder /app/dist .

# 暴露端口
EXPOSE 80

# 启动 nginx
CMD ["nginx", "-g", "daemon off;"]