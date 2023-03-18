package com.lanqiao.netdisk.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * @description:
 * @author: BAISHUN
 * @date: 2023/3/12
 * @Copyright: 博客：https://www.cnblogs.com/baishun666/
 */
@SpringBootTest
public class UtilsTest {
    @Test
    void test_01() {
        System.out.println(new Date());
        System.out.println(DateUtil.getCurrentTime());
    }
}
