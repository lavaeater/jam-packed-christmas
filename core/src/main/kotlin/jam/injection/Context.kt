package jam.injection

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
import eater.core.GameSettings
import jam.ecs.components.*
import jam.ecs.systems.*
import jam.map.ChristmasMapManager
import ktx.ashley.remove
import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import space.earlygrey.shapedrawer.ShapeDrawer
import jam.core.getContactType
import jam.screens.*
import ktx.ashley.allOf
import ktx.math.vec2
import snowFlake


object Context : InjectionContext() {
    private val shapeDrawerRegion: TextureRegion by lazy {
        val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color.WHITE)
        pixmap.drawPixel(0, 0)
        val texture = Texture(pixmap) //remember to dispose of later
        pixmap.disposeSafely()
        TextureRegion(texture, 0, 0, 1, 1)
    }

    fun initialize(game: ChristmasGame, debugBox2d: Boolean) {
        buildContext {
            val gameSettings = GameSettings()
            bindSingleton(gameSettings)
            bindSingleton(game)
            bindSingleton(PolygonSpriteBatch())
            bindSingleton(OrthographicCamera())
            bindSingleton(
                ExtendViewport(
                    gameSettings.gameWidth,
                    gameSettings.gameHeight,
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

                                val coolProp = EntityPropertyComponent.get(santaClaus)
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
            bindSingleton(Assets(inject()))
            bindSingleton(getEngine(gameSettings, debugBox2d))
            bindSingleton(SplashScreen(inject()))
            bindSingleton(GameSelectScreen(inject()))
            bindSingleton(GameOverScreen(inject()))
            bindSingleton(GameVictoryScreen(inject()))
            bindSingleton(
                ChristmasGameScreen(
                    inject(),
                    inject(),
                    inject(),
                    inject(),
                    inject(),
                    inject(),
                    inject())
            )
        }
    }

    private fun getEngine(gameSettings: GameSettings, debugBox2d: Boolean): Engine {
        return PooledEngine().apply {
            addSystem(RemoveEntitySystem())
            addSystem(SteerSystem())
            addSystem(Box2dUpdateSystem(gameSettings.timeStep, gameSettings.velocityIterations, gameSettings.positionIterations))
            addSystem(ChristmasCameraFollowSystem(inject(), 0.1f, true))
            addSystem(SantaControlSystem())
            addSystem(KeyboardInputSystem(inject(), invertX = true, invertY = false))
            addSystem(RudolfNoseSystem())
            addSystem(LightPositionUpdateSystem())
            addSystem(TransformBasedFlashlightDirectionSystem())
            addSystem(SteerSystem())
            addSystem(AiTimePieceSystem())
            addSystem(UpdateActionsSystem())
            addSystem(AshleyAiSystem())
            addSystem(AddChristmasLightsSystem(inject()))
            addSystem(SortedRenderSystem(inject(), inject(), inject(), inject(), inject(), inject(), false))
            if(debugBox2d)
                addSystem(Box2dDebugRenderSystem(inject(), inject()))
            addSystem(DeliverPresentsSystem())
            addSystem(GameOverSystem(inject(), inject()))
            addSystem(SamFuelSystem())
            addSystem(RemoveSamSiteSystem())
            addSystem(UpdateMemorySystem())
            addSystem(EnsureEntitySystem(EnsureEntityDef(allOf(SnowFlake::class).get(), 250, 0.05f, true) {
                val camera = inject<OrthographicCamera>()
                val snowFlakePosition = vec2(
                    camera.position.x + (-50..50).random().toFloat(),
                    camera.position.y + (-50..50).random().toFloat())

                snowFlake(snowFlakePosition)

            }))
            addSystem(LetItSnowSystem(inject()))
            addSystem(LightExplosionSystem())
            addSystem(SantaTargetSystem(inject(), inject()))
            addSystem(LogSystem())
        }
    }
}
