package jam.injection

import com.badlogic.ashley.core.Entity

sealed class ContactType {
    class SantaAndHouse(val santaClaus: Entity, val house: Entity) : ContactType()
    class HouseAndPresent(val house: Entity, val present: Entity): ContactType()
    class SantaAndSam(val santaClaus: Entity, val sam: Entity):ContactType()
    object Other : ContactType()
}