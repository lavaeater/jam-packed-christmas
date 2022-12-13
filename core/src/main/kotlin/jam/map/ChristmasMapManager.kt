package jam.map

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import eater.core.engine
import eater.core.world
import eater.ecs.ashley.components.Player
import eater.ecs.ashley.components.TransformComponent
import jam.ecs.components.House
import jam.ecs.components.SpriteComponent
import jam.injection.assets
import ktx.ashley.entity
import ktx.ashley.with
import ktx.math.plus
import ktx.math.random
import ktx.math.vec2

class ChristmasMapManager {
    val world by lazy { world() }
    val engine by lazy { engine() }

    val mapSizeRange = (-1000..1000)
    val houseRange = (-100..100)
    val sizeRange = 4..16

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
    val allMapEntities = mutableListOf<Entity>()
    fun createMap() {
        //val randomAngleRange = (0f..MathUtils.PI2)
        //val randomDistanceRange = 5f..10000f
        //val randomVector = vec2(1f, 0f)
        //Test a 1000 points first
        (1..1000).forEach { _ ->
            val position  = vec2(mapSizeRange.random().toFloat(), mapSizeRange.random().toFloat())
            allMapEntities.add(createTerrainThingAt(position))
        }

        (1..10).forEach {
            val position = vec2(houseRange.random().toFloat(), houseRange.random().toFloat())
            allMapEntities.add(createHouseAt(position))
        }

    }

    private fun createHouseAt(at: Vector2): Entity {
        return engine.entity {
            with<TransformComponent> {
                position.set(at)
            }
            with<Player>()
            with<House> {
                width = sizeRange.random()
                height = sizeRange.random()
            }
        }
    }

    fun createTerrainThingAt(at: Vector2): Entity {
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