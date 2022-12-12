package jam.ecs.systems

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import jam.core.GameSettings
import space.earlygrey.shapedrawer.ShapeDrawer

class FollowSantaSystem()

class RenderSystem(private val batch: PolygonSpriteBatch,
                   private val shapeDrawer: ShapeDrawer,
                   private val camera: OrthographicCamera,
                   private val gameSettings: GameSettings
): EntitySystem() {
    override fun update(deltaTime: Float) {



//        camera.rotate()

    }
}