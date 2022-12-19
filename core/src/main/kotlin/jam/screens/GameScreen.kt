package jam.screens

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.utils.viewport.ExtendViewport
import eater.core.MainGame
import eater.ecs.ashley.components.Remove
import eater.input.CommandMap
import eater.input.KeyPress
import eater.physics.addComponent
import jam.core.toColor
import jam.ecs.systems.SortedRenderSystem
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen
import ktx.app.clearScreen

abstract class GameScreen(protected val game: MainGame,
                          private val engine: Engine,
                          private val viewPort: ExtendViewport,
                          private val batch: PolygonSpriteBatch,
                          private val camera: OrthographicCamera,): KtxScreen, KtxInputAdapter {
    protected lateinit var commandMap: CommandMap
    private var paused = false
    private val bgColor = "6D6E75".toColor()
    protected fun togglePause() {
        paused = !paused
        if (paused)
            pause()
        else
            resume()
    }

    override fun hide() {
        Gdx.input.inputProcessor = null
        for (entity in engine.entities) {
            entity.addComponent<Remove>()
        }
        for (system in engine.systems)
            system.setProcessing(false)
    }

    override fun pause() {
        for (system in engine.systems) {
            if (system !is SortedRenderSystem)
                system.setProcessing(false)
        }
    }

    override fun render(delta: Float) {
        clearScreen(bgColor.r, bgColor.g, bgColor.b)
        engine.update(delta)

    }

    override fun resize(width: Int, height: Int) {
        viewPort.update(width, height)
        batch.projectionMatrix = camera.combined
    }

    override fun resume() {
        for (system in engine.systems)
            system.setProcessing(true)
    }

    override fun show() {
        Gdx.input.inputProcessor = this
        for (system in engine.systems)
            system.setProcessing(true)
    }

    override fun keyDown(keycode: Int): Boolean {
        return commandMap.execute(keycode, KeyPress.Down)
    }

    override fun keyUp(keycode: Int): Boolean {
        return commandMap.execute(keycode, KeyPress.Up)
    }
}