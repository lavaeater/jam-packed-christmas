package jam.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class SamComponent: Component, Pool.Poolable {
    var fuelInSeconds = 3f

    override fun reset() {
        fuelInSeconds = 3f
    }

    companion object {
        val mapper = mapperFor<SamComponent>()
        fun has(entity: Entity): Boolean {
            return mapper.has(entity)
        }
        fun get(entity: Entity): SamComponent {
            return mapper.get(entity)
        }
    }
}