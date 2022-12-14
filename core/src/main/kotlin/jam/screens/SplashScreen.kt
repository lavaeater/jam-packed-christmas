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
import ktx.scene2d.actors
import ktx.scene2d.container
import ktx.scene2d.label
import ktx.scene2d.table

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
                    label("SPLASH - PRESS SPACE")
                }
            }
        }


    private val image = Texture("logo.png".toInternalFile(), true).apply {
        setFilter(
            Texture.TextureFilter.Linear,
            Texture.TextureFilter.Linear
        )
    }

    override fun render(delta: Float) {
        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)
        stage.act(delta)
        stage.draw()
    }

    override fun dispose() {
        image.disposeSafely()
        batch.disposeSafely()
    }
}