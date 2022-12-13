package jam.ecs.systems

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import eater.ecs.ashley.components.Box2d
import eater.ecs.ashley.components.TransformComponent
import jam.core.GameSettings
import jam.ecs.components.House
import jam.ecs.components.SpriteComponent
import jam.injection.assets
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
    private val houseFamily = allOf(House::class, TransformComponent::class).get()

    override fun update(deltaTime: Float) {
        batch.projectionMatrix = camera.combined
        val blackColor = Color(0f, 0f, 0f, 0.7f)
        batch.use {
            for(houseEntity in engine.getEntitiesFor(houseFamily)) {
                val housePosition = TransformComponent.get(houseEntity).position
                val patch = assets().houseTenPatch
                patch.draw(batch, housePosition.x, housePosition.y, 100f, 100f, 50f, 50f, gameSettings.MetersPerPixel, gameSettings.MetersPerPixel, 0f)
            }
            for (withTexture in engine.getEntitiesFor(textureAndTransformFamily)) {
                val transformComponent = TransformComponent.get(withTexture)
                val spriteComponent = SpriteComponent.get(withTexture)
                val sprite = spriteComponent.sprite
                sprite.setOriginBasedPosition(transformComponent.position.x, transformComponent.position.y)
                sprite.setScale(gameSettings.MetersPerPixel)
                sprite.rotation = transformComponent.angleDegrees - 90f
                sprite.draw(batch)
                if(spriteComponent.shadow) {
                    sprite.color = blackColor
                    sprite.setOriginBasedPosition(
                        transformComponent.position.x + 10f, // Needs to be in relation to objects rotation somehow
                        transformComponent.position.y + 10f
                    )
                    sprite.setScale(gameSettings.MetersPerPixel / 2f)
                    sprite.draw(batch)
                    sprite.color = Color.WHITE
                }
                if(debug) {
                    shapeDrawer.filledCircle(transformComponent.position, 0.1f, Color.RED)
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