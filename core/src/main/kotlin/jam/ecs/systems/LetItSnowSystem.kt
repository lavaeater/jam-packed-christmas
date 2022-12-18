package jam.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.MathUtils.norm
import eater.ecs.ashley.components.Remove
import eater.ecs.ashley.components.TransformComponent
import eater.physics.addComponent
import jam.ecs.components.SnowFlake
import jam.ecs.components.SpriteComponent
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.math.plus
import ktx.math.times
import ktx.math.vec2

class LetItSnowSystem(private val camera: OrthographicCamera) :
    IteratingSystem(allOf(SnowFlake::class).exclude(Remove::class).get()) {
    private val snowFlakeMovementVector get() = vec2(0.5f + (-10..10).random() / 100f, 0.5f + (-10..10).random() / 100f)
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val snowFlake = SnowFlake.get(entity)
        snowFlake.timeToLive -= deltaTime
        if (snowFlake.timeToLive <= 0f) {
            entity.addComponent<Remove>()
        } else {
            val scale = norm(0f, snowFlake.timeToLiveBase, snowFlake.timeToLive)
            val spriteComponent = SpriteComponent.get(entity)
            spriteComponent.scale = spriteComponent.scale * scale
            val transformComponent = TransformComponent.get(entity)
            transformComponent.position.set(transformComponent.position + (snowFlakeMovementVector * (scale)))
            if (!camera.frustum.pointInFrustum(transformComponent.position.x, transformComponent.position.y, 0f)) {
                entity.addComponent<Remove>()
            }
        }
    }

}