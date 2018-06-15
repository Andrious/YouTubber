package com.andrioussolutions.utils

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

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
 * Created  13 Jan 2018
 */
object Utils {


    fun readFile(file: String): String {

        val f = File(file)

        if (!f.exists() || f.isDirectory) return ""

        val mContent = StringBuilder()

        try {

            for (line in Files.readAllLines(Paths.get(file), StandardCharsets.UTF_8)) {

                mContent.append(line).append('\n')
            }
        } catch (ex: Exception) {

            mContent.append("")

            ErrorHandler.log(ex)
        }

        return mContent.toString()
    }


    fun writeFile(fileName: String, content: String): Boolean {

        var write = false

        var outFile: BufferedWriter? = null

        val file = File(fileName) ?: return write

        try {

            outFile = BufferedWriter(FileWriter(file))

            outFile.write(content)

            write = true

        } catch (ex: Exception) {

            write = false

            ErrorHandler.log(ex)
        } finally {

            try {

                outFile!!.close()

            } catch (ex: Exception) {

                ErrorHandler.log(ex)
            }

        }

        return write
    }


    fun addSlashes(s: String): String {
        var s = s
        s = s.replace("\\\\".toRegex(), "\\\\\\\\")
        s = s.replace("\\n".toRegex(), "\\\\n")
        s = s.replace("\\r".toRegex(), "\\\\r")
        s = s.replace("\\00".toRegex(), "\\\\0")
        s = s.replace("'".toRegex(), "\\\\'")
        return s
    }
}
