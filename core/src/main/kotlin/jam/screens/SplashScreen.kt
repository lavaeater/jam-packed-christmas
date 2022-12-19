package jam.screens

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import eater.input.command
import eater.screens.ScreenWithStage
import jam.core.ChristmasGame
import ktx.actors.stage
import ktx.assets.toInternalFile
import ktx.scene2d.actors
import ktx.scene2d.image
import ktx.scene2d.label

class SplashScreen(mainGame: ChristmasGame) : ScreenWithStage(
    mainGame, Color(0f, 0.3f, 0.6f, 1f)
) {

    init {
        commandMap = command("SplashScreen") {
            setUp(Input.Keys.SPACE, "Move to Menu") { mainGame.goToGameSelect() }
        }
    }

    override val stage by lazy {
        stage().apply {
            actors {
                image(Texture("images/splash.png".toInternalFile())).setFillParent(true)
                label("SPLASH - PRESS SPACE")
            }
        }
    }

}