package org.qingfox.framework.test.nd;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.qingfox.framework.database.enums.SqlClassEnum;
import org.qingfox.framework.test.nd.bean.DatabaseModelBean;

import org.qingfox.framework.common.utils.FileUtil;
import org.qingfox.framework.common.utils.ReadLine;
import org.qingfox.framework.common.utils.StrUtil;

public class DatabaseModelTool {

    private final static String[] ISNULLFALSETEXT = { "否", "F", "f" };

    private Integer indexName = 0;
    private Integer indexType = 1;
    private Integer indexLabel = 2;
    private Integer indexIsnull = 3;
    private Integer indexRemark = 4;

    private List<DatabaseModelBean> databaseModelList;
    private File file;

    public DatabaseModelTool(File file) {
        this.file = file;
        analysisTextFile();
    }

    private void analysisTextFile() {
        databaseModelList = new ArrayList<>();
        try {
            FileUtil.readLine(file, new ReadLine<String>() {
                @Override
                public void nextLine(String arg0, int arg1) {
                    String[] params = arg0.split("\t", -1);
                    DatabaseModelBean dmb = new DatabaseModelBean(params[indexName], analysisType(params[indexType]), params[indexRemark], params[indexLabel], analysisIsNull(params[indexIsnull]));
                    databaseModelList.add(dmb);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean analysisIsNull(String isNullStr) {
        if (StringUtils.isEmpty(isNullStr)) {
            return true;
        }
        if (ArrayUtils.contains(ISNULLFALSETEXT, isNullStr)) {
            return false;
        }
        return true;
    }

    private SqlClassEnum analysisType(String typeStr) {
        if (StringUtils.isEmpty(typeStr)) {
            return null;
        }
        return SqlClassEnum.getEnmu(typeStr);
    }

    public String toWIKIText() {
        StringBuffer wikiText = new StringBuffer();
        wikiText.append("{| cellpadding=\"8\" cellspacing=\"8\" border=\"1\" style=\"border-width: 1px; border-color: rgb(102, 102, 102); width: 800px; font-family: verdana, arial, sans-serif; font-size: 12px; border-collapse: collapse;\" width=\"100%\"");
        wikiText.append("|-\r\n"
                + "| style=\"background: rgb(255, 255, 204); width: 120px;\" align=\"center\" | '''<font face=\"sans-serif, Arial, Verdana, Trebuchet MS\"><span style=\"font-size: 13px;\">参数</span></font>'''\r\n"
                + "| style=\"background: rgb(255, 255, 204); width: 150px;\" align=\"center\" | '''<font face=\"sans-serif, Arial, Verdana, Trebuchet MS\"><span style=\"font-size: 13px;\">说明</span></font>'''\r\n"
                + "| style=\"background: rgb(255, 255, 204); width: 100px;\" align=\"center\" | '''<font face=\"sans-serif, Arial, Verdana, Trebuchet MS\"><span style=\"font-size: 13px;\">类型</span></font>'''\r\n"
                + "| style=\"background: rgb(255, 255, 204); width: 100px;\" align=\"center\" | '''<font face=\"sans-serif, Arial, Verdana, Trebuchet MS\"><span style=\"font-size: 13px;\">必填</span></font>'''\r\n"
                + "| style=\"background: rgb(255, 255, 204); width: 100px;\" align=\"center\" | '''<font face=\"sans-serif, Arial, Verdana, Trebuchet MS\"><span style=\"font-size: 13px;\">默认值</span></font>'''\r\n"
                + "| style=\"background: rgb(255, 255, 204); width: 300px;\" align=\"center\" | '''<font face=\"sans-serif, Arial, Verdana, Trebuchet MS\"><span style=\"font-size: 13px;\">备注</span></font>'''\r\n");
        for (DatabaseModelBean databaseModelBean : databaseModelList) {
            String typeStr = "";
            switch (databaseModelBean.getType()) {
            case DATE:
                typeStr = "Long";
                break;
            case NUMBER:
                typeStr = "int";
                break;
            case STRING:
                typeStr = "String";
                break;

            }
            wikiText.append("|-\r\n" +

                    "| style=\"width: 120px; background-color: rgb(255, 255, 255);\" | " + databaseModelBean.getName() + "\r\n" +

                    "| style=\"width: 150px; background-color: rgb(255, 255, 255);\" | " + databaseModelBean.getLabel() + "\r\n" +

                    "| style=\"width: 100px; text-align: center; background-color: rgb(255, 255, 255);\" | <span style=\"text-align: center;\">" + typeStr + "</span><br/>\r\n" +

                    "| style=\"width: 100px; text-align: center; background-color: rgb(255, 255, 255);\" | " + (databaseModelBean.isNull() ? "否" : "是") + "\r\n" +

                    "| style=\"width: 100px; background-color: rgb(255, 255, 255);\" | <br/>\r\n" +

                    "| style=\"width: 300px; background-color: rgb(255, 255, 255);\" | " + databaseModelBean.getRemark() + "\r\n");
        }

        wikiText.append("|}");
        return wikiText.toString();
    }

    public static void main(String[] args) {
        File file = new File("D://Untitled1.txt");
        DatabaseModelTool tool = new DatabaseModelTool(file);
        System.out.println(tool.toWIKIText());
    }

}
