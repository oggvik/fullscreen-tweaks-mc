// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss.mixin;

/*? if !template_noop {*/
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.Widget;
/*? if button_builder {*/
/*import net.minecraft.client.gui.components.Tooltip;
*//*?}*/
import java.util.ArrayList;
import java.util.List;
import oggvik.mods.stopminimizingonfocusloss.client.ButtonPlacement;
import oggvik.mods.stopminimizingonfocusloss.client.SettingsButtonHost;
import net.minecraft.client.gui.screen.Screen;
/*? if options_screen_subpackage {*/
/*import net.minecraft.client.gui.screen.options.OptionsScreen;
*//*?} else {*/
import net.minecraft.client.gui.screen.OptionsScreen;
/*?}*/
/*? if component_factory {*/
/*import net.minecraft.network.chat.Component;
*//*?} else {*/
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
/*?}*/
/*? if legacy_string_button {*/
/*import net.minecraft.client.resources.language.I18n;
*//*?}*/
import oggvik.mods.stopminimizingonfocusloss.client.FullscreenSettingsScreen;
import oggvik.mods.stopminimizingonfocusloss.platform.MinecraftWindowBridge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen implements SettingsButtonHost {
    @Unique
    private Button stopMinimizingOnFocusLoss$settingsButton;
    @Unique
    private boolean stopMinimizingOnFocusLoss$compact;
    protected OptionsScreenMixin() {
        /*? if component_factory {*/
        /*super(Component.empty());
        *//*?} else {*/
        super(new StringTextComponent(""));
        /*?}*/
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void stopMinimizingOnFocusLoss$addSettingsButton(CallbackInfo info) {
        int buttonWidth = Math.min(150, Math.max(1, this.width - 10));
        Button button;
        /*? if button_builder {*/
        /*button = Button.builder(
                        Component.translatable("stop_minimizing_on_focus_loss.settings.button"),
                        ignored -> stopMinimizingOnFocusLoss$openSettings())
                .bounds(5, this.height - 27, buttonWidth, 20)
                .build();
        *//*?} else if legacy_string_button {*/
        /*button = new Button(5, this.height - 27, buttonWidth, 20,
                I18n.get("stop_minimizing_on_focus_loss.settings.button"),
                ignored -> stopMinimizingOnFocusLoss$openSettings());
        *//*?} else if component_factory {*/
        /*button = new Button(5, this.height - 27, buttonWidth, 20,
                Component.translatable("stop_minimizing_on_focus_loss.settings.button"),
                ignored -> stopMinimizingOnFocusLoss$openSettings());
        *//*?} else {*/
        button = new Button(5, this.height - 27, buttonWidth, 20,
                new TranslationTextComponent("stop_minimizing_on_focus_loss.settings.button"),
                ignored -> stopMinimizingOnFocusLoss$openSettings());
        /*?}*/
        stopMinimizingOnFocusLoss$settingsButton = button;
        stopMinimizingOnFocusLoss$compact = false;
        /*? if legacy_add_button {*/
        this.addButton(button);
        /*?} else {*/
        /*this.addRenderableWidget(button);
        *//*?}*/
        stopMinimizingOnFocusLoss$placeSettingsButton();
    }

    @Override
    public void stopMinimizingOnFocusLoss$placeSettingsButton() {
        Button own = stopMinimizingOnFocusLoss$settingsButton;
        if (own == null) {
            return;
        }
        List<ButtonPlacement.Rect> occupied = new ArrayList<>();
        for (Object child : this.children()) {
            if (child instanceof Widget && child != own && ((Widget) child).visible) {
                occupied.add(stopMinimizingOnFocusLoss$bounds((Widget) child));
            }
        }
        ButtonPlacement.Rect current = stopMinimizingOnFocusLoss$bounds(own);
        int buttonWidth = Math.min(150, this.width - 10);
        int firstRowY = Integer.MAX_VALUE;
        for (ButtonPlacement.Rect other : occupied) {
            firstRowY = Math.min(firstRowY, other.y);
        }
        int preferredX = 5;
        int preferredY = current.y;
        if (firstRowY != Integer.MAX_VALUE) {
            int firstRowBottom = firstRowY;
            int firstColumnX = Integer.MAX_VALUE;
            for (ButtonPlacement.Rect other : occupied) {
                if (other.y == firstRowY) {
                    firstRowBottom = Math.max(firstRowBottom, other.y + other.height);
                    if (other.width == buttonWidth) {
                        firstColumnX = Math.min(firstColumnX, other.x);
                    }
                }
            }
            preferredX = firstColumnX == Integer.MAX_VALUE ? preferredX : firstColumnX;
            preferredY = firstRowBottom + 4;
        }
        ButtonPlacement.Rect preferred = new ButtonPlacement.Rect(
                preferredX, preferredY, buttonWidth, 20);
        ButtonPlacement.Rect position = ButtonPlacement.find(this.width, this.height, preferred, occupied);
        boolean compact = position == null;
        if (compact) {
            position = ButtonPlacement.find(this.width, this.height,
                    new ButtonPlacement.Rect(preferred.x, preferred.y, 40, 20), occupied);
        }
        own.visible = position != null;
        own.active = position != null;
        if (position != null) {
            WidgetBoundsAccessor bounds = (WidgetBoundsAccessor) own;
            bounds.stopMinimizingOnFocusLoss$setX(position.x);
            bounds.stopMinimizingOnFocusLoss$setY(position.y);
            bounds.stopMinimizingOnFocusLoss$setWidth(position.width);
            if (stopMinimizingOnFocusLoss$compact == compact) {
                return;
            }
            stopMinimizingOnFocusLoss$compact = compact;
            /*? if legacy_string_button {*/
            /*own.setMessage(compact ? "..." : I18n.get("stop_minimizing_on_focus_loss.settings.button"));
            *//*?} else if component_factory {*/
            /*own.setMessage(compact ? Component.literal("...")
                    : Component.translatable("stop_minimizing_on_focus_loss.settings.button"));
            *//*?} else {*/
            own.setMessage(compact ? new StringTextComponent("...")
                    : new TranslationTextComponent("stop_minimizing_on_focus_loss.settings.button"));
            /*?}*/
            /*? if button_builder {*/
            /*own.setTooltip(compact ? Tooltip.create(
                    Component.translatable("stop_minimizing_on_focus_loss.settings.button")) : null);
            *//*?}*/
        }
    }

    @Unique
    private static ButtonPlacement.Rect stopMinimizingOnFocusLoss$bounds(Widget widget) {
        WidgetBoundsAccessor bounds = (WidgetBoundsAccessor) widget;
        return new ButtonPlacement.Rect(bounds.stopMinimizingOnFocusLoss$getX(),
                bounds.stopMinimizingOnFocusLoss$getY(), bounds.stopMinimizingOnFocusLoss$getWidth(),
                bounds.stopMinimizingOnFocusLoss$getHeight());
    }

    @Unique
    private void stopMinimizingOnFocusLoss$openSettings() {
        MinecraftWindowBridge.showScreen(new FullscreenSettingsScreen((Screen) (Object) this));
    }
}
/*?}*/
