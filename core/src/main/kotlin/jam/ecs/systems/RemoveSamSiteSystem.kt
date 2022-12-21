package jam.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import eater.ecs.ashley.components.Remove
import eater.physics.addComponent
import jam.ecs.components.SamSite
import ktx.ashley.allOf
import ktx.ashley.exclude

class RemoveSamSiteSystem: IntervalIteratingSystem(allOf(SamSite::class).exclude(Remove::class).get(),1f){
    override fun processEntity(entity: Entity) {
        if(!SamSite.get(entity).city.needsGifts)
            entity.addComponent<Remove>()
    }
}