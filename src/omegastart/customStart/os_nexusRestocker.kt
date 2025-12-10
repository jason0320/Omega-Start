package data.omegastart.customStart

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.listeners.EconomyTickListener
import com.fs.starfarer.api.impl.campaign.ids.Entities
import lunalib.lunaExtensions.getCustomEntitiesWithType

class os_nexusRestocker : EconomyTickListener { // restocks nexii monthly
    override fun reportEconomyTick(iterIndex: Int) {
    }

    override fun reportEconomyMonthEnd() {
        val nexii = Global.getSector().getCustomEntitiesWithType(Entities.CORONAL_TAP)
        nexii.forEach {
            if (it.id!="coronal_tap")
            {
                nexii.minus(it)
            }
        }
        nexii.forEach {
            if (it.cargo != null) {
                it.cargo.clear()
            }
            it.cargo.addAll(addNexusCargo(it))
        }
    }
}