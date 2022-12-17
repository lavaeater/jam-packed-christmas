package jam.injection

import ChristmasProp
import box2dLight.RayHandler
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import com.badlogic.gdx.utils.viewport.ExtendViewport
import eater.ecs.ashley.components.*
import eater.ecs.ashley.systems.*
import eater.injection.InjectionContext
import eater.physics.addComponent
import explosion
import jam.core.ChristmasGame
import jam.core.GameSettings
import jam.ecs.components.*
import jam.ecs.systems.*
import jam.map.ChristmasMapManager
import jam.screens.GameOverScreen
import jam.screens.GameScreen
import jam.screens.GameSelectScreen
import jam.screens.SplashScreen
import jam.ui.WinterHud
import ktx.ashley.remove
import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import space.earlygrey.shapedrawer.ShapeDrawer
import jam.core.getContactType



object Context : InjectionContext() {
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
            bindSingleton(createWorld().apply {
                setContactListener(object : ContactListener {
                    override fun beginContact(contact: Contact) {
                        when (val contactType = contact.getContactType()) {
                            is ContactType.SantaAndHouse -> {
                                if (NeedsGifts.has(contactType.house)) {
                                    val santaComponent = SantaClaus.get(contactType.santaClaus)
                                    santaComponent.targetHouses.add(contactType.house)
                                }
                            }

                            ContactType.Other -> {}
                            is ContactType.HouseAndPresent -> {
                                val houseEntity = contactType.house
                                houseEntity.remove<NeedsGifts>()
                                houseEntity.addComponent<NeedsChristmasLights>()
                                contactType.present.addComponent<Remove>()
                            }

                            is ContactType.SantaAndSam -> {
                                val samEntity = contactType.sam
                                val santaClaus = contactType.santaClaus

                                val samTransform = TransformComponent.get(samEntity)
                                val samPosition = samTransform.position

                                explosion(samPosition)

                                samEntity.addComponent<Remove>()

                                val coolProp = ChristmasPropComponent.get(santaClaus)
                                coolProp.getChristmasCheer().current -= (5..25).random().toFloat()
                            }
                        }
                    }

                    override fun endContact(contact: Contact) {
                        when (val contactType = contact.getContactType()) {
                            is ContactType.SantaAndHouse -> {
                                val santaComponent = SantaClaus.get(contactType.santaClaus)
                                santaComponent.targetHouses.remove(contactType.house)
                            }
                            ContactType.Other -> {}
                            else -> {}
                        }
                    }

                    override fun preSolve(contact: Contact, oldManifold: Manifold?) {
                    }

                    override fun postSolve(contact: Contact, impulse: ContactImpulse?) {
                    }

                })
            })
            bindSingleton(RayHandler(inject()).apply {
                setAmbientLight(.25f)
                setBlurNum(3)
            })
            bindSingleton(ChristmasMapManager())
            bindSingleton(ShapeDrawer(inject<PolygonSpriteBatch>() as Batch, shapeDrawerRegion))
            bindSingleton(getEngine(gameSettings, false))
            bindSingleton(Assets(inject()))
            bindSingleton(SplashScreen(inject()))
            bindSingleton(GameSelectScreen(inject()))
            bindSingleton(GameOverScreen(inject()))
            bindSingleton(WinterHud(inject(), inject()))
            bindSingleton(
                GameScreen(
                    inject(),
                    inject(),
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

    private fun getEngine(gameSettings: GameSettings, debugBox2d: Boolean): Engine {
        return PooledEngine().apply {
            addSystem(RemoveEntitySystem())
            addSystem(SteerSystem())
            //addSystem(CameraAndMapSystem(inject(), 0.75f, inject(),gameSettings.AspectRatio))
            addSystem(Box2dUpdateSystem(gameSettings.TimeStep, gameSettings.VelIters, gameSettings.PosIters))
            addSystem(ChristmasCameraFollowSystem(inject(), 0.1f, false))
            addSystem(SantaControlSystem())
            addSystem(KeyboardInputSystem(inject(), invertX = true, invertY = false))
            addSystem(RudolfNoseSystem())
            addSystem(LightPositionUpdateSystem())
            addSystem(SteerSystem())
            addSystem(AiTimePieceSystem())
            addSystem(UpdateActionsSystem())
            addSystem(AshleyAiSystem())
            addSystem(AddChristmasLightsSystem(inject()))
            addSystem(RenderSystem(inject(), inject(), inject(), inject(), inject(), inject(), false))
            if(debugBox2d)
                addSystem(Box2dDebugRenderSystem(inject(), inject()))
            addSystem(DeliverPresentsSystem())
            addSystem(GameOverSystem(inject(), inject()))
            addSystem(UpdateMemorySystem())
            addSystem(LogSystem())
        }
    }
}