import box2dLight.ConeLight
import box2dLight.PointLight
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.steer.behaviors.Seek
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils.degreesToRadians
import com.badlogic.gdx.math.MathUtils.radiansToDegrees
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import eater.ai.steering.box2d.Box2dLocation
import eater.ai.steering.box2d.Box2dSteerable
import eater.core.engine
import eater.core.world
import eater.ecs.ashley.components.*
import eater.injection.InjectionContext.Companion.inject
import jam.ecs.components.*
import jam.injection.assets
import jam.map.ChristmasMapManager
import ktx.ashley.entity
import ktx.ashley.with
import ktx.box2d.*
import ktx.math.minus
import ktx.math.plus
import ktx.math.vec2

sealed class ChristmasProp(name: String) : PropName(name) {
    object ChristmasCheer : ChristmasProp("Cheer")
    object NaughtyHealth: ChristmasProp("Naughty Health")
}

fun snowFlake(at: Vector2) {
    engine().entity {
        with<IndexComponent> {
            index = 7
        }
        with<SpriteComponent> {
            sprite = assets().snowFlakeSprite
            scale = 0.25f
        }
        with<SnowFlake>()
        with<TransformComponent> {
            position.set(at)
        }
    }
}

fun shootMissileAtSanta(from: Vector2, santaEntity: Entity) {
    val santaPosition = TransformComponent.get(santaEntity).position
    val targetAngleRad = (santaPosition - from).nor().angleRad()
    engine().entity {
        with<IndexComponent> {
            index = 4
        }
        with<SpriteComponent> {
            sprite = assets().samSprite
            shadow = true
        }
        with<Flashlight> {
            light = ConeLight(inject(), 4, Color.YELLOW, 15f, 0f, 0f, 0f, 15f)
            offset = 1f
            directionOffset = -180f
        }
        with<TransformComponent>()
        with<SamComponent> {
            fuelInSeconds = (1..3).random().toFloat()
        }
        with<Box2d> {
            body = world().body {
                type = BodyDef.BodyType.DynamicBody
                userData = this@entity.entity
                position.set(from)
                angularDamping = 1f
                linearDamping = 0.5f
                fixedRotation = false
                angle = targetAngleRad + 90f * radiansToDegrees
                box(0.25f, 1f) {
                    density = 0.5f
                    filter {
                        categoryBits = Categories.actualSam
                        maskBits = Categories.whatActualSamCollidesWith
                    }
                }
            }
        }
        with<Box2dSteerable> {
            body = Box2d.get(this@entity.entity).body
            steeringBehavior = Seek(this, Box2dLocation(TransformComponent.get(santaEntity).position))
            maxLinearSpeed = 50f
            maxAngularSpeed = 25f
            maxLinearAcceleration = 5f
            maxAngularAcceleration = 10f
        }
    }
}

fun explosion(at:Vector2) {
    engine().entity {
        with<LightComponent> {
            light = PointLight(inject(), 16, Color.WHITE, 30f, at.x, at.y)
        }
        with<LightExplosion>()
    }
    assets().boom.play()
}

fun throwPresent(from: Vector2, to: Vector2) {
    val thrownPresent = engine().entity {
        with<ChristmasPresent>()
        with<TransformComponent>()
        with<IndexComponent> {
            index = 3
        }
        with<SpriteComponent> {
            sprite = assets().presentSprite
            shadow = true
        }
        with<Box2d> {
            body = world().body {
                type = BodyDef.BodyType.DynamicBody
                userData = this@entity.entity
                position.set(from)
                angularDamping = 0.8f
                linearDamping = 0.8f
                fixedRotation = false

                box(2f, 1f) {
                    density = 0.01f
                    filter {
                        categoryBits = Categories.present
                        maskBits = Categories.whatPresentsCollideWith
                    }
                }
            }
        }

    }
    val body = Box2d.get(thrownPresent).body
    val impulse = (to - from).nor().scl(0.25f)
    body.applyLinearImpulse(impulse, body.worldCenter + vec2(0.25f, 0f), true)
}

fun hoHoHo(christmasCheer: Float = 100f, follow: Boolean = false) {
    val carriageEntity = engine().entity {
        with<SantaClaus> {
            targetCity = inject<ChristmasMapManager>().getClosestCityThatNeedsGifts(Vector2.Zero)!!
        }
        with<IndexComponent> {
            index = 5
        }
        with<ChristmasPropComponent> {
            props[ChristmasProp.ChristmasCheer] = CoolProp.FloatProperty(ChristmasProp.ChristmasCheer, christmasCheer)
        }
        with<Box2d> {
            body = world().body {
                type = BodyDef.BodyType.DynamicBody
                userData = this@entity.entity
                position.set(0f, 0f)
                // angle = 90f * MathUtils.degreesToRadians
                angularDamping = 0.8f
                linearDamping = 0.8f
                fixedRotation = false


                box(2.5f, 4f) {
                    density = 0.0001f
                    filter {
                        categoryBits = Categories.santa
                        maskBits = Categories.whatSantaCollidesWith
                    }
                }
                circle(15f) {
                    isSensor = true
                    filter {
                        categoryBits = Categories.santasSense
                        maskBits = Categories.whatSantasSenseCollidesWith
                    }
                }
            }
        }
        with<TransformComponent>()
        with<SpriteComponent> {
            sprite = assets().sleighSprite
            shadow = true
        }
    }

    engine().entity {
        with<Human>()
        with<Rudolf>()
        with<IndexComponent> {
            index = 5
        }
        with<RedNose> {
            offset = 2f
            light = ConeLight(inject(), 16, Color.RED, 100f, 0f, 0f, 90f, 5f)
        }

        if (follow)
            with<CameraFollow>()
        with<BodyControl> {
            maxForce = 1000f
        }
        with<KeyboardAndMouseInput>()
        with<Box2d> {
            body = world().body {
                type = BodyDef.BodyType.DynamicBody
                userData = this@entity.entity
                position.set(0f, 0f)
                // angle = 90f * MathUtils.degreesToRadians
                angularDamping = 0.8f
                linearDamping = 0.8f
                fixedRotation = false

                box(2.5f, 4f) {
                    density = 0.1f
                    filter {
                        categoryBits = Categories.santa
                        maskBits = Categories.whatSantaCollidesWith
                    }
                }
            }.apply {
                revoluteJointWith(Box2d.get(carriageEntity).body) {
                    localAnchorA.set(0f, -3f)
                    localAnchorB.set(0f, 3f)
                    collideConnected = false
                    enableLimit = true
                    lowerAngle = -60f * degreesToRadians
                    upperAngle = 60f * degreesToRadians
                }
            }
        }
        with<TransformComponent>()
        with<Player>()
        with<SpriteComponent> {
            sprite = assets().deerSprite
            shadow = true
        }
    }
}