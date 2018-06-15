package com.andrioussolutions.utils


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
 * Created  29 Dec 2017
 */
class ErrorHandler private constructor() : java.lang.Thread.UncaughtExceptionHandler {


    override fun uncaughtException(thread: Thread, exception: Throwable) {

        // Don't re-enter -- avoid infinite loops if crash-reporting crashes.
        if (mCrashing) return

        mCrashing = true

        val errorMsg = log(exception)

        Utils.writeFile(mLogFile, errorMsg)
    }

    companion object {

        private val mOldHandler = Thread
                .getDefaultUncaughtExceptionHandler()

        // Prevents infinite loops.
        @Volatile private var mCrashing = false

        private var mErrorHandler: ErrorHandler? = null

        private var mErrorMsg = ""

        private val mLogFile = "ErrorLog.txt"


        fun init() {

            Thread.setDefaultUncaughtExceptionHandler(instance)
        }


        val instance: ErrorHandler
            get() {

                if (mErrorHandler == null) {

                    mErrorHandler = ErrorHandler()
                }

                return mErrorHandler
            }


        fun log(message: String?): String {

            val errMsg: String

            if (null == message || message.isEmpty()) {

                errMsg = ""
            } else {

                errMsg = message.trim { it <= ' ' }

                mErrorMsg = errMsg
            }

            return errMsg
        }


        fun log(ex: Throwable): String {

            return getMessage(ex)
        }


        fun getMessage(exception: Throwable): String {

            val msg = exception.message

            mErrorMsg = msg ?: exception.toString()

            return mErrorMsg
        }


        val message: String
            get() {

                val msg = mErrorMsg

                mErrorMsg = ""

                return msg
            }


        private fun getCause(ex: Throwable): Throwable {

            var cause: Throwable?

            var result = ex

            while (null == result.message) {

                cause = result.cause

                if (cause == null) break

                result = cause
            }

            return result
        }


        fun onDestroy() {

            Thread.setDefaultUncaughtExceptionHandler(mOldHandler)
        }
    }
}
