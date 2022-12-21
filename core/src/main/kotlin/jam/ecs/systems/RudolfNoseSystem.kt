package jam.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import eater.ecs.ashley.components.BodyControl
import eater.ecs.ashley.components.Flashlight
import eater.ecs.ashley.components.Remove
import eater.ecs.ashley.components.TransformComponent
import jam.ecs.components.RedNose
import jam.ecs.components.SantaClaus
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.math.minus
import ktx.math.times

class RudolfNoseSystem :IteratingSystem(allOf(
RedNose::class,
TransformComponent::class
).exclude(Remove::class).get()
) {

    private val santaFamily = allOf(SantaClaus::class).get()
    private val santa get() = engine.getEntitiesFor(santaFamily).firstOrNull()
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val light = RedNose.get(entity)
        val transform = TransformComponent.get(entity)
        val lightPos = transform.position.cpy()
        val targetPosition = SantaClaus.get(santa!!).targetCity.cityPosition
        lightPos.add(light.direction * light.offset)
        val targetVector = (targetPosition - lightPos).nor()
        light.direction.setAngleDeg(targetVector.angleDeg())
        light.light.setPosition(lightPos.x, lightPos.y)
        light.light.direction = light.direction.angleDeg()
    }
}