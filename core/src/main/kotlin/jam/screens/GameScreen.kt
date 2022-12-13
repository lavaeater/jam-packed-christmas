package jam.screens

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.math.MathUtils.norm
import com.badlogic.gdx.utils.viewport.ExtendViewport
import hoHoHo
import jam.core.ChristmasGame
import jam.core.GameSettings
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
    private val winterHud: WinterHud
) : KtxScreen, KtxInputAdapter {
    override fun hide() {
        super.hide()
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

    private val mapManager = ChristmasMapManager()

    override fun show() {
        mapManager.createMap()
        hoHoHo(100f, true)

//        val map = createMap("two")
//        InjectionContext.context.bindSingleton(map)
//        BlobGrouper.blobPoints = map.points[PointType.BlobStart]!!
////        createFood(map.points[PointType.BlobStart]!!)
////        createHumans(map.points[PointType.HumanStart]!!)
//        createLights(map.points[PointType.Lights]!!)
//
//        for (i in 0..gameSettings.MinBlobs)
//            createBlob(map.points[PointType.BlobStart]!!.random(), (5..6).random() * 10f, follow = false)
//
//        createPlayer(map.points[PointType.PlayerStart]!!.random(), follow = true)
    }
}

fun String.toColor(): Color {
    val parts = this.chunked(2)
    val r = norm(0f, 255f, Integer.decode("0x${parts[0]}").toFloat())
    val g = norm(0f, 255f, Integer.decode("0x${parts[1]}").toFloat())
    val b = norm(0f, 255f, Integer.decode("0x${parts[2]}").toFloat())
    return Color(r, g, b, 1f)
}