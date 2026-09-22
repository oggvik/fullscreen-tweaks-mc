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
/*? if gl_state_manager {*/
/*import com.mojang.blaze3d.platform.GlStateManager;
*//*?} else if legacy_menu_list_background {*/
/*import com.mojang.blaze3d.systems.RenderSystem;
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
import net.minecraft.ChatFormatting;
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
    /*? if sdl_fullscreen_option {*/
    /*private static final int TITLE_Y = 12;
    *//*?} else if vanilla_title_y20 {*/
    /*private static final int TITLE_Y = 20;
    *//*?} else {*/
    private static final int TITLE_Y = 15;
    /*?}*/
    /*? if template_noop {*/
    /*private static final boolean SHOW_FULLSCREEN_MODE = false;
    *//*?} else {*/
    private static final boolean SHOW_FULLSCREEN_MODE = true;
    /*?}*/
    /*? if sdl_fullscreen_option {*/
    /*private static final boolean SHOW_EXCLUSIVE_FULLSCREEN = true;
    private static final boolean STYLE_SECTION_HEADINGS = true;
    private static final String PREVENTION_LABEL_KEY =
            "stop_minimizing_on_focus_loss.option.prevent_auto_iconify.sdl";
    private static final String PREVENTION_TOOLTIP_KEY =
            "stop_minimizing_on_focus_loss.tooltip.prevent_auto_iconify.sdl";
    *//*?} else {*/
    private static final boolean SHOW_EXCLUSIVE_FULLSCREEN = false;
    private static final boolean STYLE_SECTION_HEADINGS = false;
    private static final String PREVENTION_LABEL_KEY =
            "stop_minimizing_on_focus_loss.option.prevent_auto_iconify";
    private static final String PREVENTION_TOOLTIP_KEY =
            "stop_minimizing_on_focus_loss.tooltip.prevent_auto_iconify";
    /*?}*/
    /*? if button_builder {*/
    private static final boolean HAS_NATIVE_TOOLTIPS = true;
    /*?} else {*/
    /*private static final boolean HAS_NATIVE_TOOLTIPS = false;
    *//*?}*/
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
    private final List<TooltipArea> tooltipAreas = new ArrayList<>();
    private AbstractWidget fullscreenButton;
    private AbstractWidget exclusiveFullscreenButton;
    private boolean lastFullscreen;
    private boolean lastExclusiveFullscreen;
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
        compactLayout = this.height < (SHOW_FULLSCREEN_MODE || SHOW_EXCLUSIVE_FULLSCREEN ? 220 : 180);
        int startY = compactLayout
                ? Math.max(32, Math.min(this.height / 3, this.height - 145))
                : Math.max(44, Math.min(54, this.height - 176));
        int sectionHeadingOffset = STYLE_SECTION_HEADINGS ? 14 : 11;
        int controlRowStep = STYLE_SECTION_HEADINGS && !compactLayout ? 25 : 24;
        subtitleY = compactLayout ? 28 : startY - sectionHeadingOffset;
        int controlWidth = Math.min(CONTROL_WIDTH, Math.max(100, this.width - 20));
        int controlX = (this.width - controlWidth) / 2;
        fullscreenButton = MinecraftWindowBridge.createFullscreenButton(
                controlX, startY, controlWidth);
        lastFullscreen = MinecraftWindowBridge.fullscreenSetting();
        /*? if gui_graphics {*/
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
        int nextControlY = startY + controlRowStep;
        /*? if sdl_fullscreen_option {*/
        /*exclusiveFullscreenButton = MinecraftWindowBridge.createExclusiveFullscreenButton(
                controlX, nextControlY, controlWidth);
        lastExclusiveFullscreen = MinecraftWindowBridge.exclusiveFullscreenSetting();
        updateExclusiveFullscreenTooltip();
        addWidget(exclusiveFullscreenButton);
        nextControlY += controlRowStep;
        *//*?}*/
        addControl(controlX, nextControlY, controlWidth, preventionLabel(settings),
                ignored -> togglePrevention(settings),
                PREVENTION_TOOLTIP_KEY);

        int loadingModeY;
        if (SHOW_FULLSCREEN_MODE) {
            int fullscreenModeY = nextControlY + (compactLayout ? 24 : 37);
            fullscreenModeLabelY = fullscreenModeY - sectionHeadingOffset;
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
            loadingModeY = startY + (compactLayout ? 72 : 99);
        } else {
            loadingModeY = nextControlY + (compactLayout ? 24
                    : STYLE_SECTION_HEADINGS ? 61 : 37);
        }
        loadingModeLabelY = loadingModeY - sectionHeadingOffset;
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

        int minimizedY = loadingModeY + controlRowStep;
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
        /*? if sdl_fullscreen_option {*/
        /*boolean exclusiveValue = MinecraftWindowBridge.exclusiveFullscreenSetting();
        if (exclusiveFullscreenButton != null && exclusiveValue != lastExclusiveFullscreen) {
            lastExclusiveFullscreen = exclusiveValue;
            if (exclusiveFullscreenButton instanceof CycleButton<?>) {
                @SuppressWarnings("unchecked")
                CycleButton<Boolean> cycleButton = (CycleButton<Boolean>) exclusiveFullscreenButton;
                cycleButton.setValue(exclusiveValue);
            } else {
                exclusiveFullscreenButton.setMessage(
                        MinecraftWindowBridge.createExclusiveFullscreenButton(0, 0, CONTROL_WIDTH).getMessage());
            }
            updateExclusiveFullscreenTooltip();
        }
        *//*?}*/
    }

    /*? if sdl_fullscreen_option {*/
    /*private void updateExclusiveFullscreenTooltip() {
        String vanillaKey = MinecraftWindowBridge.exclusiveFullscreenSetting()
                ? "options.exclusiveFullscreen.on.tooltip"
                : "options.exclusiveFullscreen.off.tooltip";
        exclusiveFullscreenButton.setTooltip(Tooltip.create(
                Component.translatable(vanillaKey)
                        .append(" ")
                        .append(Component.translatable(
                                "stop_minimizing_on_focus_loss.tooltip.minecraft_exclusive_fullscreen"))));
    }
    *//*?}*/

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
        return translate(PREVENTION_LABEL_KEY) + ": " + value;
    }

    private String startMinimizedLabel(FullscreenSettings settings) {
        String value = translate(settings.isStartMinimized() ? "options.on" : "options.off");
        return translate("stop_minimizing_on_focus_loss.option.start_minimized") + ": " + value;
    }

    private String radioLabel(boolean selected, String valueKey) {
        return (selected ? "● " : "○ ") + translate(valueKey);
    }

    private String fallbackTooltip(int mouseX, int mouseY) {
        if (!HAS_NATIVE_TOOLTIPS) {
            for (TooltipArea area : tooltipAreas) {
            if (area.contains(mouseX, mouseY)) {
                return translate(area.translationKey);
            }
        }
        }
        return null;
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

    /*? if component_factory {*/
    private static Component sectionHeading(String translationKey) {
        Component heading = Component.translatable(translationKey);
        return STYLE_SECTION_HEADINGS
                ? heading.copy().withStyle(ChatFormatting.BOLD, ChatFormatting.UNDERLINE)
                : heading;
    }
    /*?}*/

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

    /*? if gl_state_manager {*/
    /*private void renderOptionsListBackground() {
        this.minecraft.getTextureManager().bind(Screen.BACKGROUND_LOCATION);
        GlStateManager.color4f(0.125F, 0.125F, 0.125F, 1.0F);
        blit(0, 32, 0.0F, 32.0F,
                this.width, Math.max(0, this.height - 64), 32, 32);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
    *//*?} else if legacy_string_button {*/
    /*private void renderOptionsListBackground() {
        this.minecraft.getTextureManager().bind(Screen.BACKGROUND_LOCATION);
        RenderSystem.color4f(0.125F, 0.125F, 0.125F, 1.0F);
        blit(0, 32, 0.0F, 32.0F,
                this.width, Math.max(0, this.height - 64), 32, 32);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
    *//*?} else if legacy_render_system {*/
    /*private void renderOptionsListBackground(PoseStack poseStack) {
        this.minecraft.getTextureManager().bind(Screen.BACKGROUND_LOCATION);
        RenderSystem.color4f(0.125F, 0.125F, 0.125F, 1.0F);
        blit(poseStack, 0, 32, 0.0F, 32.0F,
                this.width, Math.max(0, this.height - 64), 32, 32);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
    *//*?} else if legacy_menu_list_background {*/
    /*private void renderOptionsListBackground(PoseStack poseStack) {
        RenderSystem.setShaderTexture(0, Screen.BACKGROUND_LOCATION);
        RenderSystem.setShaderColor(0.125F, 0.125F, 0.125F, 1.0F);
        blit(poseStack, 0, 32, 0.0F, 32.0F,
                this.width, Math.max(0, this.height - 64), 32, 32);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
    *//*?}*/

    /*? if legacy_string_button {*/
    /*@Override
    public void render(int mouseX, int mouseY, float partialTick) {
        this.renderBackground();
        renderOptionsListBackground();
        this.drawCenteredString(this.font, translate("stop_minimizing_on_focus_loss.settings.title"),
                this.width / 2, TITLE_Y, 0xFFFFFF);
        this.drawCenteredString(this.font,
                translate("stop_minimizing_on_focus_loss.settings.subtitle"),
                this.width / 2, subtitleY, 0xA0A0A0);
        String tooltip = fallbackTooltip(mouseX, mouseY);
        if (tooltip != null) {
            this.drawCenteredString(this.font, tooltip, this.width / 2, this.height - 38, 0xFFD070);
        }
        if (!compactLayout) {
            if (SHOW_FULLSCREEN_MODE) {
                this.drawCenteredString(this.font,
                        translate("stop_minimizing_on_focus_loss.option.fullscreen_mode"),
                        this.width / 2, fullscreenModeLabelY, 0xA0A0A0);
            }
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
                this.width / 2, TITLE_Y, 0xFFFFFFFF);
        graphics.centeredText(this.font,
                sectionHeading("stop_minimizing_on_focus_loss.settings.subtitle"),
                this.width / 2, subtitleY, 0xFFFFFFFF);
        String tooltip = fallbackTooltip(mouseX, mouseY);
        if (tooltip != null) {
            graphics.centeredText(this.font, Component.literal(tooltip),
                    this.width / 2, this.height - 38, 0xFFFFD070);
        }
        if (!compactLayout) {
            if (SHOW_FULLSCREEN_MODE) {
                graphics.centeredText(this.font,
                        sectionHeading("stop_minimizing_on_focus_loss.option.fullscreen_mode"),
                        this.width / 2, fullscreenModeLabelY, 0xFFFFFFFF);
            }
            graphics.centeredText(this.font,
                    sectionHeading("stop_minimizing_on_focus_loss.option.loading_screen_mode"),
                    this.width / 2, loadingModeLabelY, 0xFFFFFFFF);
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
            this.width / 2, TITLE_Y, 0xFFFFFF);
    graphics.drawCenteredString(this.font,
            Component.translatable("stop_minimizing_on_focus_loss.settings.subtitle"),
            this.width / 2, subtitleY, 0xA0A0A0);
    String tooltip = fallbackTooltip(mouseX, mouseY);
    if (tooltip != null) {
        graphics.drawCenteredString(this.font, tooltip,
                this.width / 2, this.height - 38, 0xFFD070);
    }
    if (!compactLayout) {
        if (SHOW_FULLSCREEN_MODE) {
            graphics.drawCenteredString(this.font,
                    Component.translatable("stop_minimizing_on_focus_loss.option.fullscreen_mode"),
                    this.width / 2, fullscreenModeLabelY, 0xA0A0A0);
        }
        graphics.drawCenteredString(this.font,
                Component.translatable("stop_minimizing_on_focus_loss.option.loading_screen_mode"),
                this.width / 2, loadingModeLabelY, 0xA0A0A0);
    }
    /*?} else {*/
    /*renderBackground(graphics);
    graphics.drawCenteredString(this.font,
            Component.translatable("stop_minimizing_on_focus_loss.settings.title"),
            this.width / 2, TITLE_Y, 0xFFFFFF);
    graphics.drawCenteredString(this.font,
            Component.translatable("stop_minimizing_on_focus_loss.settings.subtitle"),
            this.width / 2, subtitleY, 0xA0A0A0);
    String tooltip = fallbackTooltip(mouseX, mouseY);
    if (tooltip != null) {
        graphics.drawCenteredString(this.font, tooltip,
                this.width / 2, this.height - 38, 0xFFD070);
    }
    if (!compactLayout) {
        if (SHOW_FULLSCREEN_MODE) {
            graphics.drawCenteredString(this.font,
                    Component.translatable("stop_minimizing_on_focus_loss.option.fullscreen_mode"),
                    this.width / 2, fullscreenModeLabelY, 0xA0A0A0);
        }
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
        renderOptionsListBackground(poseStack);
        drawCenteredString(poseStack, this.font,
                Component.translatable("stop_minimizing_on_focus_loss.settings.title"),
                this.width / 2, TITLE_Y, 0xFFFFFF);
        drawCenteredString(poseStack, this.font,
                Component.translatable("stop_minimizing_on_focus_loss.settings.subtitle"),
                this.width / 2, subtitleY, 0xA0A0A0);
        String tooltip = fallbackTooltip(mouseX, mouseY);
        if (tooltip != null) {
            drawCenteredString(poseStack, this.font, Component.literal(tooltip),
                    this.width / 2, this.height - 38, 0xFFD070);
        }
        if (!compactLayout) {
            if (SHOW_FULLSCREEN_MODE) {
                drawCenteredString(poseStack, this.font,
                        Component.translatable("stop_minimizing_on_focus_loss.option.fullscreen_mode"),
                        this.width / 2, fullscreenModeLabelY, 0xA0A0A0);
            }
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
        renderOptionsListBackground(poseStack);
        drawCenteredString(poseStack, this.font,
                new TranslatableComponent("stop_minimizing_on_focus_loss.settings.title"),
                this.width / 2, TITLE_Y, 0xFFFFFF);
        drawCenteredString(poseStack, this.font,
                new TranslatableComponent("stop_minimizing_on_focus_loss.settings.subtitle"),
                this.width / 2, subtitleY, 0xA0A0A0);
        String tooltip = fallbackTooltip(mouseX, mouseY);
        if (tooltip != null) {
            drawCenteredString(poseStack, this.font, new TextComponent(tooltip),
                    this.width / 2, this.height - 38, 0xFFD070);
        }
        if (!compactLayout) {
            if (SHOW_FULLSCREEN_MODE) {
                drawCenteredString(poseStack, this.font,
                        new TranslatableComponent("stop_minimizing_on_focus_loss.option.fullscreen_mode"),
                        this.width / 2, fullscreenModeLabelY, 0xA0A0A0);
            }
            drawCenteredString(poseStack, this.font,
                    new TranslatableComponent("stop_minimizing_on_focus_loss.option.loading_screen_mode"),
                    this.width / 2, loadingModeLabelY, 0xA0A0A0);
        }
        super.render(poseStack, mouseX, mouseY, partialTick);
    }
    *//*?}*/
}
