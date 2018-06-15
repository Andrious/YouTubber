package com.andrioussolutions.ftp;

import com.andrioussolutions.utils.ErrorHandler;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
/**
 * Copyright (C) 2017  Andrious Solutions Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created  30 Dec 2017
 */
public class ftpHelper{

   private static final int BUFFER_SIZE = 4096;

    private static final String FTP_FORMAT = "ftp://%s:%s@%s%s;type=i";

    private String mHost = "www.myserver.com";

    private String mUser = "tom";

    private String mPassword = "secret";

    private String mFilePath = "E:/Work/Project.zip";

    private String mUploadPath = "/MyProjects/archive/Project.zip";




    public ftpHelper(String host, String user, String pass){

        mHost = host;

        mUser = user;

        mPassword = pass;
    }




   public boolean upload(String filePath, String uploadPath){

       boolean upload = true;

       OutputStream output = null;

       FileInputStream input = null;

       try {

          String ftpUrl = String.format(FTP_FORMAT, URLEncoder.encode(mUser, "UTF-8"), URLEncoder.encode(mPassword, "UTF-8"), mHost, uploadPath);

          URL url = new URL(ftpUrl);

          output = url.openConnection().getOutputStream();

          input = new FileInputStream(filePath);

          byte[] buffer = new byte[BUFFER_SIZE];

          int bytesRead = -1;

          while ((bytesRead = input.read(buffer)) != -1) {

             output.write(buffer, 0, bytesRead);
          }

      } catch (Exception ex) {

           upload = false;

           ErrorHandler.log(ex);

      }finally{

           try{

               if (null != input) input.close();
           }catch(Exception ex){}

           try{

               if (null != output) output.close();
           }catch(Exception ex){}
      }

      return upload;
   }
}