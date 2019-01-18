package org.qingfox.framework.test.nd;

import java.util.Calendar;
import org.apache.commons.lang3.time.DateUtils;

import org.qingfox.framework.common.utils.DateUtil;

public class Test2 {
    public static void main(String[] args) {
        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(1546051548649l);
        System.out.println(DateUtil.format(cl));
    }
}
