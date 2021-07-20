/*
 * Decompiled with CFR 0_132.
 */
package tomorrow.tomo.mods.modules.world;

import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;
import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.misc.EventCollideWithBlock;
import tomorrow.tomo.event.events.world.EventMove;
import tomorrow.tomo.event.events.world.EventPostUpdate;
import tomorrow.tomo.event.value.Mode;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;
import tomorrow.tomo.utils.math.RotationUtil;

public class Phase
extends Module {
    private Mode<Enum> mode = new Mode("Mode", "mode", (Enum[])PhaseMode.values(), (Enum)PhaseMode.NewNCP);

    public Phase() {
        super("Phase", ModuleType.Render);
        this.addValues(this.mode);
    }

    @EventHandler
    private void onBlockCollision(EventCollideWithBlock e) {
        if (e.getBoundingBox() != null && e.getBoundingBox().maxY > this.mc.thePlayer.boundingBox.minY && this.mc.thePlayer.isSneaking() && this.mode.getValue() != PhaseMode.OldNCP) {
            e.setBoundingBox(null);
        }
        if (e.getBoundingBox() != null && e.getBoundingBox().maxY > this.mc.thePlayer.boundingBox.minY && this.mode.getValue() == PhaseMode.OldNCP) {
            e.setBoundingBox(null);
        }
    }

    @EventHandler
    private void onMove(EventMove e) {
        if (BlockHelper.insideBlock() && this.mc.thePlayer.isSneaking() && this.mode.getValue() == PhaseMode.SkipClip) {
            this.mc.thePlayer.boundingBox.offsetAndUpdate((double)this.mc.thePlayer.movementInput.moveForward * 3.6 * Math.cos(Math.toRadians(this.mc.thePlayer.rotationYaw + 90.0f)) + (double)this.mc.thePlayer.movementInput.moveStrafe * 3.6 * Math.sin(Math.toRadians(this.mc.thePlayer.rotationYaw + 90.0f)), 0.0, (double)this.mc.thePlayer.movementInput.moveForward * 3.6 * Math.sin(Math.toRadians(this.mc.thePlayer.rotationYaw + 90.0f)) - (double)this.mc.thePlayer.movementInput.moveStrafe * 3.6 * Math.cos(Math.toRadians(this.mc.thePlayer.rotationYaw + 90.0f)));
        }
    }

    @EventHandler
    private void onUpdate(EventPostUpdate e) {
        if (BlockHelper.insideBlock()) {
            if (this.mode.getValue() == PhaseMode.NewNCP && this.mc.thePlayer.isSneaking()) {
                this.mc.thePlayer.boundingBox.offsetAndUpdate(0.0524 * Math.cos(Math.toRadians(RotationUtil.yaw() + 90.0f)), 0.0, 0.0524 * Math.sin(Math.toRadians(RotationUtil.yaw() + 90.0f)));
            }
            if (this.mode.getValue() == PhaseMode.OldNCP && this.mc.thePlayer.isCollidedVertically) {
                double x = (double)(- MathHelper.sin(this.mc.thePlayer.getDirection())) * 0.2;
                double z = (double)MathHelper.cos(this.mc.thePlayer.getDirection()) * 0.2;
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX + x, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + z, false));
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX + x, Double.MIN_VALUE, this.mc.thePlayer.posZ + z, true));
                this.mc.thePlayer.setPosition(this.mc.thePlayer.posX + x, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + z);
            }
            if (this.mc.thePlayer.onGround && this.mode.getValue() == PhaseMode.NewNCP) {
                this.mc.thePlayer.jump();
            }
        }
    }

    static enum PhaseMode {
        NewNCP,
        OldNCP,
        SkipClip;
    }

}

