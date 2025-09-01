import request from '@/utils/request'

// 查询预约管理列表
export function listNeglog(query) {
  return request({
    url: '/negotiatelog/neglog/list',
    method: 'get',
    params: query
  })
}

// 查询预约管理详细
export function getNeglog(logId) {
  return request({
    url: '/negotiatelog/neglog/' + logId,
    method: 'get'
  })
}

// 新增预约管理
export function addNeglog(data) {
  return request({
    url: '/negotiatelog/neglog',
    method: 'post',
    data: data
  })
}

// 修改预约管理
export function updateNeglog(data) {
  return request({
    url: '/negotiatelog/neglog',
    method: 'put',
    data: data
  })
}

// 删除预约管理
export function delNeglog(logId) {
  return request({
    url: '/negotiatelog/neglog/' + logId,
    method: 'delete'
  })
}

// 新增获取可用房间列表的API
export function getAvailableRooms() {
  return request({
    url: '/negotiatelog/neglog/availableRooms',
    method: 'get'
  })
}

// 新增获取房间详情的API
export function getRoomInfo(roomId) {
  return request({
    url: '/negotiatelog/neglog/roomInfo/' + roomId,
    method: 'get'
  })
}
