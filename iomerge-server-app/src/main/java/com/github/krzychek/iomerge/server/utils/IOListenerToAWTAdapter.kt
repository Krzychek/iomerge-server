package com.github.krzychek.iomerge.server.utils

import com.github.krzychek.iomerge.server.api.movementReader.IOListener
import java.awt.event.*


class IOListenerToAWTAdapter(private val listener: IOListener) : MouseWheelListener, MouseListener, KeyListener {

    override fun mouseWheelMoved(e: MouseWheelEvent) = listener.mouseWheelMoved(e)

    override fun mouseClicked(e: MouseEvent) = listener.mouseClicked(e)

    override fun mousePressed(e: MouseEvent) = listener.mousePressed(e)

    override fun mouseReleased(e: MouseEvent) = listener.mouseReleased(e)

    override fun mouseEntered(e: MouseEvent) {
    }

    override fun mouseExited(e: MouseEvent) {
    }

    override fun keyTyped(e: KeyEvent) = listener.keyTyped(e)

    override fun keyPressed(e: KeyEvent) = listener.keyPressed(e)

    override fun keyReleased(e: KeyEvent) = listener.keyReleased(e)
}
