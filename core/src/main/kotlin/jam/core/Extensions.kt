package jam.core

import com.badlogic.gdx.physics.box2d.Contact
import eater.physics.bothAreEntities
import eater.physics.getEntityFor
import eater.physics.justOneHas
import jam.ecs.components.ChristmasPresent
import jam.ecs.components.House
import jam.ecs.components.SamComponent
import jam.ecs.components.SantaClaus
import jam.injection.ContactType

fun Contact.getContactType(): ContactType {
    return if (this.bothAreEntities()) {
        if (this.justOneHas<SantaClaus>()) {
            if (this.justOneHas<House>()) ContactType.SantaAndHouse(
                this.getEntityFor<SantaClaus>(),
                this.getEntityFor<House>()
            ) else if (this.justOneHas<SamComponent>())
                ContactType.SantaAndSam(
                    this.getEntityFor<SantaClaus>(),
                    this.getEntityFor<SamComponent>())
            else
                ContactType.Other
        }
        else if (this.justOneHas<ChristmasPresent>() && this.justOneHas<House>()) ContactType.HouseAndPresent(
            this.getEntityFor<House>(),
            this.getEntityFor<ChristmasPresent>()
        ) else ContactType.Other
    }
    else
        ContactType.Other

}
