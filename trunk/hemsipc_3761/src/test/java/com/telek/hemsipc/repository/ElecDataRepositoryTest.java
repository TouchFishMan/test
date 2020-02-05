package com.telek.hemsipc.repository;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telek.hemsipc.HemsipcApplicationTests;
import com.telek.hemsipc.model.ElecData;

/**
 * @Auther: wll
 * @Date: 2018/9/12 16:49
 * @Description:
 */
@Component
public class ElecDataRepositoryTest extends HemsipcApplicationTests {
    @Autowired
    private ICommonDAO commonDAO;

    @Test
    public void insertOne() {
        ElecData elecData = new ElecData("deviceId","201809121650");
        commonDAO.save(elecData);
    }

    @Test
    public void queryAll() {
        List<ElecData> list = (List<ElecData>)commonDAO.findAllByClass(ElecData.class);
        System.out.println(list.get(0));
        Assert.assertNotSame(list.size(), 0);
    }

    @Test
    public void queryByDeviceIdAndTime() {
		/*
		 * insertOne(); ElecData elecData =
		 * elecDataRepository.queryByDeviceIdAndTime("deviceId", "201809121650");
		 * Assert.assertNotNull(elecData);
		 */
    }
}