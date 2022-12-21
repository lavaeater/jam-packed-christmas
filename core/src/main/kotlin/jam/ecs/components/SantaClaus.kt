package jam.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import jam.map.City
import ktx.ashley.mapperFor
import ktx.math.vec2

class SantaClaus: Component, Pool.Poolable {
    var targetCity = City()
    val targetHouses = mutableSetOf<Entity>()
    override fun reset() {
        targetCity = City()
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