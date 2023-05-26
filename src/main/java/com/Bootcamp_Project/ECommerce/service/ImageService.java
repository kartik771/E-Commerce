package com.Bootcamp_Project.ECommerce.service;

import com.Bootcamp_Project.ECommerce.entities.user.User;
import com.Bootcamp_Project.ECommerce.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${project.image.download}")
    String basePath;
    @Autowired
    private UserRepository userRepository;
    public String uploadImage(String path , MultipartFile file , Long id) throws IOException {

        String name = file.getOriginalFilename();

        String fileName1=id+(name.substring(name.lastIndexOf(".")));

        String filePath = path + File.separator + fileName1;



        File newFile = new File(path);
        if(!newFile.exists())
        {
            newFile.mkdir();
        }
        Files.copy(file.getInputStream(), Paths.get(filePath));
        return name;

    }

    public InputStream getResource(String path , String fileName) throws FileNotFoundException {
        String fullPath = path + File.separator + fileName;
        InputStream is = new FileInputStream(fullPath);
        return is;
    }

    public String downloadImage(String path,String id){
        File file=new File(path);
        String[] image=file.list();
        for(int i=0;i<image.length;i++){
            String ch=image[i];
            if(ch.split("\\.")[0].equalsIgnoreCase(id)){
                return basePath+path+ch;
            }
        }
        return basePath+path;
    }

}
