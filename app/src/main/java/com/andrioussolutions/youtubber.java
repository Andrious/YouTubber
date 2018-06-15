package com.andrioussolutions;

import com.google.api.services.blogger.model.Post;
import com.google.api.services.youtube.model.ThumbnailDetails;
import com.google.api.services.youtube.model.Video;

import com.andrioussolutions.blogger.bloggerUploader;
import com.andrioussolutions.drive.GoogleDrive;
import com.andrioussolutions.drive.formUploader;
import com.andrioussolutions.ftp.ftpUploader;
import com.andrioussolutions.ui.uiLoader;
import com.andrioussolutions.utils.ErrorHandler;
import com.andrioussolutions.youtube.thumbUploader;
import com.andrioussolutions.youtube.videoUploader;
import com.andrioussolutions.youtube.youtube;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

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
 * Created  28 Dec 2017
 */
public class youtubber{

    static private boolean mPHPClicked = false;

    static private boolean mBloggerClicked = false;

    static private GoogleDrive mDrive;




    public static void main(String args[]){

        ErrorHandler.init();

        final uiLoader ui = new uiLoader("Uploader");

        ui.frame().getContentPane().setLayout(new BoxLayout (ui.frame().getContentPane(), BoxLayout.Y_AXIS));
//---------------------------------------------------------------------
        // Description Field
        ui.add(new JPanel());

        ui.add(new JLabel("Des:"));

        final JTextArea desArea = new JTextArea(10, 40);

        // If text doesn't fit on a line, jump to the next
        desArea.setLineWrap(true);

        // Makes sure that words stay intact if a line wrap occurs
        desArea.setWrapStyleWord(true);

        ui.add(new JScrollPane(desArea));

        // Upload Video Button
        final videoUploader videoBtn = new videoUploader();
        
        ui.addPanel(videoBtn.SaveCancel(desArea));

        desArea.setText(videoBtn.getDescription());
//---------------------------------------------------------------------
        ui.add(new JPanel());

        ui.add(new JLabel("Keywords:"));

        final JTextField keyField = new JTextField(52);

        ui.add(keyField);

        keyField.setText(videoBtn.getKeywords());
//---------------------------------------------------------------------
        ui.add(new JPanel());

        ui.add(videoBtn);

        ui.add(new JLabel("ID:"));

        final JTextField idField = new JTextField(7);

        idField.setText(videoBtn.getID());

        ui.add(idField);

        final thumbUploader thumbBtn = new thumbUploader();

        ui.add(thumbBtn);

        ui.add(new JLabel("Thumb:"));

        final JTextField thumbField = new JTextField(25);

        thumbField.setText(thumbBtn.getImage());

        ui.add(thumbField);

        Enable enableThumb = () ->  !idField.getText().trim().isEmpty() &&
                thumbField.getText().trim().isEmpty();

        thumbBtn.setEnabled(enableThumb.isEnabled());

        thumbField.setInputVerifier(new InputVerifier(){
            @Override
            public boolean verify(JComponent input){

                thumbBtn.setEnabled(enableThumb.isEnabled());

                return true;
            }
        });
//---------------------------------------------------------------------
        ui.add(new JPanel());

        ui.add(new JLabel("Vid:"));

        final JTextField vidField = new JTextField(40);

        ui.add(vidField);

        vidField.setText(videoBtn.getVideo());
//---------------------------------------------------------------------

        ui.add(new JLabel("Title:"));

        final JTextField titleField = new JTextField(15);

        titleField.setText(videoBtn.getTitle());

        ui.add(titleField);
//---------------------------------------------------------------------
        // Content1 Field
        ui.add(new JPanel());

        ui.add(new JLabel("Con:"));

        final JTextArea conArea = new JTextArea(5, 40);

        // If text doesn't fit on a line, jump to the next
        conArea.setLineWrap(true);

        // Makes sure that words stay intact if a line wrap occurs
        conArea.setWrapStyleWord(true);

        ui.add(new JScrollPane(conArea));

        // Upload PHP Button
        final ftpUploader ftpBtn = new ftpUploader("Upload PHP");

        ui.addPanel(ftpBtn.SaveCancel(conArea));

        conArea.setText(ftpBtn.getContent());
//---------------------------------------------------------------------
        // Content1 Field
        ui.add(new JPanel());

        ui.add(new JLabel("Con2:"));

        final JTextArea con2Area = new JTextArea(5, 40);

        // If text doesn't fit on a line, jump to the next
        con2Area.setLineWrap(true);

        // Makes sure that words stay intact if a line wrap occurs
        con2Area.setWrapStyleWord(true);

        ui.add(new JScrollPane(con2Area));

        ui.addPanel(ftpBtn.SaveCancel2(con2Area));

        con2Area.setText(ftpBtn.getContent2());

        Enable enableVideo = () ->  idField.getText().trim().isEmpty() &&
                !desArea.getText().trim().isEmpty() &&
                !keyField.getText().trim().isEmpty()&&
                !conArea.getText().trim().isEmpty() &&
                !titleField.getText().trim().isEmpty();

        videoBtn.setEnabled(enableVideo.isEnabled());

        idField.setInputVerifier(new InputVerifier(){
            @Override
            public boolean verify(JComponent input){

                videoBtn.setEnabled(enableVideo.isEnabled());

                if(idField.getText().trim().isEmpty()) vidField.setText("");

                return true;
            }
        });

        desArea.setInputVerifier(new InputVerifier(){
            @Override
            public boolean verify(JComponent input){

                videoBtn.setEnabled(enableVideo.isEnabled());

                ftpBtn.setEnabled(!desArea.getText().trim().isEmpty());

                return true;
            }
        });

        keyField.setInputVerifier(new InputVerifier(){
            @Override
            public boolean verify(JComponent input){

                videoBtn.setEnabled(enableVideo.isEnabled());

                ftpBtn.setEnabled(!keyField.getText().trim().isEmpty());

                return true;
            }
        });

        conArea.setInputVerifier(new InputVerifier(){
            @Override
            public boolean verify(JComponent input){

                videoBtn.setEnabled(enableVideo.isEnabled());

                ftpBtn.setEnabled(!conArea.getText().trim().isEmpty());

                return true;
            }
        });
//---------------------------------------------------------------------
        ui.add(new JPanel());

        ui.add(ftpBtn);

        ui.add(new JLabel("PHP:"));

        final JTextField phpField = new JTextField(15);

        phpField.setText(ftpBtn.getPHPFile());

        Enable enableFTP = () -> phpField.getText().trim().isEmpty()&&
                                !desArea.getText().trim().isEmpty() &&
                                !keyField.getText().trim().isEmpty()&&
                                !conArea.getText().trim().isEmpty();

        ftpBtn.setEnabled(enableFTP.isEnabled());

        phpField.setInputVerifier(new InputVerifier(){
            @Override
            public boolean verify(JComponent input){

                ftpBtn.setEnabled(enableFTP.isEnabled());

                return true;
            }
        });

        ui.add(phpField);

        // Upload Blogger Button
        final bloggerUploader blogBtn = new bloggerUploader("Upload Blogger");

        blogBtn.setEnabled(!titleField.getText().trim().isEmpty());

        ui.add(blogBtn);

        ui.add(new JLabel("Blog:"));

        final JTextField blogField = new JTextField(15);

        blogField.setText(blogBtn.getBlogFile());

        ui.add(blogField);

        Enable enableBlog = () -> blogField.getText().trim().isEmpty();

        blogBtn.setEnabled(enableBlog.isEnabled());

        blogField.setInputVerifier(new InputVerifier(){
            @Override
            public boolean verify(JComponent input){

                blogBtn.setEnabled(enableBlog.isEnabled());

                return true;
            }
        });
//---------------------------------------------------------------------
        ui.add(new JPanel());

        // Upload Form Button
        final formUploader frmBtn = new formUploader("Upload Form");

        ui.add(frmBtn);

        ui.add(new JLabel("Form:"));

        final JTextField formField = new JTextField(25);

        formField.setText(frmBtn.getFormURL());

        formField.setInputVerifier(new InputVerifier(){
            @Override
            public boolean verify(JComponent input){

                frmBtn.setEnabled(formField.getText().trim().isEmpty());

                return true;
            }
        });

        Enable enableForm = () -> formField.getText().trim().isEmpty() &&
                !titleField.getText().trim().isEmpty();

        frmBtn.setEnabled(enableForm.isEnabled());

        ui.add(formField);

        titleField.setInputVerifier(new InputVerifier(){
            @Override
            public boolean verify(JComponent input){

                videoBtn.setEnabled(enableVideo.isEnabled());

                frmBtn.setEnabled(enableForm.isEnabled());

                blogBtn.setEnabled(!titleField.getText().trim().isEmpty());

                return true;
            }
        });
//---------------------------------------------------------------------
        ui.add(new JPanel());

        ui.add(new JLabel("Err:"));

        // Error messages displayed here.
        final JTextArea msgArea = new JTextArea(5, 40);

        // If text doesn't fit on a line, jump to the next
        msgArea.setLineWrap(true);

        // Makes sure that words stay intact if a line wrap occurs
        msgArea.setWrapStyleWord(true);

        msgArea.setBackground(Color.lightGray);

        msgArea.setEditable(false);

        ui.add(new JScrollPane(msgArea));




        videoBtn.addSelectListener((loader)->{

                File file = loader.selectedFile();

                // A file was not created.
                if(file.getName().equals(".")) return;

                String vidFile = file.getPath();

                vidField.setText(vidFile);

                videoBtn.setVideo(vidFile);

//                phpField.setText(ftpBtn.setPhpFileName(file));

                videoBtn.setDescription(desArea.getText());

                videoBtn.setKeywords(keyField.getText());

                videoBtn.setTitle(titleField.getText());

                Video video = videoBtn.upload(file);

                String id = video.getId();

                idField.setText(id == null ? "": id);

                videoBtn.setEnabled(enableVideo.isEnabled());

                videoBtn.setID(id);

                if(null != id){

                    formField.setText(frmBtn.setFormURL(""));

                    phpField.setText(ftpBtn.setPHPFile(""));

                    blogField.setText(blogBtn.setBlogFile(""));
                }

                String errMsg = ErrorHandler.getMessage();

                msgArea.append(errMsg.trim().isEmpty() ? "" : errMsg + "\n");
        });



        thumbBtn.addSelectListener((loader)-> {

            final String id = idField.getText().trim();

            if(id.isEmpty()){

                String msg = "Enter the video's YouTube ID!";

                JOptionPane.showMessageDialog(ui, msg, "PHP Upload", JOptionPane.ERROR_MESSAGE);

                return;
            }

            File file = loader.selectedFile();

            // A file was not created.
            if(file.getName().equals(".")) return;

            String imgFile = file.getPath();

            ThumbnailDetails thumb = thumbBtn.upload(id, file);

            String url = thumb.getDefault().getUrl();

            if(url != null && !url.trim().isEmpty()){

                thumbField.setText(imgFile);

                thumbBtn.setImage(imgFile);
                
                thumbBtn.setEnabled(enableThumb.isEnabled());
            }
        });




        // A Lambda
        Ftp ftp = (Video vid, String phpFile) -> {

            if(phpFile == null || phpFile.trim().isEmpty()){

                File file = new File(vidField.getText().trim());

                phpFile = ftpBtn.setPhpFileName(file);

                if(phpFile == null || phpFile.trim().isEmpty()) return;
            }

//            ftpBtn.setDescription(desArea.getText());

            // Record the content.
            ftpBtn.setContent(conArea.getText());

            ftpBtn.setContent2(con2Area.getText());

            String url = formField.getText().trim();

            if(!url.isEmpty()){

                ftpBtn.setFormURL(url);
            }else{

//                frm.upload(vid.getSnippet().getTitle());
            }

            if (ftpBtn.upload(vid, phpFile)){

                phpField.setText(ftpBtn.setPHPFile(phpFile));

                ftpBtn.setEnabled(enableFTP.isEnabled());

                msgArea.append(phpFile +" uploaded successfully!\n");
            }

            String errMsg = ErrorHandler.getMessage();

            msgArea.append(errMsg.trim().isEmpty() ? "" : errMsg + "\n");
        };




        // lambda
        Blog blog = (Video video, String title, String phpFile) -> {

//            String phpFile = blogBtn.setPhpFileName(file);
//
//            phpField.setText(phpFile);

            Post post = blogBtn.post(title, phpFile);

            if (null != post.getContent() && !post.getContent().trim().isEmpty()){

               String description = video.getSnippet().getDescription();

               if(null != description){

                   description =   "Questions & Comments: " + post.getUrl() +
                                   "\n(anonymously. Without using your Youtube account)\n\n" +
                                   description;

                   video.getSnippet().setDescription(description);

                   youtube.update(video);
               }

                blogField.setText(blogBtn.setBlogFile(post.getUrl()));

                blogBtn.setEnabled(enableBlog.isEnabled());

                msgArea.append(post.getUrl() + "\n");
            }

            String errMsg = ErrorHandler.getMessage().trim();

            msgArea.append(errMsg.isEmpty() ? "" : errMsg + "\n");
        };



        
        // Upload PHP
        ftpBtn.addOnClickListener(e -> {

                final String id = idField.getText().trim();

                if(id.isEmpty()){

                    String msg = "Enter the video's YouTube ID!";

                    JOptionPane.showMessageDialog(ui, msg, "PHP Upload", JOptionPane.ERROR_MESSAGE);
                }

                if(mPHPClicked){

                    String msg = "You've already clicked to upload!\nSelect the video file you've uploaded!";

                    JOptionPane.showMessageDialog(ui, msg, "PHP Upload", JOptionPane.ERROR_MESSAGE);

                    return;
                }

                if(vidField.getText().trim().isEmpty()){

                    mPHPClicked = true;

                    videoBtn.save();

                    videoBtn.setText("Select Video");

                    videoBtn.removeSelectListener();

                    videoBtn.addSelectListener((loader)->{

                            mPHPClicked = false;

                            videoBtn.restore();

                            File file =  loader.selectedFile();

                            if(!file.getName().equals(".")){

                                String vidFile = file.getPath();

                                vidField.setText(vidFile);

                                videoBtn.setVideo(vidFile);

                                ftp.upload(youtube.get(id), "");
                            }
                    });

                    String msg = "Select the video file you've uploaded!";

                    JOptionPane.showMessageDialog(ui, msg, "PHP Upload", JOptionPane.INFORMATION_MESSAGE);

                    for(ActionListener listener : videoBtn.getActionListeners()){

                        listener.actionPerformed(null);
                    }
                }else{

                    Video vid = youtube.get(id);

                    ftp.upload(vid, "");
                }
        });




        // Upload Blogger
        blogBtn.addOnClickListener(e->{

                final String id = idField.getText().trim();

                if(id.isEmpty()){

                    String msg = "Enter the video's YouTube ID!";

                    JOptionPane.showMessageDialog(ui, msg, "PHP Upload", JOptionPane.ERROR_MESSAGE);
                }

                String title = titleField.getText().trim();

                if(title.isEmpty()){

                    String msg = "Enter a Title!";

                    JOptionPane.showMessageDialog(ui, msg, "Info", JOptionPane.ERROR_MESSAGE);

                    return;
                }

                if(mBloggerClicked){

                    String msg = "You've already clicked to upload!\nSelect the video file you've uploaded!";

                    JOptionPane.showMessageDialog(ui, msg, "Blogger Upload", JOptionPane.ERROR_MESSAGE);

                    return;
                }

                String phpFile = phpField.getText().trim();

                if(phpFile.isEmpty()){

                    String vidFile = vidField.getText().trim();

                    if(vidFile.isEmpty()){

                        mBloggerClicked = true;

                        videoBtn.save();

                        videoBtn.setText("Select Video");

                        videoBtn.removeSelectListener();

                        videoBtn.addSelectListener((loader) -> {

                            mBloggerClicked = false;

                            videoBtn.restore();

                            File file = loader.selectedFile();

                            if (!file.getName().equals(".")){

                                vidField.setText(file.getPath());

                                blog.post(youtube.get(id), titleField.getText().trim(), ftpBtn.setPhpFileName(file));
                            }
                        });

                        String msg = "Select the video file you've uploaded!";

                        JOptionPane.showMessageDialog(ui, msg, "Blogger Upload", JOptionPane.INFORMATION_MESSAGE);

                        for (ActionListener listener : videoBtn.getActionListeners()){

                            listener.actionPerformed(null);
                        }
                    }else{

                        File file = new File(vidFile);

                        vidField.setText(file.getPath());

                        blog.post(youtube.get(id), title, blogBtn.setPhpFileName(file));
                    }
                }else{

                    blog.post(youtube.get(id), title, phpFile);
                }
        });




        // A Lambda
        Form frm =(String title) ->{

            if(null == title || title.trim().isEmpty()) {

                  String msg = "Enter a Title!";

                  JOptionPane.showMessageDialog(ui, msg, "Info", JOptionPane.ERROR_MESSAGE);

                  return;
            }

            GoogleDrive form = getFormId(title);

            if(form.error()){

                msgArea.append(ErrorHandler.getMessage() + "\n");

                return;
            }

            String url = form.getResult();
            
            formField.setText(url);

            ftpBtn.setEnabled(enableFTP.isEnabled());

            if(!ftpBtn.isEnabled()){

                for(ActionListener listener : ftpBtn.getActionListeners()){

                    listener.actionPerformed(null);
                }
            }
        };




        // Upload Form
        frmBtn.addOnClickListener(e ->frm.upload(titleField.getText()));



        
        ui.display();
    }



    
    private static GoogleDrive getFormId(String title){

        if(null == mDrive){

            mDrive = new GoogleDrive("1DH-5AXtiLhGrqzONvaZPslxlzzZNdjuyd5KNW315GDBSkMIKB7WaoWIF")
                    .scope("https://www.googleapis.com/auth/forms");
        }

        if(null == title || title.isEmpty()){

            title = "New Form";
        }

        return mDrive.makeForm(title);
    }




    @FunctionalInterface
    public interface Ftp {

        void upload(Video vid, String phpFile);
    }



    
    @FunctionalInterface
    public interface Form {

        void upload(String title);
    }




    @FunctionalInterface
    public interface Blog {

        void post(Video video, String title, String phpFile);
    }


    @FunctionalInterface
    public interface Enable {

        boolean isEnabled();
    }
}
