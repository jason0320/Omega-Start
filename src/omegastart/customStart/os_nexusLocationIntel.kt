package data.omegastart.customStart

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.comm.IntelInfoPlugin
import com.fs.starfarer.api.impl.campaign.ids.Entities
import com.fs.starfarer.api.impl.campaign.ids.Tags
import com.fs.starfarer.api.impl.campaign.intel.BaseIntelPlugin
import com.fs.starfarer.api.ui.SectorMapAPI
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.IntervalUtil
import lunalib.lunaExtensions.getCustomEntitiesWithType

class os_nexusLocationIntel: BaseIntelPlugin() {
    val sprite = Global.getSettings().getSpriteName("icons", "omegaflag")
    val check = IntervalUtil(1f,1f)

    override fun createSmallDescription(info: TooltipMakerAPI?, width: Float, height: Float) {
        info!!.addImage(sprite, width,5f)
        info.addPara("You are privy to the location of any Coronal Hypershunt in the sector, owing to your background.", 5f)
        info.addPara("The following systems contain a Coronal Hypershunt;", 10f)

        val nexii = Global.getSector().getCustomEntitiesWithType(Entities.CORONAL_TAP)
        nexii.forEach {
            if (it.starSystem.isInConstellation) {
                if (it.starSystem.constellation.name.startsWith("The")){
                    info.addPara("${it.starSystem.name}, in ${it.starSystem.constellation.name}", 10f).setHighlight(it.starSystem.name)

                } else {
                    info.addPara("${it.starSystem.name}, in the ${it.starSystem.constellation.name}", 10f).setHighlight(it.starSystem.name)

                }
            }
            else {
                info.addPara("${it.starSystem.name}", 10f).setHighlight(it.starSystem.name)
            }
        }

        info.addPara("You may repair and restock at any Coronal Hypershunt by docking and opening a comm link.", 5f)
    }

    override fun getIcon(): String {
        return sprite
    }

    override fun advanceImpl(amount: Float) {

            if (!Global.getSector().intelManager.hasIntelOfClass(os_nexusHostilityIntel::class.java) && Global.getSector().playerFleet.fleetPoints >= 200) {
                Global.getSector().intelManager.addIntel(os_nexusHostilityIntel(), false)
            }
            else if (Global.getSector().intelManager.hasIntelOfClass(os_nexusHostilityIntel::class.java) && !Global.getSector().hasTransientScript(os_nexusHostilityIntel::class.java)){
                Global.getSector().addTransientScript(os_nexusHostilityIntel())
            }

    }

    override fun isHidden(): Boolean {
        return false
    }
    override fun autoAddCampaignMessage(): Boolean {
        return true
    }

    override fun getCommMessageSound(): String {
        return getSoundMajorPosting()
    }
    override fun getListInfoParam(): Any {
        return IntelInfoPlugin.ListInfoMode.MESSAGES
    }


    override fun isImportant(): Boolean {
        return true
    }

    override fun shouldRemoveIntel(): Boolean {
        return false
    }

    override fun getSortTier(): IntelInfoPlugin.IntelSortTier {
        return IntelInfoPlugin.IntelSortTier.TIER_1
    }

    override fun getIntelTags(map: SectorMapAPI?): Set<String>? {
        val tags = super.getIntelTags(map)
        tags.add(Tags.INTEL_IMPORTANT)
        return tags
    }


    override fun getName(): String {
        return "Omega Network"
    }

}