package jam.core

import com.badlogic.gdx.Application.LOG_INFO
import com.badlogic.gdx.Gdx
import eater.core.MainGame
import eater.injection.InjectionContext.Companion.inject
import jam.injection.Context
import jam.screens.SplashScreen
import jam.screens.GameOverScreen
import jam.screens.GameScreen
import jam.screens.GameSelectScreen
import ktx.async.KtxAsync

class ChristmasGame : MainGame() {
    override fun create() {
        KtxAsync.initiate()
        Gdx.app.logLevel = LOG_INFO

        Context.initialize(this)

        addScreen(inject<SplashScreen>())
        addScreen(inject<GameSelectScreen>())
        addScreen(inject<GameOverScreen>())
        addScreen(inject<GameScreen>())
        setScreen<GameScreen>()
    }

    fun goToGameSelect() {
        setScreen<GameSelectScreen>()
    }

    fun goToGameScreen() {
        setScreen<GameScreen>()
    }

    fun goToGameOver() {
        setScreen<GameOverScreen>()
    }
}

