package jam.core

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Contact
import eater.physics.bothAreEntities
import eater.physics.getEntityFor
import eater.physics.justOneHas
import jam.ecs.components.ChristmasPresent
import jam.ecs.components.House
import jam.ecs.components.SantaClaus
import jam.injection.ContactType
import ktx.math.vec2

fun String.toColor(): Color {
    val parts = this.chunked(2)
    val r = MathUtils.norm(0f, 255f, Integer.decode("0x${parts[0]}").toFloat())
    val g = MathUtils.norm(0f, 255f, Integer.decode("0x${parts[1]}").toFloat())
    val b = MathUtils.norm(0f, 255f, Integer.decode("0x${parts[2]}").toFloat())
    return Color(r, g, b, 1f)
}

fun Contact.getContactType(): ContactType {
    return if (this.bothAreEntities())
        if(this.justOneHas<SantaClaus>() && this.justOneHas<House>()) ContactType.SantaAndHouse(
            this.getEntityFor<SantaClaus>(),
            this.getEntityFor<House>()
        ) else if(this.justOneHas<ChristmasPresent>() && this.justOneHas<House>()) ContactType.HouseAndPresent(
            this.getEntityFor<House>(),
            this.getEntityFor<ChristmasPresent>()
        ) else ContactType.Other
    else
        ContactType.Other

}

fun Rectangle.wholePoints():MutableList<Vector2> {
    val points = mutableListOf<Vector2>()
    for(x in this.x.toInt()..(this.x + this.width).toInt())
        for(y in this.y.toInt()..(this.y + this.height).toInt())
            points.add(vec2(x.toFloat(), y.toFloat()))
    return points
}