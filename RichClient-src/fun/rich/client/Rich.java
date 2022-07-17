package fun.rich.client;


import fun.rich.client.command.CommandManager;
import fun.rich.client.command.macro.MacroManager;
import fun.rich.client.draggable.DraggableHUD;
import fun.rich.client.event.EventManager;
import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.input.EventInputKey;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.FeatureManager;
import fun.rich.client.files.FileManager;
import fun.rich.client.files.impl.FriendConfig;
import fun.rich.client.files.impl.HudConfig;
import fun.rich.client.files.impl.MacroConfig;
import fun.rich.client.friend.FriendManager;
import ViaMCP.ViaMCP;
import fun.rich.client.ui.clickgui.ClickGuiScreen;
import fun.rich.client.ui.config.ConfigManager;
import fun.rich.client.utils.math.RotationHelper;
import fun.rich.client.utils.math.TPSUtils;
import fun.rich.client.utils.other.DiscordHelper;
import fun.rich.client.utils.render.cosmetic.CosmeticRender;
import fun.rich.client.utils.render.cosmetic.impl.DragonWing;
import fun.rich.protection.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.lwjgl.opengl.Display;

import java.io.IOException;

public class Rich {
    public Long time;

    public FeatureManager featureManager;
    public FileManager fileManager;
    public static long playTimeStart = 0;
    public DraggableHUD draggableHUD;

    public MacroManager macroManager;
    public Client client;
    public ConfigManager configManager;

    public CommandManager commandManager;

    public FriendManager friendManager;
    public ClickGuiScreen clickGui;
    public static Rich instance = new Rich();

    public static double deltaTime() {
        return Minecraft.getDebugFPS() > 0 ? (1.0000 / Minecraft.getDebugFPS()) : 1;
    }

    public String name = "Rich", type = "Premium", version = "0.2.9";

    public void init() {

        time = System.currentTimeMillis();
        Display.setTitle(name + " " + type + " - https://vk.com/richsense");
        (fileManager = new FileManager()).loadFiles();
        client = new Client();

        friendManager = new FriendManager();
        featureManager = new FeatureManager();

        macroManager = new MacroManager();
        configManager = new ConfigManager();
        draggableHUD = new DraggableHUD();
        commandManager = new CommandManager();
        clickGui = new ClickGuiScreen();
        new DragonWing();
        for (RenderPlayer render : Minecraft.getMinecraft().getRenderManager().getSkinMap().values()) {
            render.addLayer(new CosmeticRender(render));
        }
        TPSUtils tpsUtils = new TPSUtils();
        try {
            ViaMCP.getInstance().start();
            ViaMCP.getInstance().initAsyncSlider();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.fileManager.getFile(FriendConfig.class).loadFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.fileManager.getFile(MacroConfig.class).loadFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.fileManager.getFile(HudConfig.class).loadFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        EventManager.register(this);
    }

    public void stop() {
        Rich.instance.configManager.saveConfig("default");
        fileManager.saveFiles();
        DiscordHelper.stopRPC();
        EventManager.unregister(this);
    }

    @EventTarget
    public void onInputKey(EventInputKey event) {
        featureManager.getAllFeatures().stream().filter(module -> module.getBind() == event.getKey()).forEach(Feature::toggle);
        macroManager.getMacros().stream().filter(macros -> macros.getKey() == event.getKey()).forEach(macros -> Minecraft.getMinecraft().player.sendChatMessage(macros.getValue()));
    }
}
