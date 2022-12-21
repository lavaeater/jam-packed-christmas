package jam.map

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import eater.ecs.ashley.components.TransformComponent
import jam.ecs.components.NeedsGifts
import ktx.math.vec2

class City {
    var difficulty = 0
    val houses: MutableList<Entity> = mutableListOf()

    val cityPosition: Vector2 by lazy {
        vec2((houses.map { TransformComponent.get(it).position.x }.sum() / houses.count()),
            (houses.map { TransformComponent.get(it).position.y }.sum() / houses.count())
        )
    }

    val cityBounds: Rectangle by lazy {
        val minX = houses.minOf { TransformComponent.get(it).position.x }
        val minY = houses.minOf { TransformComponent.get(it).position.y }
        val maxX = houses.maxOf { TransformComponent.get(it).position.x }
        val maxY = houses.maxOf { TransformComponent.get(it).position.y }
        Rectangle(minX - 5f, minY - 5f, maxX - minX + 10f, maxY - minY + 10f)
    }
    val needsGifts: Boolean
        get() {
            return houses.map { NeedsGifts.has(it) }.any { it }
        }
}