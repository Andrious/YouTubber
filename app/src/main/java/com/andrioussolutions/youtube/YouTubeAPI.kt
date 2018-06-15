package com.andrioussolutions.youtube

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

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.googleapis.media.MediaHttpUploader
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener
import com.google.api.client.http.InputStreamContent
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.ThumbnailDetails
import com.google.api.services.youtube.model.ThumbnailSetResponse
import com.google.api.services.youtube.model.Video
import com.google.api.services.youtube.model.VideoListResponse
import com.google.api.services.youtube.model.VideoSnippet
import com.google.api.services.youtube.model.VideoStatus
import com.google.common.collect.Lists

import com.andrioussolutions.utils.Auth
import com.andrioussolutions.utils.ErrorHandler

import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.ArrayList


/**
 * Upload a video to the authenticated user's channel. Use OAuth 2.0 to
 * authorize the request. Note that you must add your video files to the
 * project folder to upload them with this application.
 *
 * @author Jeremy Walker
 */
class YouTubeAPI {


    private var mPrivacyStatus = "private"

    // Set the keyword tags that you want to associate with the video.
    private val mTags = ArrayList<String>()

    private var mDirectUpload = false

    private var mTitle = "Title"

    private var mDescription = "Description"

    private var mUploadState = "...not started."


    val uploadState: String
        get() {

            val state = mUploadState

            mUploadState = ""

            return state
        }


    private fun startupYouTube(): Boolean {

        if (mStartup) return mYouTube != null

        mStartup = true

        try {

            // Authorize the request.
            val credential = Auth.authorize(mScopes, "uploadvideo")

            // This object is used to make YouTube Data API requests.
            mYouTube = YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential).setApplicationName(
                    "youtube-uploadvideo").build()

        } catch (ex: Exception) {

            mYouTube = null

            ErrorHandler.log(ex)
        }

        return mYouTube != null
    }


    fun setPrivacyStatus(status: String): YouTubeAPI {

        mPrivacyStatus = status

        return this
    }


    fun tag(tag: String): YouTubeAPI {

        mTags.add(tag)

        return this
    }


    fun setDirectUploadEnabled(enable: Boolean): YouTubeAPI {

        mDirectUpload = enable

        return this
    }


    fun setTitle(title: String): YouTubeAPI {

        mTitle = title

        return this
    }


    fun setDescription(description: String): YouTubeAPI {

        mDescription = description

        return this
    }


    operator fun get(videoId: String): Video {

        if (!startupYouTube()) return Video()

        var video: Video

        try {

            // Call the YouTube Data API's videos.list method to retrieve videos.
            val videoListResponse = mYouTube!!.videos().list("snippet,localizations").setId(videoId).execute()

            val videoList = videoListResponse.items

            if (videoList.isEmpty()) {

                video = Video()
            } else {

                video = videoList[0]
            }
        } catch (ex: Exception) {

            video = Video()

            ErrorHandler.log(ex)
        }

        return video
    }


    /**
     * Upload the user-selected video to the user's YouTube channel. The code
     * looks for the video in the application's project folder and uses OAuth
     * 2.0 to authorize the API request.
     *
     */
    fun upload(videoFile: String): Video {

        if (!startupYouTube()) return Video()

        var returnedVideo: Video

        try {

            //            InputStreamContent mediaContent = new InputStreamContent(VIDEO_FILE_FORMAT,
            //                    YouTubeAPI.class.getResourceAsStream(videoFile));

            val mediaContent = InputStreamContent(VIDEO_FILE_FORMAT,
                    FileInputStream(videoFile))

            // Add extra information to the video before uploading.
            val video = Video()

            // Set the video to be publicly visible. This is the default
            // setting. Other supporting settings are "unlisted" and "private."
            val status = VideoStatus()

            status.privacyStatus = mPrivacyStatus

            video.status = status

            // Most of the video's metadata is set on the VideoSnippet object.
            val snippet = VideoSnippet()

            snippet.title = mTitle

            snippet.description = mDescription

            // Set the keyword tags that you want to associate with the video.
            if (mTags.size > 0) {

                snippet.tags = mTags
            }

            // Add the completed snippet object to the video resource.
            video.snippet = snippet

            // Insert the video. The command sends three arguments. The first
            // specifies which information the API request is setting and which
            // information the API response should return. The second argument
            // is the video resource that contains metadata about the new video.
            // The third argument is the actual video content.
            val videoInsert = mYouTube!!.videos()
                    .insert("snippet,statistics,status", video, mediaContent)

            // Set the upload type and add an event listener.
            val uploader = videoInsert.mediaHttpUploader

            // Indicate whether direct media upload is enabled. A value of
            // "True" indicates that direct media upload is enabled and that
            // the entire media content will be uploaded in a single request.
            // A value of "False," which is the default, indicates that the
            // request will use the resumable media upload protocol, which
            // supports the ability to resume an upload operation after a
            // network interruption or other transmission failure, saving
            // time and bandwidth in the event of network failures.
            uploader.isDirectUploadEnabled = mDirectUpload

            val progressListener = MediaHttpUploaderProgressListener { uploader ->
                when (uploader.uploadState) {
                    MediaHttpUploader.UploadState.INITIATION_STARTED ->

                        mUploadState = "Initiation Started"

                    MediaHttpUploader.UploadState.INITIATION_COMPLETE ->

                        mUploadState = "Initiation Completed"

                    MediaHttpUploader.UploadState.MEDIA_IN_PROGRESS ->

                        //                            mUploadState = "Upload in progress: " + uploader.getProgress();
                        mUploadState = "Upload in progress: " + uploader.numBytesUploaded

                    MediaHttpUploader.UploadState.MEDIA_COMPLETE ->

                        mUploadState = "Upload Completed!"

                    MediaHttpUploader.UploadState.NOT_STARTED ->

                        mUploadState = "Upload Not Started!"
                }
            }

            uploader.progressListener = progressListener

            // Call the API and upload the video.
            returnedVideo = videoInsert.execute()

            //            // Print data about the newly inserted video from the API response.
            //            System.out.println("\n================== Returned Video ==================\n");
            //            System.out.println("  - Id: " + returnedVideo.getId());
            //            System.out.println("  - Title: " + returnedVideo.getSnippet().getTitle());
            //            System.out.println("  - Tags: " + returnedVideo.getSnippet().getTags());
            //            System.out.println("  - Privacy Status: " + returnedVideo.getStatus().getPrivacyStatus());
            //            System.out.println("  - Video Count: " + returnedVideo.getStatistics().getViewCount());

        } catch (ex: GoogleJsonResponseException) {

            returnedVideo = Video()

            System.err.println("GoogleJsonResponseException code: " + ex.details.code + " : "
                    + ex.details.message)

            ErrorHandler.log(ex)

        } catch (ex: Exception) {

            returnedVideo = Video()

            ErrorHandler.log(ex)
        }

        return returnedVideo
    }


    fun update(video: Video): Video {

        if (!startupYouTube()) return video

        try {

            mYouTube!!.videos().update("snippet", video).execute()

        } catch (ex: Exception) {

            ErrorHandler.log(ex)
        }

        return video
    }


    fun uploadThumbnail(videoId: String, file: File): ThumbnailDetails {

        if (!startupYouTube()) return ThumbnailDetails()

        var input: FileInputStream?

        try {

            input = FileInputStream(file)

        } catch (ex: Exception) {

            input = null
        }

        if (input == null) return ThumbnailDetails()

        // Create an object that contains the thumbnail image file's contents.
        val mediaContent = InputStreamContent(IMAGE_FILE_FORMAT, BufferedInputStream(input))

        mediaContent.length = file.length()

        // Create an API request that specifies that the mediaContent
        // object is the thumbnail of the specified video.
        var thumbnailSet: YouTube.Thumbnails.Set?

        try {

            thumbnailSet = mYouTube!!.thumbnails().set(videoId, mediaContent)

        } catch (ex: Exception) {

            thumbnailSet = null
        }

        if (thumbnailSet == null) return ThumbnailDetails()

        // Set the upload type and add an event listener.
        val uploader = thumbnailSet.mediaHttpUploader

        // Indicate whether direct media upload is enabled. A value of
        // "True" indicates that direct media upload is enabled and that
        // the entire media content will be uploaded in a single request.
        // A value of "False," which is the default, indicates that the
        // request will use the resumable media upload protocol, which
        // supports the ability to resume an upload operation after a
        // network interruption or other transmission failure, saving
        // time and bandwidth in the event of network failures.
        uploader.isDirectUploadEnabled = false

        // Set the upload state for the thumbnail image.
        val progressListener = MediaHttpUploaderProgressListener { uploader ->
            when (uploader.uploadState) {
            // This value is set before the initiation request is
            // sent.
                MediaHttpUploader.UploadState.INITIATION_STARTED ->

                    mUploadState = "Initiation Started"

            // This value is set after the initiation request
            //  completes.
                MediaHttpUploader.UploadState.INITIATION_COMPLETE ->

                    mUploadState = "Initiation Completed"

            // This value is set after a media file chunk is
            // uploaded.
                MediaHttpUploader.UploadState.MEDIA_IN_PROGRESS ->

                    //                       mUploadState = "Upload in progress";
                    mUploadState = "Upload in progress: " + uploader.numBytesUploaded

            // This value is set after the entire media file has
            //  been successfully uploaded.
                MediaHttpUploader.UploadState.MEDIA_COMPLETE ->

                    mUploadState = "Upload Completed!"

            // This value indicates that the upload process has
            //  not started yet.
                MediaHttpUploader.UploadState.NOT_STARTED ->

                    mUploadState = "Upload Not Started!"
            }//                        System.out.println("Upload percentage: " + uploader.getProgress());
        }

        uploader.progressListener = progressListener

        // Upload the image and set it as the specified video's thumbnail.
        var setResponse: ThumbnailSetResponse?

        try {

            setResponse = thumbnailSet.execute()

        } catch (ex: Exception) {

            setResponse = null
        }

        return if (setResponse == null) ThumbnailDetails() else setResponse.items[0]

    }

    companion object {

        /**
         * Define a global instance of a Youtube object, which will be used
         * to make YouTube Data API requests.
         */
        private var mYouTube: YouTube? = null

        private var mStartup = false

        /**
         * Define a global variable that specifies the MIME type of the video
         * being uploaded.
         */
        private val VIDEO_FILE_FORMAT = "video/*"

        /**
         * Define a global variable that specifies the MIME type of the image
         * being uploaded.
         */
        private val IMAGE_FILE_FORMAT = "image/png"

        // This OAuth 2.0 access scope allows an application to upload files
        // to the authenticated user's YouTube channel, but doesn't allow
        // other types of access.
        //    private static final List<String> mScopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.upload");
        private val mScopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube")
    }
}
