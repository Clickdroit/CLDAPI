package fr.clickdroit.api.utils.anvil;

import java.lang.reflect.Field;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.Container;
import net.minecraft.server.v1_8_R3.ContainerAnvil;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.ICrafting;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AnvilNMS {
    public static class AnvilContainer extends ContainerAnvil {
        private AnvilGUI menu;

        static {
            try {
                Field fieldText = ContainerAnvil.class.getDeclaredField("l");
                fieldText.setAccessible(true);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        public AnvilContainer(EntityHuman human, AnvilGUI menu) {
            super(human.inventory, human.world, new BlockPosition(0, 0, 0), human);
            this.menu = menu;
        }

        public boolean a(EntityHuman human) {
            return true;
        }

        public void a(String text) {
            this.menu.itemName = (text == null) ? "" : text;
            super.a(text);
        }
    }

    private static final ChatMessage PACKET_MESSAGE = new ChatMessage(Blocks.ANVIL.a() + ".name", new Object[0]);

    public static Inventory open(AnvilGUI menu) {
        EntityPlayer nmsPlayer = ((CraftPlayer)menu.getPlayer()).getHandle();
        AnvilContainer container = new AnvilContainer((EntityHuman)nmsPlayer, menu);
        Inventory inv = container.getBukkitView().getTopInventory();
        for (int slot = 0; slot < (menu.getItems()).length; slot++) {
            ItemStack item = menu.getItems()[slot];
            if (item != null)
                inv.setItem(slot, item);
        }
        int windowId = nmsPlayer.nextContainerCounter();
        nmsPlayer.playerConnection.sendPacket((Packet)new PacketPlayOutOpenWindow(windowId, "minecraft:anvil", (IChatBaseComponent)PACKET_MESSAGE, 0));
        nmsPlayer.activeContainer = (Container)container;
        nmsPlayer.activeContainer.windowId = windowId;
        nmsPlayer.activeContainer.addSlotListener((ICrafting)nmsPlayer);
        return inv;
    }
}
