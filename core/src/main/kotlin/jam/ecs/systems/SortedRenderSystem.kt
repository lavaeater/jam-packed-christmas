package jam.ecs.systems

import box2dLight.RayHandler
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import eater.ecs.ashley.components.IndexComponent
import eater.ecs.ashley.components.TransformComponent
import jam.core.GameSettings
import jam.ecs.components.ChristmasPresent
import jam.ecs.components.House
import jam.ecs.components.SpriteComponent
import jam.injection.assets
import jam.map.ChristmasMapManager
import ktx.ashley.allOf
import ktx.graphics.use
import ktx.math.minus
import ktx.math.plus
import ktx.math.times
import ktx.math.vec2
import space.earlygrey.shapedrawer.ShapeDrawer

class SortedRenderSystem(private val batch: PolygonSpriteBatch,
                         private val shapeDrawer: ShapeDrawer,
                         private val camera: OrthographicCamera,
                         private val gameSettings: GameSettings,
                         private val rayHandler: RayHandler,
                         private val christmasMapManager: ChristmasMapManager,
                         private val debug: Boolean) : SortedIteratingSystem(allOf(IndexComponent::class).get(),
    Comparator<Entity> { p0, p1 ->
        val i0 = IndexComponent.get(p0).index
        val i1 = IndexComponent.get(p1).index
        i0.compareTo(i1)
    }) {
    private val dotColor = Color(1f, 0f, 0f, 0.5f)
    private val textureAndTransformFamily = allOf(SpriteComponent::class, TransformComponent::class).get()
    private val houseFamily = allOf(House::class, TransformComponent::class).get()
    private val blackColor = Color(0f, 0f, 0f, 0.7f)

    private var camera2dPosition = vec2()
        get() {
            field.set(camera.position.x, camera.position.y)
            return field
        }


    override fun update(deltaTime: Float) {
        batch.projectionMatrix = camera.combined
        rayHandler.setCombinedMatrix(camera)
        rayHandler.updateAndRender()
        batch.use {
            super.update(deltaTime)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        if(SpriteComponent.has(entity)) {
            renderSprite(entity, deltaTime)
        }
        if(House.has(entity)) {
            renderHouse(entity, deltaTime)
        }
    }

    private fun renderHouse(entity: Entity, deltaTime: Float) {
        val housePosition = TransformComponent.get(entity).position
        val house = House.get(entity)
        var houseDrawPos = vec2(
            housePosition.x - (house.width / 2f),
            housePosition.y - (house.height / 2f)
        )

        val cameraDiff = (houseDrawPos - camera2dPosition).clamp(0f, 100f).scl(0.02f)
        val patch = assets().houseNinePatch
        for(i in 0..house.floors) {
            houseDrawPos += cameraDiff * (i.toFloat() / 10f)
            patch.draw(
                batch,
                houseDrawPos.x,
                houseDrawPos.y,
                0f,
                0f,
                house.width * gameSettings.PixelsPerMeter,
                house.height * gameSettings.PixelsPerMeter,
                gameSettings.MetersPerPixel * (0.5f + i / 15f),
                gameSettings.MetersPerPixel * (0.5f + i / 15f),
                0f
            )
        }
    }

    private fun renderSprite(entity: Entity, deltaTime: Float) {
        val transformComponent = TransformComponent.get(entity)
        val spriteComponent = SpriteComponent.get(entity)
        val sprite = spriteComponent.sprite
        sprite.setOriginBasedPosition(transformComponent.position.x, transformComponent.position.y)
        sprite.setScale(gameSettings.MetersPerPixel * spriteComponent.scale)
        sprite.rotation = transformComponent.angleDegrees - 90f
        sprite.draw(batch)
//        if (spriteComponent.shadow) {
//            sprite.color = blackColor
//            sprite.setOriginBasedPosition(
//                transformComponent.position.x + 10f, // Needs to be in relation to objects rotation somehow
//                transformComponent.position.y + 10f
//            )
//            sprite.setScale(gameSettings.MetersPerPixel / 2f * spriteComponent.scale)
//            sprite.draw(batch)
//            sprite.color = Color.WHITE
//        }
        if (ChristmasPresent.has(entity)) {
            spriteComponent.scale = spriteComponent.scale * 0.995f
        }
    }

}