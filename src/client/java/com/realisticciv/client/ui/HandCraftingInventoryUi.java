package com.realisticciv.client.ui;

import com.realisticciv.client.mixin.AbstractContainerScreenAccessor;
import com.realisticciv.crafting.handcrafting.HandCraftingMatch;
import com.realisticciv.crafting.handcrafting.HandCraftingResolver;
import com.realisticciv.crafting.operation.OperationOutputDefinition;
import com.realisticciv.crafting.operation.OperationOutputRole;
import com.realisticciv.networking.payload.StartHandCraftPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

/**
 * Lightweight v0.1.5 augmentation of the vanilla inventory screen.
 *
 * <p>The vanilla 2x2 grid remains familiar, but RealisticCiv adds an explicit
 * Craft button. The button only sends a request; the logical server resolves
 * the grid again and owns reservation, timing, cancellation and output.</p>
 */
public final class HandCraftingInventoryUi {
    private static final int BUTTON_WIDTH = 48;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_OFFSET_X = 124;
    private static final int BUTTON_OFFSET_Y = 59;

    /**
     * InventoryScreen can be reinitialized while still being the same screen
     * instance (resize/recipe-book layout). Keep only the newest button and
     * register the per-screen callbacks once.
     */
    private static final Map<InventoryScreen, Button> BUTTONS = new WeakHashMap<>();

    private HandCraftingInventoryUi() {
    }

    public static void initialize() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (!(screen instanceof InventoryScreen inventoryScreen)) {
                return;
            }

            boolean firstInitialization = !BUTTONS.containsKey(inventoryScreen);
            Button craftButton = createButton(inventoryScreen);
            BUTTONS.put(inventoryScreen, craftButton);
            Screens.getWidgets(screen).add(craftButton);
            positionButton(inventoryScreen, craftButton);
            updateButton(inventoryScreen, craftButton);

            if (firstInitialization) {
                ScreenEvents.afterTick(screen).register(ignored -> {
                    Button currentButton = BUTTONS.get(inventoryScreen);
                    if (currentButton == null) {
                        return;
                    }

                    positionButton(inventoryScreen, currentButton);
                    updateButton(inventoryScreen, currentButton);
                });

                ScreenEvents.afterForeground(screen).register((ignored, graphics, mouseX, mouseY, tickDelta) ->
                        renderOutputPreview(inventoryScreen, graphics, mouseX, mouseY)
                );
            }
        });
    }

    private static Button createButton(InventoryScreen screen) {
        int left = leftPos(screen);
        int top = topPos(screen);

        return Button.builder(
                        Component.translatable("gui.realisticciv.hand_crafting.craft"),
                        button -> ClientPlayNetworking.send(StartHandCraftPayload.INSTANCE)
                )
                .bounds(left + BUTTON_OFFSET_X, top + BUTTON_OFFSET_Y, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build();
    }

    private static void positionButton(InventoryScreen screen, Button button) {
        int left = leftPos(screen);
        int top = topPos(screen);
        button.setX(left + BUTTON_OFFSET_X);
        button.setY(top + BUTTON_OFFSET_Y);
    }

    private static void updateButton(InventoryScreen screen, Button button) {
        Optional<HandCraftingMatch> match = currentMatch(screen);

        button.active = match.isPresent();
        if (match.isPresent()) {
            HandCraftingMatch resolved = match.get();
            Component operationName = Component.translatable(
                    resolved.definition().id().toLanguageKey("operation")
            );
            double seconds = resolved.definition().baseDurationTicks() / 20.0D;
            button.setTooltip(Tooltip.create(Component.translatable(
                    "gui.realisticciv.hand_crafting.ready",
                    operationName,
                    String.format(Locale.ROOT, "%.1f", seconds)
            )));
        } else {
            button.setTooltip(Tooltip.create(Component.translatable(
                    "gui.realisticciv.hand_crafting.no_match"
            )));
        }
    }

    private static void renderOutputPreview(InventoryScreen screen, GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        Optional<HandCraftingMatch> match = currentMatch(screen);
        if (match.isEmpty()) {
            return;
        }

        ItemStack preview = primaryOutput(match.get());
        if (preview.isEmpty()) {
            return;
        }

        Slot resultSlot = screen.getMenu().slots.get(0);
        int x = leftPos(screen) + resultSlot.x;
        int y = topPos(screen) + resultSlot.y;

        Minecraft minecraft = Minecraft.getInstance();
        graphics.item(preview, x, y);

        if (isHovering(mouseX, mouseY, x, y, 16, 16)) {
            graphics.setTooltipForNextFrame(minecraft.font, preview, mouseX, mouseY);
        }
    }

    private static Optional<HandCraftingMatch> currentMatch(InventoryScreen screen) {
        return HandCraftingResolver.find(screen.getMenu().getCraftSlots().getItems());
    }

    private static ItemStack primaryOutput(HandCraftingMatch match) {
        for (OperationOutputDefinition output : match.definition().outputs()) {
            if (output.role() == OperationOutputRole.PRIMARY) {
                return output.createStack();
            }
        }
        return match.definition().outputs().isEmpty()
                ? ItemStack.EMPTY
                : match.definition().outputs().getFirst().createStack();
    }

    private static int leftPos(InventoryScreen screen) {
        return ((AbstractContainerScreenAccessor) screen).realisticciv$getLeftPos();
    }

    private static int topPos(InventoryScreen screen) {
        return ((AbstractContainerScreenAccessor) screen).realisticciv$getTopPos();
    }

    private static boolean isHovering(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }
}
