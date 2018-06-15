package com.andrioussolutions.drive

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.http.HttpRequest
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.services.script.Script
import com.google.api.services.script.model.ExecutionRequest
import com.google.api.services.script.model.Operation
import com.google.api.services.script.model.Status

import com.andrioussolutions.utils.Auth
import com.andrioussolutions.utils.ErrorHandler

import java.io.IOException
import java.util.ArrayList

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
 * Created  14 Jan 2018
 */
class GoogleDrive {

    private val mScopes = ArrayList<String>()

    private var mScriptId: String? = null

    private var mOperation: Operation? = null

    private var mResult: String? = null

    private var mMessage: String? = null


    /**
     * Build and return an authorized Script client service.
     *
     * @return an authorized Script client service
     */
    private val scriptService: Script?
        get() {

            if (mScopes == null) return null

            var service: Script?

            try {

                val credential = Auth.authorize(mScopes, "makeForm")

                service = Script.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, setHttpTimeout(credential))
                        .setApplicationName("drive-makeForm")
                        .build()

            } catch (ex: Exception) {

                service = null

                ErrorHandler.log(ex)
            }

            return service
        }


    val error: Status?
        get() {

            val error: Status?

            if (mOperation == null) {

                error = null
            } else {

                error = mOperation!!.error
            }

            return error
        }


    val message: String
        get() {

            if (mMessage == null) {

                val error = error

                if (error == null) {

                    mMessage = ""
                } else {

                    val errMsg = error.details[0]

                    mMessage = errMsg["errorMessage"] as String
                }
            }

            return mMessage
        }


    val response: Map<String, Any>?
        get() {

            val response: Map<String, Any>?

            if (mOperation == null) {

                response = null
            } else {

                response = mOperation!!.response
            }

            return response
        }


    val result: String
        get() {

            if (mResult == null) {

                val response = response

                if (response == null) {

                    mResult = ""
                } else {

                    mResult = response["result"] as String
                }
            }

            return mResult
        }


    constructor() {

    }


    constructor(scriptId: String, scope: String) {

        scriptId(scriptId)

        scope(scope)
    }


    constructor(scriptId: String) {

        scriptId(scriptId)
    }


    fun scriptId(scriptId: String): GoogleDrive {

        mScriptId = scriptId

        return this
    }


    fun scope(scope: String): GoogleDrive {

        mScopes.add(scope)

        return this
    }


    fun scope(scopes: List<String>): GoogleDrive {

        for (scope in scopes) {

            scope(scope)
        }

        return this
    }


    fun makeForm(title: String): GoogleDrive {

        mOperation = null

        mMessage = null

        mResult = null

        if (mScriptId == null) return this

        val service = scriptService ?: return this

        val params = ArrayList<Any>()

        params.add(title)

        // Create an execution request object.
        val request = ExecutionRequest()
                .setFunction("makeForm")
                .setParameters(params)

        try {

            // Make the API request.
            mOperation = service.scripts().run(mScriptId, request).execute()

            // Log an error.
            if (mOperation!!.error != null) {

                val errMsg = mOperation!!.error.details[0]

                ErrorHandler.log(errMsg["errorMessage"] as String)
            }
        } catch (ex: Exception) {

            ErrorHandler.log(ex)
        }

        return this
    }


    /**
     * Create a HttpRequestInitializer from the given one, except set
     * the HTTP read timeout to be longer than the default (to allow
     * called scripts time to execute).
     *
     * @return an initializer with an extended read timeout.
     */
    private fun setHttpTimeout(requestInitializer: HttpRequestInitializer): HttpRequestInitializer {
        return HttpRequestInitializer { httpRequest ->
            requestInitializer.initialize(httpRequest)
            // This allows the API to call (and avoid timing out on)
            // functions that take up to 6 minutes to complete (the maximum
            // allowed script run time), plus a little overhead.
            httpRequest.readTimeout = 380000
        }
    }


    fun done(): Boolean {

        return mOperation != null && mOperation!!.done!!
    }


    fun error(): Boolean {

        return error != null
    }
}

