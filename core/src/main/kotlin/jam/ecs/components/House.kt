package jam.ecs.components

import box2dLight.Light
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class House : Component, Pool.Poolable {
    var floors = (5..15).random()
    var width = 4
    var height = 4
    val lights = mutableListOf<Light>()
    override fun reset() {
        width = 4
        height = 4
        lights.clear()
        floors = (5..15).random()
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