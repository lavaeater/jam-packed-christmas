@file:JvmName("Lwjgl3Launcher")

package jam.core.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import jam.core.ChristmasGame

/** Launches the desktop (LWJGL3) application. */
fun main() {
    Lwjgl3Application(ChristmasGame(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("jam-packed-christmas")
        setWindowedMode(800, 600)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
    })
}
