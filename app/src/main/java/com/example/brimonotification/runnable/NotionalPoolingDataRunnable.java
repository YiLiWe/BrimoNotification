package com.example.brimonotification.runnable;

import com.example.brimonotification.bean.NotionalPoolingBean;
import com.example.brimonotification.service.NotionalPoolingAccessibilityService;

import lombok.Data;

//获取归集数据
@Data
public class NotionalPoolingDataRunnable implements Runnable {
    private final NotionalPoolingAccessibilityService service;

    @Override
    public void run() {
        synchronized (service) {
            NotionalPoolingBean poolingBean = new NotionalPoolingBean();
            poolingBean.setAccount("119301023317509");
            poolingBean.setBank("BRI");
            poolingBean.setPayerName("juwendi");
            poolingBean.setAmount("10000");
            service.setPoolingBean(poolingBean);
        }
    }
}
