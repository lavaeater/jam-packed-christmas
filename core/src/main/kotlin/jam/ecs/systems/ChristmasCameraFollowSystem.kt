package jam.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import eater.ecs.ashley.components.TransformComponent
import eater.ecs.ashley.systems.CameraFollowSystem
import ktx.math.minus
import ktx.math.vec2
import ktx.math.vec3

class ChristmasCameraFollowSystem(camera: OrthographicCamera, alpha: Float):CameraFollowSystem(camera, alpha) {

    val backing = vec2()
    val cameraUp: Vector2
        get() {
            backing.set(camera.up.x, camera.up.y)
            return backing
        }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = TransformComponent.get(entity)
        val position = transform.position
        cameraPosition.set(position)

        camera.position.lerp(
            vec3(cameraPosition, 0f), alpha
        )
//TODO: Turn on rotation, maaan
//        var toRotate = cameraUp.angleDeg() -  MathUtils.lerp(cameraUp.angleDeg(), transform.direction.angleDeg(), alpha)
//        toRotate = if(toRotate > 180f) toRotate - 180f else toRotate
//
//        camera.rotate(toRotate)

        camera.update(true)
    }
}