package com.andrioussolutions.utils

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
 * Created  28 Jan 2018
 */
class ini private constructor() {




    init {

        iniFile = IniProperties()

        val file = "app\\src\\main\\java\\com\\andrioussolutions\\YouTubber.ini"

        try {

            iniFile!!.load(file)

        } catch (ex: Exception) {

            iniFile = null

            ErrorHandler.log(ex)
        }

    }

    companion object {

        private var iniFile: IniProperties? = null

        private var mThis: ini? = null


        private val instance: Boolean
            get() {

                if (mThis == null) {

                    mThis = ini()
                }

                return iniFile != null
            }


        fun iniFile(): IniProperties? {

            instance

            return iniFile
        }


        operator fun get(property: String): String {

            if (!instance) return ""

            val value = iniFile!!.getProperty(property)

            return value ?: ""
        }


        operator fun get(section: String, property: String): String {

            if (!instance) return ""

            val value = iniFile!!.getProperty(section, property)

            return value ?: ""
        }


        operator fun set(name: String, value: String): Boolean {

            if (!instance) return false

            iniFile!!.setProperty(name, value)

            return true
        }


        operator fun set(section: String, name: String, value: String): Boolean {

            if (!instance) return false

            iniFile!!.setProperty(section, name, value)

            return true
        }
    }
}
