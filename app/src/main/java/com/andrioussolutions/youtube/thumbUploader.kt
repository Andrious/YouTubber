package com.andrioussolutions.youtube

import com.google.api.services.youtube.model.ThumbnailDetails

import com.andrioussolutions.utils.ini

import java.io.File
import java.util.HashSet

import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter

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
 * Created  29 Jan 2018
 */
class thumbUploader : JButton() {

    private var mFile: File? = null

    private val mSelectListeners = HashSet<SelectListener>()

    private var saveText: String? = null

    private var saveListener: SelectListener? = null


    var image: String?
        get() = ini["Youtube", "image"]
        set(imageFile) {
            var imageFile = imageFile

            if (null == imageFile || imageFile.trim { it <= ' ' }.isEmpty()) {

                imageFile = ""
            }

            ini["Youtube", "image"] = imageFile
        }

    init {

        mFile = File(".")

        this.text = "Upload Thumbnail"

        this.addActionListener { e -> pickThumbnail() }
    }


    fun pickThumbnail(): File {

        val chooser = JFileChooser(ini["YouTubber", "homedir"])

        chooser.fileSelectionMode = JFileChooser.FILES_ONLY
        //
        // disable the "All files" option.
        //
        chooser.isAcceptAllFileFilterUsed = false

        chooser.fileFilter = FileNameExtensionFilter("Image files (*.jpg, *.png)", "jpg", "png")

        if (chooser.showOpenDialog(this.parent) == JFileChooser.APPROVE_OPTION) {

            mFile = chooser.selectedFile

            for (listener in mSelectListeners) {

                listener.selected(this)
            }
        } else {

            mFile = File(".")
        }

        return mFile
    }


    fun save() {

        if (saveText == null) {

            saveText = text

            if (mSelectListeners.size > 0) saveListener = mSelectListeners.iterator().next()
        }
    }


    fun restore() {

        if (saveText != null) {

            text = saveText

            saveText = null

            if (saveListener != null) {

                removeSelectListener()

                addSelectListener(saveListener)
            }
        }
    }


    fun addSelectListener(listener: SelectListener) {

        mSelectListeners.add(listener)
    }


    fun selectedFile(): File? {

        return mFile
    }


    fun upload(id: String?, file: File?): ThumbnailDetails {

        if (id == null || id.trim { it <= ' ' }.isEmpty()) return ThumbnailDetails()

        return if (file == null) ThumbnailDetails() else youtube.uploadThumbnail(id, file)

    }


    fun removeSelectListener(): SelectListener {

        val listener = mSelectListeners.iterator().next()

        mSelectListeners.clear()

        return listener
    }


    interface SelectListener {

        fun selected(loader: thumbUploader)
    }
}
