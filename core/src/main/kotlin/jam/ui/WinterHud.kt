package jam.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Widget
import eater.ui.LavaHud
import ktx.actors.stage
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.actors
import ktx.scene2d.label

class WinterHud(batch: PolygonSpriteBatch) : LavaHud(batch) {
    override val stage: Stage by lazy {

        stage().apply {
            actors {
                label("TESTLABEL ON STAGE")
            }
        }
    }
}

class CompassWidget: Widget() {


}