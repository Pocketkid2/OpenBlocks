package openblocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatBasic;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import openblocks.api.MutantRegistry;
import openblocks.common.*;
import openblocks.common.block.*;
import openblocks.common.entity.*;
import openblocks.common.item.*;
import openblocks.common.item.ItemImaginationGlasses.ItemCrayonGlasses;
import openblocks.events.EventTypes;
import openblocks.integration.ModuleComputerCraft;
import openblocks.integration.ModuleOpenPeripheral;
import openblocks.mutant.*;
import openblocks.rubbish.BrickManager;
import openmods.Log;
import openmods.Mods;
import openmods.api.IOpenMod;
import openmods.config.RegisterItem;
import openmods.entity.EntityBlock;
import openmods.item.ItemGeneric;
import openmods.utils.EnchantmentUtils;

import org.apache.commons.lang3.ObjectUtils;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = "OpenBlocks", name = "OpenBlocks", version = "@VERSION@", dependencies = "required-after:OpenMods;after:OpenPeripheral")
@NetworkMod(serverSideRequired = true, clientSideRequired = true)
public class OpenBlocks implements IOpenMod {

	@Instance(value = "OpenBlocks")
	public static OpenBlocks instance;

	@SidedProxy(clientSide = "openblocks.client.ClientProxy", serverSide = "openblocks.common.ServerProxy")
	public static IOpenBlocksProxy proxy;

	public static class Blocks {
		public static BlockLadder ladder;
		public static BlockGuide guide;
		public static BlockElevator elevator;
		public static BlockHeal heal;
		public static BlockTarget target;
		public static BlockGrave grave;
		public static BlockFlag flag;
		public static BlockTank tank;
		public static BlockTrophy trophy;
		public static BlockBearTrap bearTrap;
		public static BlockSprinkler sprinkler;
		public static BlockCannon cannon;
		public static BlockVacuumHopper vacuumHopper;
		public static BlockSponge sponge;
		public static BlockBigButton bigButton;
		public static BlockImaginary imaginary;
		public static BlockFan fan;
		public static BlockXPBottler xpBottler;
		public static BlockVillageHighlighter villageHighlighter;
		public static BlockPath path;
		public static BlockAutoAnvil autoAnvil;
		public static BlockAutoEnchantmentTable autoEnchantmentTable;
		public static BlockXPDrain xpDrain;
		public static BlockBlockBreaker blockBreaker;
		public static BlockBlockPlacer blockPlacer;
		public static BlockItemDropper itemDropper;
		public static BlockRopeLadder ropeLadder;
		public static BlockDonationStation donationStation;
		public static BlockPaintMixer paintMixer;
		public static BlockCanvas canvas;
		public static BlockMachineOreCrusher machineOreCrusher;
		public static BlockPaintCan paintCan;
		public static BlockCanvasGlass canvasGlass;
		public static BlockProjector projector;
		public static BlockDrawingTable drawingTable;
		public static BlockGoldenEgg goldenEgg;
	}

	public static class Items {
		@RegisterItem(name = "hangglider")
		public static ItemHangGlider hangGlider;

		@RegisterItem(name = "generic")
		public static ItemGeneric generic;

		@RegisterItem(name = "luggage")
		public static ItemLuggage luggage;

		@RegisterItem(name = "sonicglasses")
		public static ItemSonicGlasses sonicGlasses;

		@RegisterItem(name = "pencilGlasses")
		public static ItemImaginationGlasses pencilGlasses;

		@RegisterItem(name = "crayonGlasses")
		public static ItemCrayonGlasses crayonGlasses;

		@RegisterItem(name = "technicolorGlasses")
		public static ItemImaginationGlasses technicolorGlasses;

		@RegisterItem(name = "seriousGlasses")
		public static ItemImaginationGlasses seriousGlasses;

		@RegisterItem(name = "craneControl")
		public static ItemCraneControl craneControl;

		@RegisterItem(name = "craneBackpack")
		public static ItemCraneBackpack craneBackpack;

		@RegisterItem(name = "slimalyzer")
		public static ItemSlimalyzer slimalyzer;

		@RegisterItem(name = "filledbucket")
		public static ItemFilledBucket filledBucket;

		@RegisterItem(name = "sleepingBag")
		public static ItemSleepingBag sleepingBag;

		@RegisterItem(name = "paintBrush")
		public static ItemPaintBrush paintBrush;

		@RegisterItem(name = "stencil")
		public static ItemStencil stencil;

		@RegisterItem(name = "squeegee")
		public static ItemSqueegee squeegee;

		@RegisterItem(name = "heightMap")
		public static ItemHeightMap heightMap;

		@RegisterItem(name = "emptyMap")
		public static ItemEmptyMap emptyMap;

		@RegisterItem(name = "cartographer")
		public static ItemCartographer cartographer;

		@RegisterItem(name = "tastyClay")
		public static ItemTastyClay tastyClay;

		@RegisterItem(name = "goldenEye")
		public static ItemGoldenEye goldenEye;
	}

	public static class Fluids {
		public static Fluid XPJuice;
		public static Fluid openBlocksXPJuice;
	}

	public static FluidStack XP_FLUID = null;

	public static enum GuiId {
		luggage
	}

	public static final GuiId[] GUIS = GuiId.values();

	public static CreativeTabs tabOpenBlocks = new CreativeTabs("tabOpenBlocks") {
		@Override
		public ItemStack getIconItemStack() {
			return new ItemStack(ObjectUtils.firstNonNull(OpenBlocks.Blocks.flag, Block.sponge), 1, 0);
		}

		@Override
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@SideOnly(Side.CLIENT)
		public void displayAllReleventItems(List result) {
			super.displayAllReleventItems(result);
			if (explosiveEnch != null) EnchantmentUtils.addAllBooks(explosiveEnch, result);
		}

	};

	public static int renderId;

	public static final Achievement brickAchievement = new Achievement(70997, "openblocks.droppedBrick", 13, 13, Item.brick, null).registerAchievement();

	public static final StatBase brickStat = (new StatBasic(70998, "stat.openblocks.bricksDropped")).registerStat();

	public static Enchantment explosiveEnch;

	@EventHandler
	public void preInit(FMLPreInitializationEvent evt) {
		EventTypes.registerTypes();
		Configuration configFile = new Configuration(evt.getSuggestedConfigurationFile());
		Config.readConfig(configFile);
		if (configFile.hasChanged()) {
			configFile.save();
		}

		Config.register();

		NetworkRegistry.instance().registerGuiHandler(instance, proxy.createGuiHandler());
		if (Config.enableGraves) {
			MinecraftForge.EVENT_BUS.register(new PlayerDeathHandler());
		}

		MinecraftForge.EVENT_BUS.register(new TileEntityEventHandler());

		if (Config.itemLuggageId > 0) {
			EntityRegistry.registerModEntity(EntityLuggage.class, "Luggage", 702, OpenBlocks.instance, 64, 1, true);
		}

		EntityRegistry.registerModEntity(EntityHangGlider.class, "Hang Glider", 701, OpenBlocks.instance, 64, 1, true);

		if (Config.itemCraneId > 0) {
			EntityRegistry.registerModEntity(EntityMagnet.class, "Magnet", 703, OpenBlocks.instance, 64, 1, true);
			EntityRegistry.registerModEntity(EntityBlock.class, "Block", 704, OpenBlocks.instance, 64, 1, true);
		}

		if (Config.itemCartographerId > 0) {
			EntityRegistry.registerModEntity(EntityCartographer.class, "Cartographer", 705, OpenBlocks.instance, 64, 8, true);
		}

		EntityRegistry.registerModEntity(EntityMutant.class, "Mutant", 708, OpenBlocks.instance, 64, 8, true);
		MutantRegistry.registerMutant(EntityCreeper.class, new DefinitionCreeper());
		MutantRegistry.registerMutant(EntityZombie.class, new DefinitionZombie());
		MutantRegistry.registerMutant(EntityPig.class, new DefinitionPig());
		MutantRegistry.registerMutant(EntityEnderman.class, new DefinitionEnderman());
		MutantRegistry.registerMutant(EntitySpider.class, new DefinitionSpider());
		MutantRegistry.registerMutant(EntityChicken.class, new DefinitionChicken());
		MutantRegistry.registerMutant(EntitySheep.class, new DefinitionSheep());
		MutantRegistry.registerMutant(EntityOcelot.class, new DefinitionOcelot());

		EntityRegistry.registerModEntity(EntityItemProjectile.class, "EntityItemProjectile", 706, OpenBlocks.instance, 64, 1, true);

		if (Config.itemGoldenEyeId > 0) {
			EntityRegistry.registerModEntity(EntityGoldenEye.class, "GoldenEye", 707, OpenBlocks.instance, 64, 8, true);
			MinecraftForge.EVENT_BUS.register(StructureRegistry.instance);
		}

		Fluids.openBlocksXPJuice = new Fluid("xpjuice").setLuminosity(10).setDensity(800).setViscosity(1500);
		FluidRegistry.registerFluid(Fluids.openBlocksXPJuice);
		Fluids.XPJuice = FluidRegistry.getFluid("xpjuice");
		XP_FLUID = new FluidStack(OpenBlocks.Fluids.openBlocksXPJuice, 1);

		FluidContainerRegistry.registerFluidContainer(Fluids.XPJuice, MetasBucket.xpbucket.newItemStack(), FluidContainerRegistry.EMPTY_BUCKET);

		OpenBlocks.Items.generic.initRecipes();

		MagnetWhitelists.instance.initTesters();

		MinecraftForge.EVENT_BUS.register(MapDataManager.instance);

		if (Loader.isModLoaded(Mods.COMPUTERCRAFT)) ModuleComputerCraft.registerAddons();
		if (Loader.isModLoaded(Mods.OPENPERIPHERAL)) ModuleOpenPeripheral.registerAdapters();

		if (!Config.soSerious) {
			MinecraftForge.EVENT_BUS.register(new BrickManager());
		}

		proxy.preInit();
	}

	/**
	 * @param evt
	 */
	@EventHandler
	public void init(FMLInitializationEvent evt) {
		TickRegistry.registerTickHandler(new ServerTickHandler(), Side.SERVER);
		proxy.init();
		proxy.registerRenderInformation();
	}

	/**
	 * @param evt
	 */
	@EventHandler
	public void postInit(FMLPostInitializationEvent evt) {
		proxy.postInit();
	}

	@Mod.EventHandler
	public void processMessage(FMLInterModComms.IMCEvent event) {
		for (FMLInterModComms.IMCMessage m : event.getMessages()) {
			if (m.isStringMessage() && m.key.equalsIgnoreCase("donateUrl")) {
				DonationUrlManager.instance().addUrl(m.getSender(), m.getStringValue());
			}
		}
	}

	public static String getModId() {
		return OpenBlocks.class.getAnnotation(Mod.class).modid();
	}

	@Override
	public Log getLog() {
		return null;
	}

	@Override
	public CreativeTabs getCreativeTab() {
		return tabOpenBlocks;
	}

	@Override
	public String getId() {
		return "openblocks";
	}

	@Override
	public int getRenderId() {
		return renderId;
	}
}
