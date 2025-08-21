import request from '@/utils/request'

// 查询茶水管理列表
export function listNegtea(query) {
  return request({
    url: '/negotiatetea/negtea/list',
    method: 'get',
    params: query
  })
}

// 查询茶水管理详细
export function getNegtea(teaId) {
  return request({
    url: '/negotiatetea/negtea/' + teaId,
    method: 'get'
  })
}

// 新增茶水管理
export function addNegtea(data) {
  return request({
    url: '/negotiatetea/negtea',
    method: 'post',
    data: data
  })
}

// 修改茶水管理
export function updateNegtea(data) {
  return request({
    url: '/negotiatetea/negtea',
    method: 'put',
    data: data
  })
}

// 删除茶水管理
export function delNegtea(teaId) {
  return request({
    url: '/negotiatetea/negtea/' + teaId,
    method: 'delete'
  })
}
