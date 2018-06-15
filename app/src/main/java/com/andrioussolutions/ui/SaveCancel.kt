package com.andrioussolutions.ui

import java.awt.*
import javax.swing.*
import javax.swing.text.JTextComponent


class SaveCancel(comp: JTextComponent, saveField: SaveFld, cancelField: CancelFld) {

    internal var mSave: JButton

    internal var mCancel: JButton


    init {

        mSave = JButton("Save")

        mSave.maximumSize = Dimension(8, 4)

        mSave.addActionListener { e -> saveField.save(comp.text) }

        mCancel = JButton("Cancel")

        mCancel.maximumSize = Dimension(8, 4)

        mCancel.addActionListener { e -> comp.text = cancelField.cancel() }
    }


    fun saveBtn(): JButton {

        return mSave
    }


    fun cancelBtn(): JButton {

        return mCancel
    }


    interface SaveFld {

        fun save(data: String): Boolean
    }


    interface CancelFld {

        fun cancel(): String
    }
}
