package jam.core

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.Contact
import eater.physics.bothAreEntities
import eater.physics.getEntityFor
import eater.physics.justOneHas
import jam.ecs.components.ChristmasPresent
import jam.ecs.components.House
import jam.ecs.components.SantaClaus
import jam.injection.ContactType

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