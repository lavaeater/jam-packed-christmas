package jam.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class NeedsGifts @JvmOverloads constructor() : Component, Pool.Poolable {
    override fun reset() {

    }

    companion object {
        val mapper = mapperFor<NeedsGifts>()
        fun has(entity: Entity): Boolean {
            return mapper.has(entity)
        }
        fun get(entity: Entity): NeedsGifts {
            return mapper.get(entity)
        }
    }
}
