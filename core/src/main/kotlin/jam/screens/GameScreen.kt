package jam.screens

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.utils.viewport.ExtendViewport
import eater.ecs.ashley.components.Remove
import eater.physics.addComponent
import hoHoHo
import jam.core.ChristmasGame
import jam.core.GameSettings
import jam.core.toColor
import jam.map.ChristmasMapManager
import jam.ui.WinterHud
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen
import ktx.app.clearScreen

class GameScreen(
    private val game: ChristmasGame,
    private val engine: Engine,
    private val viewPort: ExtendViewport,
    private val batch: PolygonSpriteBatch,
    private val camera: OrthographicCamera,
    private val gameSettings: GameSettings,
    private val winterHud: WinterHud,
    private val mapManager: ChristmasMapManager
) : KtxScreen, KtxInputAdapter {
    override fun hide() {
        for(entity in engine.entities) {
            entity.addComponent<Remove>()
        }
        for(system in engine.systems)
            system.setProcessing(false)
    }

    private val bgColor = "6D6E75".toColor()

    override fun pause() {
        super.pause()
    }

    override fun render(delta: Float) {
        clearScreen(bgColor.r, bgColor.g, bgColor.b)
        engine.update(delta)
        winterHud.render(delta)
    }

    override fun resize(width: Int, height: Int) {
        viewPort.update(width, height)
        batch.projectionMatrix = camera.combined
    }

    override fun resume() {
        super.resume()
    }

    override fun show() {
        mapManager.createMap()
        hoHoHo(100f, true)
        for(system in engine.systems)
            system.setProcessing(true)
    }
}

