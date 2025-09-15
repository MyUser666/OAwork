package com.intoa.negotiate.domain.dto;

import com.intoa.negotiate.domain.NegRoom;

import java.io.Serializable;

/**
 * 房间可用性DTO
 * <p>
 * 用于表示房间在特定时间段内的可用性状态，包括房间基本信息和是否可用的标识。
 * </p>
 * 
 * @author intoa
 * @date 2025-09-12
 */
public class RoomAvailabilityDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 房间基本信息
     */
    private NegRoom room;

    /**
     * 是否可用
     */
    private Boolean available;

    /**
     * 不可用原因
     */
    private String unavailableReason;

    public RoomAvailabilityDTO() {
    }

    public RoomAvailabilityDTO(NegRoom room, Boolean available, String unavailableReason) {
        this.room = room;
        this.available = available;
        this.unavailableReason = unavailableReason;
    }

    public NegRoom getRoom() {
        return room;
    }

    public void setRoom(NegRoom room) {
        this.room = room;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getUnavailableReason() {
        return unavailableReason;
    }

    public void setUnavailableReason(String unavailableReason) {
        this.unavailableReason = unavailableReason;
    }
}