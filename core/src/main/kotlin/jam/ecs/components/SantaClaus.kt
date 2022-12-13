package jam.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class SantaClaus: Component, Pool.Poolable {
    var closestHouse = Entity()
    val targetHouses = mutableSetOf<Entity>()
    override fun reset() {
        closestHouse = Entity()
        targetHouses.clear()
    }

    companion object {
        val mapper = mapperFor<SantaClaus>()
        fun has(entity: Entity): Boolean {
            return mapper.has(entity)
        }
        fun get(entity: Entity): SantaClaus {
            return mapper.get(entity)
        }
    }
}