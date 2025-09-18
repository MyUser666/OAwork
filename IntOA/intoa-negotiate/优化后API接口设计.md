# 预约管理系统优化后需求分析和API接口设计

## 1. 需求分析优化

### 1.1 核心业务流程优化

预约管理系统的核心业务流程需要更加清晰地定义各个状态之间的转换关系和约束条件：

```
创建预约 → 临时锁定资源 → 填写预约信息 → 提交预约 → 待确认(资源临时占用)
待确认 → 已确认(资源正式占用) [管理员确认预约]
待确认 → 已取消(资源释放) [超时自动取消/用户主动取消]
已确认 → 已签到 [用户实际到场签到]
已确认 → 已取消 [管理员取消/用户取消]
已签到 → 已完成 [预约时间结束/用户提前完成]
已签到 → 已取消 [特殊情况取消]
```

### 1.2 状态定义与约束条件

| 状态码 | 状态名称 | 说明 | 可见性 | 可操作性 |
|--------|----------|------|--------|----------|
| 0 | 待确认 | 用户选择资源后系统临时锁定，用户填写信息但未提交 | 仅当前用户可见 | 可取消、可超时 |
| 1 | 已确认 | 管理员确认预约，资源正式占用 | 所有相关用户可见 | 可签到、可取消 |
| 2 | 已签到 | 用户实际到场签到 | 所有相关用户可见 | 可完成、可取消 |
| 3 | 已完成 | 预约正常结束 | 历史记录 | 不可操作 |
| 4 | 已取消 | 预约被取消，资源释放 | 历史记录 | 不可操作 |

### 1.3 资源锁定机制优化

1. **临时锁定（待确认状态）**
   - 用户提交预约请求时，系统立即在Redis中锁定指定房间和时间段
   - 锁定信息对普通用户不可见，仅管理员可见
   - 设置锁定过期时间（如30分钟），过期自动释放

2. **正式锁定（已确认状态）**
   - 管理员确认预约后临时锁定转为正式锁定
   - 锁定信息对所有相关用户可见
   - 锁定持续到预约完成或取消

### 1.4 并发控制机制优化

1. **时间冲突检测**
   - 预约创建时检查房间时间冲突
   - 时间重叠算法考虑缓冲时间避免紧密连接
   - 同一房间同一时间段只能有一个有效预约

2. **资源锁定验证**
   - 待确认和已确认状态的预约都视为资源占用
   - 创建新预约时检查所有相关资源的占用情况
   - 采用Redis原子操作确保并发安全性

## 2. API接口设计优化

### 2.1 基础管理接口

#### 2.1.1 查询预约列表
**接口地址**: `GET /negotiatelog/neglog/list`  
**功能说明**: 查询预约列表，支持分页和条件查询  
**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| logRoomName | String | 否 | 预约房间 |
| title | String | 否 | 预约标题 |
| clientName | String | 否 | 当事人姓名 |
| status | String | 否 | 状态（0待确认 1已确认 2已签到 3已完成 4已取消） |
| params.beginStartTime | String | 否 | 开始时间起始值（格式：yyyy-MM-dd） |
| params.endStartTime | String | 否 | 开始时间结束值（格式：yyyy-MM-dd） |
| params.beginEndTime | String | 否 | 结束时间起始值（格式：yyyy-MM-dd） |
| params.endEndTime | String | 否 | 结束时间结束值（格式：yyyy-MM-dd） |
| pageNum | Integer | 否 | 页码 |
| pageSize | Integer | 否 | 每页条数 |

**响应参数**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "total": 100,
    "rows": [
      {
        "logId": 1,
        "logRoomName": "洽谈室A",
        "title": "项目洽谈",
        "logNickName": "张三",
        "logTeaName": "龙井茶",
        "logTeaNum": 2,
        "clientName": "李四",
        "clientContact": "13800138000",
        "caseReference": "项目合作",
        "startTime": "2025-09-03 10:00:00",
        "endTime": "2025-09-03 12:00:00",
        "status": "1",
        "createBy": "张三",
        "createTime": "2025-09-03 09:00:00",
        "updateBy": "张三",
        "updateTime": "2025-09-03 09:00:00",
        "remark": "重要客户"
      }
    ]
  }
}
```

#### 2.1.2 获取预约详情
**接口地址**: `GET /negotiatelog/neglog/{logId}`  
**功能说明**: 根据预约ID获取预约详细信息  
**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| logId | Long | 是 | 预约ID |

**响应参数**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "logId": 1,
    "logRoomName": "洽谈室A",
    "title": "项目洽谈",
    "logNickName": "张三",
    "logTeaName": "龙井茶",
    "logTeaNum": 2,
    "clientName": "李四",
    "clientContact": "13800138000",
    "caseReference": "项目合作",
    "startTime": "2025-09-03 10:00:00",
    "endTime": "2025-09-03 12:00:00",
    "status": "1",
    "createBy": "张三",
    "createTime": "2025-09-03 09:00:00",
    "updateBy": "张三",
    "updateTime": "2025-09-03 09:00:00",
    "remark": "重要客户"
  }
}
```

#### 2.1.3 新增预约（创建待确认预约）
**接口地址**: `POST /negotiatelog/neglog`  
**功能说明**: 新增预约信息，创建待确认状态的预约并临时锁定资源  
**请求参数**:
```json
{
  "logRoomName": "洽谈室A",
  "title": "项目洽谈",
  "logNickName": "张三",
  "logTeaName": "龙井茶",
  "logTeaNum": 2,
  "clientName": "李四",
  "clientContact": "13800138000",
  "caseReference": "项目合作",
  "startTime": "2025-09-03 10:00:00",
  "endTime": "2025-09-03 12:00:00",
  "remark": "重要客户"
}
```

**响应参数**:
```json
{
  "code": 200,
  "msg": "操作成功"
}
```

#### 2.1.4 修改预约
**接口地址**: `PUT /negotiatelog/neglog`  
**功能说明**: 修改预约信息（仅限待确认状态）  
**请求参数**:
```json
{
  "logId": 1,
  "logRoomName": "洽谈室A",
  "title": "项目洽谈",
  "logNickName": "张三",
  "logTeaName": "龙井茶",
  "logTeaNum": 2,
  "clientName": "李四",
  "clientContact": "13800138000",
  "caseReference": "项目合作",
  "startTime": "2025-09-03 10:00:00",
  "endTime": "2025-09-03 12:00:00",
  "remark": "重要客户"
}
```

**响应参数**:
```json
{
  "code": 200,
  "msg": "操作成功"
}
```

#### 2.1.5 删除预约
**接口地址**: `DELETE /negotiatelog/neglog/{logIds}`  
**功能说明**: 删除预约信息（支持批量删除，仅限待确认状态）  
**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| logIds | Long[] | 是 | 预约ID数组 |

**响应参数**:
```json
{
  "code": 200,
  "msg": "操作成功"
}
```

#### 2.1.6 导出预约列表
**接口地址**: `POST /negotiatelog/neglog/export`  
**功能说明**: 导出预约列表为Excel文件  
**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| logRoomName | String | 否 | 预约房间 |
| title | String | 否 | 预约标题 |
| clientName | String | 否 | 当事人姓名 |
| status | String | 否 | 状态（0待确认 1已确认 2已签到 3已完成 4已取消） |
| params.beginStartTime | String | 否 | 开始时间起始值（格式：yyyy-MM-dd） |
| params.endStartTime | String | 否 | 开始时间结束值（格式：yyyy-MM-dd） |
| params.beginEndTime | String | 否 | 结束时间起始值（格式：yyyy-MM-dd） |
| params.endEndTime | String | 否 | 结束时间结束值（格式：yyyy-MM-dd） |

**响应参数**: Excel文件下载

### 2.2 资源选择接口

#### 2.2.1 获取房间详情
**接口地址**: `GET /negotiatelog/neglog/room/{roomId}`  
**功能说明**: 获取指定房间的详细信息  
**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| roomId | Long | 是 | 房间ID |

**响应参数**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "roomId": 1,
    "roomName": "洽谈室A",
    "location": "一楼东侧",
    "capacity": 10,
    "equipment": "投影仪,白板,音响",
    "bufferTime": 30,
    "status": "0",
    "orderNum": 1,
    "remark": "标准洽谈室"
  }
}
```

#### 2.2.2 获取所有可用房间列表
**接口地址**: `GET /negotiatelog/neglog/availableRooms`  
**功能说明**: 获取所有可用房间列表  
**请求参数**: 无

**响应参数**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "roomId": 1,
      "roomName": "洽谈室A",
      "location": "一楼东侧",
      "capacity": 10,
      "equipment": "投影仪,白板,音响",
      "bufferTime": 30,
      "status": "0",
      "orderNum": 1,
      "remark": "标准洽谈室"
    }
  ]
}
```

#### 2.2.3 获取指定房间在指定日期的可用时间段
**接口地址**: `GET /negotiatelog/neglog/room/{roomId}/available-times`  
**功能说明**: 获取指定房间在指定日期的可用时间段  
**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| roomId | Long | 是 | 房间ID |
| date | String | 是 | 日期（格式：yyyy-MM-dd） |

**响应参数**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "startTime": "2025-09-03 09:00:00",
      "endTime": "2025-09-03 10:00:00"
    },
    {
      "startTime": "2025-09-03 14:00:00",
      "endTime": "2025-09-03 16:00:00"
    }
  ]
}
```

#### 2.2.4 获取在指定时间段内可用的房间列表
**接口地址**: `GET /negotiatelog/neglog/available-rooms`  
**功能说明**: 获取在指定时间段内可用的房间列表  
**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| startTime | String | 是 | 开始时间（格式：yyyy-MM-dd HH:mm） |
| endTime | String | 是 | 结束时间（格式：yyyy-MM-dd HH:mm） |

**响应参数**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "roomId": 1,
      "roomName": "洽谈室A",
      "location": "一楼东侧",
      "capacity": 10,
      "equipment": "投影仪,白板,音响",
      "bufferTime": 30
    }
  ]
}
```

### 2.3 资源锁定接口

#### 2.3.1 临时锁定资源
**接口地址**: `POST /negotiatelog/neglog/temp-lock`  
**功能说明**: 临时锁定房间和茶水资源  
**请求参数**:
```json
{
  "roomId": 1,
  "roomName": "洽谈室A",
  "startTime": "2025-09-03 10:00:00",
  "endTime": "2025-09-03 12:00:00",
  "teaId": 1,
  "teaName": "龙井茶",
  "teaQuantity": 2,
  "userId": 1
}
```

**响应参数**:
```json
{
  "code": 200,
  "msg": "操作成功"
}
```

#### 2.3.2 检查临时锁定是否过期
**接口地址**: `GET /negotiatelog/neglog/check-lock/{userId}`  
**功能说明**: 检查用户的临时锁定是否过期  
**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |

**响应参数**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "isValid": true
  }
}
```

### 2.4 状态流转接口

#### 2.4.1 确认预约
**接口地址**: `PUT /negotiatelog/neglog/confirm/{logId}`  
**功能说明**: 确认预约，更新预约状态为已确认  
**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| logId | Long | 是 | 预约ID |

**响应参数**:
```json
{
  "code": 200,
  "msg": "操作成功"
}
```

#### 2.4.2 用户签到
**接口地址**: `PUT /negotiatelog/neglog/checkin/{logId}`  
**功能说明**: 用户签到，更新预约状态为已签到  
**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| logId | Long | 是 | 预约ID |

**响应参数**:
```json
{
  "code": 200,
  "msg": "操作成功"
}
```

#### 2.4.3 完成预约
**接口地址**: `PUT /negotiatelog/neglog/complete/{logId}`  
**功能说明**: 完成预约，更新预约状态为已完成  
**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| logId | Long | 是 | 预约ID |

**响应参数**:
```json
{
  "code": 200,
  "msg": "操作成功"
}
```

#### 2.4.4 取消预约
**接口地址**: `PUT /negotiatelog/neglog/cancel/{logId}`  
**功能说明**: 取消预约，更新预约状态为已取消  
**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| logId | Long | 是 | 预约ID |
| cancelReason | String | 是 | 取消原因 |

**响应参数**:
```json
{
  "code": 200,
  "msg": "操作成功"
}
```

### 2.5 缓存操作接口

#### 2.5.1 查询房间锁定状态
**接口地址**: `GET /negotiatelog/neglog/room-lock-status`  
**功能说明**: 查询指定房间在指定时间段的锁定状态  
**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| roomName | String | 是 | 房间名称 |
| date | String | 是 | 日期（格式：yyyy-MM-dd） |
| startTime | String | 是 | 开始时间（格式：HH:mm） |
| endTime | String | 是 | 结束时间（格式：HH:mm） |

**响应参数**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "isLocked": true,
    "lockStatus": "1"
  }
}
```

#### 2.5.2 查询茶水库存
**接口地址**: `GET /negotiatelog/neglog/tea-stock`  
**功能说明**: 查询指定茶水的库存数量  
**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| teaName | String | 是 | 茶水名称 |

**响应参数**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "stockQuantity": 50
  }
}
```

## 3. 优化建议

### 3.1 系统架构优化

1. **完善Redis集成**
   - 实现完整的Redis分布式锁机制
   - 统一临时锁定和正式锁定的接口
   - 添加锁定状态检查方法

2. **规范状态管理**
   - 统一预约状态的设置逻辑
   - 实现状态检查方法
   - 添加状态流转日志记录

3. **加强并发控制**
   - 实现时间冲突检测算法
   - 添加用户并发预约限制
   - 完善高并发场景下的资源竞争处理

4. **优化库存管理**
   - 明确预占和正式扣减的逻辑
   - 添加库存不足的异常处理
   - 实现库存变动记录功能

5. **实现超时处理机制**
   - 添加定时任务清理过期锁定
   - 实现临时锁定的过期时间检查
   - 完善超时自动取消功能

### 3.2 数据一致性保障

1. **事务管理**
   - 所有涉及锁定记录和预约记录的操作都需要使用事务
   - 确保锁定操作和数据库操作的一致性

2. **异常处理**
   - 完善异常处理机制，确保系统稳定性
   - 添加回滚机制处理锁定失败的情况

3. **定时任务处理**
   - 实现过期锁定清理的定时任务
   - 定时检查待确认状态的预约并自动取消超时的预约