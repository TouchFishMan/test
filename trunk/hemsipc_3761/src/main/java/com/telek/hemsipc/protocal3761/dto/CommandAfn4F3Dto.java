package com.telek.hemsipc.protocal3761.dto;

import com.telek.hemsipc.http.Pojo;
import lombok.Data;

/**
 * @author wangxb
 * @date 20-1-7 上午10:41
 */
@Data
public class CommandAfn4F3Dto {

    @Pojo(empty = false, maxLength = 16)
    private String mainIp;

    @Pojo(type = Integer.class, empty = false, maxValue = 65535)
    private Integer mainPort;

    @Pojo(empty = false, maxLength = 16)
    private String subIp;

    @Pojo(type = Integer.class, empty = false, maxValue = 65535)
    private Integer subPort;

    @Pojo(empty = false, maxLength = 16)
    private String APN = "CMNET";
}
