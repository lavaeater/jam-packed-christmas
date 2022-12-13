package jam.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class House: Component, Pool.Poolable {
    var width = 4
    var height = 4
    override fun reset() {

    }

    companion object {
        val mapper = mapperFor<House>()
        fun has(entity: Entity): Boolean {
            return mapper.has(entity)
        }
        fun get(entity: Entity): House {
            return mapper.get(entity)
        }
    }
}