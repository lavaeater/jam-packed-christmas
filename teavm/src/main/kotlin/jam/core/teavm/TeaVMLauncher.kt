@file:JvmName("TeaVMLauncher")

package jam.core.teavm

import com.badlogic.gdx.ApplicationListener
import com.github.xpenatan.gdx.backends.teavm.TeaApplicationConfiguration
import com.github.xpenatan.gdx.backends.web.WebApplication
import jam.core.ChristmasGame

/** Launches the TeaVM/HTML application. */
fun main() {
    val config = TeaApplicationConfiguration("canvas").apply {
        width = 800
        height = 600
    }
    WebApplication(ChristmasGame(), config)
}
