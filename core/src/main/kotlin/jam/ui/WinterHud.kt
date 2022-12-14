package jam.ui

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Widget
import eater.extensions.boundLabel
import eater.ui.LavaHud
import jam.ecs.components.House
import jam.ecs.components.NeedsGifts
import ktx.actors.stage
import ktx.ashley.allOf
import ktx.scene2d.actors
import ktx.scene2d.label

class WinterHud(batch: PolygonSpriteBatch, private val engine: Engine) : LavaHud(batch) {
    private val houseFamily = allOf(House::class).get()
    private val houses get() = engine.getEntitiesFor(houseFamily)
    private val houseCount get() = houses.count()
    private val needsPresentsFamily = allOf(NeedsGifts::class).get()
    private val housesThatNeedsGifts get() = engine.getEntitiesFor(needsPresentsFamily)
    private val needsPresentsCount get() = housesThatNeedsGifts.count()

    override val stage: Stage by lazy {

        stage().apply {
            actors {
                boundLabel({"$needsPresentsCount left to deliver out of $houseCount houses"})
            }
        }
    }
}

class CompassWidget: Widget() {


}