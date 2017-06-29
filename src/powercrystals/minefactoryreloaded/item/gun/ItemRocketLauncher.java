package powercrystals.minefactoryreloaded.item.gun;

import codechicken.lib.model.ModelRegistryHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import powercrystals.minefactoryreloaded.MineFactoryReloadedClient;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.entity.EntityRocket;
import powercrystals.minefactoryreloaded.item.base.ItemFactoryGun;
import powercrystals.minefactoryreloaded.net.MFRPacket;
import powercrystals.minefactoryreloaded.render.ModelHelper;
import powercrystals.minefactoryreloaded.render.entity.EntityRocketRenderer;
import powercrystals.minefactoryreloaded.render.item.RocketLauncherItemRenderer;
import powercrystals.minefactoryreloaded.setup.MFRThings;

public class ItemRocketLauncher extends ItemFactoryGun {

	public ItemRocketLauncher() {

		setUnlocalizedName("mfr.rocketlauncher");
		setMaxStackSize(1);
		setRegistryName(MineFactoryReloadedCore.modId, "rocket_launcher");
	}

	@Override
	protected boolean hasGUI(ItemStack stack) {
		return false;
	}

	@Override
	protected boolean fire(ItemStack stack, World world, EntityPlayer player) {
		int slot = -1;
		Item rocket = MFRThings.rocketItem;
		NonNullList<ItemStack> mainInventory = player.inventory.mainInventory;
		for (int j = 0, e = mainInventory.size(); j < e; ++j)
			if (!mainInventory.get(j).isEmpty() && mainInventory.get(j).getItem() == rocket) {
				slot = j;
				break;
			}
		if (slot > 0) {
			int damage = mainInventory.get(slot).getItemDamage();
			if (!player.capabilities.isCreativeMode)
				mainInventory.get(slot).shrink(1);
				if (mainInventory.get(slot).getCount() <= 0)
					mainInventory.set(slot, ItemStack.EMPTY);

			if (world.isRemote) {
				MFRPacket.sendRocketLaunchToServer(player.getEntityId(), 
						damage == 0 ? MineFactoryReloadedClient.instance.getLockedEntity() : Integer.MIN_VALUE);
			} else if (!player.addedToChunk) {
				EntityRocket r = new EntityRocket(world, player, null);
				world.spawnEntity(r);
			}
			return true;
		}
		return false;
	}

	@Override
	protected int getDelay(ItemStack stack, boolean fired) {
		return fired ? 100 : 40;
	}

	@Override
	protected String getDelayTag(ItemStack stack) {
		return "mfr:SPAMRLaunched";
	}

	@Override
	public boolean preInit() {

		super.preInit();
		EntityRegistry.registerModEntity(new ResourceLocation(MineFactoryReloadedCore.modId, "rocket_launcher"), EntityRocket.class, "Rocket", 3, MineFactoryReloadedCore.instance(), 160, 1, true);

		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {

		ModelHelper.registerModel(this, "rocket_launcher");
		ModelRegistryHelper.register(new ModelResourceLocation(MineFactoryReloadedCore.modId + ":rocket_launcher", "inventory"), new RocketLauncherItemRenderer());
		RenderingRegistry.registerEntityRenderingHandler(EntityRocket.class, EntityRocketRenderer::new);
	}
}
