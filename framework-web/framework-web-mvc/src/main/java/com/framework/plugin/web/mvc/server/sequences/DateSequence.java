package com.framework.plugin.web.common.server.sequences;

import com.framework.common.utils.DateUtil;
import com.framework.plugin.web.common.server.sequences.inf.SequenceInf;

public class DateSequence implements SequenceInf {

    public String currval;

    @Override
    public void init() {
        currval = DateUtil.getNowDate();
    }

    @Override
    public String nextval() {
        String nowDate = DateUtil.getNowDate();
        currval = nowDate;
        return nowDate;
    }

    @Override
    public String currval() {
        return currval;
    }

}
