package jam.ecs.components

import box2dLight.Light
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor
import ktx.math.vec2

class House : Component, Pool.Poolable {
    var width = 4
    var height = 4
    val lights = mutableListOf<Light>()
    val cityPosition = vec2()
    override fun reset() {
        width = 4
        height = 4
        lights.clear()
        cityPosition.setZero()
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