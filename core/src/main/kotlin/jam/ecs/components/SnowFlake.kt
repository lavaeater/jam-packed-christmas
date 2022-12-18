package jam.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor
import ktx.math.vec2

class SnowFlake: Component, Pool.Poolable {
    var timeToLive = 25f
    var timeToLiveBase = 25f
    val movementVector = vec2(0.15f, 0.15f)

    override fun reset() {
        timeToLive = timeToLiveBase
        movementVector.set(0.15f, 0.15f)
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