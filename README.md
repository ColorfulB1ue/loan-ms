# Loan-MS 贷款管理系统

基于 Spring Boot 3 + Vue 3 的企业级贷款申请与审批管理系统。

## 技术栈

### 后端
- **框架**: Spring Boot 3.5.13
- **ORM**: MyBatis 3.0.5
- **数据库**: MySQL 8.0+
- **认证**: JWT (jjwt 0.11.5，HS256，2h 有效期，支持黑名单吊销)
- **密码加密**: BCrypt (strength=12)
- **接口文档**: SpringDoc OpenAPI 2.8.6
- **AOP**: spring-boot-starter-aop
- **分页**: PageHelper 1.4.7
- **参数校验**: JSR380 (spring-boot-starter-validation)
- **Excel导出**: Apache POI 5.2.5
- **监控**: Spring Boot Actuator
- **Java版本**: 17

### 前端
- **框架**: Vue 3.5.32
- **UI组件库**: Element Plus 2.13.7
- **路由**: Vue Router 5.0.4
- **状态管理**: Pinia 3.0.4
- **HTTP客户端**: Axios 1.15.0
- **图表库**: ECharts 6.1.0（按需引入）
- **JWT解析**: jwt-decode 4.0.0
- **构建工具**: Vite 8.0.4

## 功能模块

### 客户端功能
- 用户注册/登录
- KYC实名认证
- 贷款产品浏览
- 在线贷款申请
- 额度申请/提额
- 还款计划查看
- 在线还款
- 站内消息通知
- 账户解冻申请
- 贷款合同下载
- 数据导出Excel

### 管理员端功能
- KYC资料审核
- 贷款申请审批与放款
- 额度申请审批
- 贷款产品管理
- 还款管理与催收记录
- 财务统计与数据看板
- 系统消息发送
- 用户账户冻结/解冻
- 逾期账单自动扫描与罚息计算
- 操作审计日志

### 系统功能
- 智能风控引擎（信用评分 + 规则引擎）
- 敏感数据自动脱敏
- 统一错误码体系
- JSR380参数校验
- 操作审计日志
- Excel数据导出
- 电子合同生成
- Docker容器化部署

## 快速开始

### 环境要求
- JDK 17+
- Node.js 18+
- MySQL 8.0+
- Maven 3.8+

### 数据库配置

1. 创建数据库

```sql
CREATE DATABASE loan CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 执行初始化脚本

```bash
# 文件位置：backend/src/main/resources/sql/init.sql
```

3. 修改数据库连接配置

```yaml
# 文件位置：backend/src/main/resources/application-dev.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/loan
    username: root
    password: 1234
```

### 后端启动

```bash
cd backend
# 使用 Maven 编译运行
./mvnw spring-boot:run
# Windows
mvnw.cmd spring-boot:run
```

后端服务默认运行在 `http://localhost:8080`

接口文档地址: `http://localhost:8080/doc.html`

### 前端启动

```bash
cd frontend
# 安装依赖
npm install
# 启动开发服务器
npm run dev
```

前端默认运行在 `http://localhost:5173`

### Docker部署

```bash
# 1. 配置环境变量
cp .env.example .env
# 编辑 .env 文件，修改密码和密钥

# 2. 一键启动
docker-compose up -d

# 3. 访问
# 前端：http://localhost
# 后端API：http://localhost:8080
# 接口文档：http://localhost:8080/doc.html
```

## 默认账号

| 角色 | 用户名 | 密码 | 说明 |
|------|--------|------|------|
| 管理员 | admin | 123456 | 系统管理员 |
| 客户 | user1 | 123456 | 已认证客户 |
| 客户 | user2 | 123456 | 待审核认证 |
| 客户 | user3 | 123456 | 已认证客户 |

> 注：以上账号为数据库预置的测试账号，可直接登录。新注册用户的密码需满足：长度≥8位、同时包含字母和数字、不在弱口令黑名单中。

## 项目结构

```
loan-ms/
├── backend/                    # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/young/
│   │   │   │   ├── common/           # 公共类（异常、拦截器、注解、AOP、枚举）
│   │   │   │   ├── config/           # 配置类
│   │   │   │   ├── controller/       # 控制器
│   │   │   │   ├── dto/              # 数据传输对象
│   │   │   │   ├── mapper/           # MyBatis映射
│   │   │   │   ├── pojo/             # 实体类
│   │   │   │   ├── risk/             # 风控引擎
│   │   │   │   ├── service/          # 业务逻辑接口
│   │   │   │   │   └── impl/         # 业务逻辑实现
│   │   │   │   ├── task/             # 定时任务
│   │   │   │   └── utils/            # 工具类
│   │   │   └── resources/
│   │   │       ├── mapper/           # MyBatis XML
│   │   │       ├── sql/              # 数据库脚本
│   │   │       └── application*.yml  # 配置文件
│   │   └── test/                     # 单元测试
│   ├── Dockerfile
│   └── pom.xml
├── frontend/                   # 前端项目
│   ├── src/
│   │   ├── api/                # API 请求模块
│   │   ├── components/         # 公共组件
│   │   ├── composables/        # 组合式函数
│   │   ├── constants/          # 业务常量
│   │   ├── layout/             # 布局组件
│   │   ├── router/             # 路由配置
│   │   ├── stores/             # Pinia 状态管理
│   │   ├── styles/             # 公共样式
│   │   ├── utils/              # 工具函数
│   │   └── views/              # 页面组件
│   │       ├── admin/          # 管理端页面
│   │       └── client/         # 客户端页面
│   ├── Dockerfile
│   ├── nginx.conf
│   └── package.json
├── docker-compose.yml          # Docker编排
├── .env.example                # 环境变量示例
└── README.md
```

## 核心数据表

| 表名 | 说明 |
|------|------|
| `sys_user` | 系统用户表 |
| `user_profile` | 用户实名认证表 |
| `user_credit` | 用户授信额度表 |
| `loan_product` | 贷款产品表 |
| `loan_application` | 贷款申请表 |
| `repayment_plan` | 还款计划表 |
| `repayment_record` | 还款记录表 |
| `collection_record` | 催收记录表 |
| `sys_message` | 系统消息表 |
| `credit_application` | 额度申请表 |
| `unfreeze_application` | 解冻申请表 |
| `risk_assessment` | 风险评估记录表 |
| `audit_log` | 操作审计日志表 |

## API接口

### 认证模块
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/logout` - 退出登录
- `POST /api/auth/change-password` - 修改密码

### 贷款模块
- `POST /api/loan/apply` - 提交贷款申请
- `GET /api/loan/my` - 查询我的贷款
- `GET /api/loan/pending` - 查询待审批贷款（管理端）
- `POST /api/loan/approve/{id}` - 审批通过并放款
- `POST /api/loan/reject/{id}` - 驳回贷款

### 还款模块
- `GET /api/repayment/my-plans` - 查询我的还款计划
- `POST /api/repayment/pay` - 还款
- `POST /api/repayment/pay-early/{loanId}` - 提前结清

### 导出模块
- `GET /api/export/loans` - 导出贷款记录
- `GET /api/export/plans` - 导出还款计划
- `GET /api/export/records` - 导出还款记录

### 合同模块
- `GET /api/contract/generate/{loanId}` - 生成贷款合同
- `GET /api/contract/download/{loanId}` - 下载贷款合同

### 健康检查
- `GET /actuator/health` - 应用健康状态

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 请求参数错误 |
| 401 | 未登录或登录已过期 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 429 | 请求过于频繁 |
| 500 | 服务器内部错误 |
| 1001xxx | 认证模块错误 |
| 2001xxx | 用户模块错误 |
| 3001xxx | 贷款模块错误 |
| 4001xxx | 额度模块错误 |
| 5001xxx | 还款模块错误 |

## 环境变量

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| `SPRING_PROFILES_ACTIVE` | 激活的配置文件 | dev |
| `DB_PASSWORD` | 数据库密码 | 1234 |
| `JWT_SECRET` | JWT签名密钥 | (内置测试密钥) |
| `CORS_ORIGINS` | 允许的跨域源 | http://localhost:5173 |
| `UPLOAD_DIR` | 文件上传目录 | uploads/ |

## 联系方式

如有问题或需要商业服务，欢迎联系：

- **QQ**: `1600386893`
- **服务内容**: 付费部署、定制修改、功能扩展等

## License

MIT License