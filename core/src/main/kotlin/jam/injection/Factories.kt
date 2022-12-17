import box2dLight.ConeLight
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

fun shootMissileAtSanta(from: Vector2, santaEntity: Entity) {
    val santaPosition = TransformComponent.get(santaEntity).position
    val targetAngleRad = (santaPosition - from).nor().angleRad()
    engine().entity {
        with<SpriteComponent> {
            sprite = assets().samSprite
            shadow = true
        }
        with<TransformComponent>()
        with<SamComponent>()
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

}

/**
 * val directionToTarget = (state.target!!.worldCenter - body.worldCenter).nor()
 *                         val currentDirection = body.linearVelocity.nor()
 *                         currentDirection.lerp(directionToTarget, 0.25f)
 *
 * //                        if ((body.angle * radiansToDegrees - currentDirection.angleDeg()) > 5f) {
 * //                            body.applyTorque(-150f, true)
 * //                        } else if ((body.angle * radiansToDegrees - currentDirection.angleDeg()) < -5f) {
 * //                            body.applyTorque(150f, true)
 * //                        }
 *
 *                         body.setTransform(body.position, currentDirection.angleRad() - MathUtils.HALF_PI)
 *                         body.applyForce(currentDirection.scl(state.force), body.worldCenter, true)
 *
 *
 *                     } else {
 *                         val potentialTargets = engine().getEntitiesFor(if(player) robotCars else playerCars)
 *                         val target = potentialTargets.map { Box2d.get(it).body }
 *                             .sortedBy { it.worldCenter.dst(body.worldCenter) }.firstOrNull()
 *                         if (target != null) {
 *                             state.hasTarget = true
 *                             state.target = target
 *                         }
 *                         val forwardNormal = state.startDirection.cpy()
 *                         val currentSpeed = body.forwardVelocity().dot(forwardNormal)
 *                         if (currentSpeed < state.maxSpeed)
 *                             body.applyForce(forwardNormal.scl(state.force), body.worldCenter, true)
 */



fun throwPresent(from: Vector2, to: Vector2) {
    val thrownPresent = engine().entity {
        with<ChristmasPresent>()
        with<TransformComponent>()
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
        with<RedNose> {
            offset = 2f
            light = ConeLight(inject(), 16, Color.RED, 100f, 0f, 0f, 90f, 5f)
        }
        with<ChristmasPropComponent> {
            props[ChristmasProp.ChristmasCheer] = CoolProp.FloatProperty(ChristmasProp.ChristmasCheer, christmasCheer)
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
//
//fun createBlob(
//    at: Vector2,
//    health: Float = 100f,
//    radius: Float = 3f,
//    follow: Boolean = false
//) {
//    BlobGrouper.addNewBlob(engine().entity {
//        with<Blob> {
//            this.radius = radius
//        }
//        with<PropsAndStuff> {
//            props.add(Prop.FloatProp.Health(health))
//        }
//        with<AiComponent> {
//            actions.addAll(BlobActions.allActions)
//        }
//        if (follow) {
//            with<CameraFollow>()
//            with<LogComponent> {
//                logFunction = { entity ->
//                    val aiComponent = AiComponent.get(entity)
//                    info { "We ${if (BlobGrouper.canSplit) "can" else "cannot"} split" }
//                    info { "Health: ${PropsAndStuff.get(entity).getHealth().current}" }
//                    info { "Top action: ${aiComponent.topAction(entity)?.name}" }
//                    info { aiComponent.actions.joinToString { "${it.score} - ${it.name}\n" } }
//                    info { "Messages: ${Blob.get(entity).messageCount}" }
//                    info { "Blob count: ${BlobGrouper.blobCount}" }
//                    info { "Top Message: ${Blob.get(entity).peekOldestMessage()}" }
//                    info { "Neighbours: ${Blob.get(entity).neighbours.size}" }
//                }
//            }
//        }
//        with<Memory>()
//        val b2Body = world().body {
//            type = BodyDef.BodyType.DynamicBody
//            userData = this@entity.entity
//            position.set(at)
//            circle(3.0f) {
//                filter {
//                    categoryBits = Categories.blob
//                    maskBits = Categories.whatBlobsCollideWith
//                }
//            }
//            circle(10.0f) {
//                isSensor = true
//                filter {
//                    categoryBits = Categories.blob
//                    maskBits = Categories.blob
//                }
//            }
//        }
//        with<Box2d> {
//            body = b2Body
//        }
//        with<TransformComponent>()
//        with<Box2dSteerable> {
//            isIndependentFacing = false
//            body = b2Body
//            maxLinearSpeed = inject<GameSettings>().BlobMaxSpeed
//            maxLinearAcceleration = inject<GameSettings>().BlobMaxAcceleration
//            maxAngularAcceleration = 100f
//            maxAngularSpeed = 10f
//            boundingRadius = 5f
//            steeringBehavior = getWanderSteering(this@entity.entity, this)
//        }
//        with<AgentProperties>() {
//            fieldOfView = 270f
//            viewDistance = 50f
//        }
//    })
//}
//
//fun createMap(key: String): Map {
//    var scaleFactor = 1f
//    if (key == "two")
//        scaleFactor = 2f
//    val gridSize = 8f * scaleFactor
//    val mapOffset = vec2(-50f, -50f)
//    val mapAssets = assets().maps[key]!!
//    val textureRegion = TextureRegion(mapAssets.first)
//    val topTextureRegion = TextureRegion(mapAssets.third)
//    lateinit var map: Map
//    engine().entity {
//        map = with {
//            mapTextureRegion = textureRegion
//            mapTopLayerRegion = topTextureRegion
//            mapScale = scaleFactor
//            mapOrigin.set(mapOffset)
//            mapBounds = Rectangle(
//                mapOffset.x + gridSize,
//                mapOffset.y + gridSize,
//                textureRegion.regionWidth.toFloat() - 2 * gridSize,
//                textureRegion.regionHeight.toFloat() - 2 * gridSize
//            )
//        }
//        createBounds(mapAssets.second, gridSize, mapOffset, map)
//    }
//    return map
//}
//
//fun createBounds(intLayer: String, tileSize: Float, mapOffset: Vector2, map: Map) {
//    /*
//    To make it super easy, we just create a square per int-tile in the layer.
//     */
//    intLayer.lines().reversed().forEachIndexed { y, l ->
//        l.split(',').forEachIndexed { x, c ->
//            if (PointType.allTypes.containsKey(c)) {
//                val pointType = PointType.allTypes[c]!!
//                if (!map.points.containsKey(pointType)) {
//                    map.points[pointType] = mutableListOf()
//                }
//                map.points[pointType]!!.add(
//                    vec2(
//                        x * tileSize + mapOffset.x + tileSize / 2f,
//                        y * tileSize + mapOffset.y - tileSize / 2f
//                    )
//                )
//            }
//        }
//    }
//
//    for (bound in map.points[PointType.Impassable]!!) {
//        map.mapBodies.add(world().body {
//            type = BodyDef.BodyType.StaticBody
//            position.set(
//                bound.x,
//                bound.y
//            )
//            box(tileSize, tileSize) {
//                filter {
//                    categoryBits = Categories.walls
//                    maskBits = Categories.whatWallsCollideWith
//                }
//            }
//        })
//    }
//}

