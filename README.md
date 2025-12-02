# dxm-houduan

## 项目简介

Dxm-houduan 是一个基于 RuoYi 框架的WMS（仓库管理系统）后端项目。

## 项目结构

```
dxm-houduan/
├── ruoyi-admin-wms/     # 主应用模块
├── ruoyi-common/        # 公共模块
├── ruoyi-modules/       # 业务模块
│   ├── ruoyi-demo/      # 示例模块
│   └── ruoyi-system/    # 系统管理模块
└── script/              # 脚本目录
    └── sql/             # 数据库脚本
```

## 后端组分工

### 开发团队
- **刘慧琦**
- **毛文慧**  
- **何芯轶**

### 技术栈
- **框架**: Spring Boot 2.7.x
- **ORM**: MyBatis Plus
- **安全框架**: SaToken
- **数据库**: MySQL
- **构建工具**: Maven
- **Java版本**: 17

### 主要功能模块
1. **系统管理模块** (ruoyi-system)
   - 用户管理
   - 角色权限管理
   - 菜单管理
   - 字典管理
   - 参数配置

2. **WMS业务模块**
   - 消息分析报告管理
   - 资产持仓管理
   - 市场消息管理
   - 持仓历史管理

### 快速开始

1. **环境准备**
   - JDK 17+
   - Maven 3.6+
   - MySQL 5.7+

2. **数据库初始化**
   - 执行 `script/sql/` 目录下的SQL脚本

3. **项目启动**
   ```bash
   mvn clean compile
   mvn spring-boot:run -f ruoyi-admin-wms/pom.xml
   ```

### 开发规范
- 遵循RuoYi框架开发规范
- 使用MapStruct进行对象映射
- 统一异常处理
- 日志规范管理

### 部署说明
- 支持Docker容器化部署
- 配置文件位于 `ruoyi-admin-wms/src/main/resources/`
- 日志文件输出到 `logs/` 目录