package com.framework.plugin.web.common.server.sequences;

import com.framework.plugin.web.common.server.sequences.inf.SequenceInf;

public class NumberSequence implements SequenceInf {

    public String currval;

    @Override
    public String nextval() {

        Long currvalLog = Long.parseLong(currval) + 1;
        currval = currvalLog.toString();
        return currvalLog.toString();
    }

    @Override
    public String currval() {
        return currval;
    }

    public void setCurrval(String currval) {
        this.currval = currval;
    }

    @Override
    public void init() {

    }

}
