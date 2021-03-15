package com.plzhans.assignment.api.controller;


import com.plzhans.assignment.api.service.test.TestService;
import com.plzhans.assignment.api.service.test.datatype.TestDto;
import com.plzhans.assignment.common.controller.ControllerBase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(TestController.PATH)
public class TestController extends ControllerBase {

    public static final String PATH = "/v1/test";

    TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("")
    public List<TestDto> getAllUsers() {
        return testService.getAllTests();
    }
}
