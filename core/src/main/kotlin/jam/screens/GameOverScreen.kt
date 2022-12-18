package jam.screens

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import eater.core.MainGame
import eater.input.command
import eater.screens.BasicScreen
import jam.core.ChristmasGame
import ktx.actors.stage
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile
import ktx.graphics.use
import ktx.scene2d.actors
import ktx.scene2d.image
import ktx.scene2d.label

class GameOverScreen(mainGame: ChristmasGame) : BasicScreen(mainGame) {
    init {
        commandMap = command("Go back") {
            setUp(Input.Keys.SPACE, "Go go back") { mainGame.goToGameSelect() }
        }
    }

    private val stage by lazy {
        stage().apply {
            actors {
                image(Texture("images/game-over.png".toInternalFile())).setFillParent(true)
            }
        }
    }

    override fun render(delta: Float) {
        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)
        stage.act(delta)
        stage.draw()
    }

    override fun dispose() {
        batch.disposeSafely()
    }
}