package jam.injection

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import eater.core.GameSettings
import eater.injection.InjectionContext.Companion.inject
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
        scale(gameSettings.metersPerPixel)
    }
    val sleighSprite = Sprite(Texture("sleigh.png".toInternalFile())).apply {
        setOriginCenter()
        flip(false, false)
        scale(gameSettings.metersPerPixel)
    }

    val snowFlakeSprite = Sprite(Texture("snow-flake.png".toInternalFile())).apply {
        setOriginCenter()
        scale(gameSettings.metersPerPixel)
    }

    val presentSprite = Sprite(Texture("present.png".toInternalFile())).apply {
        setOriginCenter()
        scale(gameSettings.metersPerPixel)
    }

    val houseTopTexture = Texture("city/housetop-1.png".toInternalFile())
    val houseNinePatch = NinePatch(houseTopTexture, 4, 4, 4, 4)

    val samSiteSprite = Sprite(Texture("city/sam-launcher.png".toInternalFile()))
    val samSprite = Sprite(Texture("city/sam.png".toInternalFile()))

    val hohoho = Gdx.audio.newSound("audio/hohoho.wav".toInternalFile())
    val merryChristmas = Gdx.audio.newSound("audio/merry.wav".toInternalFile())
    val boom = Gdx.audio.newSound("audio/boom.wav".toInternalFile())

    override fun dispose() {
        registeredDisposables.disposeSafely()
    }
}
