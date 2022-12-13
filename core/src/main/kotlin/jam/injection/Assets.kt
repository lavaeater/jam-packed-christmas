package jam.injection

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.ray3k.tenpatch.TenPatchDrawable
import eater.injection.InjectionContext.Companion.inject
import jam.core.GameSettings
import ktx.assets.DisposableContainer
import ktx.assets.DisposableRegistry
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile
import ktx.scene2d.Scene2DSkin

fun assets(): Assets {
    return inject()
}

class Assets(private val gameSettings: GameSettings) : DisposableRegistry by DisposableContainer() {
//    val buddy: Map<AnimDef, Animation<TextureRegion>> by lazy {
//        val texture = Texture("player/buddy.png".toInternalFile()).alsoRegister()
//        AnimDef.animDefs.associateWith { ad ->
//            Animation(0.1f, *Array(ad.numberOfFrames) { x ->
//                TextureRegion(texture, x * 32, ad.rowIndex * 32, 32, 32)
//            })
//        }
//    }
    init {

        Scene2DSkin.defaultSkin = Skin("ui/uiskin.json".toInternalFile())
    }

    val terrainSprite = Sprite(Texture("terrain/terrain.png".toInternalFile()))
    val deerSprite = Sprite(Texture("deer.png".toInternalFile())).apply {
        setOriginCenter()
        scale(gameSettings.MetersPerPixel)
    }
    val sleighSprite = Sprite(Texture("sleigh.png".toInternalFile())).apply {
        setOriginCenter()
        flip(false, false)
        scale(gameSettings.MetersPerPixel)
    }

    val houseTopTexture = Texture("city/housetop-1.png".toInternalFile())
    val houseTenPatch = NinePatch(houseTopTexture,4,4,4,4)

    override fun dispose() {
        registeredDisposables.disposeSafely()
    }
}