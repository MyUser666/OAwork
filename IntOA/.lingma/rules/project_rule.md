# IntOA 项目专属规则

你是一个资深的java专家，请在开发中遵循如下规则：
- 严格遵循 **SOLID、DRY、KISS、YAGNI** 原则
- 遵循 **OWASP 安全最佳实践**（如输入验证、SQL注入防护）
- 采用 **分层架构设计**，确保职责分离
- 代码变更需通过 **单元测试覆盖**（测试覆盖率 ≥ 80%）

---

## 一、模块结构
```
intoa
├── intoa-admin         # 主应用入口模块
├── intoa-common        # 通用工具模块
│            └── annotation                    // 自定义注解
│            └── config                        // 全局配置
│            └── constant                      // 通用常量
│            └── core                          // 核心控制
│            └── enums                         // 通用枚举
│            └── exception                     // 通用异常
│            └── filter                        // 过滤器处理
│            └── utils                         // 通用类处理
├── intoa-framework     # 核心框架模块
│             └── aspectj                       // 注解实现
│             └── config                        // 系统配置
│             └── datasource                    // 数据权限
│             └── interceptor                   // 拦截器
│             └── manager                       // 异步处理
│             └── security                      // 权限控制
│             └── web                           // 前端控制
├── intoa-system        # 系统管理模块
├── intoa-quartz        # 定时任务模块
├── intoa-generator     # 代码生成模块
└── intoa-negotiate     # 业务模块（预约管理）
```
---

## 二、技术栈规范
### 技术栈要求
- **框架**：Spring Boot 3.5.4 + Spring Security + MyBatis + Java 17
- **数据库**：MySQL 8.2.0
- **连接池**：Druid 1.2.23
- **缓存**：Redis
- **安全认证**：JWT
- **API文档**：SpringDoc OpenAPI
- **分页插件**：PageHelper 2.1.1
- **JSON处理**：FastJSON 2.0.57
- **代码生成**：Velocity 2.3
- **构建工具**：Maven

---

## 三、应用逻辑设计规范
### 1. 分层架构原则
| 层级             | 职责                                                                 | 约束条件                                                                 |
|----------------|----------------------------------------------------------------------|--------------------------------------------------------------------------|
| **Controller** | 处理 HTTP 请求与响应，定义 API 接口                                 | - 禁止直接操作数据库<br>- 必须通过 Service 层调用                          |
| **Service**    | 业务逻辑实现，事务管理，数据校验                                   | - 必须通过 Mapper 访问数据库<br>- 返回 DTO 而非实体类       |
| **Mapper**     | 数据持久化操作，定义数据库查询逻辑                                 | - 使用 MyBatis 注解或 XML 映射<br>- 避免 N+1 查询问题       |
| **Domain**     | 数据库表结构映射对象                                               | - 仅用于数据库交互<br>- 禁止直接返回给前端（需通过 DTO 转换）               |

---

## 四、核心代码规范
### 1. 实体类（Domain）规范
```java
/**
 * 房间管理对象 neg_room
 * 
 * @author beihai
 * @date 2023-07-19
 */
@Data
@TableName("neg_room")
public class NegRoom {
    private static final long serialVersionUID = 1L;

    /** 房间ID */
    private Long roomId;

    /** 房间名称 */
    private String roomName;

    /** 位置 */
    private String location;

    /** 容量 */
    private Long capacity;

    /** 设备 */
    private String equipment;

    /** 缓冲时间 */
    private Long bufferTime;

    /** 显示顺序 */
    private Long orderNum;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    private Date updateTime;

    /** 备注 */
    private String remark;
}
```

### 2. 数据访问层（Mapper）规范
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.intoa.negotiate.mapper.NegRoomMapper">
    
    <resultMap type="NegRoom" id="NegRoomResult">
        <result property="roomId" column="room_id"/>
        <result property="roomName" column="room_name"/>
        <result property="location" column="location"/>
        <result property="capacity" column="capacity"/>
        <result property="equipment" column="equipment"/>
        <result property="bufferTime" column="buffer_time"/>
        <result property="orderNum" column="order_num"/>
        <result property="createBy" column="create_by"/>
        <result property="createTime" column="create_time"/>
        <result property="updateBy" column="update_by"/>
        <result property="updateTime" column="update_time"/>
        <result property="remark" column="remark"/>
    </resultMap>
    
    <sql id="selectNegRoomVo">
        select room_id, room_name, location, capacity, equipment, buffer_time, order_num, create_by, create_time, update_by, update_time, remark from neg_room
    </sql>
    
    <select id="selectNegRoomList" parameterType="NegRoom" resultMap="NegRoomResult">
        <include refid="selectNegRoomVo"/>
        <where>  
            <if test="roomName != null  and roomName != ''"> and room_name like concat('%', #{roomName}, '%')</if>
            <if test="location != null  and location != ''"> and location = #{location}</if>
            <if test="capacity != null "> and capacity = #{capacity}</if>
            <if test="equipment != null  and equipment != ''"> and equipment = #{equipment}</if>
            <if test="bufferTime != null "> and buffer_time = #{bufferTime}</if>
        </where>
        order by order_num asc
    </select>
    
</mapper>
```

### 3. 服务层（Service）规范
```java
/**
 * 房间管理Service业务层处理
 * 
 * @author beihai
 * @date 2023-07-19
 */
@Service
public class NegRoomServiceImpl implements INegRoomService {
    @Autowired
    private NegRoomMapper negRoomMapper;

    /**
     * 查询房间管理
     * 
     * @param roomId 房间管理主键
     * @return 房间管理
     */
    @Override
    public NegRoom selectNegRoomByRoomId(Long roomId) {
        return negRoomMapper.selectNegRoomByRoomId(roomId);
    }

    /**
     * 查询房间管理列表
     * 
     * @param negRoom 房间管理
     * @return 房间管理
     */
    @Override
    public List<NegRoom> selectNegRoomList(NegRoom negRoom) {
        return negRoomMapper.selectNegRoomList(negRoom);
    }

    /**
     * 新增房间管理
     * 
     * @param negRoom 房间管理
     * @return 结果
     */
    @Override
    public int insertNegRoom(NegRoom negRoom) {
        negRoom.setCreateTime(DateUtils.getNowDate());
        return negRoomMapper.insertNegRoom(negRoom);
    }

    /**
     * 修改房间管理
     * 
     * @param negRoom 房间管理
     * @return 结果
     */
    @Override
    public int updateNegRoom(NegRoom negRoom) {
        negRoom.setUpdateTime(DateUtils.getNowDate());
        return negRoomMapper.updateNegRoom(negRoom);
    }

    /**
     * 批量删除房间管理
     * 
     * @param roomIds 需要删除的房间管理主键
     * @return 结果
     */
    @Override
    public int deleteNegRoomByRoomIds(Long[] roomIds) {
        return negRoomMapper.deleteNegRoomByRoomIds(roomIds);
    }

    /**
     * 删除房间管理信息
     * 
     * @param roomId 房间管理主键
     * @return 结果
     */
    @Override
    public int deleteNegRoomByRoomId(Long roomId) {
        return negRoomMapper.deleteNegRoomByRoomId(roomId);
    }
}
```

### 4. 控制器（RestController）规范
```java
/**
 * 房间管理Controller
 * 
 * @author beihai
 * @date 2023-07-19
 */
@RestController
@RequestMapping("/negotiate/room")
public class NegRoomController extends BaseController {
    @Autowired
    private INegRoomService negRoomService;

    /**
     * 查询房间管理列表
     */
    @PreAuthorize("@ss.hasPermi('negotiate:room:list')")
    @GetMapping("/list")
    public TableDataInfo list(NegRoom negRoom) {
        startPage();
        List<NegRoom> list = negRoomService.selectNegRoomList(negRoom);
        return getDataTable(list);
    }

    /**
     * 获取房间管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('negotiate:room:query')")
    @GetMapping(value = "/{roomId}")
    public AjaxResult getInfo(@PathVariable("roomId") Long roomId) {
        return AjaxResult.success(negRoomService.selectNegRoomByRoomId(roomId));
    }

    /**
     * 新增房间管理
     */
    @PreAuthorize("@ss.hasPermi('negotiate:room:add')")
    @Log(title = "房间管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody NegRoom negRoom) {
        return toAjax(negRoomService.insertNegRoom(negRoom));
    }

    /**
     * 修改房间管理
     */
    @PreAuthorize("@ss.hasPermi('negotiate:room:edit')")
    @Log(title = "房间管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody NegRoom negRoom) {
        return toAjax(negRoomService.updateNegRoom(negRoom));
    }

    /**
     * 删除房间管理
     */
    @PreAuthorize("@ss.hasPermi('negotiate:room:remove')")
    @Log(title = "房间管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roomIds}")
    public AjaxResult remove(@PathVariable Long[] roomIds) {
        return toAjax(negRoomService.deleteNegRoomByRoomIds(roomIds));
    }
}
```

---

## 五、数据传输对象（DTO）规范
```java
// 使用 @Data 注解
@Data
public class NegRoomDTO {
    /** 房间ID */
    private Long roomId;

    /** 房间名称 */
    @NotBlank(message = "房间名称不能为空")
    private String roomName;

    /** 位置 */
    @NotBlank(message = "位置不能为空")
    private String location;

    /** 容量 */
    @NotNull(message = "容量不能为空")
    private Long capacity;

    /** 设备 */
    private String equipment;

    /** 缓冲时间 */
    private Long bufferTime;

    /** 显示顺序 */
    private Long orderNum;

    /** 备注 */
    private String remark;

    public static NegRoomDTO fromEntity(NegRoom entity) {
        NegRoomDTO dto = new NegRoomDTO();
        dto.setRoomId(entity.getRoomId());
        dto.setRoomName(entity.getRoomName());
        dto.setLocation(entity.getLocation());
        dto.setCapacity(entity.getCapacity());
        dto.setEquipment(entity.getEquipment());
        dto.setBufferTime(entity.getBufferTime());
        dto.setOrderNum(entity.getOrderNum());
        dto.setRemark(entity.getRemark());
        return dto;
    }
}
```

---

## 六、全局异常处理规范
### 1. 统一响应类（AjaxResult）
```java
/**
 * 统一响应结果
 * 
 * @author beihai
 */
public class AjaxResult extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    /** 状态码 */
    public static final String CODE_TAG = "code";

    /** 返回内容 */
    public static final String MSG_TAG = "msg";

    /** 数据对象 */
    public static final String DATA_TAG = "data";

    /**
     * 初始化一个新创建的 AjaxResult 对象，使其表示一个空消息。
     */
    public AjaxResult() {
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     * 
     * @param code 状态码
     * @param msg 返回内容
     */
    public AjaxResult(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     * 
     * @param code 状态码
     * @param msg 返回内容
     * @param data 数据对象
     */
    public AjaxResult(int code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (StringUtils.isNotNull(data)) {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 返回成功消息
     * 
     * @return 成功消息
     */
    public static AjaxResult success() {
        return AjaxResult.success("操作成功");
    }

    /**
     * 返回成功数据
     * 
     * @return 成功消息
     */
    public static AjaxResult success(Object data) {
        return AjaxResult.success("操作成功", data);
    }

    /**
     * 返回成功消息
     * 
     * @param msg 返回内容
     * @return 成功消息
     */
    public static AjaxResult success(String msg) {
        return AjaxResult.success(msg, null);
    }

    /**
     * 返回成功消息
     * 
     * @param msg 返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static AjaxResult success(String msg, Object data) {
        return new AjaxResult(HttpStatus.SUCCESS, msg, data);
    }

    /**
     * 返回错误消息
     * 
     * @return 警告消息
     */
    public static AjaxResult error() {
        return AjaxResult.error("操作失败");
    }

    /**
     * 返回错误消息
     * 
     * @param msg 返回内容
     * @return 警告消息
     */
    public static AjaxResult error(String msg) {
        return AjaxResult.error(msg, null);
    }

    /**
     * 返回错误消息
     * 
     * @param msg 返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static AjaxResult error(String msg, Object data) {
        return new AjaxResult(HttpStatus.ERROR, msg, data);
    }

    /**
     * 返回错误消息
     * 
     * @param code 状态码
     * @param msg 返回内容
     * @return 警告消息
     */
    public static AjaxResult error(int code, String msg) {
        return new AjaxResult(code, msg, null);
    }
}
```

### 2. 全局异常处理器（GlobalExceptionHandler）
```java
/**
 * 全局异常处理器
 * 
 * @author beihai
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 权限校验异常
     */
    @ExceptionHandler(PreAuthorizeException.class)
    public AjaxResult handlePreAuthorizeException(PreAuthorizeException e) {
        log.error(e.getMessage());
        return AjaxResult.error("没有权限，请联系管理员授权");
    }

    /**
     * 验证码失效异常
     */
    @ExceptionHandler(CaptchaExpireException.class)
    public AjaxResult handleCaptchaExpireException(CaptchaExpireException e) {
        log.error(e.getMessage());
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 验证码错误异常
     */
    @ExceptionHandler(CaptchaException.class)
    public AjaxResult handleCaptchaException(CaptchaException e) {
        log.error(e.getMessage());
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public AjaxResult handleRuntimeException(RuntimeException e) {
        log.error("运行时异常:", e);
        return AjaxResult.error("运行时异常:" + e.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e) {
        log.error(e.getMessage(), e);
        return AjaxResult.error("服务器内部错误，请联系管理员");
    }
}
```

---

## 七、安全与性能规范
1. **输入校验**：
    - 使用 `@Valid` 注解 + JSR-303 校验注解（如 `@NotBlank`, `@Size`）
    - 禁止直接拼接 SQL 防止注入攻击
2. **事务管理**：
    - `@Transactional` 注解仅标注在 Service 方法上
    - 避免在循环中频繁提交事务
3. **性能优化**：
    - 使用 MyBatis 的延迟加载避免 N+1 查询问题
    - 避免在循环中执行数据库查询（批量操作优先）

---

## 八、代码风格规范
1. **命名规范**：
    - 类名：`UpperCamelCase`（如 `NegRoomServiceImpl`）
    - 方法/变量名：`lowerCamelCase`（如 `selectNegRoomList`）
    - 常量：`UPPER_SNAKE_CASE`（如 `MAX_LOGIN_ATTEMPTS`）
2. **注释规范**：
    - 方法必须添加注释且方法级注释使用 Javadoc 格式
    - 计划待完成的任务需要添加 `// TODO` 标记
    - 存在潜在缺陷的逻辑需要添加 `// FIXME` 标记
    - 实体类字段需要注释说明业务含义
    - 枚举值需要注释说明含义
3. **代码格式化**：
    - 使用 IntelliJ IDEA 默认的 Spring Boot 风格
    - 禁止手动修改代码缩进（依赖 IDE 自动格式化）

---

## 九、部署规范
1. **部署规范**：
    - 生产环境需禁用开发工具自动重启功能
    - 敏感信息通过 `application.properties` 外部化配置
    - 使用 `Spring Profiles` 管理环境差异（如 `dev`, `prod`）

---

## 十、扩展性设计规范
1. **接口优先**：
    - 服务层接口（`INegRoomService`）与实现（`NegRoomServiceImpl`）分离
2. **扩展点预留**：
    - 关键业务逻辑需提供策略模式或模板方法模式支持扩展
3. **日志规范**：
    - 使用 `SLF4J` 记录日志（禁止直接使用 `System.out.println`）
    - 核心操作需记录 `INFO` 级别日志，异常记录 `ERROR` 级别