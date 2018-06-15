package com.andrioussolutions.blogger;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.blogger.BloggerScopes;
import com.google.api.services.blogger.model.Post;

import com.andrioussolutions.utils.Auth;
import com.andrioussolutions.utils.ErrorHandler;

import java.io.IOException;
import java.util.Arrays;
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
 * Created  28 Dec 2017
 */
public class Blogger{

    private String mBlogID = "";

    private Credential mCredential;

    private com.google.api.services.blogger.Blogger mBlogger;

    private Post mPost;




    public Blogger(String blogID){

        mBlogID = blogID;

        mPost = new Post();

        try{

            mCredential = Auth.authorize(Arrays.asList(BloggerScopes.BLOGGER), "Blogger-PostsInsert");

        }catch (IOException ex){

            mCredential = null;
        }

        if (mCredential == null) return;

        mBlogger = new com.google.api.services.blogger.Blogger.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, mCredential)
                .setApplicationName("Blogger-PostsInsert-Snippet/1.0")
                .build();
    }




    public String getBlogID(){

        return mBlogID;
    }




    public Post getPost(){

        return mPost;
    }




    public com.google.api.services.blogger.Blogger getBlogger(){

       return mBlogger;
    }




    public Credential getCredential(){

        return mCredential;
    }




    public Post setTitle(String title){

        if(null == title || title.isEmpty()){

            title = "New Blog";
        }else{

            title = title.trim();
        }

        mPost.setTitle(title);

        return mPost;
    }




    public Post setContent(String content){

        if(null == content || content.isEmpty()){

            content = "No content.";
        }else{

            content = content.trim();
        }

        mPost.setContent(content);

        return mPost;
    }




    public Post post(){

        if (mBlogger == null) return new Post();

        Post post;

        try{

            // The request action.
            com.google.api.services.blogger.Blogger.Posts.Insert insertPost = mBlogger.posts()
                    .insert(mBlogID, mPost);

            // Restrict the result content to just the data we need.
//            insertPost.setFields("author/displayName,content,published,title,url");
            insertPost.setFields("content,published,title,url");

            // This step sends the request to the server.
            post = insertPost.execute();

        }catch (IOException ex){

            post = new Post();

            ErrorHandler.log(ex);
        }

        return post;
    }
}
