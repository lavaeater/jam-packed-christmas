package jam.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import jam.map.City
import ktx.ashley.mapperFor

class SamSite: Component, Pool.Poolable {
    var city = City()
    override fun reset() {
        city = City()
    }

    companion object {
        val mapper = mapperFor<SamSite>()
        fun has(entity: Entity): Boolean {
            return mapper.has(entity)
        }
        fun get(entity: Entity): SamSite {
            return mapper.get(entity)
        }
    }
}