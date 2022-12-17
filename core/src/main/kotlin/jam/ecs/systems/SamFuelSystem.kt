package jam.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import eater.ecs.ashley.components.Remove
import eater.ecs.ashley.components.TransformComponent
import eater.physics.addComponent
import explosion
import jam.ecs.components.SamComponent
import ktx.ashley.allOf
import ktx.ashley.exclude

class SamFuelSystem : IteratingSystem(allOf(SamComponent::class).exclude(Remove::class).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val samComponent = SamComponent.get(entity)
        samComponent.fuelInSeconds -= deltaTime
        if(samComponent.fuelInSeconds <= 0f) {
            entity.addComponent<Remove>()
            explosion(TransformComponent.get(entity).position)
        }
    }
}