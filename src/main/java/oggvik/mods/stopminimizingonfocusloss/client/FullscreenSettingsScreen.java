// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss.client;

/*? if !template_noop {*/
/*? if render_extractor {*/
/*import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
*//*?} else if gui_graphics {*/
import net.minecraft.client.gui.GuiGraphics;
/*? if identifier {*/
/*import net.minecraft.resources.Identifier;
*//*?} else {*/
import net.minecraft.resources.ResourceLocation;
/*?}*/
/*? if !identifier {*/
import com.mojang.blaze3d.systems.RenderSystem;
/*?}*/
/*?} else if !legacy_string_button {*/
/*import com.mojang.blaze3d.vertex.PoseStack;
*//*?}*/
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.AbstractWidget;
/*? if !legacy_add_button {*/
import net.minecraft.client.gui.components.CycleButton;
/*?}*/
import net.minecraft.client.gui.screens.Screen;
/*? if component_factory {*/
import net.minecraft.network.chat.Component;
/*?} else {*/
/*import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
*//*?}*/
/*? if legacy_string_button {*/
/*import net.minecraft.client.resources.language.I18n;
*//*?}*/
import oggvik.mods.stopminimizingonfocusloss.config.FullscreenMode;
import oggvik.mods.stopminimizingonfocusloss.config.FullscreenSettings;
import oggvik.mods.stopminimizingonfocusloss.config.SettingsManager;
import oggvik.mods.stopminimizingonfocusloss.platform.MinecraftWindowBridge;

/** Small dependency-free settings screen that remains compatible with Sodium and Embeddium. */
public final class FullscreenSettingsScreen extends Screen {
    private static final int CONTROL_WIDTH = 260;
    private static final int CONTROL_HEIGHT = 20;
    /*? if render_extractor {*/
    /*private static final Identifier MENU_LIST_BACKGROUND =
            Identifier.withDefaultNamespace("textures/gui/menu_list_background.png");
    private static final Identifier INWORLD_MENU_LIST_BACKGROUND =
            Identifier.withDefaultNamespace("textures/gui/inworld_menu_list_background.png");
    *//*?}*/
    /*? if gui_graphics {*/
    /*? if identifier {*/
    /*private static final Identifier MENU_LIST_BACKGROUND =
            Identifier.withDefaultNamespace("textures/gui/menu_list_background.png");
    private static final Identifier HEADER_SEPARATOR =
            Identifier.withDefaultNamespace("textures/gui/header_separator.png");
    private static final Identifier FOOTER_SEPARATOR =
            Identifier.withDefaultNamespace("textures/gui/footer_separator.png");
    *//*?} else {*/
    private static final ResourceLocation MENU_LIST_BACKGROUND =
            /*? if resource_location_factory {*/
            ResourceLocation.withDefaultNamespace("textures/gui/menu_list_background.png");
            /*?} else {*/
            /*new ResourceLocation("textures/gui/menu_list_background.png");
            *//*?}*/
    private static final ResourceLocation HEADER_SEPARATOR =
            /*? if resource_location_factory {*/
            ResourceLocation.withDefaultNamespace("textures/gui/header_separator.png");
            /*?} else {*/
            /*new ResourceLocation("textures/gui/header_separator.png");
            *//*?}*/
    private static final ResourceLocation FOOTER_SEPARATOR =
            /*? if resource_location_factory {*/
            ResourceLocation.withDefaultNamespace("textures/gui/footer_separator.png");
            /*?} else {*/
            /*new ResourceLocation("textures/gui/footer_separator.png");
            *//*?}*/
    /*?}*/
        /*?}*/

    private final Screen parent;
    private AbstractWidget fullscreenButton;
    private boolean lastFullscreen;

    public FullscreenSettingsScreen(Screen parent) {
        /*? if component_factory {*/
        super(Component.translatable("stop_minimizing_on_focus_loss.settings.title"));
        /*?} else {*/
        /*super(new TranslatableComponent("stop_minimizing_on_focus_loss.settings.title"));
        *//*?}*/
        this.parent = parent;
    }

    @Override
    protected void init() {
        FullscreenSettings settings = SettingsManager.get();
        int startY = Math.max(32, Math.min(this.height / 3, this.height - 113));
        int controlWidth = Math.min(CONTROL_WIDTH, Math.max(100, this.width - 20));
        fullscreenButton = MinecraftWindowBridge.createFullscreenButton(
                (this.width - controlWidth) / 2, startY, controlWidth);
        lastFullscreen = MinecraftWindowBridge.fullscreenSetting();
        /*? if gui_graphics && !transparent_settings_background {*/
        addRenderableOnly((graphics, mouseX, mouseY, partialTick) -> {
            /*? if modern_menu_list_background {*/
            /*? if !identifier {*/
            RenderSystem.enableBlend();
            /*?}*/
            graphics.blit(HEADER_SEPARATOR,
                    0, 31, 0, 0, this.width, 2, 32, 2);
            graphics.blit(MENU_LIST_BACKGROUND,
                    0, 33, 0, 0, this.width, Math.max(0, this.height - 66), 32, 32);
            graphics.blit(FOOTER_SEPARATOR,
                    0, Math.max(33, this.height - 33), 0, 0, this.width, 2, 32, 2);
            /*? if !identifier {*/
            RenderSystem.disableBlend();
            /*?}*/
            /*?} else {*/
            /*graphics.setColor(0.125F, 0.125F, 0.125F, 1.0F);
            graphics.blit(Screen.BACKGROUND_LOCATION,
                    0, 33, 0, 0, this.width, Math.max(0, this.height - 66), 32, 32);
            graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            *//*?}*/
        });
        /*?}*/
        addWidget(fullscreenButton);
        addControl(startY + 24, preventionLabel(settings), ignored -> togglePrevention(settings));
        addControl(startY + 48, modeLabel(settings), ignored -> cycleMode(settings));
        addControl(this.height - 26, translate("gui.done"), ignored -> onClose(), 200);
    }

    @Override
    public void tick() {
        super.tick();
        boolean value = MinecraftWindowBridge.fullscreenSetting();
        if (fullscreenButton != null && value != lastFullscreen) {
            lastFullscreen = value;
            // F11 or another mod can change the vanilla option while this screen is open.
            /*? if legacy_add_button {*/
            /*fullscreenButton.setMessage(MinecraftWindowBridge.createFullscreenButton(0, 0, CONTROL_WIDTH).getMessage());
            *//*?} else {*/
            if (fullscreenButton instanceof CycleButton<?>) {
                @SuppressWarnings("unchecked")
                CycleButton<Boolean> cycleButton = (CycleButton<Boolean>) fullscreenButton;
                cycleButton.setValue(value);
            } else {
                fullscreenButton.setMessage(MinecraftWindowBridge.createFullscreenButton(0, 0, CONTROL_WIDTH).getMessage());
            }
            /*?}*/
        }
    }

    private void addControl(int y, String label, Button.OnPress action) {
        addControl(y, label, action, CONTROL_WIDTH);
    }

    private void addControl(int y, String label, Button.OnPress action, int requestedWidth) {
        int width = Math.min(requestedWidth, Math.max(100, this.width - 20));
        int x = (this.width - width) / 2;
        /*? if button_builder {*/
        addWidget(Button.builder(Component.literal(label), action)
                .bounds(x, y, width, CONTROL_HEIGHT)
                .build());
        /*?} else if legacy_string_button {*/
        /*addWidget(new Button(x, y, width, CONTROL_HEIGHT, label, action));
        *//*?} else if component_factory {*/
        /*addWidget(new Button(x, y, width, CONTROL_HEIGHT, Component.literal(label), action));
        *//*?} else {*/
        /*addWidget(new Button(x, y, width, CONTROL_HEIGHT, new TextComponent(label), action));
        *//*?}*/
    }

    private void addWidget(AbstractWidget button) {
        /*? if legacy_add_button {*/
        /*this.addButton(button);
        *//*?} else {*/
        this.addRenderableWidget(button);
        /*?}*/
    }

    private void cycleMode(FullscreenSettings settings) {
        FullscreenMode next = settings.getFullscreenMode() == FullscreenMode.NATIVE
                ? FullscreenMode.BORDERLESS
                : FullscreenMode.NATIVE;
        saveAndRefresh(settings.withFullscreenMode(next));
    }

    private void togglePrevention(FullscreenSettings settings) {
        saveAndRefresh(settings.withPreventAutoIconify(!settings.isPreventAutoIconify()));
    }

    private void saveAndRefresh(FullscreenSettings settings) {
        SettingsManager.set(settings);
        MinecraftWindowBridge.reapply();
        MinecraftWindowBridge.showScreen(new FullscreenSettingsScreen(parent));
    }

    private String modeLabel(FullscreenSettings settings) {
        String value = settings.getFullscreenMode() == FullscreenMode.BORDERLESS
                ? translate("stop_minimizing_on_focus_loss.value.borderless")
                : translate("stop_minimizing_on_focus_loss.value.native");
        return translate("stop_minimizing_on_focus_loss.option.fullscreen_mode") + ": " + value;
    }

    private String preventionLabel(FullscreenSettings settings) {
        String value = translate(settings.isPreventAutoIconify() ? "options.on" : "options.off");
        return translate("stop_minimizing_on_focus_loss.option.prevent_auto_iconify") + ": " + value;
    }

    private static String translate(String key) {
        /*? if legacy_string_button {*/
        /*return I18n.get(key);
        *//*?} else if component_factory {*/
        return Component.translatable(key).getString();
        /*?} else {*/
        /*return new TranslatableComponent(key).getString();
        *//*?}*/
    }

    @Override
    public void onClose() {
        MinecraftWindowBridge.showScreen(parent);
    }

    /*? if legacy_string_button {*/
    /*@Override
    public void render(int mouseX, int mouseY, float partialTick) {
        this.renderBackground();
        this.drawCenteredString(this.font, translate("stop_minimizing_on_focus_loss.settings.title"),
                this.width / 2, 15, 0xFFFFFF);
        super.render(mouseX, mouseY, partialTick);
    }
    *//*?} else if render_extractor {*/
    /*        @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        Identifier menuListBackground = this.minecraft.level == null
                ? MENU_LIST_BACKGROUND
                : INWORLD_MENU_LIST_BACKGROUND;
        Identifier headerSeparator = this.minecraft.level == null
                ? Screen.HEADER_SEPARATOR
                : Screen.INWORLD_HEADER_SEPARATOR;
        Identifier footerSeparator = this.minecraft.level == null
                ? Screen.FOOTER_SEPARATOR
                : Screen.INWORLD_FOOTER_SEPARATOR;
        graphics.blit(RenderPipelines.GUI_TEXTURED, headerSeparator,
                0, 31, 0.0F, 0.0F, this.width, 2, 32, 2);
        graphics.blit(RenderPipelines.GUI_TEXTURED, menuListBackground,
                0, 33, 0.0F, 0.0F, this.width, Math.max(0, this.height - 66), 32, 32);
        graphics.blit(RenderPipelines.GUI_TEXTURED, footerSeparator,
                0, Math.max(33, this.height - 33), 0.0F, 0.0F, this.width, 2, 32, 2);
        graphics.centeredText(this.font,
                Component.translatable("stop_minimizing_on_focus_loss.settings.title"),
                this.width / 2, 15, 0xFFFFFFFF);
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
    }
    */        /*?} else if gui_graphics {*/
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    /*? if render_background_delta {*/
    super.render(graphics, mouseX, mouseY, partialTick);
    graphics.drawCenteredString(this.font,
            Component.translatable("stop_minimizing_on_focus_loss.settings.title"),
            this.width / 2, 15, 0xFFFFFF);
    /*?} else {*/
    /*renderBackground(graphics);
    graphics.drawCenteredString(this.font,
            Component.translatable("stop_minimizing_on_focus_loss.settings.title"),
            this.width / 2, 15, 0xFFFFFF);
    super.render(graphics, mouseX, mouseY, partialTick);
    *//*?}*/
    }
    /*?} else if component_factory {*/
    /*@Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(poseStack);
        drawCenteredString(poseStack, this.font,
                Component.translatable("stop_minimizing_on_focus_loss.settings.title"),
                this.width / 2, 15, 0xFFFFFF);
        super.render(poseStack, mouseX, mouseY, partialTick);
    }
    *//*?} else {*/
    /*@Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(poseStack);
        drawCenteredString(poseStack, this.font,
                new TranslatableComponent("stop_minimizing_on_focus_loss.settings.title"),
                this.width / 2, 15, 0xFFFFFF);
        super.render(poseStack, mouseX, mouseY, partialTick);
    }
    *//*?}*/
}
/*?}*/
