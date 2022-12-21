package jam.ecs.components

import box2dLight.Light
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class RedNose: Component, Pool.Poolable {
    lateinit var light: Light
    var offset = 1f
    val direction = Vector2.X.cpy()
    var on = true
    override fun reset() {
        offset = 1f
        direction.set(Vector2.X)
        on = true
    }

    companion object {
        val mapper = mapperFor<RedNose>()
        fun has(entity: Entity): Boolean {
            return mapper.has(entity)
        }
        fun get(entity: Entity): RedNose {
            return mapper.get(entity)
        }
    }
}