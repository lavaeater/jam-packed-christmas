package jam.screens

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Align
import eater.core.MainGame
import eater.extensions.boundLabel
import eater.input.command
import eater.screens.BasicScreen
import jam.core.ChristmasGame
import ktx.actors.stage
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile
import ktx.graphics.use
import ktx.scene2d.*

class SplashScreen(mainGame: ChristmasGame) : BasicScreen(
    mainGame
) {

    init {
        commandMap = command("SplashScreen") {
            setUp(Input.Keys.SPACE, "Move to Menu") { mainGame.goToGameSelect() }
        }
    }

    private val stage by lazy {
            stage().apply {
                actors {
                    image(Texture("images/splash.png".toInternalFile())).setFillParent(true)
                    label("SPLASH - PRESS SPACE")
                }
            }
        }


    override fun render(delta: Float) {
        clearScreen(red = 0f, green = 0.3f, blue = 0.6f)
        stage.act(delta)
        stage.draw()
    }

    override fun dispose() {
        batch.disposeSafely()
    }
}