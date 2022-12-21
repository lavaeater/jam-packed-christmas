package jam.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.MathUtils.clamp
import com.badlogic.gdx.math.MathUtils.norm
import eater.ecs.ashley.components.Remove
import eater.ecs.ashley.components.TransformComponent
import eater.physics.addComponent
import jam.ecs.components.SnowFlake
import eater.ecs.ashley.components.SpriteComponent
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.math.plus
import ktx.math.times

class LetItSnowSystem(private val camera: OrthographicCamera) :
    IteratingSystem(allOf(SnowFlake::class).exclude(Remove::class).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val snowFlake = SnowFlake.get(entity)
        snowFlake.timeToLive -= deltaTime
        if((1..20).random() == 1)
            snowFlake.movementVector.set(snowFlake.movementVector.x + (-20..20).random() / 100f, snowFlake.movementVector.y)

        if((1..20).random() == 1)
            snowFlake.movementVector.set(snowFlake.movementVector.x, snowFlake.movementVector.y + (-20..20).random() / 100f)


        if (snowFlake.timeToLive <= 0f) {
            entity.addComponent<Remove>()
        } else {
            val scale = norm(0f, snowFlake.timeToLiveBase, snowFlake.timeToLive)
            val spriteComponent = SpriteComponent.get(entity)
            spriteComponent.scale = clamp(spriteComponent.scale * scale, 0.01f, 1f)
            val transformComponent = TransformComponent.get(entity)
            transformComponent.position.set(transformComponent.position + (snowFlake.movementVector * (scale)))
            if (!camera.frustum.pointInFrustum(transformComponent.position.x, transformComponent.position.y, 0f)) {
                entity.addComponent<Remove>()
            }
        }
    }

}