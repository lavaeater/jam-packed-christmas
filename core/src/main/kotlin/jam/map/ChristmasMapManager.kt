package jam.map

import Categories
import ChristmasProp
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import eater.ai.ashley.AiComponent
import eater.ai.ashley.ConsideredAction
import eater.core.engine
import eater.core.world
import eater.ecs.ashley.components.*
import jam.core.wholePoints
import jam.ecs.components.House
import jam.ecs.components.NeedsGifts
import jam.ecs.components.SantaClaus
import jam.ecs.components.SpriteComponent
import jam.injection.assets
import ktx.ashley.allOf
import ktx.ashley.entity
import ktx.ashley.with
import ktx.box2d.body
import ktx.box2d.box
import ktx.box2d.circle
import ktx.box2d.filter
import ktx.math.plus
import ktx.math.vec2
import shootMissileAtSanta
import java.awt.font.TransformAttribute

class ChristmasMapManager {
    private val world by lazy { world() }
    private val engine by lazy { engine() }

    private val mapSizeRange = (-1000..1000)
    private val cityRange = (-1000..1000)
    private val maxGridSize = 8
    private val sizeRange = 4..maxGridSize

    /**
     * I don't think I want a tile-based map this time around.
     *
     * I want things to be free and flowy.
     *
     * So, do we accomplish that by generating tons of entities (yes)
     * that need to be rendered separately? Yes.
     *
     *
     */
    private val allMapEntities = mutableListOf<Entity>()
    val cities = mutableListOf<City>()

    fun fixCityDifficulty(city: City) {
        /**
         * Now the fun begins!
         */
        if ((1..5).contains(city.difficulty)) {
            addSamSites(city)
        }
    }

    private fun addSamSites(city: City) {
        val cityBounds = city.cityBounds
        val outerBounds =
            Rectangle(cityBounds.x - 5f, cityBounds.y - 5f, cityBounds.width + 10f, cityBounds.height + 10f)
        val validPoints = outerBounds.wholePoints() - cityBounds.wholePoints().toSet()
        for (i in 1..city.difficulty) {
            val samPosition = validPoints.random()
            val detectorRange = (15..50).random().toFloat()

            engine.entity {
                with<TransformComponent>()
                with<ChristmasPropComponent> {
                    props[ChristmasProp.NaughtyHealth] = CoolProp.FloatProperty(ChristmasProp.NaughtyHealth)

                }
                with<AiComponent> {
                    val santaFamily = allOf(SantaClaus::class).get()
                    val samCoolDown = (1..5).random().toFloat()
                    var coolDown = 0f
                    actions.addAll(
                        listOf(
                            ConsideredAction(
                                "Shoot if Santa is in Range, maan",
                                0f..1f,
                                { entity, deltaTime ->
                                    if(coolDown <= 0f) {
                                        val samSitePosition = TransformComponent.get(entity).position
                                        val santaEntity = engine().getEntitiesFor(santaFamily).first()
                                        val santaPosition = TransformComponent.get(santaEntity).position
                                        if (samSitePosition.dst(santaPosition) < detectorRange) {
                                            shootMissileAtSanta(samPosition, santaEntity)
                                        }
                                        coolDown = samCoolDown
                                    } else {
                                        coolDown -= deltaTime
                                    }

                                    false
                                },
                            )
                        )
                    )
                }
                with<SpriteComponent> {
                    sprite = assets().samSiteSprite
                }
                with<Box2d> {
                    body = world.body {
                        type = BodyDef.BodyType.StaticBody
                        userData = this@entity.entity
                        position.set(samPosition)
                        angularDamping = 0.8f
                        linearDamping = 0.8f
                        fixedRotation = true

                        box(1f, 1f) {
                            density = 0.1f
                            filter {
                                categoryBits = Categories.samSite
                                maskBits = Categories.whatSamSitesCollideWith
                            }
                        }
                        circle(detectorRange) {
                            isSensor = true
                            filter {
                                categoryBits = Categories.samSite
                                maskBits = Categories.whatSamSitesCollideWith
                            }
                        }
                    }
                }
            }
        }
    }

    fun getClosestCityThatNeedsGifts(to: Vector2): City? {
        return cities.filter { it.needsGifts }.minByOrNull { it.cityPosition.dst2(to) }
    }

    fun createMap() {
        (1..1000).forEach { _ ->
            val position = vec2(mapSizeRange.random().toFloat(), mapSizeRange.random().toFloat())
            allMapEntities.add(createTerrainThingAt(position))
        }

        (1..10).forEach {

            val position = if (it == 0) vec2() else vec2(cityRange.random().toFloat(), cityRange.random().toFloat())
            createCity(position)
        }

    }

    private fun createCity(at: Vector2) {
        /**
         * Position is top left or something lazy like that.
         */
        val city = City()
        cities.add(city)
        val gridSize = 2..3
        for (x in 0 until gridSize.random())
            for (y in 0 until gridSize.random()) {
                val houseEntity = createHouseAt(at + vec2(x * maxGridSize * 2f, y * maxGridSize * 2f))
                city.houses.add(houseEntity)
                allMapEntities.add(houseEntity)
            }

    }

    private fun createHouseAt(at: Vector2): Entity {
        val width = sizeRange.random()
        val height = sizeRange.random()
        return engine.entity {
            with<TransformComponent> {
                position.set(at)
            }
            with<Box2d> {
                body = world.body {
                    type = BodyDef.BodyType.StaticBody
                    userData = this@entity.entity
                    position.set(at)
                    angularDamping = 0.8f
                    linearDamping = 0.8f
                    fixedRotation = true

                    box(width.toFloat(), height.toFloat()) {
                        density = 0.1f
                        filter {
                            categoryBits = Categories.house
                            maskBits = Categories.whatHousesCollideWith
                        }
                    }
                }
            }
            with<House> {
                this.width = width
                this.height = height
            }
            with<NeedsGifts>()
        }
    }

    private fun createTerrainThingAt(at: Vector2): Entity {
        return engine.entity {
            with<TransformComponent> {
                position.set(at)
            }
            with<Player>()
            with<SpriteComponent> {
                sprite = assets().terrainSprite
            }
        }
    }
}