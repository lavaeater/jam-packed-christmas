package jam.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import eater.ecs.ashley.components.BodyControl
import eater.ecs.ashley.components.Flashlight
import eater.ecs.ashley.components.Remove
import eater.ecs.ashley.components.TransformComponent
import jam.ecs.components.RedNose
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.math.times

class RudolfNoseSystem :IteratingSystem(allOf(
RedNose::class,
TransformComponent::class
).exclude(Remove::class).get()
) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val light = RedNose.get(entity)
        val transform = TransformComponent.get(entity)
        val lightPos = transform.position.cpy()
        light.direction.setAngleDeg(transform.direction.angleDeg())
        lightPos.add(light.direction * light.offset)
        light.light.setPosition(lightPos.x, lightPos.y)
        light.light.direction = light.direction.angleDeg()
    }
}