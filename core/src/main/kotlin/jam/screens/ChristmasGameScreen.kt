package jam.screens

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.utils.viewport.ExtendViewport
import eater.input.command
import hoHoHo
import jam.core.ChristmasGame
import eater.core.GameSettings
import jam.map.ChristmasMapManager
import jam.ui.WinterHud

class ChristmasGameScreen(
    christmasGame: ChristmasGame,
    engine: Engine,
    viewPort: ExtendViewport,
    batch: PolygonSpriteBatch,
    camera: OrthographicCamera,
    private val gameSettings: GameSettings,
    private val mapManager: ChristmasMapManager,
    private val winterHud: WinterHud
) : GameScreen(christmasGame, engine, viewPort, batch, camera) {

    init {
        commandMap = command("Some basic Controls") {
            setUp(Input.Keys.ESCAPE, "Pause / Resume") { togglePause() }
        }
    }

    override fun render(delta: Float) {
        super.render(delta)
        winterHud.render(delta)
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        winterHud.resize(width, height)
    }

    override fun show() {
        super.show()
        mapManager.createMap()
        hoHoHo(100f, true)
    }
}

