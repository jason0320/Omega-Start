package data.omegastart.plugins

import com.fs.starfarer.api.PluginPick
import com.fs.starfarer.api.campaign.*
import com.fs.starfarer.api.campaign.CampaignPlugin.PickPriority

class os_campaignPlugin: BaseCampaignPlugin() {

    override fun pickAICoreOfficerPlugin(commodityId: String): PluginPick<AICoreOfficerPlugin>? {
        return when (commodityId) {
            "os_playercore" -> PluginPick<AICoreOfficerPlugin>(os_coreThingyPlugin(), PickPriority.MOD_SET)
            else -> null
        }
    }




}