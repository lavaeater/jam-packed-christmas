package jam.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class SnowFlake: Component, Pool.Poolable {
    var timeToLive = 25f
    var timeToLiveBase = 25f

    override fun reset() {
        timeToLive = timeToLiveBase
    }

    companion object {
        val mapper = mapperFor<SnowFlake>()
        fun has(entity: Entity): Boolean {
            return mapper.has(entity)
        }
        fun get(entity: Entity): SnowFlake {
            return mapper.get(entity)
        }
    }
}