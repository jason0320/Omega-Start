package data.omegastart.customStart

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.comm.IntelInfoPlugin
import com.fs.starfarer.api.impl.campaign.ids.Entities
import com.fs.starfarer.api.impl.campaign.ids.Tags
import com.fs.starfarer.api.impl.campaign.intel.BaseIntelPlugin
import com.fs.starfarer.api.ui.IntelUIAPI
import com.fs.starfarer.api.ui.SectorMapAPI
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.IntervalUtil
import lunalib.lunaExtensions.getCustomEntitiesWithType
import org.lwjgl.input.Keyboard

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
        val buttong =  addGenericButton(info, width, "Show Motherships on map", "os_nexusLocationToggle")
        buttong.setShortcut(Keyboard.KEY_T, true)
    }

    override fun buttonPressConfirmed(buttonId: Any?, ui: IntelUIAPI) {
        if (buttonId == "os_nexusLocationToggle"){
            toggleNexusList(this, ui)
        }
    }
    fun toggleNexusList(curr: IntelInfoPlugin?, ui: IntelUIAPI) {
        val removelist = ArrayList<IntelInfoPlugin>()
        Global.getSector().intelManager.getIntel(os_nexusShowLocationIntel::class.java).forEach {
            removelist.add(it)
        }
        removelist.forEach {
            Global.getSector().intelManager.removeIntel(it)
        }
        if (ui.isShowingCustomIntelSubset) {
            ui.updateIntelList(true)
            ui.updateUIForItem(curr)
            //for (IntelInfoPlugin intel : Global.getSector().getIntelManager().getIntel(LuddicShrineIntel.class)) {
            for (intel in Global.getSector().intelManager.getIntel(os_nexusLocationIntel::class.java)) {
                ui.selectItem(intel)
                break
            }
        } else {
            val nexii = Global.getSector().getCustomEntitiesWithType(Entities.CORONAL_TAP).forEach {
                Global.getSector().intelManager.addIntel(os_nexusShowLocationIntel(it), true)

            }
            val show: MutableList<IntelInfoPlugin> = ArrayList()
            for (intel in Global.getSector().intelManager.getIntel(os_nexusShowLocationIntel::class.java)) {
                show.add(intel)
            }
            Global.getSector().intelManager.sortIntel(show)
            ui.updateIntelList(true, show)
            ui.updateUIForItem(curr)
        }
        return
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