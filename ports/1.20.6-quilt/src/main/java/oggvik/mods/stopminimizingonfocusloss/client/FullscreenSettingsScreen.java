// SPDX-FileCopyrightText: 2026 Oggvik
// SPDX-License-Identifier: AGPL-3.0-or-later

package oggvik.mods.stopminimizingonfocusloss.client;

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
/*? if button_builder {*/
import net.minecraft.client.gui.components.Tooltip;
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
import oggvik.mods.stopminimizingonfocusloss.config.LoadingScreenMode;
import oggvik.mods.stopminimizingonfocusloss.config.SettingsManager;
import oggvik.mods.stopminimizingonfocusloss.platform.MinecraftWindowBridge;

import java.util.ArrayList;
import java.util.List;

/** Small dependency-free settings screen that remains compatible with Sodium and Embeddium. */
public final class FullscreenSettingsScreen extends Screen {
    private static final int CONTROL_WIDTH = 300;
    private static final int CONTROL_HEIGHT = 20;
    private static final int CONTROL_GAP = 4;
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
            /*ResourceLocation.withDefaultNamespace("textures/gui/menu_list_background.png");
            *//*?} else {*/
            new ResourceLocation("textures/gui/menu_list_background.png");
            /*?}*/
    private static final ResourceLocation HEADER_SEPARATOR =
            /*? if resource_location_factory {*/
            /*ResourceLocation.withDefaultNamespace("textures/gui/header_separator.png");
            *//*?} else {*/
            new ResourceLocation("textures/gui/header_separator.png");
            /*?}*/
    private static final ResourceLocation FOOTER_SEPARATOR =
            /*? if resource_location_factory {*/
            /*ResourceLocation.withDefaultNamespace("textures/gui/footer_separator.png");
            *//*?} else {*/
            new ResourceLocation("textures/gui/footer_separator.png");
            /*?}*/
    /*?}*/
        /*?}*/

    private final Screen parent;
    private final List<TooltipArea> tooltipAreas = new ArrayList<>();
    private AbstractWidget fullscreenButton;
    private boolean lastFullscreen;
    private boolean compactLayout;
    private int subtitleY;
    private int fullscreenModeLabelY;
    private int loadingModeLabelY;

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
        tooltipAreas.clear();
        compactLayout = this.height < 220;
        int startY = compactLayout
                ? Math.max(32, Math.min(this.height / 3, this.height - 145))
                : Math.max(44, Math.min(54, this.height - 176));
        subtitleY = compactLayout ? 28 : startY - 11;
        int controlWidth = Math.min(CONTROL_WIDTH, Math.max(100, this.width - 20));
        int controlX = (this.width - controlWidth) / 2;
        fullscreenButton = MinecraftWindowBridge.createFullscreenButton(
                controlX, startY, controlWidth);
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
        addTooltip(fullscreenButton, controlX, startY, controlWidth,
                "stop_minimizing_on_focus_loss.tooltip.minecraft_fullscreen");
        addControl(controlX, startY + 24, controlWidth, preventionLabel(settings),
                ignored -> togglePrevention(settings),
                "stop_minimizing_on_focus_loss.tooltip.prevent_auto_iconify");

        int fullscreenModeY = startY + (compactLayout ? 48 : 61);
        fullscreenModeLabelY = fullscreenModeY - 11;
        int halfWidth = (controlWidth - CONTROL_GAP) / 2;
        addControl(controlX, fullscreenModeY, halfWidth,
                radioLabel(settings.getFullscreenMode() == FullscreenMode.NATIVE,
                        "stop_minimizing_on_focus_loss.value.native"),
                ignored -> selectFullscreenMode(settings, FullscreenMode.NATIVE),
                "stop_minimizing_on_focus_loss.tooltip.fullscreen_mode.native");
        addControl(controlX + halfWidth + CONTROL_GAP, fullscreenModeY,
                controlWidth - halfWidth - CONTROL_GAP,
                radioLabel(settings.getFullscreenMode() == FullscreenMode.BORDERLESS,
                        "stop_minimizing_on_focus_loss.value.borderless"),
                ignored -> selectFullscreenMode(settings, FullscreenMode.BORDERLESS),
                "stop_minimizing_on_focus_loss.tooltip.fullscreen_mode.borderless");

        int loadingModeY = startY + (compactLayout ? 72 : 99);
        loadingModeLabelY = loadingModeY - 11;
        int thirdWidth = (controlWidth - CONTROL_GAP * 2) / 3;
        addLoadingModeControl(settings, controlX, loadingModeY, thirdWidth,
                LoadingScreenMode.SAME_AS_GAME,
                "stop_minimizing_on_focus_loss.value.same_as_game",
                "stop_minimizing_on_focus_loss.tooltip.loading_screen_mode.same_as_game");
        addLoadingModeControl(settings, controlX + thirdWidth + CONTROL_GAP,
                loadingModeY, thirdWidth, LoadingScreenMode.WINDOWED,
                "stop_minimizing_on_focus_loss.value.windowed",
                "stop_minimizing_on_focus_loss.tooltip.loading_screen_mode.windowed");
        addLoadingModeControl(settings, controlX + (thirdWidth + CONTROL_GAP) * 2,
                loadingModeY, controlWidth - thirdWidth * 2 - CONTROL_GAP * 2,
                LoadingScreenMode.FULLSCREEN,
                "stop_minimizing_on_focus_loss.value.fullscreen",
                "stop_minimizing_on_focus_loss.tooltip.loading_screen_mode.fullscreen");

        int minimizedY = startY + (compactLayout ? 96 : 123);
        addControl(controlX, minimizedY, controlWidth, startMinimizedLabel(settings),
                ignored -> toggleStartMinimized(settings),
                "stop_minimizing_on_focus_loss.tooltip.start_minimized");
        addControl((this.width - Math.min(200, Math.max(100, this.width - 20))) / 2,
                this.height - 26, Math.min(200, Math.max(100, this.width - 20)),
                translate("gui.done"), ignored -> onClose(), null);
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

    private void addControl(int x, int y, int width, String label,
                            Button.OnPress action, String tooltipKey) {
        AbstractWidget button;
        /*? if button_builder {*/
        button = Button.builder(Component.literal(label), action)
                .bounds(x, y, width, CONTROL_HEIGHT)
                .build();
        /*?} else if legacy_string_button {*/
        /*button = new Button(x, y, width, CONTROL_HEIGHT, label, action);
        *//*?} else if component_factory {*/
        /*button = new Button(x, y, width, CONTROL_HEIGHT, Component.literal(label), action);
        *//*?} else {*/
        /*button = new Button(x, y, width, CONTROL_HEIGHT, new TextComponent(label), action);
        *//*?}*/
        addWidget(button);
        addTooltip(button, x, y, width, tooltipKey);
    }

    private void addLoadingModeControl(FullscreenSettings settings, int x, int y, int width,
                                       LoadingScreenMode mode, String valueKey, String tooltipKey) {
        addControl(x, y, width,
                radioLabel(settings.getLoadingScreenMode() == mode, valueKey),
                ignored -> selectLoadingMode(settings, mode),
                tooltipKey);
    }

    private void addTooltip(AbstractWidget widget, int x, int y, int width, String tooltipKey) {
        if (tooltipKey == null) {
            return;
        }
        tooltipAreas.add(new TooltipArea(x, y, width, CONTROL_HEIGHT, tooltipKey));
        /*? if button_builder {*/
        widget.setTooltip(Tooltip.create(Component.translatable(tooltipKey)));
        /*?}*/
    }

    private void addWidget(AbstractWidget button) {
        /*? if legacy_add_button {*/
        /*this.addButton(button);
        *//*?} else {*/
        this.addRenderableWidget(button);
        /*?}*/
    }

    private void selectFullscreenMode(FullscreenSettings settings, FullscreenMode mode) {
        if (settings.getFullscreenMode() != mode) {
            saveAndRefresh(settings.withFullscreenMode(mode));
        }
    }

    private void togglePrevention(FullscreenSettings settings) {
        saveAndRefresh(settings.withPreventAutoIconify(!settings.isPreventAutoIconify()));
    }

    private void selectLoadingMode(FullscreenSettings settings, LoadingScreenMode mode) {
        if (settings.getLoadingScreenMode() != mode) {
            saveAndRefresh(settings.withLoadingScreenMode(mode));
        }
    }

    private void toggleStartMinimized(FullscreenSettings settings) {
        saveAndRefresh(settings.withStartMinimized(!settings.isStartMinimized()));
    }

    private void saveAndRefresh(FullscreenSettings settings) {
        SettingsManager.set(settings);
        MinecraftWindowBridge.reapply();
        MinecraftWindowBridge.showScreen(new FullscreenSettingsScreen(parent));
    }

    private String preventionLabel(FullscreenSettings settings) {
        String value = translate(settings.isPreventAutoIconify() ? "options.on" : "options.off");
        return translate("stop_minimizing_on_focus_loss.option.prevent_auto_iconify") + ": " + value;
    }

    private String startMinimizedLabel(FullscreenSettings settings) {
        String value = translate(settings.isStartMinimized() ? "options.on" : "options.off");
        return translate("stop_minimizing_on_focus_loss.option.start_minimized") + ": " + value;
    }

    private String radioLabel(boolean selected, String valueKey) {
        return (selected ? "● " : "○ ") + translate(valueKey);
    }

    private String hint(int mouseX, int mouseY) {
        /*? if !button_builder {*/
        /*for (TooltipArea area : tooltipAreas) {
            if (area.contains(mouseX, mouseY)) {
                return translate(area.translationKey);
            }
        }
        *//*?}*/
        return translate("stop_minimizing_on_focus_loss.settings.subtitle");
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

    private static final class TooltipArea {
        private final int x;
        private final int y;
        private final int width;
        private final int height;
        private final String translationKey;

        private TooltipArea(int x, int y, int width, int height, String translationKey) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.translationKey = translationKey;
        }

        private boolean contains(int mouseX, int mouseY) {
            return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
        }
    }

    /*? if legacy_string_button {*/
    /*@Override
    public void render(int mouseX, int mouseY, float partialTick) {
        this.renderBackground();
        this.drawCenteredString(this.font, translate("stop_minimizing_on_focus_loss.settings.title"),
                this.width / 2, 15, 0xFFFFFF);
        this.drawCenteredString(this.font, hint(mouseX, mouseY), this.width / 2, subtitleY, 0xA0A0A0);
        if (!compactLayout) {
            this.drawCenteredString(this.font,
                    translate("stop_minimizing_on_focus_loss.option.fullscreen_mode"),
                    this.width / 2, fullscreenModeLabelY, 0xA0A0A0);
            this.drawCenteredString(this.font,
                    translate("stop_minimizing_on_focus_loss.option.loading_screen_mode"),
                    this.width / 2, loadingModeLabelY, 0xA0A0A0);
        }
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
        graphics.centeredText(this.font, Component.literal(hint(mouseX, mouseY)),
                this.width / 2, subtitleY, 0xFFA0A0A0);
        if (!compactLayout) {
            graphics.centeredText(this.font,
                    Component.translatable("stop_minimizing_on_focus_loss.option.fullscreen_mode"),
                    this.width / 2, fullscreenModeLabelY, 0xFFA0A0A0);
            graphics.centeredText(this.font,
                    Component.translatable("stop_minimizing_on_focus_loss.option.loading_screen_mode"),
                    this.width / 2, loadingModeLabelY, 0xFFA0A0A0);
        }
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
    graphics.drawCenteredString(this.font, hint(mouseX, mouseY),
            this.width / 2, subtitleY, 0xA0A0A0);
    if (!compactLayout) {
        graphics.drawCenteredString(this.font,
                Component.translatable("stop_minimizing_on_focus_loss.option.fullscreen_mode"),
                this.width / 2, fullscreenModeLabelY, 0xA0A0A0);
        graphics.drawCenteredString(this.font,
                Component.translatable("stop_minimizing_on_focus_loss.option.loading_screen_mode"),
                this.width / 2, loadingModeLabelY, 0xA0A0A0);
    }
    /*?} else {*/
    /*renderBackground(graphics);
    graphics.drawCenteredString(this.font,
            Component.translatable("stop_minimizing_on_focus_loss.settings.title"),
            this.width / 2, 15, 0xFFFFFF);
    graphics.drawCenteredString(this.font, hint(mouseX, mouseY),
            this.width / 2, subtitleY, 0xA0A0A0);
    if (!compactLayout) {
        graphics.drawCenteredString(this.font,
                Component.translatable("stop_minimizing_on_focus_loss.option.fullscreen_mode"),
                this.width / 2, fullscreenModeLabelY, 0xA0A0A0);
        graphics.drawCenteredString(this.font,
                Component.translatable("stop_minimizing_on_focus_loss.option.loading_screen_mode"),
                this.width / 2, loadingModeLabelY, 0xA0A0A0);
    }
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
        drawCenteredString(poseStack, this.font, Component.literal(hint(mouseX, mouseY)),
                this.width / 2, subtitleY, 0xA0A0A0);
        if (!compactLayout) {
            drawCenteredString(poseStack, this.font,
                    Component.translatable("stop_minimizing_on_focus_loss.option.fullscreen_mode"),
                    this.width / 2, fullscreenModeLabelY, 0xA0A0A0);
            drawCenteredString(poseStack, this.font,
                    Component.translatable("stop_minimizing_on_focus_loss.option.loading_screen_mode"),
                    this.width / 2, loadingModeLabelY, 0xA0A0A0);
        }
        super.render(poseStack, mouseX, mouseY, partialTick);
    }
    *//*?} else {*/
    /*@Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(poseStack);
        drawCenteredString(poseStack, this.font,
                new TranslatableComponent("stop_minimizing_on_focus_loss.settings.title"),
                this.width / 2, 15, 0xFFFFFF);
        drawCenteredString(poseStack, this.font, new TextComponent(hint(mouseX, mouseY)),
                this.width / 2, subtitleY, 0xA0A0A0);
        if (!compactLayout) {
            drawCenteredString(poseStack, this.font,
                    new TranslatableComponent("stop_minimizing_on_focus_loss.option.fullscreen_mode"),
                    this.width / 2, fullscreenModeLabelY, 0xA0A0A0);
            drawCenteredString(poseStack, this.font,
                    new TranslatableComponent("stop_minimizing_on_focus_loss.option.loading_screen_mode"),
                    this.width / 2, loadingModeLabelY, 0xA0A0A0);
        }
        super.render(poseStack, mouseX, mouseY, partialTick);
    }
    *//*?}*/
}
