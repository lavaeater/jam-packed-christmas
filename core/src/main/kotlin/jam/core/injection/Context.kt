package jam.core.injection

import box2dLight.RayHandler
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.viewport.ExtendViewport
import eater.ecs.ashley.systems.*
import eater.injection.InjectionContext
import jam.core.ChristmasGame
import jam.core.GameSettings
import jam.core.screens.SplashScreen
import jam.core.screens.GameOverScreen
import jam.core.screens.GameScreen
import jam.core.screens.GameSelectScreen
import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import space.earlygrey.shapedrawer.ShapeDrawer

object Context:InjectionContext() {
    private val shapeDrawerRegion: TextureRegion by lazy {
        val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color.WHITE)
        pixmap.drawPixel(0, 0)
        val texture = Texture(pixmap) //remember to dispose of later
        pixmap.disposeSafely()
        TextureRegion(texture, 0, 0, 1, 1)
    }

    fun initialize(game: ChristmasGame) {
        buildContext {
            val gameSettings = GameSettings()
            bindSingleton(gameSettings)
            bindSingleton(game)
            bindSingleton(de.pottgames.tuningfork.Audio.init())
            bindSingleton(PolygonSpriteBatch())
            bindSingleton(OrthographicCamera())
            bindSingleton(
                ExtendViewport(
                    gameSettings.GameWidth,
                    gameSettings.GameHeight,
                    inject<OrthographicCamera>() as Camera
                )
            )
            bindSingleton(createWorld())
            bindSingleton(RayHandler(inject()).apply {
                setAmbientLight(.01f)
                setBlurNum(3)
            })
            bindSingleton(ShapeDrawer(inject<PolygonSpriteBatch>() as Batch, shapeDrawerRegion))
            bindSingleton(getEngine(gameSettings))
            bindSingleton(Assets())
            //bindSingleton(HackLightEngine(0.01f, 0.01f, 0.01f, 0.1f))
            bindSingleton(SplashScreen(inject()))
            bindSingleton(GameSelectScreen(inject()))
            bindSingleton(GameOverScreen(inject()))
            bindSingleton(
                GameScreen(
                    inject(),
                    inject(),
                    inject(),
                    inject(),
                    inject(),
                    inject()
                )
            )
        }
    }

    private fun getEngine(gameSettings: GameSettings): Engine {
        return PooledEngine().apply {
            addSystem(RemoveEntitySystem())
            addSystem(CameraAndMapSystem(inject(), 0.75f, inject(),gameSettings.AspectRatio))
            addSystem(Box2dUpdateSystem(gameSettings.TimeStep, gameSettings.VelIters, gameSettings.PosIters))
            addSystem(BodyControlSystem())
            addSystem(KeyboardInputSystem(inject()))
            addSystem(FlashlightDirectionSystem())
            addSystem(LightPositionUpdateSystem())
            addSystem(SteerSystem())
            addSystem(AiTimePieceSystem())
            addSystem(UpdateActionsSystem())
            addSystem(AshleyAiSystem())
//            addSystem(EnsureEntitySystem(EnsureEntityDef(allOf(Human::class).get(), 15) { createHuman() }))
//            addSystem(RenderSystem(inject(), inject(), inject(), inject()))
//            addSystem(Box2dDebugRenderSystem(inject(), inject()))
            addSystem(UpdateMemorySystem())
            addSystem(LogSystem())
        }
    }
}