package jam.map

import Box2dCategories
import ChristmasProperty
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import eater.ecs.ashley.components.AiComponent
import eater.ai.ashley.ConsideredAction
import eater.core.engine
import eater.core.wholePoints
import eater.core.world
import eater.ecs.ashley.components.*
import jam.ecs.components.*
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

class ChristmasMapManager {
    private val world by lazy { world() }
    private val engine by lazy { engine() }

    private val mapSizeRange = (-1000..1000)
    private val cityRange = (-1000..1000)
    private val maxGridSize = 16
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
    val cities = mutableListOf<City>()

    fun fixCityDifficulty(city: City) {
        /**
         * Now the fun begins!
         */
        if ((1..10).contains(city.difficulty)) {
            addSamSites(city)
        }
    }

    private fun addSamSites(city: City) {
        val cityBounds = city.cityBounds
        val outerBounds =
            Rectangle(cityBounds.x - 10f, cityBounds.y - 10f, cityBounds.width + 20f, cityBounds.height + 20f)
        val validPoints = outerBounds.wholePoints() - cityBounds.wholePoints().toSet()
        for (i in 1..city.difficulty) {
            val samPosition = validPoints.random()
            val detectorRange = (15..35).random().toFloat() * city.difficulty

            engine.entity {
                with<SamSite> {
                    this.city = city
                }
                with<ZIndexComponent> {
                    index = 1
                }
                with<TransformComponent>()
                with<EntityPropertyComponent> {
                    props[ChristmasProperty.NaughtyHealth] = SimpleProperty.FloatProperty(ChristmasProperty.NaughtyHealth)

                }
                with<AiComponent> {
                    val santaFamily = allOf(SantaClaus::class).get()
                    val samCoolDownRange = (3..10)
                    var coolDown = 0f
                    actions.addAll(
                        listOf(
                            ConsideredAction(
                                "Shoot if Santa is in Range, maan",
                                0f..1f,
                                { entity, deltaTime ->
                                    if (coolDown <= 0f) {
                                        val samSitePosition = TransformComponent.get(entity).position
                                        val santaEntity = engine().getEntitiesFor(santaFamily).first()
                                        val santaPosition = TransformComponent.get(santaEntity).position
                                        if (samSitePosition.dst(santaPosition) < detectorRange) {
                                            shootMissileAtSanta(samPosition, santaEntity)
                                        }
                                        coolDown = samCoolDownRange.random().toFloat()
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
                                categoryBits = Box2dCategories.samSite
                                maskBits = Box2dCategories.whatSamSitesCollideWith
                            }
                        }
                        circle(detectorRange) {
                            isSensor = true
                            filter {
                                categoryBits = Box2dCategories.samSite
                                maskBits = Box2dCategories.whatSamSitesCollideWith
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
        (1..2000).forEach { _ ->
            val position = vec2(mapSizeRange.random().toFloat(), mapSizeRange.random().toFloat())
//            allMapEntities.add(createTerrainThingAt(position))
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
        val gridSize = 2..4
        val rows = gridSize.random()
        val columns = (2 .. (if(rows > 3) 3 else rows)).random()
        for (x in 0 until columns)
            for (y in 0 until rows) {
                val houseEntity = createHouseAt(at + vec2(x * maxGridSize * 1.5f, y * maxGridSize * 1.5f))
                city.houses.add(houseEntity)
//                allMapEntities.add(houseEntity)
            }
    }

    private fun createHouseAt(at: Vector2): Entity {
        val width = sizeRange.random()
        val height = sizeRange.random()
        return engine.entity {
            with<ZIndexComponent> {
                index = 2
            }
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
                            categoryBits = Box2dCategories.house
                            maskBits = Box2dCategories.whatHousesCollideWith
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
            with<ZIndexComponent>()
            with<TransformComponent> {
                position.set(at)
            }
            with<Player>()
            with<SpriteComponent> {
                sprite = assets().terrainSprite
            }
            with<ZIndexComponent>()
        }
    }
}
