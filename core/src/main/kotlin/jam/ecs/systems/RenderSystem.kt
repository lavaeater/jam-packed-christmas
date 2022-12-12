package jam.ecs.systems

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import eater.ecs.ashley.components.Box2d
import eater.ecs.ashley.components.TransformComponent
import jam.core.GameSettings
import jam.ecs.components.SpriteComponent
import ktx.ashley.allOf
import ktx.graphics.use
import ktx.math.plus
import ktx.math.times
import ktx.math.vec2
import space.earlygrey.shapedrawer.ShapeDrawer

class FollowSantaSystem()

class RenderSystem(
    private val batch: PolygonSpriteBatch,
    private val shapeDrawer: ShapeDrawer,
    private val camera: OrthographicCamera,
    private val gameSettings: GameSettings,
    private val debug: Boolean
) : EntitySystem() {

    private val textureAndTransformFamily = allOf(SpriteComponent::class, TransformComponent::class).get()

    override fun update(deltaTime: Float) {
        batch.projectionMatrix = camera.combined
        batch.use {
            for (withTexture in engine.getEntitiesFor(textureAndTransformFamily)) {
                val transformComponent = TransformComponent.get(withTexture)
                val spriteComponent = SpriteComponent.get(withTexture)
                val sprite = spriteComponent.sprite
                sprite.setOriginBasedPosition(transformComponent.position.x, transformComponent.position.y)
                sprite.setScale(gameSettings.MetersPerPixel)
                sprite.rotation = transformComponent.angleDegrees - 90f
                sprite.draw(batch)
                shapeDrawer.filledCircle(transformComponent.position, 0.1f, Color.RED)
                if(debug) {
                    shapeDrawer.line(transformComponent.position, transformComponent.position + (transformComponent.direction * 10f),Color.BLUE)
                    if(Box2d.has(withTexture)) {
                        val body = Box2d.get(withTexture).body
                        shapeDrawer.line(body.worldCenter, body.worldCenter + vec2(1f, 0f).scl(10f).rotateRad(body.angle), Color.GREEN)
                    }
                }
            }
        }
    }
}