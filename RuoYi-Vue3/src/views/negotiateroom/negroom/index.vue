<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="房间名称" prop="roomName">
        <el-input
          v-model="queryParams.roomName"
          placeholder="请输入房间名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备信息" prop="equipment">
        <el-input
          v-model="queryParams.equipment"
          placeholder="请输入设备信息"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in oa_negroom_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
          v-hasPermi="['negotiateroom:negroom:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['negotiateroom:negroom:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['negotiateroom:negroom:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['negotiateroom:negroom:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="negroomList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center">
        <template #default="scope">
          <span>{{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="房间名称" align="center" prop="roomName" />
      <el-table-column label="位置" align="center" prop="location" />
      <el-table-column label="容纳人数" align="center" prop="capacity" />
      <el-table-column label="设备信息" align="center" prop="equipment" />
      <el-table-column label="缓冲时间" align="center" prop="bufferTime" />
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="oa_negroom_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="显示顺序" align="center" prop="orderNum" />
      <el-table-column label="更新人" align="center" prop="updateBy" />
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['negotiateroom:negroom:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['negotiateroom:negroom:remove']">删除</el-button>
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

    <!-- 添加或修改洽谈室管理对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="negroomRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="房间名称" prop="roomName">
          <el-input v-model="form.roomName" placeholder="请输入房间名称" />
        </el-form-item>
        <el-form-item label="位置" prop="location">
          <el-input v-model="form.location" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="容纳人数" prop="capacity">
          <el-input v-model="form.capacity" placeholder="请输入容纳人数" />
        </el-form-item>
        <el-form-item label="设备信息" prop="equipment">
          <el-input v-model="form.equipment" placeholder="请输入设备信息" />
        </el-form-item>
        <el-form-item label="缓冲时间" prop="bufferTime">
          <el-time-picker
            v-model="form.bufferTime"
            value-format="HH:mm"
            format="HH:mm"
            placeholder="请选择缓冲时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option
              v-for="dict in oa_negroom_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="显示顺序" prop="orderNum">
          <el-input v-model="form.orderNum" placeholder="请输入显示顺序" />
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

<script setup name="Negroom">
import { listNegroom, getNegroom, delNegroom, addNegroom, updateNegroom } from "@/api/negotiateroom/negroom"
import useUserStore from '@/store/modules/user'

const { proxy } = getCurrentInstance()
const { oa_negroom_status } = proxy.useDict('oa_negroom_status')
const userStore = useUserStore()

const negroomList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    roomName: null,
    location: null,
    equipment: null,
    status: null,
  },
  rules: {
    roomName: [
      { required: true, message: "房间名称不能为空", trigger: "blur" }
    ],
    capacity: [
      { required: true, message: "容纳人数不能为空", trigger: "blur" }
    ],
    bufferTime: [
      { required: true, message: "缓冲时间不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "状态不能为空", trigger: "change" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询洽谈室管理列表 */
function getList() {
  loading.value = true
  listNegroom(queryParams.value).then(response => {
    negroomList.value = response.rows
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
    roomId: null,
    roomName: null,
    location: null,
    capacity: null,
    equipment: null,
    bufferTime: "00:10",
    status: null,
    orderNum: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  }
  proxy.resetForm("negroomRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.roomId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加洽谈室管理"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _roomId = row.roomId || ids.value
  getNegroom(_roomId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改洽谈室管理"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["negroomRef"].validate(valid => {
    if (valid) {
      if (form.value.roomId != null) {
        updateNegroom(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addNegroom(form.value).then(response => {
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
  const _roomIds = row.roomId || ids.value
  proxy.$modal.confirm('是否确认删除洽谈室管理编号为"' + _roomIds + '"的数据项？').then(function() {
    return delNegroom(_roomIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('negotiateroom/negroom/export', {
    ...queryParams.value
  }, `negroom_${new Date().getTime()}.xlsx`)
}

getList()
</script>
