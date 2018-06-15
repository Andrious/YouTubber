package com.andrioussolutions.blogger

import com.google.api.services.blogger.model.Post

import com.andrioussolutions.ui.btnLoader
import com.andrioussolutions.utils.ini

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
class bloggerUploader(title: String) : btnLoader(title) {

    private var mBlogFile = ""

    //    private String BlogFile = "app\\src\\main\\java\\com\\andrioussolutions\\blogfile.txt";

    private val mBlogId: String


    // Utils.readFile(BlogFile);
    val blogFile: String
        get() {

            mBlogFile = ini.get("Blogger", "file")

            return mBlogFile
        }


    init {

        mBlogId = ini.get("Blogger", "id")
    }


    fun setBlogFile(url: String?): String {
        var url = url

        if (url == null || url.trim { it <= ' ' }.isEmpty()) {

            url = ""
        }

        mBlogFile = url.trim { it <= ' ' }

        //        Utils.writeFile(BlogFile, url);

        ini.set("Blogger", "file", mBlogFile)

        return url
    }


    fun post(title: String, phpFile: String): Post {

        val blog = Blogger(mBlogId)

        blog.setTitle(title)

        //        String phpFile = setPhpFileName(file);

        val mContent = "<object height=\"1000\" width=\"100%\" type=\"text/html\" data=\"http://andrioussolutions.000webhostapp.com/posts/" + phpFile.trim { it <= ' ' } + "\">\n\n   <p style=\"font: 50px \'Open Sans\', sans-serif;color: #555;margin: 0 0 20px;line-height: 1.5;\">\n      Down for maintenance.\n   </p>\n   <p style=\"font: 14px \'Open Sans\', sans-serif;color: #555;margin: 0 0 20px;line-height: 0.5;align: center\">\n      We are performing scheduled maintenance. We should be back online shortly.\n   </p>\n</object>\n<small id=\"disclaimer\">\n Andrious Solutions Ltd. will not liable for any loss or damages incurred seemingly as a result of using the information conveyed in the videos in any way.\n</small>\n<br />\n<div align=\"right\" style=\"position: relative;right: 60px;\">\n  <object height=\"80\" width=\"80\" type=\"text/html\" data=\"http://andrioussolutions.000webhostapp.com/posts/lordrunaricon.php\"></object>\n</div>\n<a href=\"http://www.andrioussolutions.com/p/video-topic-suggestion.html\" title=\"Suggest Videos\" style=\"position: relative;right: -5%;bottom: -10px\" target=_blank>\n    <img src=\"http://andrioussolutions.000webhostapp.com/img/Suggestion_Box.png\" height=\"50\" width=\"50\">\n</a>\n<a href=\"http://www.andrioussolutions.com/p/schedule.html\" title=\"Subjects Schedule\" style=\"position: relative;left: 80%;bottom: -10px\" target=_blank>\n    <img src=\"http://andrioussolutions.000webhostapp.com/img/Calendar.png\" height=\"40\" width=\"40\">\n</a>"

        blog.setContent(mContent)

//        if (post != null){
        //
        //            // Now we can navigate the response.
        //            System.out.println("Title: " + post.getTitle());
        //            System.out.println("Author: " + post.getAuthor().getDisplayName());
        //            System.out.println("Published: " + post.getPublished());
        //            System.out.println("URL: " + post.getUrl());
        //            System.out.println("Content: " + post.getContent());
        //        }

        return blog.post()
    }

}
