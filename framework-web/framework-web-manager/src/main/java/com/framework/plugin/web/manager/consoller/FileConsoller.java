package com.framework.plugin.web.system.consoller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.framework.common.utils.DateUtil;
import com.framework.common.utils.FileUtil;
import com.framework.common.utils.RandomUtil;
import com.framework.plugin.web.common.bean.ConditionBean;
import com.framework.plugin.web.common.server.consoller.basic.BasicController;
import com.framework.plugin.web.common.server.enums.ExceptionEnum;
import com.framework.plugin.web.common.server.exception.ServiceException;
import com.framework.plugin.web.common.server.request.CRUDRequest;
import com.framework.plugin.web.common.server.response.ServiceResponse;
import com.framework.plugin.web.common.server.service.CRUDService;
import com.framework.plugin.web.common.web.spring.factory.SpringFactory;
import com.framework.plugin.web.system.bean.AccountBean;
import com.framework.plugin.web.system.caches.AccountCache;
import com.framework.plugin.web.system.caches.DictCache;

@RestController
@RequestMapping("/system/file")
public class FileConsoller extends BasicController {

    @Autowired
    private CRUDService crudService;

    @Autowired
    private SpringFactory springFactory;

    @Autowired
    private AccountCache accountCache;

    @Autowired
    private DictCache dictCache;

    public final static String PATH_CODE = "SERVER_FILE_CONFIG_PATH";

    @RequestMapping("/doDownload")
    public void doDownload(HttpServletResponse hResponse,
            HttpServletRequest hRequest, HttpSession hSession, String id)
            throws IOException {

        AccountBean bean = accountCache.get((String) hSession
                .getAttribute("tokenId"));
        if (bean == null) {
            throw new ServiceException(ExceptionEnum.LOGINUNKNOW,
                    "/system/login/index.jsp");
        }

        if (StringUtils.isEmpty(id)) {
            throw new ServiceException(ExceptionEnum.FILEUNKNOW);
        }

        CRUDRequest request = new CRUDRequest();
        request.setTable("T_SYSTEM_FILE");
        ConditionBean condition = new ConditionBean();
        condition.setName("ID");
        condition.setValue(id);
        request.addCondition(condition);

        List<Map<String, String>> list = crudService.search(request);

        if (list == null || list.isEmpty()) {
            throw new ServiceException(ExceptionEnum.FILEUNKNOW);
        }

        Map<String, String> fileMap = list.get(0);
        String filepath = fileMap.get("FILEPATH");
        String filename = fileMap.get("FILENAME");
        String filetype = fileMap.get("FILETYPE");
        String filesrcname = fileMap.get("FILESRCNAME");
        File file = new File(FileUtil.pathSeparator(dictCache.get(PATH_CODE)
                .getValue())
                + FileUtil.pathSeparator(filepath)
                + FileUtil.pathSeparator(filename));

        if (!file.exists()) {
            throw new ServiceException(ExceptionEnum.FILEUNKNOW);
        }

        filesrcname = URLEncoder.encode(filesrcname, "UTF-8");

        hResponse.setContentType(filetype);
        hResponse.addHeader("Content-Disposition", "img;filename="
                + filesrcname);

        OutputStream os = null;
        FileInputStream fis = null;
        try {
            os = hResponse.getOutputStream();
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int i = 0;

            while ((i = fis.read(b)) > 0) {
                os.write(b, 0, i);
            }
            os.flush();
        } catch (Exception e) {
            throw new ServiceException(ExceptionEnum.FILEDOWNLOADERROR);
        } finally {
            if (fis != null) {
                fis.close();
                fis = null;
            }
            if (os != null) {
                os.close();
                os = null;
            }
        }

    }

    @RequestMapping("/doUpload")
    public void doUpload(HttpServletResponse hResponse,
            MultipartRequest mRequest, HttpSession hSession) {
        AccountBean bean = accountCache.get((String) hSession
                .getAttribute("tokenId"));
        if (bean == null) {
            throw new ServiceException(ExceptionEnum.LOGINUNKNOW,
                    "/system/login/index.jsp");
        }

        ServiceResponse<Map<String, String>> sResponse = this
                .getServiceResponse();
        
        Map<String, MultipartFile> fileMap = mRequest.getFileMap();
        for (MultipartFile file : fileMap.values()) {
            String filename = RandomUtil.getUUID();
            String filesrcname = file.getOriginalFilename();
            String filesuffix = FilenameUtils.getExtension(filesrcname);
            String filetype = file.getContentType();
            long filesize = file.getSize();
            String adddate = DateUtil.getNowDate();
            String status = "2";

            String filepath = FileUtil.getDatePath(DateUtil.DAY);
            String fullfilepath = FileUtil.pathSeparator(dictCache.get(
                    PATH_CODE).getValue())
                    + FileUtil
                            .pathSeparator(FileUtil.getDatePath(DateUtil.DAY));
            LinkedHashMap<String, String> fileData = new LinkedHashMap<String, String>();

            fileData.put("FILENAME", filename);
            fileData.put("FILESRCNAME", filesrcname);
            fileData.put("FILESUFFIX", filesuffix);
            fileData.put("FILETYPE", filetype);
            fileData.put("FILESIZE", String.valueOf(filesize));
            fileData.put("ADDDATE", adddate);
            fileData.put("STATUS", status);
            fileData.put("FILEPATH", filepath);
            fileData.put("ACCOUNTID", bean.getId());
            fileData.put("FULLFILEPATH", fullfilepath);

            File objFile = new File(FileUtil.pathSeparator(fullfilepath)
                    + FileUtil.pathSeparator(filename));

            if (!objFile.getParentFile().exists()) {
                objFile.getParentFile().mkdirs();
            }

            try {
                file.transferTo(objFile);
                CRUDRequest request = new CRUDRequest();
                request.setTable("T_SYSTEM_FILE");
                request.addData(fileData);
                List<Map<String, String>> resultList = crudService.add(request);
                sResponse.setResult(resultList.get(0));
                sResponse.setSuccess(true);
            } catch (Exception e) {
                sResponse.setSuccess(false);
            }

        }

        sResponse.print(hResponse, "text/html;charset=utf-8");

    }
}
