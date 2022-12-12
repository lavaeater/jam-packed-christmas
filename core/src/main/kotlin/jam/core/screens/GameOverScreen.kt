package jam.core.screens

import com.badlogic.gdx.graphics.Texture
import eater.core.MainGame
import eater.screens.BasicScreen
import jam.core.ChristmasGame
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile
import ktx.graphics.use

class GameOverScreen(mainGame: ChristmasGame) : BasicScreen(mainGame) {
    private val image = Texture("logo.png".toInternalFile(), true).apply { setFilter(
        Texture.TextureFilter.Linear,
        Texture.TextureFilter.Linear
    ) }
    override fun render(delta: Float) {
        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)
        batch.use {

        }
    }

    override fun dispose() {
        image.disposeSafely()
        batch.disposeSafely()
    }
}