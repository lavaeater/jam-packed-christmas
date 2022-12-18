package jam.core

import com.badlogic.gdx.Application.LOG_INFO
import com.badlogic.gdx.Gdx
import eater.core.MainGame
import eater.injection.InjectionContext.Companion.inject
import jam.injection.Context
import jam.screens.*
import ktx.async.KtxAsync

class ChristmasGame : MainGame() {
    override fun create() {
        KtxAsync.initiate()
        Gdx.app.logLevel = LOG_INFO

        Context.initialize(this)

        addScreen(inject<SplashScreen>())
        addScreen(inject<GameSelectScreen>())
        addScreen(inject<GameOverScreen>())
        addScreen(inject<GameVictoryScreen>())
        addScreen(inject<GameScreen>())
        setScreen<SplashScreen>()
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

    fun gotoGameVictory() {
        setScreen<GameVictoryScreen>()
    }
}

