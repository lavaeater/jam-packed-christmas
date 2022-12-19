package jam.screens

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.utils.viewport.ExtendViewport
import eater.ecs.ashley.components.Remove
import eater.input.KeyPress
import eater.input.command
import eater.physics.addComponent
import hoHoHo
import jam.core.ChristmasGame
import jam.core.GameSettings
import jam.core.toColor
import jam.ecs.systems.SortedRenderSystem
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

    private val commandMap = command("Some basic Controls") {
        setUp(Input.Keys.ESCAPE, "Pause / Resume") {togglePause()}
    }

    private var paused = false
    private fun togglePause() {
        paused = !paused
        if(paused)
            pause()
        else
            resume()
    }

    override fun hide() {
        Gdx.input.inputProcessor = null
        for(entity in engine.entities) {
            entity.addComponent<Remove>()
        }
        for(system in engine.systems)
            system.setProcessing(false)
    }

    private val bgColor = "6D6E75".toColor()

    override fun pause() {
        for (system in engine.systems) {
            if(system !is SortedRenderSystem)
            system.setProcessing(false)
        }
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
        for(system in engine.systems)
            system.setProcessing(true)
    }

    override fun show() {
        Gdx.input.inputProcessor = this
        mapManager.createMap()
        hoHoHo(100f, true)
        for(system in engine.systems)
            system.setProcessing(true)
    }

    override fun keyDown(keycode: Int): Boolean {
        return commandMap.execute(keycode, KeyPress.Down)
    }

    override fun keyUp(keycode: Int): Boolean {
        return commandMap.execute(keycode, KeyPress.Up)
    }
}

