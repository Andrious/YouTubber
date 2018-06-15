package com.andrioussolutions.ui

import com.andrioussolutions.utils.ErrorHandler

import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.text.SimpleDateFormat
import java.util.HashSet

import javax.swing.*

/**
 * Copyright (C) 2018  Greg T. F. Perry
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
open class btnLoader() : JButton() {

    private val mClickListeners = HashSet<OnClickListener>()

    var mDescription = ""


    val baseName: String
        get() = mBaseName


    open val phpFile: String
        get() = mPHPFile


    init {

        this.addActionListener { e ->

            for (listener in mClickListeners) {

                listener.onClick(e)
            }
        }
    }


    constructor(label: String) : this() {

        this.text = label
    }


    fun addOnClickListener(listener: OnClickListener) {

        mClickListeners.add(listener)
    }


    fun setPhpFileName(file: File): String {

        val fileName = file.name

        mBaseName = fileName.substring(0, fileName.lastIndexOf(".")).replace(" ".toRegex(), "_")

        try {

            val attr = Files.readAttributes<BasicFileAttributes>(file.toPath(), BasicFileAttributes::class.java)

            mPHPFile = SimpleDateFormat("YYYY/MM").format(attr.creationTime().toMillis()) + '/' + mBaseName + ".php"

        } catch (ex: Exception) {

            mPHPFile = mBaseName + ".php"

            ErrorHandler.log(ex)
        }

        return mPHPFile
    }


    fun setDescription(description: String?) {

        if (description != null && !description.trim { it <= ' ' }.isEmpty()) {

            mDescription = description.trim { it <= ' ' }
        }
    }

    companion object {

        private var mBaseName = ""

        private var mPHPFile = ""
    }
}

