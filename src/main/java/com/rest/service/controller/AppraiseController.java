package com.rest.service.controller;

import com.rest.service.AppraiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.text.ParseException;

@RestController
public class AppraiseController {

    @Autowired
    private AppraiseService appraiseService;
    @PostMapping("/insertAppraise")
    @ResponseBody
    public String insertAppraise(String json) throws ParseException {
        return appraiseService.insertAppraise(json);
    }

    // 回复留言
    @PostMapping("/replyAppraise")
    @ResponseBody
    public String replyAppraise(String json) throws ParseException{
        return appraiseService.replyAppraise(json);
    }
}
