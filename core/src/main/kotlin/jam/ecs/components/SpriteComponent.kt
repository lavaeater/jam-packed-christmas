package jam.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class SpriteComponent: Component, Pool.Poolable {

    var sprite = Sprite()

    override fun reset() {
        sprite = Sprite()
    }

    companion object {
        val mapper = mapperFor<SpriteComponent>()
        fun has(entity: Entity): Boolean {
            return mapper.has(entity)
        }
        fun get(entity: Entity): SpriteComponent {
            return mapper.get(entity)
        }
    }
}

