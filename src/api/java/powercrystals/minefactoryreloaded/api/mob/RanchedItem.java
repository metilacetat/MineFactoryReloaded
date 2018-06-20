package powercrystals.minefactoryreloaded.api.mob;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

/**
 * @author skyboy026
 * <p>
 * Defines an ItemStack or a FluidStack that is the result of an entity being ranched
 */
public final class RanchedItem {

	private final @Nonnull
	ItemStack item;
	private final FluidStack fluid;

	public RanchedItem(Block item, int amount, int meta) {

		this(new ItemStack(item, amount, meta));
	}

	public RanchedItem(Block item, int amount) {

		this(new ItemStack(item, amount));
	}

	public RanchedItem(Block item) {

		this(new ItemStack(item));
	}

	public RanchedItem(Item item, int amount, int meta) {

		this(new ItemStack(item, amount, meta));
	}

	public RanchedItem(Item item, int amount) {

		this(new ItemStack(item, amount));
	}

	public RanchedItem(Item item) {

		this(new ItemStack(item));
	}

	public RanchedItem(@Nonnull ItemStack item) {

		this.item = item;
		fluid = null;
	}

	public RanchedItem(FluidStack fluid) {

		this.fluid = fluid;
		item = ItemStack.EMPTY;
	}

	public boolean hasFluid() {

		return item.isEmpty() & fluid != null;
	}

	public Object getResult() {

		return item.isEmpty() ? fluid : item;
	}

}
