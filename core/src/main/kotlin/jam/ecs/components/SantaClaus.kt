package jam.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor
import ktx.math.vec2

class SantaClaus: Component, Pool.Poolable {
    val targetCityPosition = vec2()
    val targetHouses = mutableSetOf<Entity>()
    override fun reset() {
        targetCityPosition.setZero()
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