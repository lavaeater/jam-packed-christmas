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

class GameSelectScreen(mainGame: ChristmasGame) : ScreenWithStage(
    mainGame, Color(0.1f, 0.5f, 0.7f, 1f)
) {

    init {
        commandMap = command("GameSelectCommands") {
            setUp(Input.Keys.SPACE, "STart Game") { mainGame.goToGameScreen() }
        }
    }

    override val stage by lazy {
        stage().apply {
            actors {
                image(Texture("images/game-selekt.png".toInternalFile())).setFillParent(true)
                label("SPLASH - PRESS SPACE")
            }
        }
    }
}
