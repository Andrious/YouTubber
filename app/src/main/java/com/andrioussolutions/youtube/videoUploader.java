package com.andrioussolutions.youtube;

import com.google.api.services.youtube.model.Video;

import com.andrioussolutions.ui.SaveCancel;
import com.andrioussolutions.utils.Utils;
import com.andrioussolutions.utils.ini;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
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
 * Created  01 Jan 2018
 */
public class videoUploader extends JButton{

        private String title;

        private File mFile;

        private Set<SelectListener> mSelectListeners = new HashSet<>();

        private String saveText;

        private SelectListener saveListener;

        private String DescriptionFile = "app\\src\\main\\java\\com\\andrioussolutions\\description.txt";

        private String KeywordsFile = "app\\src\\main\\java\\com\\andrioussolutions\\keywords.txt";

        private String TitleFile = "app\\src\\main\\java\\com\\andrioussolutions\\title.txt";

        private String IDFile = "app\\src\\main\\java\\com\\andrioussolutions\\id.txt";

        private String vidFile = "app\\src\\main\\java\\com\\andrioussolutions\\video.txt";



        public videoUploader() {

            mFile = new File(".");

            this.setText("Upload Video");

            this.addActionListener((e)->pickVideo());
        }




        public void save(){

            if(saveText == null){

                saveText = getText();

                if(mSelectListeners.size() > 0) saveListener = mSelectListeners.iterator().next();
            }
        }




        public void restore(){

            if(saveText != null){

                setText(saveText);

                saveText = null;

                if(saveListener != null) {

                    removeSelectListener();

                    addSelectListener(saveListener);
                }
            }
        }




        public File pickVideo() {

            JFileChooser chooser = new JFileChooser("C:\\Users\\Drawn\\Documents\\My CamStudio Videos");

            chooser.setDialogTitle(title);

            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            //
            // disable the "All files" option.
            //
            chooser.setAcceptAllFileFilterUsed(false);

            chooser.setFileFilter(new FileNameExtensionFilter("Video files (*.avi, *.mp4)", "avi", "mp4"));

            if (chooser.showOpenDialog(this.getParent()) == JFileChooser.APPROVE_OPTION){

                mFile = chooser.getSelectedFile();

                for (SelectListener listener : mSelectListeners){

                    listener.selected(this);
                }
            }else{

                mFile = new File(".");
            }

            return mFile;
        }




        public File selectedFile(){

            return mFile;
        }




    public String getVideo(){

        return ini.get("Youtube", "file");//Utils.readFile(vidFile);
    }




    public void setVideo(String video){

        if(null == video || video.trim().isEmpty()){

            video = "";
        }

        ini.set("Youtube", "file", video);//Utils.writeFile(vidFile, video);
    }




    public String getID(){

        return ini.get("Youtube", "id");//Utils.readFile(IDFile);
    }




    public void setID(String id){

        if(null == id || id.trim().isEmpty()){

            id = "";
        }

        ini.set("Youtube", "id", id);//Utils.writeFile(IDFile, id);
    }

    


    public JComponent SaveCancel(JTextComponent comp){

        JPanel panel = new JPanel();

        SaveCancel scBtns = new SaveCancel(comp, save, cancel);

        panel.add(scBtns.saveBtn());

        panel.add(scBtns.cancelBtn());

        return panel;
    }


    

    public String getDescription(){

       return cancel.cancel();
    }




    private SaveCancel.CancelFld cancel = () -> Utils.readFile(DescriptionFile);




   public boolean setDescription(String description){

       return save.save(description);
   }




    private SaveCancel.SaveFld save = (String description) ->{

        if(description != null && !description.trim().isEmpty()){

            youtube.setDescription(description);
        }else{

            description = "";
        }

        return Utils.writeFile(DescriptionFile, description);
   };


   
   
    public String getKeywords(){

        return ini.get("Keywords", "words");//Utils.readFile(KeywordsFile);
    }




    public boolean setKeywords(String keywords){

        if(keywords != null && !keywords.trim().isEmpty()){

            youtube.setKeyWords(keywords);
        }else{

            keywords = "";
        }

        return ini.set("Keywords", "words", keywords);//Utils.writeFile(KeywordsFile, keywords);
    }




    public String getTitle(){

       return ini.get("Youtube", "title");//Utils.readFile(TitleFile);
    }




    public void setTitle(String title){

        if(title == null || title.trim().isEmpty()){

            title = "";
        }

        youtube.setTitle(title);

        ini.set("Youtube", "title", title);//Utils.writeFile(TitleFile, title);
    }




    public Video upload(File file){

        if(file == null) return new Video();

        return youtube.upload(file.getPath());
    }




    public void addSelectListener(SelectListener listener){

        mSelectListeners.add(listener);
    }




    public SelectListener removeSelectListener(){

        SelectListener listener = mSelectListeners.iterator().next();

        mSelectListeners.clear();

        return listener;
    }




    public interface SelectListener {

        void selected(videoUploader loader);
    }
}

