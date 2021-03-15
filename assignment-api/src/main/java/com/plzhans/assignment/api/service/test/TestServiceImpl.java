package com.plzhans.assignment.api.service.test;

import com.plzhans.assignment.api.repository.TestRepository;
import com.plzhans.assignment.api.service.test.datatype.TestDto;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestServiceImpl implements TestService {

    TestRepository testRepository;

    public TestServiceImpl(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Override
    public List<TestDto> getAllTests() {
        List<TestDto> list = new ArrayList<>();
        val entities = this.testRepository.findAll();
        entities.forEach(entity->{
            list.add(new TestDto(entity));
        });
        return list;
    }
}
