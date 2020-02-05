package com.telek.hemsipc.protocal3761.controller;

import com.google.gson.reflect.TypeToken;
import com.telek.hemsipc.http.Pojo;
import com.telek.hemsipc.protocal3761.dto.CommandAfn4F33Dto;
import com.telek.hemsipc.util.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangxb
 * @date 20-1-8 下午2:42
 */
public class BaseController {
    public String valid(Object bean) {
        Field[] fileds = bean.getClass().getDeclaredFields();
        String errorMsg = null;
        try {
            for (Field field : fileds) {
                if (field.isAnnotationPresent(Pojo.class)) {
                    field.setAccessible(true);
                    Pojo annotation = field.getAnnotation(Pojo.class);
                    int maxLength = annotation.maxLength();
                    int minLength = annotation.minLength();
                    int maxValue = annotation.maxValue();
                    int minValue = annotation.minValue();
                    boolean empty = annotation.empty();
                    Object value = field.get(bean);
                    Class fieldType = field.getType();
                    if (fieldType == String.class) {
                        if (!empty) {
                            if (value == null || "".equals(value.toString().trim())) {
                                errorMsg = field.getName() + "不能为空！";
                            } else if (maxLength > 0 && value.toString().length() > maxLength) {
                                errorMsg = field.getName() + "超长";
                            } else if (minLength > 0 && value.toString().length() < minLength) {
                                errorMsg = field.getName() + "过短";
                            }
                        }
                    } else if (fieldType == Integer.class) {
                        if (!empty) {
                            if (value == null || (int) value == 0) {
                                errorMsg = field.getName() + "不能为空！";
                            } else if (maxValue > 0 && (int) value > maxValue) {
                                errorMsg = field.getName() + "值过大";
                            } else if (minValue > 0 && (int) value < minValue) {
                                errorMsg = field.getName() + "值过小";
                            }
                        }
                    }
                    if (!StringUtil.isBlank(errorMsg)) {
                        return errorMsg;
                    }
                }
            }
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            errorMsg = "接口错误";
            return errorMsg;
        }
        return null;
    }

    protected <T> List<T> resolveListParams(Object parameter, Class<T> beanClass) throws Exception {
//        T obj = beanClass.newInstance();
        TypeToken<ArrayList<CommandAfn4F33Dto>> typeToken1 = new TypeToken<ArrayList<CommandAfn4F33Dto>>() {
        };
        Type type1 = typeToken1.getType();
        List<CommandAfn4F33Dto> parameterList1 = StringUtil.strToBeans(
                StringUtil.toString(parameter), typeToken1);

        TypeToken<ArrayList<T>> typeToken = new TypeToken<ArrayList<T>>() {
        };
        Type type = typeToken.getType();
        List<T> parameterList = StringUtil.strToBeans(
                StringUtil.toString(parameter), typeToken);
        return parameterList;
    }

    protected <T> T resolveParams(Object parameter, Class<T> beanClass) throws Exception {
        T params = (T) StringUtil.strToBean(
                StringUtil.toString(parameter), beanClass);
        return params;
    }

}
