package com.andrioussolutions.ftp;

import com.google.api.services.youtube.model.Video;

import com.andrioussolutions.ui.SaveCancel;
import com.andrioussolutions.ui.btnLoader;
import com.andrioussolutions.utils.ErrorHandler;
import com.andrioussolutions.utils.Utils;
import com.andrioussolutions.utils.ini;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.text.JTextComponent;
/**
 * Copyright (C) 2018  Andrious Solutions Ltd.
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
 * Created  02 Jan 2018
 */
public class ftpUploader extends btnLoader{

    private String mContent = "";

    private String mContent2 = "";

    private String contentFile = "app\\src\\main\\java\\com\\andrioussolutions\\content.txt";

    private String contentFile2 = "app\\src\\main\\java\\com\\andrioussolutions\\content2.txt";

    private String mFormURL = "";
    
    private String mPHPFile = "";

//    private String PHPFile = "app\\src\\main\\java\\com\\andrioussolutions\\phpfile.txt";



    
    public ftpUploader(String title){
        super(title);
    }




    public boolean upload(Video video, String newFile){

        if(video == null || newFile == null || newFile.trim().isEmpty() || video.getId() == null || video.getId().trim().isEmpty()) return false;

        String oldFile = "app\\src\\main\\java\\com\\andrioussolutions\\post_template.php";

        Path p = Paths.get(oldFile);

        String path = oldFile.substring(0, oldFile.lastIndexOf(p.getFileName().toString()));

//        String newFile = setPhpFileName(file);

        String phpFile = path + getBaseName() + ".php";

        boolean upload = findReplace(oldFile, phpFile, "$youtubeID = ''", "$youtubeID = '" + video.getId() + "'");

        if(!upload) return false;

        if(!mFormURL.trim().isEmpty()){

            findReplace(phpFile, phpFile, "$formURL = ''", "$formURL = '" + mFormURL + "'");
        }

//        if(!mDescription.trim().isEmpty()){ //(null != description){

        String description = video.getSnippet().getDescription();

        if( null == description) description = "";

        if(!description.isEmpty()){

            findReplace(phpFile, phpFile, "$metaDescription = ''",
                    "$metaDescription = '" + parseDescription(description) + "'");
        }

        if(!mContent.trim().isEmpty()){

            mContent = mContent.replaceAll("appSettings", getBaseName());

            upload = findReplace(phpFile, phpFile, "$content1 = ''",
                    "$content1 = '" + mContent + "'");

            if (!upload) return false;
        }

        if(!mContent2.trim().isEmpty()){

            mContent2 = mContent2.replaceAll("appSettings", getBaseName());

            upload = findReplace(phpFile, phpFile, "$content2 = ''",
                    "$content2 = '" + mContent2 + "'");

            if (!upload) return false;
        }

        String keywords = "";

        if(null != video.getSnippet().getTags()){

            for(String word : video.getSnippet().getTags()){

                keywords += Utils.addSlashes(word) + ", ";
            }
        }

        if(!keywords.trim().isEmpty()){

            findReplace(phpFile, phpFile, "$metaKeywords = ''", "$metaKeywords = '" + keywords + "'");
        }

        ftpHelper ftp = new ftpHelper("files.000webhost.com:21", "andrioussolutions", "Gtfpweb1");

        upload = ftp.upload(phpFile, "/public_html/posts/" + newFile);

        File uploadFile = new File(phpFile);

        if(!uploadFile.delete()){

        }
        return upload;
    }




    private static boolean findReplace(String oldFile, String newFile, String find, String replace){

        boolean findReplace = false;

        try{

            List<String> newLines = new ArrayList<>();

            for (String line : Files.readAllLines(Paths.get(oldFile), StandardCharsets.UTF_8)){

                if (!findReplace && line.contains(find)){

                    findReplace = true;

                    newLines.add(line.replace(find, replace));
                }else{

                    newLines.add(line);
                }
            }

            Files.write(Paths.get(newFile), newLines, StandardCharsets.UTF_8);

        }catch(Exception ex){

            findReplace = false;

            ErrorHandler.log(ex);
        }

        return findReplace;
    }




    public String getPHPFile(){

        mPHPFile = ini.get("PHP", "file");

        return mPHPFile;
    }




    public String setPHPFile(String file){

        if(file == null || file.trim().isEmpty()){

            file = "";
        }

        mPHPFile = file.trim();

        ini.set("PHP", "file", mPHPFile); // Utils.writeFile(PHPFile, file);

        return file;
    }


    

    public String getFormURL(){

        return mFormURL;
    }




    public String setFormURL(String url){

        if(url == null || url.trim().isEmpty()){

            url = "";
        }

        mFormURL = url.trim();

        return url;
    }



    public JComponent SaveCancel(JTextComponent comp){

        JPanel panel = new JPanel();

//        panel.setBorder(BCnt.set());

        SaveCancel scBtns = new SaveCancel(comp, save, cancel);

        panel.add(scBtns.saveBtn());

        panel.add(scBtns.cancelBtn());

        return panel;
    }


    

    public JComponent SaveCancel2(JTextComponent comp){

        JPanel panel = new JPanel();

        SaveCancel scBtns = new SaveCancel(comp, save2, cancel2);

        panel.add(scBtns.saveBtn());

        panel.add(scBtns.cancelBtn());

        return panel;
    }




    public String getContent(){

        return cancel.cancel();
    }




    public String getContent2(){

        return cancel2.cancel();
    }



    
    private SaveCancel.CancelFld cancel = () ->{

        mContent = Utils.readFile(contentFile);

        return mContent;
    };




    private SaveCancel.CancelFld cancel2 = () ->{

        mContent2 = Utils.readFile(contentFile2);

        return mContent2;
    };




    public void setContent(String content){

        save.save(content);
    }




    private SaveCancel.SaveFld save = (String content) ->{

        if(content == null || content.trim().isEmpty()){

            content = "";
        }

        mContent = content.trim();

        return Utils.writeFile(contentFile, content);
    };


    

    public void setContent2(String content){

        save2.save(content);
    }




    private SaveCancel.SaveFld save2 = (String content) ->{

        if(content == null || content.trim().isEmpty()){

            content = "";
        }

        mContent2 = content.trim();

        return Utils.writeFile(contentFile2, content);
    };



    
    private String parseDescription(String description){

        String timeStamp;

        String regex = "\\⏩\\s*\\d+:\\d\\d\\s+";

        boolean stamps = false;

        Pattern p = Pattern.compile(regex);

        String lines[] = description.split("\\r?\\n");

        StringBuilder content = new StringBuilder() ;

        StringBuilder descrip = new StringBuilder() ;

        for(String line: lines){

            if(line.indexOf("\uD83D\uDC4D") > 0){

                break;
            }
            
            Matcher m = p.matcher(line);

            if (m.find()){

                if(!stamps){

                    stamps = true;

                    content.append("<ul style=\"list-style-type:none;line-height: 1.5;font-size: small;\">").append('\n');
                }

                timeStamp = m.group(0).replace("⏩", "").trim();

                int colon = timeStamp.indexOf(':');

                timeStamp = String.format("%02d", Integer.parseInt(timeStamp.substring(0, colon))) + ':' + timeStamp.substring(colon+1, timeStamp.length());

                line = "<li>⏩ <a href=\"#\" onclick=\"videoTimestamp(this);return false;\">" + timeStamp + "</a> " + line.replace(m.group(0), "").trim() + "</li>";

                content.append(line).append('\n');

                continue;
            }else{

                if(stamps){

                    stamps = false;

                    content.append("</ul>").append('\n');

                    continue;
                }
            }

            descrip.append(Utils.addSlashes(line)).append('\n');
        }

        if(content.length() > 0){

            mContent = content.toString() + mContent;
        }

       return descrip.toString().trim();
    }

}
