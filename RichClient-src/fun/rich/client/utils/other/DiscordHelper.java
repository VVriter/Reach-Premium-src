package fun.rich.client.utils.other;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import fun.rich.client.Rich;
import fun.rich.client.utils.Helper;
import fun.rich.protection.UserData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;

public class DiscordHelper implements Helper {
    private static final String discordID = "967706919601066004";
    private static final DiscordRichPresence discordRichPresence = new DiscordRichPresence();
    private static final DiscordRPC discordRPC = DiscordRPC.INSTANCE;

    public static void startRPC() {
        try {
            DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
            discordRPC.Discord_Initialize(discordID, eventHandlers, true, null);
            DiscordHelper.discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
            DiscordHelper.discordRichPresence.details = "UID: " + UserData.instance().getUID();
            DiscordHelper.discordRichPresence.largeImageKey = "large";
            DiscordHelper.discordRichPresence.largeImageText = "vk.com/richsense";
            DiscordHelper.discordRichPresence.state = "Version: " + Rich.instance.version;
            discordRPC.Discord_UpdatePresence(discordRichPresence);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopRPC() {
        discordRPC.Discord_Shutdown();
    }
}
