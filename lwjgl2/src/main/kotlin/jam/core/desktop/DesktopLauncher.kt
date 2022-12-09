@file:JvmName("DesktopLauncher")

package jam.core.desktop

import com.badlogic.gdx.Files
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import jam.core.ChristmasGame

/** Launches the desktop (LWJGL) application. */
fun main() {
    LwjglApplication(ChristmasGame(), LwjglApplicationConfiguration().apply {
        title = "jam-packed-christmas"
        width = 640
        height = 480
        intArrayOf(128, 64, 32, 16).forEach{
            addIcon("libgdx$it.png", Files.FileType.Internal)
        }
    })
}
