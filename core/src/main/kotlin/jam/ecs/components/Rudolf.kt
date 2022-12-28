package jam.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class Rudolf : Component, Pool.Poolable {
    override fun reset() {

    }

    companion object {
        val mapper = mapperFor<Rudolf>()
        fun has(entity: Entity): Boolean {
            return mapper.has(entity)
        }
        fun get(entity: Entity): Rudolf {
            return mapper.get(entity)
        }
    }
}
