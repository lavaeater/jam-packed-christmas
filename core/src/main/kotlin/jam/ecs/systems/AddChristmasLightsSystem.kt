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
import ktx.ashley.remove

class AddChristmasLightsSystem(private val rayHandler: RayHandler):IteratingSystem(allOf(House::class, NeedsChristmasLights::class, TransformComponent::class).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity.remove<NeedsChristmasLights>()
        val house = House.get(entity)
        if(house.lights.isEmpty()) {
            val transform = TransformComponent.get(entity)
            /*
            We shall simply add 8 lights on every house,
            so, corner, and middle of the sides.
             */
            val lightPosition = transform.position.cpy()
            lightPosition.x -= (0.5f + house.width / 2f)
            house.lights.add(
                PointLight(
                    rayHandler,
                    8,
                    if ((1..2).random() == 1) Color.RED else Color.GREEN,
                    3f,
                    lightPosition.x,
                    lightPosition.y
                ).apply { isStaticLight = true }
            )
            lightPosition.x = transform.position.x + (0.5f + house.width / 2f)
            house.lights.add(
                PointLight(
                    rayHandler,
                    8,
                    if ((1..2).random() == 1) Color.RED else Color.GREEN,
                    3f,
                    lightPosition.x,
                    lightPosition.y
                ).apply { isStaticLight = true }
            )
            lightPosition.y = transform.position.y + (0.5f + house.height / 2f)
            house.lights.add(
                PointLight(
                    rayHandler,
                    8,
                    if ((1..2).random() == 1) Color.RED else Color.GREEN,
                    3f,
                    lightPosition.x,
                    lightPosition.y
                ).apply { isStaticLight = true }
            )
            lightPosition.y = transform.position.y - (0.5f + house.height / 2f)
            house.lights.add(
                PointLight(
                    rayHandler,
                    8,
                    if ((1..2).random() == 1) Color.RED else Color.GREEN,
                    3f,
                    lightPosition.x,
                    lightPosition.y
                ).apply { isStaticLight = true }
            )
            lightPosition.x = transform.position.x - (0.5f + house.width / 2f)
            lightPosition.y = transform.position.y - (0.5f + house.height / 2f)

            house.lights.add(
                PointLight(
                    rayHandler,
                    8,
                    if ((1..2).random() == 1) Color.RED else Color.GREEN,
                    3f,
                    lightPosition.x,
                    lightPosition.y
                ).apply { isStaticLight = true }
            )
            lightPosition.x = transform.position.x + (0.5f + house.width / 2f)
            lightPosition.y = transform.position.y + (0.5f + house.height / 2f)
            house.lights.add(
                PointLight(
                    rayHandler,
                    8,
                    if ((1..2).random() == 1) Color.RED else Color.GREEN,
                    3f,
                    lightPosition.x,
                    lightPosition.y
                ).apply { isStaticLight = true }
            )
            lightPosition.x = transform.position.x - (0.5f + house.width / 2f)
            lightPosition.y = transform.position.y + (0.5f + house.height / 2f)
            house.lights.add(
                PointLight(
                    rayHandler,
                    8,
                    if ((1..2).random() == 1) Color.RED else Color.GREEN,
                    3f,
                    lightPosition.x,
                    lightPosition.y
                ).apply { isStaticLight = true }
            )
            lightPosition.x = transform.position.x + (0.5f + house.width / 2f)
            lightPosition.y = transform.position.y - (0.5f + house.height / 2f)
            house.lights.add(
                PointLight(
                    rayHandler,
                    8,
                    if ((1..2).random() == 1) Color.RED else Color.GREEN,
                    3f,
                    lightPosition.x,
                    lightPosition.y
                ).apply { isStaticLight = true }
            )
        }

    }
}