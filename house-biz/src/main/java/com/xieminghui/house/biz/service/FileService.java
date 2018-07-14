package com.xieminghui.house.biz.service;


import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Service
public class FileService {

    @Value("${file.path}")
    private String filePath;

    public List<String> getImgPaths(List<MultipartFile> files){
        List<String> paths = Lists.newArrayList();
        files.forEach(file ->{
            File localFile = null;
            if(!file.isEmpty()){
                try {
                    //本地文件的根路径 也就是图片全路径
                    localFile = saveToLocal(file,filePath);

                    // 把第一个字符串  按照第二个分割,留下后面的
                    // Users/xieminghui/100/imgs  Users/xieminghui -> /100/imgs
                    String path = StringUtils.substringAfterLast(localFile.getAbsolutePath(),filePath);
                    paths.add(path);
                } catch (Exception e){
                    throw new IllegalArgumentException(e);
                }
            }
        });
        return paths;
    }


    private File saveToLocal(MultipartFile file, String filePath) throws IOException {
        //文件路径最终为 filePath 是绝对路径  + 时间  +  加上文件原始名字
        File newFile = new File(filePath + "/" + Instant.now().getEpochSecond() +"/"+file.getOriginalFilename());
        if (!newFile.exists()) {
            newFile.getParentFile().mkdirs();
            newFile.createNewFile();
        }
        Files.write(file.getBytes(), newFile);
        return newFile;
    }
}
