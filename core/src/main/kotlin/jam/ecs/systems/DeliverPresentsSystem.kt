package jam.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import eater.ecs.ashley.components.TransformComponent
import jam.ecs.components.NeedsGifts
import jam.ecs.components.SantaClaus
import ktx.ashley.allOf
import throwPresent

class DeliverPresentsSystem:IntervalIteratingSystem(allOf(SantaClaus::class, TransformComponent::class).get(), 0.25f) {
    override fun processEntity(entity: Entity) {
        val santaClaus = SantaClaus.get(entity)
        val toRemove = santaClaus.targetHouses.filter { !NeedsGifts.has(it) }
        santaClaus.targetHouses.removeAll(toRemove.toSet())
        if(santaClaus.targetHouses.any()) {
            val target = santaClaus.targetHouses.random()
            if (NeedsGifts.has(target)) {
                throwPresent(TransformComponent.get(entity).position, TransformComponent.get(target).position)
            }
        }
    }
}