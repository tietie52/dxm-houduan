# 硬币数据管理系统

## 项目简介
本系统基于RuoYi框架开发，是一个集成Dify API的硬币数据管理系统，提供硬币数据的同步、查询、新增、修改、删除等全量CRUD功能，支持定时/手动两种方式调用Dify API，并通过标准化RESTful接口与前端完成数据交互。

## 核心功能
1. **Dify API多方式调用**：支持定时任务自动同步、手动触发对话交互两种模式
2. **硬币数据管理**：完整的CRUD接口，支持分页查询
3. **标准化前端对接**：统一响应格式、数据转换、错误处理

## 技术栈
- **框架**: Spring Boot 3.2.6
- **ORM**: MyBatis Plus 3.5.6
- **安全框架**: SaToken 1.37.0
- **数据库**: MySQL
- **构建工具**: Maven
- **Java版本**: 17
- **API集成**: Dify API

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

## 技术实现细节

### 一、Dify API调用实现
系统通过两种方式调用Dify API，适配不同业务场景：

#### 1. 定时任务调用（CoinServiceImpl.fetchCoinDataFromDify）
- 请求构建：基于HttpHeaders + HttpEntity构建请求，包含认证信息与请求体
- API端点：`/workflows/run`（执行工作流）
- 响应处理：灵活解析多格式响应数据，适配不同返回结构

#### 2. 手动触发调用（CoinController.postDifyApi）
- 请求方式：接收前端POST请求参数
- API端点：`/chat-messages`（对话交互）
- 响应解析：针对不同响应结构做定制化解析

### 二、前端对接实现
系统遵循RESTful API设计规范，与前端完成标准化对接：

#### 1. 核心接口列表
| 请求方式 | 接口地址                | 接口功能               |
|----------|-------------------------|------------------------|
| GET      | /coins/sync             | 手动同步硬币数据       |
| POST     | /coins/postDifyApi      | 调用Dify API并保存数据 |
| GET      | /coins/list             | 分页查询硬币列表       |
| GET      | /coins/{id}             | 获取单条硬币详情       |
| POST     | /coins                  | 新增硬币数据           |
| PUT      | /coins                  | 修改硬币数据           |
| DELETE   | /coins/{ids}            | 批量删除硬币数据       |

#### 2. 对接规范
- 统一响应格式：基于`R`类封装所有返回数据，保证前端解析一致性
- 分页支持：通过`PageQuery`（入参）、`TableDataInfo`（出参）实现分页
- 数据转换：服务层完成`Coin`（实体）与`CoinVo`（视图对象）格式转换

## 环境依赖
- JDK 17+
- Maven 3.6+
- MySQL 5.7+
- Spring Boot 3.2.6

## 快速启动

1. **环境准备**
   - 安装JDK 17及以上版本
   - 安装Maven 3.6及以上版本
   - 安装MySQL 5.7及以上版本

2. **数据库初始化**
   - 执行 `script/sql/` 目录下的SQL脚本

3. **项目启动**
   ```bash
   # 编译打包
   mvn clean package -DskipTests
   
   # 启动项目
   java -jar ruoyi-admin-wms/target/ruoyi-admin-wms-5.2.0.jar
   ```

## 注意事项
1. Dify API的认证信息需在配置文件中正确配置
2. 分页查询默认页码为1，默认页大小为10，可通过参数调整
3. 定时任务执行频率可在配置文件中修改
4. 系统默认使用dev环境配置，可通过`--spring.profiles.active`参数切换环境

