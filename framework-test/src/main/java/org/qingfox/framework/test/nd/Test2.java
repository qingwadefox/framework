package org.qingfox.framework.test.nd;

import java.util.Calendar;
import org.apache.commons.lang3.time.DateUtils;

import org.qingfox.framework.common.utils.DateUtil;

public class Test2 {
    public static void main(String[] args) {
        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(1547733393329L);
//        cl.set(Calendar.HOUR_OF_DAY, 0);
//        cl.set(Calendar.MINUTE, 0);
//        cl.set(Calendar.SECOND, 0);
        System.out.println(DateUtil.format(cl));
    }
}
