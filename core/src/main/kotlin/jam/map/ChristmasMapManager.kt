package jam.map

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import eater.core.engine
import eater.core.world
import eater.ecs.ashley.components.Player
import eater.ecs.ashley.components.TransformComponent
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
        val randomAngleRange = (0f..MathUtils.PI2)
        val randomDistanceRange = 5f..1000f
        val randomVector = vec2(1f, 0f)
        //Test a 1000 points first
        (1..1000).forEach { _ ->
            randomVector.rotateRad(randomAngleRange.random())
            randomVector.scl(randomDistanceRange.random())
            allMapEntities.add(createTerrainThingAt(Vector2.Zero + randomVector))
            randomVector.set(Vector2.X)
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