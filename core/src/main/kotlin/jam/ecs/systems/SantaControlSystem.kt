package jam.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import eater.ecs.ashley.components.BodyControl
import eater.ecs.ashley.components.Box2d
import eater.ecs.ashley.components.Player
import eater.ecs.ashley.components.TransformComponent
import ktx.ashley.allOf
import ktx.math.times
import ktx.math.vec2

class SantaControlSystem :
    IteratingSystem(allOf(Box2d::class, BodyControl::class, Player::class, TransformComponent::class).get()) {
    private val forceDirectionVector = vec2(1f, 0f)
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val body = Box2d.get(entity).body
        val control = BodyControl.get(entity)
        val direction = TransformComponent.get(entity).direction

        forceDirectionVector.set(direction)

        body.applyForce(forceDirectionVector * control.directionVector.y * 2500f * deltaTime, body.worldCenter, true)
        body.applyTorque(control.directionVector.x * 250f * deltaTime, true)
    }
}