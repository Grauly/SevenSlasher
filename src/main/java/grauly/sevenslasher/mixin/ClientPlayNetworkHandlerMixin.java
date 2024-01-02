package grauly.sevenslasher.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Shadow
    public abstract void sendChatCommand(String command);

    @Inject(method = "sendChatMessage(Ljava/lang/String;)V", at = @At("HEAD"), cancellable = true)
    public void onChatSend(String content, CallbackInfo ci) {
        if (content.startsWith("7")) {
            var newContent = content.replaceFirst("7", "");
            //Yes IntelliJ, there CAN be NPE here, but there won't, I cannot cast command without player
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("text.sevenslasher.triggered").setStyle(Style.EMPTY.withColor(Color.RED.getRGB())));
            sendChatCommand(newContent);
            ci.cancel();
        }
    }

    @ModifyVariable(method = "sendChatMessage(Ljava/lang/String;)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public String escapeSevens(String content) {
        return content.replaceFirst("\\\\", "");
    }
}
