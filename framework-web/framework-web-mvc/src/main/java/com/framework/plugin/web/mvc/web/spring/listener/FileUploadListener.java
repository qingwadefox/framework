package com.framework.plugin.web.common.web.spring.listener;

import org.apache.commons.fileupload.ProgressListener;

public class FileUploadListener implements ProgressListener {

    @Override
    public void update(long arg0, long arg1, int arg2) {
        
        System.out.println(arg0);
        System.out.println(arg1);
        System.out.println(arg2);

    }

}
