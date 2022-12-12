package jam.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.MathUtils
import eater.ecs.ashley.components.TransformComponent
import eater.ecs.ashley.systems.CameraFollowSystem
import ktx.math.vec2
import ktx.math.vec3

class ChristmasCameraFollowSystem(camera: OrthographicCamera, alpha: Float):CameraFollowSystem(camera, alpha) {

    val cameraUp = vec2(0f, 1f)

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = TransformComponent.get(entity)
        val position = transform.position
        cameraPosition.set(position)

        camera.position.lerp(
            vec3(cameraPosition, 0f), alpha
        )

        cameraUp.set(camera.up.x, cameraUp.y)
        val toRotate =  MathUtils.lerp(cameraUp.angleDeg(), transform.direction.angleDeg(), alpha) - cameraUp.angleDeg()


        camera.rotate(toRotate)

        camera.update(true)
    }
}