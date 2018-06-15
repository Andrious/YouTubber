package com.andrioussolutions.drive

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
 * Created  24 Jan 2018
 */
class formUploader
//    private String formIdFile = "app\\src\\main\\java\\com\\andrioussolutions\\formid.txt";


(title: String) : btnLoader(title) {

    private var mFormURL = ""


    //Utils.readFile(formIdFile);
    val formURL: String
        get() {

            mFormURL = ini.get("Form", "url")

            return mFormURL
        }


    fun setFormURL(url: String?): String {
        var url = url

        if (url == null || url.trim { it <= ' ' }.isEmpty()) {

            url = ""
        }

        mFormURL = url.trim { it <= ' ' }

        ini.set("Form", "url", mFormURL)//Utils.writeFile(formIdFile, url);

        return url
    }
}
