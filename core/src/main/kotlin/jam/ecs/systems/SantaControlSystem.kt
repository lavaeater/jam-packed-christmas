package jam.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import eater.ecs.ashley.components.*
import eater.injection.InjectionContext.Companion.inject
import ktx.ashley.allOf
import ktx.math.times
import ktx.math.vec2
import space.earlygrey.shapedrawer.ShapeDrawer

class SantaControlSystem: IteratingSystem(allOf(Box2d::class, BodyControl::class, Player::class, TransformComponent::class).get()) {
    private val dragForceMagnitudeFactor = 0.25f
    private val forceDirectionVector = vec2(1f, 0f)
    private val shapeDrawer by lazy { inject<ShapeDrawer>()}
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val body = Box2d.get(entity).body
        val control = BodyControl.get(entity)
        val direction = TransformComponent.get(entity).direction

//        body.applyForce(control.moveDirection * control.currentForce, body.worldCenter, true)


        //        body.angularVelocity = control.directionVector.x * 2.5f
        forceDirectionVector.set(direction)
        body.applyForce(forceDirectionVector * control.directionVector.y * 10000f * deltaTime, body.worldCenter, true)
        body.applyTorque(control.directionVector.x * 1000f * deltaTime, true)

//        val forward = body.linearVelocity
//        val speed = forward.len()
//        val dragForceMagnitude = -dragForceMagnitudeFactor * speed
//        body.applyForce(forward.scl(dragForceMagnitude), body.worldCenter, true)
    }
}