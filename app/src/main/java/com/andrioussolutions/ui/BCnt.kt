package com.andrioussolutions.ui

import java.awt.*

import javax.swing.*
import javax.swing.border.Border
import javax.swing.border.TitledBorder

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
 * Created  08 Jan 2018
 */
object BCnt {

    private var mCount = 0

    fun set(): Border {

        mCount += 1

        return BorderFactory.createTitledBorder(null, Integer.toString(mCount), TitledBorder.CENTER, TitledBorder.BOTTOM, Font("times new roman", Font.PLAIN, 12), Color.black)
    }
}
