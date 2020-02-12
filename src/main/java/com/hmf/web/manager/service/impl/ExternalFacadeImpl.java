package com.hmf.web.manager.service.impl;

import com.hmf.web.utils.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Component
@Path("/province")
public class ExternalFacadeImpl {
    Logger log = LoggerFactory.getLogger(ExternalFacadeImpl.class);

    /**
     * http://localhost:8080/pingdian/province/city/1
     * @param id
     * @return
     */
    @Path("/city/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResult<Boolean> getCity(@PathParam("id") Long id) {
        log.info("springboot1.5.7 整合RESTeasy 成功了");
        ApiResult<Boolean> result = new ApiResult<>();
        result.setData(true);
        return result;
    }
}