package jam.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class TextureRegionComponent: Component, Pool.Poolable {

    var textureRegion = TextureRegion()

    override fun reset() {

    }

    companion object {
        val mapper = mapperFor<TextureRegionComponent>()
        fun has(entity: Entity): Boolean {
            return mapper.has(entity)
        }
        fun get(entity: Entity): TextureRegionComponent {
            return mapper.get(entity)
        }
    }
}

