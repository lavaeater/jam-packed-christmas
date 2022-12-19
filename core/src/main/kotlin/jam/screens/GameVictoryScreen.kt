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

class GameVictoryScreen(mainGame: ChristmasGame) : ScreenWithStage(mainGame, Color(0.7f, 0.7f, 0.7f, 1f)) {
    init {
        commandMap = command("Go back") {
            setUp(Input.Keys.SPACE, "Go go back") { mainGame.goToGameSelect() }
        }
    }

    override val stage by lazy {
        stage().apply {
            actors {
                image(Texture("images/victory.png".toInternalFile())).setFillParent(true)
            }
        }
    }
}