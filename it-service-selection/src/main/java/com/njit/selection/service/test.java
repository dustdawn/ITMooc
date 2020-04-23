package com.njit.selection.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author dustdawn
 * @date 2020/4/23 14:48
 */
public class test {
    public static void main(String[] args) {



        String json = "{'userId':'297e651d719104ba0171910a2de90000','courseId':'297eea2270e646510170e653a3590000'}";
        JSONObject jsonObject = JSONObject.parseObject(json);

        System.out.print(jsonObject.toJSONString());

    }
}
