package jam.ui

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Widget
import com.badlogic.gdx.utils.Align
import eater.ecs.ashley.components.EntityPropertyComponent
import eater.extensions.boundLabel
import eater.extensions.boundProgressBar
import eater.injection.InjectionContext.Companion.inject
import eater.ui.LavaHud
import jam.ecs.components.House
import jam.ecs.components.NeedsGifts
import jam.ecs.components.SantaClaus
import jam.ecs.systems.AddChristmasLightsSystem
import jam.ecs.systems.getChristmasCheer
import jam.map.ChristmasMapManager
import ktx.actors.stage
import ktx.ashley.allOf
import ktx.scene2d.actors
import ktx.scene2d.label
import ktx.scene2d.table

class WinterHud(batch: PolygonSpriteBatch, private val engine: Engine) : LavaHud(batch) {
    private val houseFamily = allOf(House::class).get()
    private val houses get() = engine.getEntitiesFor(houseFamily)
    private val houseCount get() = houses.count()
    private val needsPresentsFamily = allOf(NeedsGifts::class).get()
    private val housesThatNeedsGifts get() = engine.getEntitiesFor(needsPresentsFamily)
    private val needsPresentsCount get() = housesThatNeedsGifts.count()

    private val christmasMapManager by lazy { inject<ChristmasMapManager>() }
    private val citiesLeftToVisit get() = christmasMapManager.cities.filter { it.needsGifts }
    private val santaFamily = allOf(SantaClaus::class, EntityPropertyComponent::class).get()
    private val santaEntity get() = engine.getEntitiesFor(santaFamily).first()
    private val christmasCheer get() = EntityPropertyComponent.get(santaEntity).getChristmasCheer()

    override val stage: Stage by lazy {

        stage().apply {
            actors {
                table {
                    table {
                        boundLabel({ "Houses to visit: $needsPresentsCount / $houseCount" }).apply {
                            setFontScale(3f)
                            align(Align.left)
                        }.cell(align = Align.left)
                        row()
                        boundLabel({ "Cities to visit: ${citiesLeftToVisit.count()} / ${christmasMapManager.cities.count()}" }).apply {
                            setFontScale(3f)
                            align(Align.left)
                        }.cell(align = Align.left)
                        row()
                        table {
                            label("Christmas cheer: ").apply {
                                setFontScale(3f)
                                align(Align.left)
                            }
                            boundProgressBar({christmasCheer.current},christmasCheer.min, christmasCheer.max)
                        }.cell(align = Align.left)

                        align(Align.left)
                        pad(15f)
                    }
                    center()
                    bottom()
                    pad(15f)
                    pack()
                }.setFillParent(true)
            }
        }
    }
}

class CompassWidget: Widget() {


}
