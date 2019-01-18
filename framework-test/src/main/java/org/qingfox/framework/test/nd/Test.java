package org.qingfox.framework.test.nd;

import java.util.Calendar;
import java.util.Scanner;

import  org.qingfox.framework.common.utils.DateUtil;

public class Test {

    public static long DIFFTIME = 8 * 60 * 60 * 1000;
    public static String DATESTR = "14:07 18:00 18:47";

    public static void main(String[] args) {
//        Scanner scan = new Scanner(System.in);
        String[] dateStrs = DATESTR.split(" ");
        Calendar cl1 = Calendar.getInstance();
        cl1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dateStrs[0].split(":")[0]));
        cl1.set(Calendar.MINUTE, Integer.parseInt(dateStrs[0].split(":")[1]));

        Calendar cl2 = Calendar.getInstance();
        cl2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dateStrs[1].split(":")[0]));
        cl2.set(Calendar.MINUTE, Integer.parseInt(dateStrs[1].split(":")[1]));

        Calendar cl3 = Calendar.getInstance();
        cl3.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dateStrs[2].split(":")[0]));
        cl3.set(Calendar.MINUTE, Integer.parseInt(dateStrs[2].split(":")[1]));

        Calendar cl4 = Calendar.getInstance();
        cl4.setTimeInMillis((cl3.getTimeInMillis() + (DIFFTIME - (cl2.getTimeInMillis() - cl1.getTimeInMillis()))) + 2 * 60 * 1000);

        System.out.println(DateUtil.format(cl4));

    }
}
