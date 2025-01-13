package com.xjzai1.usercenter_backend.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class TeamDeleteRequest implements Serializable {

    private static final long serialVersionUID = 4376774187380720687L;
    /**
     * teamid
     */
    private Integer teamId;
}
