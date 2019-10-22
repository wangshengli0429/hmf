package com.hmf.web.app.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: CrtPayVo
 * @Version: 1.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CrtPayVo implements Serializable{
    private static final long serialVersionUID = 1L;
    private String codeUrl;
    private String tradeState;
}
