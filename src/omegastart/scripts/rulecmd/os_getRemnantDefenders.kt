package data.omegastart.scripts.rulecmd

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.InteractionDialogAPI
import com.fs.starfarer.api.campaign.rules.MemoryAPI
import com.fs.starfarer.api.impl.campaign.ids.Factions
import com.fs.starfarer.api.impl.campaign.ids.Sounds
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin
import com.fs.starfarer.api.util.Misc
import java.awt.Color

class os_getRemnantDefenders: BaseCommandPlugin() {
    override fun execute(ruleId: String?, dialog: InteractionDialogAPI?, params: MutableList<Misc.Token>?, memoryMap: MutableMap<String, MemoryAPI>?): Boolean {
        val memory = getEntityMemory(memoryMap)
        val type = params!![0].getInt(memoryMap)
        val color = Color(100,250,210,250)
        when (type){
            1 -> {
                if (memory.getFleet("\$defenderFleet") == null){
                    return false
                }
                if (memory.getFleet("\$defenderFleet").faction.id == Factions.OMEGA){
                    return true
                }
            }
            2 -> {
                dialog!!.visualPanel.showFleetInfo("Omega Automated Defenses", memory.getFleet("\$defenderFleet"), null, null)
                dialog.optionPanel.addOption("Transmit your Omega IFF code", "os_yeetRemmies", color, "")


            }
            3 -> {
                dialog!!.visualPanel.fadeVisualOut()
                memory.unset("\$hasDefenders")
                memory.unset("\$defenderFleet")
                memory["\$defenderFleetDefeated"] = true
                Global.getSoundPlayer().playUISound(Sounds.STORY_POINT_SPEND_TECHNOLOGY, 1f,1f)
                return true
            }
        }


        return false
    }
}