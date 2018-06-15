package com.andrioussolutions.youtube

import com.google.api.services.youtube.model.ThumbnailDetails
import com.google.api.services.youtube.model.Video
import java.io.File
import java.util.*

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
 * Created  31 Dec 2017
 */
object youtube {

    private val youtube = YouTubeAPI()

    private var mDescription = ""

    private var mTitle = ""

    @JvmStatic
    fun upload(videoFile: String): Video {

        val title = if (mTitle.trim { it <= ' ' }.isEmpty()) "\uD83D\uDCF1 Decode Android" else mTitle

        var description = "⏩\n" +
                "\n" +
                "\uD83D\uDD17\n" +
                mDescription + "\n" +
                "\n" +
                "This video is part of the Sample Framework series. A sample framework offered to developers to build their Android applications. Take the software and change it to meet your particular needs.\n" +
                "\n" +
                "                                                                                                                   Please, give a thumbs up. \uD83D\uDC4D\n" +
                "\n" +
                "\uD83D\uDD38 Note:\n" +
                "\n" +
                "\uD83C\uDFB5 Music: \n" +
                "\n" +
                "\uD83C\uDFA8 Theme images by LordRunar on istockphoto.com\n" +
                "\n" +
                "More videos at #andrioussolutions"

        description = if (mDescription.trim { it <= ' ' }.isEmpty()) description else mDescription

        youtube.setDescription(description).setTitle(title).setPrivacyStatus("unlisted")

        return youtube.upload(videoFile)
    }

    @JvmStatic
    fun setTitle(title: String?): YouTubeAPI {

        if (title == null || title.trim { it <= ' ' }.isEmpty()) {

            mTitle = ""
        } else {

            mTitle = title.trim { it <= ' ' }
        }

        return youtube
    }

    @JvmStatic
    fun setDescription(description: String?): YouTubeAPI {

        if (description == null || description.trim { it <= ' ' }.isEmpty()) {

            mDescription = ""
        } else {

            mDescription = description.trim { it <= ' ' }
        }

        return youtube
    }

    @JvmStatic
    fun setKeyWords(keywords: String?): YouTubeAPI {

        if (keywords != null && !keywords.isEmpty()) {

            val tokenizer = StringTokenizer(keywords)

            while (tokenizer.hasMoreTokens()) {

                youtube.tag(tokenizer.nextToken())
            }
        }

        return youtube
    }

    @JvmStatic
    operator fun get(id: String): Video {

        return youtube.get(id)
    }

    @JvmStatic
    fun update(video: Video): Video {

        return youtube.update(video)
    }

    @JvmStatic
    fun uploadThumbnail(id: String, file: File): ThumbnailDetails {

        return youtube.uploadThumbnail(id, file)
    }
}
