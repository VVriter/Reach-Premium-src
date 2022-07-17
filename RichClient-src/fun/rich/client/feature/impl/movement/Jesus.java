package fun.rich.client.feature.impl.movement;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.player.EventLiquidSolid;
import fun.rich.client.event.events.impl.player.EventPreMotion;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.impl.BooleanSetting;
import fun.rich.client.ui.settings.impl.ListSetting;
import fun.rich.client.ui.settings.impl.NumberSetting;
import fun.rich.client.utils.math.TimerHelper;
import fun.rich.client.utils.movement.MovementUtils;
import fun.rich.client.utils.Helper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.BlockPos;

public class    Jesus extends Feature {
    public TimerHelper timerHelper = new TimerHelper();
    public static ListSetting mode = new ListSetting("Jesus Mode", "Old Matrix", () -> true, "Old Matrix", "ReallyWorld");
    public static NumberSetting speed = new NumberSetting("Speed", 0.65F, 0.1F, 10.0F, 0.01F, () -> !mode.currentMode.equals("ReallyWorld New"));
    public static BooleanSetting useTimer = new BooleanSetting("Use Timer", false, () -> true);
    private final NumberSetting timerSpeed = new NumberSetting("Timer Speed", 1.05F, 1.01F, 1.5F, 0.01F, () -> useTimer.getBoolValue());
    private final BooleanSetting speedCheck = new BooleanSetting("Speed Potion Check", false, () -> true);
    private final BooleanSetting autoMotionStop = new BooleanSetting("Auto Motion Stop", true, () -> mode.currentMode.equals("Old Matrix"));
    private final BooleanSetting autoWaterDown = new BooleanSetting("Auto Water Down", false, () -> mode.currentMode.equals("Old Matrix"));

    private int waterTicks = 0;

    public Jesus() {
        super("Jesus", "Позволяет прыгать на воде", FeatureCategory.Movement);
        this.addSettings(mode, speed, useTimer, timerSpeed, speedCheck, autoWaterDown, autoMotionStop);
    }

    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
        if ((mode.currentMode.equals("ReallyWorld")) && this.autoWaterDown.getBoolValue()) {
            mc.player.motionY -= 500.0D;
        }

        this.waterTicks = 0;
        super.onDisable();
    }


    private boolean isWater() {
        BlockPos bp1 = new BlockPos(mc.player.posX - 0.5D, mc.player.posY - 0.5D, mc.player.posZ - 0.5D);
        BlockPos bp2 = new BlockPos(mc.player.posX - 0.5D, mc.player.posY - 0.5D, mc.player.posZ + 0.5D);
        BlockPos bp3 = new BlockPos(mc.player.posX + 0.5D, mc.player.posY - 0.5D, mc.player.posZ + 0.5D);
        BlockPos bp4 = new BlockPos(mc.player.posX + 0.5D, mc.player.posY - 0.5D, mc.player.posZ - 0.5D);
        return mc.player.world.getBlockState(bp1).getBlock() == Blocks.WATER && mc.player.world.getBlockState(bp2).getBlock() == Blocks.WATER && mc.player.world.getBlockState(bp3).getBlock() == Blocks.WATER && mc.player.world.getBlockState(bp4).getBlock() == Blocks.WATER || mc.player.world.getBlockState(bp1).getBlock() == Blocks.LAVA && mc.player.world.getBlockState(bp2).getBlock() == Blocks.LAVA && mc.player.world.getBlockState(bp3).getBlock() == Blocks.LAVA && mc.player.world.getBlockState(bp4).getBlock() == Blocks.LAVA;
    }

    @EventTarget
    public void onLiquidBB(EventLiquidSolid event) {
        if (Jesus.mode.currentMode.equals("ReallyWorld New2")) {
            event.setCancelled(true);
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        this.setSuffix(mode.getCurrentMode());
        if (mc.player.isPotionActive(MobEffects.SPEED) || !this.speedCheck.getBoolValue()) {
            BlockPos blockPos = new BlockPos(Minecraft.getMinecraft().player.posX, mc.player.posY - 0.1D, mc.player.posZ);
            Block block = mc.world.getBlockState(blockPos).getBlock();
            if (useTimer.getBoolValue()) {
                mc.timer.timerSpeed = this.timerSpeed.getNumberValue();
            }
            if (mode.currentMode.equalsIgnoreCase("Old Matrix")) {
                if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 9.999999747378752E-2D, mc.player.posZ)).getBlock() instanceof BlockLiquid) {
                    mc.player.motionY = 0.07000000074505806D;
                }
                if (block instanceof BlockLiquid && !Minecraft.getMinecraft().player.onGround) {
                    if (Minecraft.getMinecraft().world.getBlockState(new BlockPos(Minecraft.getMinecraft().player.posX, mc.player.posY, mc.player.posZ)).getBlock() == Blocks.WATER) {
                        mc.player.motionX = 0.0F;
                        mc.player.motionY = 0.036F;
                        mc.player.motionZ = 0.0F;
                    } else {
                        MovementUtils.setSpeed(speed.getNumberValue());
                    }
                    if (Minecraft.getMinecraft().player.isCollided) {
                        mc.player.motionY = 0.2;
                    }
                }
            } else if (mode.currentMode.equalsIgnoreCase("ReallyWorld")) {
                BlockPos blockPos2 = new BlockPos(mc.player.posX, mc.player.posY - 0.02, mc.player.posZ);
                Block block2 = mc.world.getBlockState(blockPos2).getBlock();

                if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 0.5, mc.player.posZ)).getBlock() == Blocks.WATER && mc.player.onGround) {
                    mc.player.motionY = 0.2;
                }
                if (block2 instanceof BlockLiquid && !mc.player.onGround) {
                    if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 0.13, mc.player.posZ)).getBlock() == Blocks.WATER) {
                        mc.player.motionY = 0.1f;
                        MovementUtils.setSpeed(0.1f);
                        mc.player.jumpMovementFactor = 0f;
                    } else {
                        MovementUtils.setSpeed(1.1f);
                        mc.player.motionY = -0.1f;
                    }

                    if (mc.player.isCollidedHorizontally) {
                        mc.player.motionY = 0.18f;
                    }
                }


                if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 0.2, mc.player.posZ)).getBlock() instanceof BlockLiquid) {
                    mc.player.motionY = 0.18;
                }
            }
        }
    }
}