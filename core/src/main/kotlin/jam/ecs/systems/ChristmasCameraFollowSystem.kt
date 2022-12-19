package jam.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import eater.ecs.ashley.components.TransformComponent
import eater.ecs.ashley.systems.CameraFollowSystem
import ktx.math.vec2
import ktx.math.vec3

class ChristmasCameraFollowSystem(camera: OrthographicCamera, alpha: Float, private val rotate:Boolean):CameraFollowSystem(camera, alpha) {

    val backing = vec2()
    val cameraUp: Vector2
        get() {
            backing.set(camera.up.x, camera.up.y)
            return backing
        }

    val lerpVector = vec2()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = TransformComponent.get(entity)
        val position = transform.position
        cameraPosition.set(position)

        camera.position.lerp(
            vec3(cameraPosition, 0f), alpha
        )
        if(rotate) {
            lerpVector.set(cameraUp)
            lerpVector.lerp(transform.direction, 0.5f)
            camera.up.set(lerpVector.x, lerpVector.y, 0f)
        }

        camera.update(true)
    }
}