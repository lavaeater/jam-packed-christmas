package jam.ecs.systems

import box2dLight.PointLight
import box2dLight.RayHandler
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color
import eater.ecs.ashley.components.TransformComponent
import jam.ecs.components.NeedsChristmasLights
import jam.ecs.components.House
import ktx.ashley.allOf

class AddChristmasLightsSystem(private val rayHandler: RayHandler):IteratingSystem(allOf(House::class, NeedsChristmasLights::class, TransformComponent::class).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val house = House.get(entity)
        val transform = TransformComponent.get(entity)
        va
        for(i in 0..(5..10).random())
            PointLight(rayHandler, 8, if(i % 2 == 0) Color.RED else Color.GREEN, 5f, )
    }
}