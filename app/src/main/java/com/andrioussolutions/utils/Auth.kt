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
 * Created  26 Dec 2017
 */

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory

import java.io.File
import java.io.FileReader
import java.io.IOException
import java.io.Reader

/**
 * Shared class used by every sample. Contains methods for authorizing a user and caching credentials.
 */
object Auth {

    /**
     * Define a global instance of the HTTP transport.
     */
    val HTTP_TRANSPORT: HttpTransport = NetHttpTransport()

    /**
     * Define a global instance of the JSON factory.
     */
    val JSON_FACTORY: JsonFactory = JacksonFactory()

    /**
     * This is the directory that will be used under the user's home directory where OAuth tokens will be stored.
     */
    private val CREDENTIALS_DIRECTORY = ".oauth-credentials"

    private var mErrorMsg = ""

    /**
     * Authorizes the installed application to access user's protected data.
     *
     * @param scopes              list of scopes needed to run youtube upload.
     * @param credentialDatastore name of the credential datastore to cache OAuth tokens
     */
    @Throws(IOException::class)
    fun authorize(scopes: List<String>, credentialDatastore: String): Credential {

        var clientSecretReader: Reader?

        try {

            // Load client secrets.
            //           clientSecretReader = new InputStreamReader(Auth.class.getResourceAsStream("client_secrets.json"));

            val file = File("app\\src\\main\\java\\com\\andrioussolutions\\client_secrets.json")

            clientSecretReader = FileReader(file)

        } catch (ex: Exception) {

            mErrorMsg = ErrorHandler.getMessage(ex)

            clientSecretReader = null
        }

        if (!mErrorMsg.isEmpty()) {

            println(mErrorMsg)

            System.exit(1)
        }

        val clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, clientSecretReader)

        // Checks that the defaults have been replaced (Default = "Enter X here").
        if (clientSecrets.details.clientId.startsWith("Enter") || clientSecrets.details.clientSecret.startsWith("Enter")) {

            println(
                    "Enter Client ID and Secret from https://console.developers.google.com/project/_/apiui/credential " + "into src/main/resources/client_secrets.json")
            System.exit(1)
        }

        // This creates the credentials datastore at ~/.oauth-credentials/${credentialDatastore}
        //        FileDataStoreFactory fileDataStoreFactory = new FileDataStoreFactory(new File(System.getProperty("user.home") + "/" + CREDENTIALS_DIRECTORY));

        //        DataStore<StoredCredential> datastore = fileDataStoreFactory.getDataStore(credentialDatastore);

        val build = GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, scopes)

        //        build.setCredentialDataStore(datastore);

        val flow = build.build()

        // Build the local server and bind it to port 8080
        //        LocalServerReceiver localReceiver = new LocalServerReceiver.Builder().setPort(8080).build();
        val localReceiver = LocalServerReceiver.Builder().build()

        // Authorize.
        return AuthorizationCodeInstalledApp(flow, localReceiver).authorize("user")
    }
}