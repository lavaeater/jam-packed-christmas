package jam.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.MathUtils.clamp
import com.badlogic.gdx.math.MathUtils.radiansToDegrees
import com.badlogic.gdx.math.Vector2
import eater.ecs.ashley.components.TransformComponent
import eater.ecs.ashley.systems.CameraFollowSystem
import ktx.log.info
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

    val lerpVector = vec2()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = TransformComponent.get(entity)
        val position = transform.position
        cameraPosition.set(position)

        camera.position.lerp(
            vec3(cameraPosition, 0f), alpha
        )
        lerpVector.set(cameraUp)
        lerpVector.lerp(transform.direction, 0.5f)
        camera.up.set(lerpVector.x, lerpVector.y, 0f)

//        var toRotate = clamp(cameraUp.angleDeg() -  MathUtils.lerp(cameraUp.angleDeg(), transform.direction.angleDeg(), alpha),-60f, 60f)
//        toRotate = if(toRotate > 120f)
//            toRotate - 120f
//        else toRotate
//
//        toRotate = if(toRotate < -120f)
//            toRotate + 120f
//        else toRotate

//        info { (toRotate * radiansToDegrees).toString() }

//        camera.rotate(toRotate)

        camera.update(true)
    }
}