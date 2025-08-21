import request from '@/utils/request'

// 查询洽谈室管理列表
export function listNegroom(query) {
  return request({
    url: '/negotiateroom/negroom/list',
    method: 'get',
    params: query
  })
}

// 查询洽谈室管理详细
export function getNegroom(roomId) {
  return request({
    url: '/negotiateroom/negroom/' + roomId,
    method: 'get'
  })
}

// 新增洽谈室管理
export function addNegroom(data) {
  return request({
    url: '/negotiateroom/negroom',
    method: 'post',
    data: data
  })
}

// 修改洽谈室管理
export function updateNegroom(data) {
  return request({
    url: '/negotiateroom/negroom',
    method: 'put',
    data: data
  })
}

// 删除洽谈室管理
export function delNegroom(roomId) {
  return request({
    url: '/negotiateroom/negroom/' + roomId,
    method: 'delete'
  })
}
