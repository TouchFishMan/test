package com.telek.hemsipc.service;

import com.telek.hemsipc.model.Device;
import com.telek.hemsipc.model.Dl645ReadingProtocol;

/**
 * @Auther: wll
 * @Date: 2018/9/14 14:11
 * @Description:
 */
public interface ICollectionService {
    String readCollectionData(Device device, Dl645ReadingProtocol readingProtocol);


    void readAddress();
}
