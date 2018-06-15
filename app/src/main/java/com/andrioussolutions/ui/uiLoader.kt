package com.andrioussolutions.ui

import java.awt.*

import javax.swing.*

/**
 * Copyright (C) 2018  Andrious Solutions Ltds.
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
class uiLoader @JvmOverloads constructor(title: String = "") : JPanel() {

    private var mFrame: JFrame? = null

    private var mBase: Container? = null

    private var mPalette: Container? = null


    init {

        mBase = this

        mPalette = this

        setup(title)
    }


    private fun setup(title: String) {

        mFrame = JFrame(title)

        // Opens the frame in the middle of the screen
        // You could also define position based on a component                   |
        mFrame!!.setLocationRelativeTo(null)

        //        // Toolkit is the super class for the Abstract Window Toolkit
        //        // It allows us to ask questions of the OS
        //        Toolkit tk = Toolkit.getDefaultToolkit();
        //
        //        // A Dimension can hold the width and height of a component
        //        // getScreenSize returns the size of the screen
        //        Dimension dim = tk.getScreenSize();
        //
        //        // dim.width returns the width of the screen
        //        // this.getWidth returns the width of the frame you are making
        //        int xPos = (dim.width / 2) - (mFrame.getWidth() / 2);
        //        int yPos = (dim.height / 2) - (mFrame.getHeight() / 2);
        //
        //        // You could also define the x, y position of the frame
        //        mFrame.setLocation(xPos, yPos);

        // Define how the user exits the program
        // This closes when they click the close button
        // Define if the user can resize the frame (true by default)
        mFrame!!.isResizable = true

        // Define how the frame exits (Click the Close Button)
        // Without this Java will eventually close the app
        mFrame!!.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        /*        // add scrolling to the frame.
        JScrollPane jScrollPane = new JScrollPane(this);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        mFrame.getContentPane().add(jScrollPane);
*/

        mFrame!!.contentPane.add(this, "Center")

        // Define the size of the frame
        mFrame!!.setSize(700, 700)
    }


    fun frame(): JFrame? {

        mPalette = mBase

        return mFrame
    }


    fun add(component: JComponent): Component {

        return onPalette(component)!!.add(addComponent(component))
    }


    fun add(component: JComponent, pos: Any): JComponent {

        onPalette(component)!!.add(addComponent(component), pos)

        return component
    }


    fun add(panel: JPanel, pos: Any): JPanel {

        onPalette(panel)!!.add(addComponent(panel), pos)

        return panel
    }


    fun add(component: JComponent, pos: Int): Component {

        return onPalette(component)!!.add(addComponent(component), pos)
    }


    // Determine which 'container' will take in the component.
    private fun onPalette(component: JComponent): Container? {

        val container: Container?

        if (component is JPanel) {

            if (mPalette === mBase) {

                container = mBase
            } else {

                container = mPalette!!.parent
            }

            //            component.setBorder(BCnt.set());

            mPalette = component
        } else {

            container = mPalette
        }

        return container
    }


    private fun addComponent(component: JComponent): JComponent {

        val addComponent: JComponent

        // Always place non-panel components in a panel(FlowLayout) first
        // Unless the Palette is the 'base' panel or a FlowLayout.
        if (component !is JPanel && mPalette!!.layout.javaClass != FlowLayout::class.java) {

            val panel = JPanel()

            //            panel.setBorder(BCnt.set());

            panel.add(component)

            addComponent = panel

            // It's now the new palette.
            mPalette = panel
        } else {

            addComponent = component
        }

        return addComponent
    }


    fun addPanel(layout: LayoutManager): Container {

        val container = mPalette

        mPalette = JPanel(layout)

        mBase = mPalette

        //        ((JPanel)mPalette).setBorder(BCnt.set());

        container!!.add(mPalette)

        return mPalette
    }


    fun addPanel(): Component {

        return addPanel(FlowLayout())
    }


    fun addPanel(component: JComponent): Container {

        mPalette!!.add(component)

        return mPalette
    }


    fun display(): uiLoader {

        mFrame!!.isVisible = true

        return this
    }
}
