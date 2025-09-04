<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="预约房间" prop="logRoomName">
        <el-select v-model="queryParams.logRoomName" placeholder="请选择预约房间" clearable>
          <el-option
            v-for="dict in oa_negroom_name"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="用户昵称" prop="logNickName">
        <el-input
          v-model="queryParams.logNickName"
          placeholder="请输入用户昵称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="当事人姓名" prop="clientName">
        <el-input
          v-model="queryParams.clientName"
          placeholder="请输入当事人姓名"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开始时间" style="width: 308px">
        <el-date-picker
          v-model="daterangeStartTime"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间" style="width: 308px">
        <el-date-picker
          v-model="daterangeEndTime"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in oa_neglog_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="更新时间" style="width: 308px">
        <el-date-picker
          v-model="daterangeUpdateTime"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['negotiatelog:neglog:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['negotiatelog:neglog:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['negotiatelog:neglog:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['negotiatelog:neglog:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="neglogList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center">
        <template #default="scope">
          <span>{{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预约房间" align="center" prop="logRoomName">
        <template #default="scope">
          <span>{{ getDictLabel(oa_negroom_name, scope.row.logRoomName) }}</span>
          <!-- <dict-tag :options="oa_negroom_name" :value="scope.row.logRoomName"/> -->
        </template>
      </el-table-column>
      <el-table-column label="预约标题" align="center" prop="title" />
      <el-table-column label="用户昵称" align="center" prop="logNickName" />
      <el-table-column label="预约茶水" align="center" prop="logTeaName">
        <template #default="scope">
          <span>{{ getDictLabel(on_negtea_name, scope.row.logTeaName) }}</span>
          <!-- <dict-tag :options="on_negtea_name" :value="scope.row.logTeaName"/> -->
        </template>
      </el-table-column>
      <el-table-column label="预约茶水数量" align="center" prop="logTeaNum" />
      <el-table-column label="当事人姓名" align="center" prop="clientName" />
      <el-table-column label="相关案号/案由" align="center" prop="caseReference" />
      <el-table-column label="开始时间" align="center" prop="startTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="oa_neglog_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['negotiatelog:neglog:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['negotiatelog:neglog:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改预约管理对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="neglogRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="预约房间" prop="logRoomName">
          <el-select v-model="form.logRoomName" placeholder="请选择预约房间">
            <el-option
              v-for="dict in oa_negroom_name"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="预约标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入预约标题" />
        </el-form-item>
        <el-form-item label="用户昵称" prop="logNickName">
          <el-input v-model="form.logNickName" placeholder="请输入用户昵称" />
        </el-form-item>
        <el-form-item label="预约茶水" prop="logTeaName">
          <el-select v-model="form.logTeaName" placeholder="请选择预约茶水">
            <el-option
              v-for="dict in on_negtea_name"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="预约茶水数量" prop="logTeaNum">
          <el-input v-model="form.logTeaNum" placeholder="请输入预约茶水数量" />
        </el-form-item>
        <el-form-item label="当事人姓名" prop="clientName">
          <el-input v-model="form.clientName" placeholder="请输入当事人姓名" />
        </el-form-item>
        <el-form-item label="当事人联系方式" prop="clientContact">
          <el-input v-model="form.clientContact" placeholder="请输入当事人联系方式" />
        </el-form-item>
        <el-form-item label="相关案号/案由" prop="caseReference">
          <el-input v-model="form.caseReference" placeholder="请输入相关案号/案由" />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker clearable
            v-model="form.startTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker clearable
            v-model="form.endTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option
              v-for="dict in oa_neglog_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Neglog">
import { listNeglog, getNeglog, delNeglog, addNeglog, updateNeglog } from "@/api/negotiatelog/neglog"

const { proxy } = getCurrentInstance()
const { oa_neglog_status, on_negtea_name, oa_negroom_name } = proxy.useDict('oa_neglog_status', 'on_negtea_name', 'oa_negroom_name')
// 数据字典映射方法
const getDictLabel = (dictList, value) => {
  const item = dictList.find(dict => dict.value === value)
  return item ? item.label : value
}

const neglogList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const daterangeStartTime = ref([])
const daterangeEndTime = ref([])
const daterangeUpdateTime = ref([])

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    logRoomName: null,
    logNickName: null,
    clientName: null,
    startTime: null,
    endTime: null,
    status: null,
    updateTime: null,
  },
  rules: {
    logRoomName: [
      { required: true, message: "预约房间不能为空", trigger: "change" }
    ],
    logNickName: [
      { required: true, message: "用户昵称不能为空", trigger: "blur" }
    ],
    logTeaName: [
      { required: true, message: "预约茶水不能为空", trigger: "change" }
    ],
    logTeaNum: [
      { required: true, message: "预约茶水数量不能为空", trigger: "blur" }
    ],
    startTime: [
      { required: true, message: "开始时间不能为空", trigger: "blur" }
    ],
    endTime: [
      { required: true, message: "结束时间不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "状态不能为空", trigger: "change" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询预约管理列表 */
function getList() {
  loading.value = true
  queryParams.value.params = {}
  if (null != daterangeStartTime && '' != daterangeStartTime) {
    queryParams.value.params["beginStartTime"] = daterangeStartTime.value[0]
    queryParams.value.params["endStartTime"] = daterangeStartTime.value[1]
  }
  if (null != daterangeEndTime && '' != daterangeEndTime) {
    queryParams.value.params["beginEndTime"] = daterangeEndTime.value[0]
    queryParams.value.params["endEndTime"] = daterangeEndTime.value[1]
  }
  if (null != daterangeUpdateTime && '' != daterangeUpdateTime) {
    queryParams.value.params["beginUpdateTime"] = daterangeUpdateTime.value[0]
    queryParams.value.params["endUpdateTime"] = daterangeUpdateTime.value[1]
  }
  listNeglog(queryParams.value).then(response => {
    neglogList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

// 取消按钮
function cancel() {
  open.value = false
  reset()
}

// 表单重置
function reset() {
  form.value = {
    logId: null,
    logRoomName: null,
    title: null,
    logNickName: null,
    logTeaName: null,
    logTeaNum: null,
    clientName: null,
    clientContact: null,
    caseReference: null,
    startTime: null,
    endTime: null,
    status: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  }
  proxy.resetForm("neglogRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeStartTime.value = []
  daterangeEndTime.value = []
  daterangeUpdateTime.value = []
  proxy.resetForm("queryRef")
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.logId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加预约管理"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _logId = row.logId || ids.value
  getNeglog(_logId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改预约管理"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["neglogRef"].validate(valid => {
    if (valid) {
      if (form.value.logId != null) {
        updateNeglog(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addNeglog(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _logIds = row.logId || ids.value
  proxy.$modal.confirm('是否确认删除预约管理编号为"' + _logIds + '"的数据项？').then(function() {
    return delNeglog(_logIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('negotiatelog/neglog/export', {
    ...queryParams.value
  }, `neglog_${new Date().getTime()}.xlsx`)
}

getList()
</script>
